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
package com.jporm.rx.transaction;

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

import rx.Single;
import rx.observers.TestSubscriber;

public class TransactionTest extends BaseTestApi {

    @Test
    public void failing_transaction_should_be_rolledback_at_the_end() throws Throwable {
        JpoRx jpo = newJpo();

        AtomicLong firstUserId = new AtomicLong();

        TestSubscriber<CommonUser> subscriber = new TestSubscriber<>();
        jpo.transaction().execute((Session txSession) -> {
            CommonUser user = new CommonUser();
            user.setFirstname(UUID.randomUUID().toString());
            user.setLastname(UUID.randomUUID().toString());

            Single<CommonUser> saved = txSession.save(user).flatMap(firstUser -> {
                firstUserId.set(firstUser.getId());
                // This action should fail because the object does not provide
                // all the mandatory fields
                CommonUser failingUser = new CommonUser();
                return txSession.save(failingUser);
            });
            return saved;
        }).doOnError(ex -> {
            getLogger().info("Exception is: {}", ex);
        }).subscribe(subscriber);

        subscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        subscriber.assertError(RuntimeException.class);

        Boolean exist = jpo.session().findById(CommonUser.class, firstUserId.get()).exist().toBlocking().value();
        assertFalse(exist);
    }

    @Test
    public void transaction_should_be_committed_at_the_end() throws Throwable {
        JpoRx jpo = newJpo();

        TestSubscriber<CommonUser> subscriber = new TestSubscriber<>();

        jpo.transaction().execute((Session txSession) -> {
            CommonUser user = new CommonUser();
            user.setFirstname(UUID.randomUUID().toString());
            user.setLastname(UUID.randomUUID().toString());

            return txSession.save(user);
        }).flatMap(user -> {
            return jpo.session().findById(CommonUser.class, user.getId()).fetchOneOptional().map(optionalFoundUser -> {
                assertTrue(optionalFoundUser.isPresent());
                assertEquals(user.getFirstname(), optionalFoundUser.get().getFirstname());
                return optionalFoundUser.get();
            });
        }).subscribe(subscriber);

        subscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        subscriber.assertCompleted();
    }

}
