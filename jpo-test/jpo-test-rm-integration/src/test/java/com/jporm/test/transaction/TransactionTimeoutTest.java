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

import org.junit.Test;

import com.jporm.commons.core.exception.JpoTransactionTimedOutException;
import com.jporm.commons.core.transaction.TransactionDefinition;
import com.jporm.rm.JPO;
import com.jporm.rm.JPOBuilder;
import com.jporm.rm.session.Session;
import com.jporm.rm.transaction.TransactionVoidCallback;
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


	@Test(expected=JpoTransactionTimedOutException.class)
	public void testTransactionSpecificTimeout() {

		//Transaction specific timeout needs to have priority over the default one.
		JPO jpo = new JPOBuilder()
		.setTransactionDefaultTimeout(5)
		.build(getTestData().getSessionProvider());

		long start = System.currentTimeMillis();
		int timeoutSeconds = 1;
		jpo.session().txVoidNow(TransactionDefinition.build(timeoutSeconds), new TransactionVoidCallback() {
			@Override
			public void doInTransaction(final Session session) {
				while (true) {
					AutoId autoId = new AutoId();
					autoId = session.save(autoId);
					getLogger().info("Saved bean with id {}", autoId.getId());
					assertNotNull(session.find(Employee.class, autoId.getId()));
					if ((System.currentTimeMillis()-start)>(1000*2*timeoutSeconds)) {
						throw new RuntimeException("A timeout should have been called before");
					}
				}
			};
		});
	}

	@Test(expected=JpoTransactionTimedOutException.class)
	public void testDefaultTransactionTimeout() {

		int timeoutSeconds = 1;

		JPO jpo = new JPOBuilder()
		.setTransactionDefaultTimeout(timeoutSeconds)
		.build(getTestData().getSessionProvider());

		long start = System.currentTimeMillis();

		jpo.session().txVoidNow(new TransactionVoidCallback() {
			@Override
			public void doInTransaction(final Session session) {
				while (true) {
					AutoId autoId = new AutoId();
					autoId = session.save(autoId);
					getLogger().info("Saved bean with id {}", autoId.getId());
					assertNotNull(session.find(Employee.class, autoId.getId()));
					if ((System.currentTimeMillis()-start)>(1000*2*timeoutSeconds)) {
						throw new RuntimeException("A timeout should have been called before");
					}
				}
			};
		});
	}

}
