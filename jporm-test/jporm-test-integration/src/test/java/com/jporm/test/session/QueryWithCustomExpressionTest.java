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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.jporm.core.query.clause.where.Exp;
import com.jporm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;
import com.jporm.transaction.TransactionCallback;

/**
 *
 * @author Francesco Cina
 *
 * 05/giu/2011
 */
public class QueryWithCustomExpressionTest extends BaseTestAllDB {

	public QueryWithCustomExpressionTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	private final int userQuantity = 100;
	private Long firstId;

	@Before
	public void setUp() {
		getJPOrm().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
				for (int i=0; i<userQuantity; i++) {
					CommonUser user = new CommonUser();
					user.setUserAge(Long.valueOf(i));
					user.setFirstname("name");
					user.setLastname("surname");
					user = session.saveQuery(user).now();

					if (i==0) {
						firstId = user.getId();
					}

				}

				return null;
			}
		});
		assertNotNull(firstId);
	}

	@Test
	public void testCustomExpression1() {
		getJPOrm().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int module = new Random().nextInt(10);

				List<CommonUser> results = session.findQuery(CommonUser.class).where("MOD(CommonUser.id, 10) = ?", module).getList();

				assertFalse( results.isEmpty() );

				for (CommonUser user : results) {
					assertTrue( (user.getId()%10) == module );
				}

				return null;
			}
		});
	}

	@Test
	public void testCustomExpression2() {
		getJPOrm().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int max = new Random().nextInt(19) + 1;
				int module = new Random().nextInt(max);

				List<CommonUser> results = session.findQuery(CommonUser.class).where(Exp.gt("id", 0)).and("CommonUser.id >= 0").and("MOD(CommonUser.id, ?) = ?", max, module).getList();

				assertFalse( results.isEmpty() );

				for (CommonUser user : results) {
					assertTrue( (user.getId()%max) == module );
				}

				return null;
			}
		});
	}

}
