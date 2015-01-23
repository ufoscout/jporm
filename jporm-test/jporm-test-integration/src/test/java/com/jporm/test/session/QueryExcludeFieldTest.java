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
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.Random;

import org.junit.Test;

import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section05.AutoId;
import com.jporm.test.domain.section08.CommonUser;
import com.jporm.transaction.TransactionCallback;
import com.jporm.transaction.TransactionalSession;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class QueryExcludeFieldTest extends BaseTestAllDB {

	public QueryExcludeFieldTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testExcludeOnFind() {
		getJPOrm().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final TransactionalSession session) {
				AutoId autoId = new AutoId();
				final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
				autoId.setValue(value);
				autoId = session.saveOrUpdate(autoId).now();

				AutoId autoIdWithoutValue = session.find(AutoId.class, autoId.getId()).ignore("value").getUnique(); //$NON-NLS-1$
				AutoId autoIdWithValue = session.find(AutoId.class, autoId.getId()).ignore(false, "value").getUnique(); //$NON-NLS-1$

				assertEquals( autoId.getId(), autoIdWithValue.getId() );
				assertNull( autoIdWithoutValue.getValue() );
				assertEquals( autoId.getId(), autoIdWithValue.getId() );
				assertEquals( value, autoIdWithValue.getValue() );

				return null;
			}
		});

	}

	@Test
	public void testGetShouldReturnFirstResultSetEntry() {
		getJPOrm().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final TransactionalSession session) {
				long suffix = new Random().nextLong();

				session.deleteQuery(CommonUser.class).now();

				CommonUser user = new CommonUser();
				user.setUserAge(0l);
				user.setFirstname("aaa" + suffix);
				user.setLastname("aaa" + suffix);
				session.save(user).now();

				user.setFirstname("bbb" + suffix);
				session.save(user).now();

				user.setFirstname("ccc" + suffix);

				assertEquals(  session.findQuery(CommonUser.class).orderBy().desc("firstname").getList().get(0).getFirstname() ,
						session.findQuery(CommonUser.class).orderBy().desc("firstname").get().get().getFirstname() );

				assertEquals(  session.findQuery(CommonUser.class).orderBy().asc("firstname").getList().get(0).getFirstname() ,
						session.findQuery(CommonUser.class).orderBy().asc("firstname").get().get().getFirstname() );

				return null;
			}
		});

	}
}
