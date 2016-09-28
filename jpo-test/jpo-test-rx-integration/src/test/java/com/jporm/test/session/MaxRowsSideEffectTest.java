/*******************************************************************************
 * Copyright 2013 Francesco Cina'
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
package com.jporm.test.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section05.AutoId;

import rx.Completable;
import rx.Single;
import rx.observers.TestSubscriber;

/**
 *
 * @author Francesco Cina
 *
 *         05/giu/2011
 */
public class MaxRowsSideEffectTest extends BaseTestAllDB {

    private int beanQuantity = 100;

    public MaxRowsSideEffectTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testMaxRowsSideEffect() throws InterruptedException {

        int howManyThreads = 120;

        List<Thread> runnables = new ArrayList<>();
        final AtomicInteger failures = new AtomicInteger(0);
        final AtomicInteger executed = new AtomicInteger(0);
        for (int i = 0; i < howManyThreads; i++) {
            Thread thread = new Thread(() -> {
                TestSubscriber<Object> subscriber = new TestSubscriber<>();
                getJPO().tx().execute((Session session) -> {
                    executed.getAndIncrement();
                    Random random = new Random();
                    for (int j = 0; j < 20; j++) {
                        int maxRows = random.nextInt(beanQuantity - 1) + 1;
                        int resultSize = session.find(AutoId.class).limit(maxRows).fetchAll().buffer(1000).toBlocking().first().size();
                        getLogger().info("Expected rows [{}], found rows [{}]", maxRows, resultSize); //$NON-NLS-1$
                        boolean failure = (maxRows != resultSize);
                        failure = failure || (session.find(AutoId.class).fetchAll().buffer(100).toBlocking().first().size() < 100);
                        if (failure) {
                            failures.getAndIncrement();
                            return null;
                        }
                    }
                    return Completable.complete();
                }).subscribe(subscriber);
                subscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
            });
            runnables.add(thread);
            thread.start();

        }

        int count = 0;
        for (Thread thread : runnables) {
            getLogger().debug("wait for {}", count++);
            thread.join();
        }

        assertEquals(howManyThreads, executed.get());
        assertEquals(0, failures.get());

    }

    @Before
    public void testSetUp() throws InterruptedException, ExecutionException {
        transaction((Session session) -> {
            for (int i = 0; i < beanQuantity; i++) {
                AutoId bean = new AutoId();
                bean.setValue(UUID.randomUUID().toString());
                assertNotNull(session.save(bean).toBlocking().value());
            }
            return Single.just("");
        });
    }

}
