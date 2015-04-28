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
package com.jporm.test.session.find.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.jporm.rm.JpoRm;
import com.jporm.rm.session.Session;
import com.jporm.rm.transaction.TransactionCallback;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CachedUser;

/**
 *
 * @author cinafr
 *
 */
@SuppressWarnings("nls")
public class BeanCacheAnnotationTest extends BaseTestAllDB {

	public BeanCacheAnnotationTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	private final JpoRm jpo = getJPO();
	private final String firstname = UUID.randomUUID().toString();
	private CachedUser user;

	@Before
	public void setUp() {
		jpo.transaction().execute(new TransactionCallback<Void>() {

			@Override
			public Void doInTransaction(final Session session) {
				user = new CachedUser();
				user.setFirstname(firstname);
				user.setLastname("lastname");
				user = session.save(user);

				getLogger().info("Created user with id [{}]", user.getId());

				return null;
			}

		});
	}

	@Test
	public void testCacheBean() {

		jpo.transaction().execute(new TransactionCallback<Void>() {

			@Override
			public Void doInTransaction(final Session session) {

				//The bean should be cached automatically
				CachedUser userFromDB = session.findById(CachedUser.class, user.getId()).fetchUnique();

				assertNotNull(userFromDB);
				assertEquals(firstname, userFromDB.getFirstname());

				//Delete the bean from DB
				assertTrue( session.delete(userFromDB) > 0) ;
				assertFalse( session.findById(CachedUser.class, userFromDB.getId()).exist() );

				//Find again, it should be retrieved from the cache even if not present in the DB
				CachedUser userFromCache = session.findById(CachedUser.class, user.getId()).fetchUnique();

				assertNotNull(userFromCache);
				assertEquals(firstname, userFromCache.getFirstname());

				return null;
			}
		});


	}


}
