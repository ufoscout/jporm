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

import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;

import com.jporm.rx.JpoRx;
import com.jporm.rx.session.Session;
import com.jporm.rx.transaction.ObservableFunction;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;

import rx.Completable;
import rx.Observable;
import rx.observers.TestSubscriber;

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

    @Test
    public void testSessionActionsLoop() throws InterruptedException {

        final JpoRx jpo = getJPO();
        final int howMany = 1000;
        CountDownLatch latch = new CountDownLatch(howMany);
        Random random = new Random();

        for (int i = 0; i < (howMany / 2); i++) {
            jpo.session().find("user.firstname").from(CommonUser.class, "user").where().ge("id", random.nextInt()).limit(1).fetchString()
                .doOnError(e -> latch.countDown())
                .doOnCompleted(() -> latch.countDown())
                .subscribe(new TestSubscriber<>());
        }

        for (int i = 0; i < (howMany / 2); i++) {
            jpo.session().find("user.firstname").from(CommonUser.class, "user").where().ge("id", random.nextInt()).limit(1).fetchString()
                    .flatMap(firstname -> {
                        throw new RuntimeException("Manually thrown exception");
                    })
                    .doOnError(e -> latch.countDown())
                    .doOnCompleted(() -> latch.countDown())
                    .subscribe(new TestSubscriber<>());
        }

        latch.await(15, TimeUnit.SECONDS);
        assertTrue(latch.getCount() == 0);
    }

    @Test
    public void testTransactionLoop() throws InterruptedException {

        final JpoRx jpo = getJPO();
        final int howMany = 1000;
        CountDownLatch latch = new CountDownLatch(howMany);

        for (int i = 0; i < (howMany / 2); i++) {
            jpo.tx().execute((Session session) -> {
                return Completable.complete();
            })
            .doOnError(e -> latch.countDown())
            .doOnCompleted(() -> latch.countDown())
            .subscribe(new TestSubscriber<>());
        }

        for (int i = 0; i < (howMany / 2); i++) {
            jpo.tx().execute(new ObservableFunction<String>() {
                @Override
                public Observable<String> apply(Session t) {
                    throw new RuntimeException("Manually thrown exception to force rollback");
                }
            })
            .doOnError(e -> latch.countDown())
            .doOnCompleted(() -> latch.countDown())
            .subscribe(new TestSubscriber<>());
        }

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(latch.getCount() == 0);
    }
}
