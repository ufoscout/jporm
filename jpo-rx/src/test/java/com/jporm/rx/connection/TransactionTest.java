/*******************************************************************************
 * Copyright 2015 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.jporm.rx.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.jporm.rx.BaseTestApi;
import com.jporm.rx.JpoRx;
import com.jporm.rx.session.Session;
import com.jporm.test.domain.section08.CommonUser;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;

public class TransactionTest extends BaseTestApi {

    @Test
    public void failing_transaction_should_be_rolledback_at_the_end() throws Throwable {
        JpoRx jpo = newJpo();

        AtomicLong firstUserId = new AtomicLong();

        TestObserver<CommonUser> subscriber = new TestObserver<>();
        jpo.tx().execute((Session txSession) -> {
            CommonUser user = new CommonUser();
            user.setFirstname(UUID.randomUUID().toString());
            user.setLastname(UUID.randomUUID().toString());

            Maybe<CommonUser> saved = txSession.save(user).flatMap(firstUser -> {
                firstUserId.set(firstUser.getId());
                // This action should fail because the object does not provide
                // all the mandatory fields
                CommonUser failingUser = new CommonUser();
                return txSession.save(failingUser);
            }).toMaybe();
            return saved;
        }).doOnError(ex -> {
            getLogger().info("Exception is: {}", ex);
        }).subscribe(subscriber);

        subscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        subscriber.assertError(RuntimeException.class);


        Maybe<Boolean> exists = jpo.tx().execute((Session session) -> session.findById(CommonUser.class, firstUserId.get()).exist().toMaybe());

        Boolean exist = exists.blockingGet();
        assertFalse(exist);
    }

    @Test
    public void transaction_should_be_committed_at_the_end() throws Throwable {
        JpoRx jpo = newJpo();

        TestObserver<CommonUser> subscriber = new TestObserver<>();

        jpo.tx().execute((Session txSession) -> {
            CommonUser user = new CommonUser();
            user.setFirstname(UUID.randomUUID().toString());
            user.setLastname(UUID.randomUUID().toString());

            return txSession.save(user).toMaybe();
        }).flatMap(user -> {
            return jpo.tx().execute((Session session) -> session.findById(CommonUser.class, user.getId()).fetchOneOptional().toMaybe()).map(optionalFoundUser -> {
                assertTrue(optionalFoundUser.isPresent());
                assertEquals(user.getFirstname(), optionalFoundUser.get().getFirstname());
                System.out.println("INTO FIND MAP");
                return optionalFoundUser.get();
            });
        }).subscribe(subscriber);

        subscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        subscriber.assertComplete();
    }

}
