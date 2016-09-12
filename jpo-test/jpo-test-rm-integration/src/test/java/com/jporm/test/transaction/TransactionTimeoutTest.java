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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.jporm.commons.core.exception.JpoTransactionTimedOutException;
import com.jporm.rm.JpoRm;
import com.jporm.rm.JpoRmBuilder;
import com.jporm.rm.JpoRmImpl;
import com.jporm.rm.connection.ConnectionProvider;
import com.jporm.rm.session.Session;
import com.jporm.rm.spring.JdbcTemplateConnectionProvider;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.test.domain.section05.AutoId;

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

        ConnectionProvider connProvider = ((JpoRmImpl) getTestData().getJpo()).getTransactionProvider();
        if (connProvider instanceof JdbcTemplateConnectionProvider) {
            return;
        }

        int timeoutSeconds = 1;

        JpoRm jpo = JpoRmBuilder.get().setTransactionDefaultTimeout(timeoutSeconds).build(connProvider);

        long start = System.currentTimeMillis();

        try {
            jpo.tx().execute((Session session) -> {
                    while (true) {
                        AutoId autoId = new AutoId();
                        autoId = session.save(autoId);
                        getLogger().info("Saved bean with id {}", autoId.getId());
                        assertNotNull(session.findById(Employee.class, autoId.getId()));
                        if ((System.currentTimeMillis() - start) > (1000 * 2 * timeoutSeconds)) {
                            throw new RuntimeException("A timeout should have been called before");
                        }
                }
            });
            fail("A timeout should have been thrown");
        } catch (JpoTransactionTimedOutException e) {
            // OK
        }
    }

    @Test
    public void testTransactionSpecificTimeout() {

        ConnectionProvider connProvider = ((JpoRmImpl) getTestData().getJpo()).getTransactionProvider();
        if (connProvider instanceof JdbcTemplateConnectionProvider) {
            return;
        }

        // Transaction specific timeout needs to have priority over the default
        // one.
        JpoRm jpo = JpoRmBuilder.get().setTransactionDefaultTimeout(5).build(connProvider);

        long start = System.currentTimeMillis();
        int timeoutSeconds = 1;
        try {
            jpo.tx().timeout(timeoutSeconds).execute((final Session session) -> {
                    while (true) {
                        AutoId autoId = new AutoId();
                        autoId = session.save(autoId);
                        getLogger().info("Saved bean with id {}", autoId.getId());
                        assertNotNull(session.findById(Employee.class, autoId.getId()));
                        if ((System.currentTimeMillis() - start) > (1000 * 2 * timeoutSeconds)) {
                            throw new RuntimeException("A timeout should have been called before");
                        }
                }
            });
            fail("A timeout should have been thrown");
        } catch (JpoTransactionTimedOutException e) {
            // OK
        }
    }

}
