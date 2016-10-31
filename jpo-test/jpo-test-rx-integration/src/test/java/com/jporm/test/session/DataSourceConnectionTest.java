/**
 * *****************************************************************************
 * Copyright 2013 Francesco Cina'
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
package com.jporm.test.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import com.jporm.rx.JpoRx;
import com.jporm.rx.connection.MaybeFunction;
import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;

/**
 *
 * @author Francesco Cina
 *
 *         05/giu/2011
 */
public class DataSourceConnectionTest extends BaseTestAllDB {

    public DataSourceConnectionTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Before
    public void setUp() {
        CommonUser user1 = new CommonUser();
        user1.setFirstname(UUID.randomUUID().toString());
        user1.setLastname(UUID.randomUUID().toString());
        user1 = getJPO().session().save(user1).blockingGet();

        user1 = new CommonUser();
        user1.setFirstname(UUID.randomUUID().toString());
        user1.setLastname(UUID.randomUUID().toString());
        user1 = getJPO().session().save(user1).blockingGet();

        user1 = new CommonUser();
        user1.setFirstname(UUID.randomUUID().toString());
        user1.setLastname(UUID.randomUUID().toString());
        user1 = getJPO().session().save(user1).blockingGet();
    }

    @Test
    public void testSessionActionsLoop() throws InterruptedException {

        final JpoRx jpo = getJPO();

        final int howMany = 1000;
        final int howManyErrors = 500;

        final AtomicBoolean exceptionThrown = new AtomicBoolean(false);
        final AtomicInteger errorCount = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(howMany);
        Random random = new Random();

        for (int i = 0; i < ((howMany - howManyErrors)/2); i++) {

            jpo.session().find("user.firstname").from(CommonUser.class, "user").where().ge("id", random.nextInt()).limit(10).fetchAll((rs, count) ->  rs.getString(0) )
                .doOnError(e -> {
                    getLogger().error(e.getMessage(), e);
                    errorCount.getAndIncrement();
                })
                .doAfterTerminate(() -> latch.countDown())
                .subscribe(new TestObserver<>());


            jpo.session().find("user.firstname").from(CommonUser.class, "user").where().ge("id", random.nextInt()).limit(10).fetchString()
                .doOnError(e -> {
                    getLogger().error(e.getMessage(), e);
                    errorCount.getAndIncrement();
                })
                .doAfterTerminate(() -> latch.countDown())
                .subscribe(new TestObserver<>());
        }

        for (int i = 0; i < (howManyErrors); i++) {
            jpo.session().find("user.firstname").from(CommonUser.class, "user").where().ge("id", -random.nextInt(1000_000_000)).limit(10).fetchString()
                    .flatMap(firstname -> {
                        exceptionThrown.set(true);
                        throw new RuntimeException("Manually thrown exception");
                    })
                    .doOnError(e -> errorCount.incrementAndGet())
                    .doAfterTerminate(() -> latch.countDown())
                    .subscribe(new TestObserver<>());
        }

        latch.await(15, TimeUnit.SECONDS);
        assertTrue(latch.getCount() == 0);
        assertEquals( howManyErrors , errorCount.get());
        assertTrue(exceptionThrown.get());
    }

    @Test
    public void testTransactionLoop() throws InterruptedException {

        final JpoRx jpo = getJPO();

        final int howMany = 1000;
        final int howManyErrors = 500;

        final AtomicInteger errorCount = new AtomicInteger();
        final AtomicBoolean exceptionThrown = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(howMany);

        for (int i = 0; i < (howMany - howManyErrors); i++) {
            jpo.tx().execute((Session session) -> {
                return Maybe.empty();
            })
            .doOnError(e -> errorCount.incrementAndGet())
            .doAfterTerminate(() -> latch.countDown())
            .subscribe(new TestObserver<>());
        }

        for (int i = 0; i < (howManyErrors); i++) {
            jpo.tx().execute(new MaybeFunction<String>() {
                @Override
                public Maybe<String> apply(Session t) {
                    exceptionThrown.set(true);
                    throw new RuntimeException("Manually thrown exception to force rollback");
                }
            })
            .doOnError(e -> errorCount.incrementAndGet())
            .doAfterTerminate(() -> latch.countDown())
            .subscribe(new TestObserver<>());
        }

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(latch.getCount() == 0);
        assertEquals( howManyErrors , errorCount.get());
        assertTrue(exceptionThrown.get());
    }

    @Test
    public void testTransactionLoopWithExceptionInFollowingFlatmap() throws InterruptedException {

        final JpoRx jpo = getJPO();

        final int howMany = 1000;
        final int howManyErrors = 500;

        final AtomicInteger errorCount = new AtomicInteger();
        final AtomicBoolean exceptionThrown = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(howMany);

        for (int i = 0; i < (howMany - howManyErrors); i++) {
            jpo.tx().execute((Session session) -> {
                return Maybe.just("");
            })
            .flatMap(result -> {
                exceptionThrown.set(true);
                throw new RuntimeException("Manually thrown exception to force rollback");
            })
            .doOnError(e -> errorCount.incrementAndGet())
            .doAfterTerminate(() -> latch.countDown())
            .subscribe(new TestObserver<>());
        }

        for (int i = 0; i < (howManyErrors); i++) {
            jpo.tx().execute(new MaybeFunction<String>() {
                @Override
                public Maybe<String> apply(Session t) {
                    throw new RuntimeException("Manually thrown exception to force rollback");
                }
            })
            .flatMap(result -> {
                throw new RuntimeException("Manually thrown exception to force rollback");
            })
            .doOnError(e -> errorCount.incrementAndGet())
            .doAfterTerminate(() -> latch.countDown())
            .subscribe(new TestObserver<>());
        }

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(latch.getCount() == 0);
        assertEquals( howMany , errorCount.get());
        assertTrue(exceptionThrown.get());
    }
}
