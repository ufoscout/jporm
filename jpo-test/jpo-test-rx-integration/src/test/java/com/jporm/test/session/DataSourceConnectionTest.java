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

import org.junit.Test;

import com.jporm.rx.JpoRX;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Francesco Cina
 *
 * 05/giu/2011
 */
public class DataSourceConnectionTest extends BaseTestAllDB {

    public DataSourceConnectionTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testTransactionLoop() throws InterruptedException {

        final JpoRX jpo = getJPO();
        final int howMany = 1000;
        CountDownLatch latch = new CountDownLatch(howMany);

        for (int i = 0; i < (howMany / 2); i++) {
            jpo.transaction()
                    .execute(session -> {
                        return CompletableFuture.completedFuture(null);
                    })
                    .handle((result, ex) -> {
                        latch.countDown();
                        return null;
                    });
        }

        for (int i = 0; i < (howMany / 2); i++) {
            jpo.transaction()
                    .execute(session -> {
                        throw new RuntimeException("Manually thrown exception to force rollback");
                    })
                    .handle((result, ex) -> {
                        latch.countDown();
                        return null;
                    });
        }

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testSessionActionsLoop() throws InterruptedException {

        final JpoRX jpo = getJPO();
        final int howMany = 1000;
        CountDownLatch latch = new CountDownLatch(howMany);
        Random random = new Random();

        for (int i = 0; i < (howMany / 2); i++) {
            jpo.session()
                    .find("user.firstname").from(CommonUser.class, "user").limit(1).where().ge("id", random.nextInt()).fetchString()
                    .handle((firstname, ex) -> {
                        latch.countDown();
                        return null;
                    });
        }

        for (int i = 0; i < (howMany / 2); i++) {
            jpo.session()
                    .find("user.firstname").from(CommonUser.class, "user").limit(1).where().ge("id", random.nextInt()).fetchString()
                    .thenCompose(firstname -> {
                        throw new RuntimeException("Manually thrown exception");
                    })
                    .handle((firstname, ex) -> {
                        latch.countDown();
                        return null;
                    });
        }

        latch.await(5, TimeUnit.SECONDS);

    }
}
