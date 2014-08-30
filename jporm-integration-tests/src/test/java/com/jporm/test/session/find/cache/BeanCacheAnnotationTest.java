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

import static org.junit.Assert.*;

import java.util.Random;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.jporm.JPO;
import com.jporm.session.Session;
import com.jporm.session.TransactionCallback;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CachedUser;
import com.jporm.test.domain.section08.UserAddress;
import com.jporm.test.domain.section08.UserCountry;

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

	private JPO jpo = getJPOrm();
	private String firstname = UUID.randomUUID().toString();
	private CachedUser user;
	private UserAddress address;

	@Before
	public void setUp() {
		jpo.session().doInTransaction(new TransactionCallback<Void>() {

			@Override
			public Void doInTransaction(final Session session) {
				user = new CachedUser();
				user.setFirstname(firstname);
				user.setLastname("lastname");
				user = session.save(user).now();

				getLogger().info("Created user with id [{}]", user.getId());

				{
					UserCountry country = new UserCountry();
					country.setName("Atlantis-" + new Random().nextInt());

					address = new UserAddress();
					address.setUserId(user.getId());
					address.setCountry(country);
					address = session.save(address).now();

					assertNotNull(address);
					assertNotNull(address.getCountry());
				}

				return null;
			}

		});
	}

	@Test
	public void testCacheBean() {

		jpo.session().doInTransaction(new TransactionCallback<Void>() {

			@Override
			public Void doInTransaction(final Session session) {

				//The bean should be cached automatically
				CachedUser userFromDB = session.find(CachedUser.class, user.getId()).get();

				assertNotNull(userFromDB);
				assertEquals(firstname, userFromDB.getFirstname());
				assertNotNull(userFromDB.getAddress());
				assertNotNull(userFromDB.getAddress().getCountry());
				assertEquals(address.getCountry().getId(), userFromDB.getAddress().getCountry().getId());
				assertEquals(address.getCountry().getName(), userFromDB.getAddress().getCountry().getName());

				//Delete the bean from DB
				assertTrue( session.delete(userFromDB).now() > 0) ;
				assertFalse( session.find(CachedUser.class, userFromDB.getId()).exist() );

				//Find again, it should be retrieved from the cache even if not present in the DB
				CachedUser userFromCache = session.find(CachedUser.class, user.getId()).get();

				assertNotNull(userFromCache);
				assertEquals(firstname, userFromCache.getFirstname());
				assertNotNull(userFromCache.getAddress());
				assertNotNull(userFromCache.getAddress().getCountry());
				assertEquals(address.getCountry().getId(), userFromCache.getAddress().getCountry().getId());
				assertEquals(address.getCountry().getName(), userFromCache.getAddress().getCountry().getName());

				return null;
			}
		});


	}


}
