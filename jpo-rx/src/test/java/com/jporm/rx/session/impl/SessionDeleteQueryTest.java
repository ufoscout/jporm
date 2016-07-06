/**
 * *****************************************************************************
 * Copyright 2015 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ****************************************************************************
 */
package com.jporm.rx.session.impl;

import static org.junit.Assert.*;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.jporm.rx.BaseTestApi;
import com.jporm.rx.JpoRx;
import com.jporm.rx.session.Session;
import com.jporm.test.domain.section08.CommonUser;

import rx.observers.TestSubscriber;

public class SessionDeleteQueryTest extends BaseTestApi {

    @Test
    public void testOne() throws Throwable {
        JpoRx jpo = newJpo();
        final String firstname = UUID.randomUUID().toString();
        final String lastname = UUID.randomUUID().toString();

        CommonUser newUser = new CommonUser();
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);

        TestSubscriber<Optional<CommonUser>> subscriber = new TestSubscriber<>();

        Session session = jpo.session();
        session.save(newUser)
        .flatMap(savedUser -> {
            return session.findById(CommonUser.class, savedUser.getId()).fetchOneUnique().flatMap(foundUser -> {

                return session.delete(CommonUser.class).where().eq("id", new Random().nextInt()).execute().flatMap(deleteResult -> {

                    assertTrue(deleteResult.deleted() == 0);

                    return session.findById(CommonUser.class, savedUser.getId()).fetchOneUnique().flatMap(foundUser2 -> {
                        assertNotNull(foundUser2);

                        return session.delete(CommonUser.class).where().eq("id", savedUser.getId()).execute().flatMap(deleteResult2 -> {
                            assertTrue(deleteResult2.deleted() == 1);

                            return session.findById(CommonUser.class, savedUser.getId()).fetchOneOptional().map(foundUser3 -> {
                                assertFalse(foundUser3.isPresent());
                                return foundUser3;
                            });
                        });
                    });

                });

            });
        })
        .subscribe(subscriber);
        subscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        subscriber.assertCompleted();
    }

}
