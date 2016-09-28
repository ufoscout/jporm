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
package com.jporm.test.transaction;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.jporm.commons.core.exception.JpoTransactionTimedOutException;
import com.jporm.rx.JpoRx;
import com.jporm.rx.JpoRxBuilder;
import com.jporm.rx.connection.MaybeFunction;
import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section05.AutoId;

import rx.Single;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class TransactionTimeoutTest extends BaseTestAllDB {

    public TransactionTimeoutTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testDefaultTransactionTimeout() {

        int timeoutSeconds = 1;

        JpoRx jpo = JpoRxBuilder.get().setTransactionDefaultTimeout(timeoutSeconds).build(getTestData().getConnectionProvider());

        long start = System.currentTimeMillis();

        Single<Object> tx = jpo.tx().execute(new MaybeFunction<Object>() {

            @Override
            public Single<Object> apply(Session session) {
                while (true) {
                    try {
                        AutoId autoId = new AutoId();
                        autoId = session.save(autoId).toBlocking().value();
                        getLogger().info("Saved bean with id {}", autoId.getId());
                        if ((System.currentTimeMillis() - start) > (1000 * 2 * timeoutSeconds)) {
                            throw new RuntimeException("A timeout should have been called before");
                        }
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        boolean timeout = false;
        try {
            tx.toBlocking().value();
            fail("A timeout exception should be thrown");
        } catch (JpoTransactionTimedOutException e) {
            timeout = true;
        }

        assertTrue(timeout);
    }

    @Test
    public void testTransactionSpecificTimeout() {

        // Transaction specific timeout needs to have priority over the default
        // one.
        JpoRx jpo = JpoRxBuilder.get().setTransactionDefaultTimeout(5).build(getTestData().getConnectionProvider());

        long start = System.currentTimeMillis();
        int timeoutSeconds = 1;
        Single<Object> tx = jpo.tx().timeout(timeoutSeconds).execute(new MaybeFunction<Object>() {

            @Override
            public Single<Object> apply(Session session) {
                while (true) {
                    try {
                        AutoId autoId = new AutoId();
                        autoId = session.save(autoId).toBlocking().value();
                        getLogger().info("Saved bean with id {}", autoId.getId());
                        if ((System.currentTimeMillis() - start) > (1000 * 2 * timeoutSeconds)) {
                            throw new RuntimeException("A timeout should have been called before");
                        }
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        boolean timeout = false;
        try {
            tx.toBlocking().value();
            fail("A timeout exception should be thrown");
        } catch (JpoTransactionTimedOutException e) {
            timeout = true;
        }

        assertTrue(timeout);

    }

}
