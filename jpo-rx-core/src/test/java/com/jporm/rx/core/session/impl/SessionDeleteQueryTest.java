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
package com.jporm.rx.core.session.impl;

import java.util.UUID;

import org.junit.Test;

import com.jporm.rx.JpoRX;
import com.jporm.rx.core.BaseTestApi;
import com.jporm.rx.core.session.Session;
import com.jporm.test.domain.section08.CommonUser;
import java.util.Random;

public class SessionDeleteQueryTest extends BaseTestApi {

    @Test
    public void testOne() throws Throwable {
        JpoRX jpo = newJpo();
        final String firstname = UUID.randomUUID().toString();
        final String lastname = UUID.randomUUID().toString();

        CommonUser newUser = new CommonUser();
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);

        Session session = jpo.session();
        session.save(newUser)
                .thenAccept(savedUser -> {

                    threadAssertNotNull(savedUser);
                    session.find(CommonUser.class, savedUser.getId()).get()
                    .thenAccept(foundUser -> {
                        threadAssertNotNull(foundUser);

                        session.deleteQuery(CommonUser.class).where().eq("id", new Random().nextInt()).now()
                        .thenAccept(deleteResult -> {

                            threadAssertTrue(deleteResult.deleted() == 0);

                            session.find(CommonUser.class, savedUser.getId()).get()
                            .thenAccept(foundUser2 -> {
                                threadAssertNotNull(foundUser2);

                                session.deleteQuery(CommonUser.class).where().eq("id", savedUser.getId()).now()
                                .thenAccept(deleteResult2 -> {
                                    threadAssertTrue(deleteResult2.deleted() == 1);

                                    session.find(CommonUser.class, savedUser.getId()).getOptional()
                                    .thenAccept(foundUser3 -> {
                                        threadAssertFalse(foundUser3.isPresent());
                                        resume();
                                    });
                                });
                            });

                        });

                    });
                });
        await(2000, 1);
    }

}
