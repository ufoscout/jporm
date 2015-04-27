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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import com.jporm.rm.session.Session;
import com.jporm.rm.transaction.TransactionCallback;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section05.AutoId;

/**
 *
 * @author Francesco Cina
 *
 * 05/giu/2011
 */
public class MaxRowsSideEffectTest extends BaseTestAllDB {

	public MaxRowsSideEffectTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	private int beanQuantity = 100;

	@Before
	public void setUp() {
		getJPO().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
				for (int i=0; i<beanQuantity; i++) {
					AutoId bean = new AutoId();
					bean.setValue(UUID.randomUUID().toString());
					session.save(bean);
				}
				return null;
			}
		});
	}

	@Test
	public void testMaxRowsSideEffect() throws InterruptedException {

		int howManyThreads = 20;

		List<Thread> runnables = new ArrayList<Thread>();
		final AtomicInteger failures = new AtomicInteger(0);

		for (int i=0; i<howManyThreads; i++) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					getJPO().session().txNow(new TransactionCallback<Void>() {
						@Override
						public Void doInTransaction(final Session session) {
							Random random = new Random();
							for (int j=0; j<20; j++) {
								int maxRows = random.nextInt(beanQuantity-1) + 1;
								int resultSize = session.findQuery(AutoId.class).limit(maxRows).fetchList().size();
								getLogger().info("Expected rows [{}], found rows [{}]", maxRows, resultSize); //$NON-NLS-1$
								boolean failure = (maxRows != resultSize );
								failure = failure || ( session.findQuery(AutoId.class).fetchList().size() < 100);
								if (failure) {
									failures.set(failures.get() + 1);
									return null;
								}
							}
							return null;
						}
					});

				}
			});
			thread.start();
			runnables.add(thread);

		}

		for (Thread thread : runnables) {
			thread.join();
		}

		assertTrue(failures.get()==0);

	}

}
