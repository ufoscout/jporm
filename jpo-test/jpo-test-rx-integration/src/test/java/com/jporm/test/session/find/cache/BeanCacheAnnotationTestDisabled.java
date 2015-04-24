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

import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import com.jporm.rx.JpoRX;
import com.jporm.rx.core.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CachedUser;

/**
 *
 * @author cinafr
 *
 */
@SuppressWarnings("nls")
@Ignore
//THIS TEST REQUIRE THE CACHE THAT IS NOT CURRENTLY IMPLEMENTED
public class BeanCacheAnnotationTestDisabled extends BaseTestAllDB {

	public BeanCacheAnnotationTestDisabled(final String testName, final TestData testData) {
		super(testName, testData);
	}

	private final String firstname = UUID.randomUUID().toString();

	@Test
	public void testCacheBean() throws InterruptedException {

		JpoRX jpo = getJPO();

		jpo.transaction().now(session -> {
			CachedUser user = new CachedUser();
			user.setFirstname(firstname);
			user.setLastname("lastname");
			return session.save(user);
		})
		.thenCompose(cachedUser -> {
			try {
				Session session = jpo.session();
				//The bean should be cached automatically
				CachedUser userFromDB;
				userFromDB = session.find(CachedUser.class, cachedUser.getId()).getUnique().get();

				assertNotNull(userFromDB);
				assertEquals(firstname, userFromDB.getFirstname());

				//Delete the bean from DB
				assertTrue( session.delete(userFromDB).get().deleted() > 0) ;
				assertFalse( session.find(CachedUser.class, userFromDB.getId()).exist().get() );

				//Find again, it should be retrieved from the cache even if not present in the DB
				CachedUser userFromCache = session.find(CachedUser.class, cachedUser.getId()).getUnique().get();

				assertNotNull(userFromCache);
				assertEquals(firstname, userFromCache.getFirstname());
				testComplete();
				return null;
			} catch (Exception e) {
				fail(e.getMessage());
				throw new RuntimeException(e);
			}
		});

		await();

	}


}
