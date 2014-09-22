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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.jporm.query.OrmRowMapper;
import com.jporm.session.Session;
import com.jporm.session.TransactionCallback;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.User;

/**
 *
 * @author Francesco Cina
 *
 * 05/giu/2011
 */
public class QueryPaginationTest extends BaseTestAllDB {

	public QueryPaginationTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	private final int userQuantity = 100;
	private Long firstId;

	@Before
	public void setUp() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
				for (int i=0; i<userQuantity; i++) {
					User user = new User();
					user.setUserAge(Long.valueOf(i));
					user.setFirstname("name");
					user.setLastname("surname");
					user = session.save(user);

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
	public void testMaxRowsPaginationWithOrderAsc() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int maxRows = new Random().nextInt(userQuantity) + 1;

				List<User> results = session.findQuery(User.class).maxRows(maxRows).where().ge("id", firstId).orderBy().asc("id").getList();

				assertEquals( maxRows , results.size() );

				for (User user : results) {
					assertTrue(user.getId() >= firstId );
					assertTrue(user.getUserAge() < maxRows );
				}

				return null;
			}
		});
	}

	@Test
	public void testMaxRowsPaginationWithOrderDesc() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int maxRows = new Random().nextInt(userQuantity) + 1;

				List<User> results = session.findQuery(User.class).maxRows(maxRows).where().ge("id", firstId).orderBy().desc("id").getList();

				assertEquals( maxRows , results.size() );

				for (User user : results) {
					assertTrue(user.getId() >= firstId );
					assertTrue(user.getUserAge() >= (userQuantity-maxRows) );
				}

				return null;
			}
		});
	}

	@Test
	public void testFirstRowPaginationWithOrderAsc() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int firstRow = new Random().nextInt(userQuantity);

				List<User> results = session.findQuery(User.class).firstRow(firstRow).where().ge("id", firstId).orderBy().asc("id").getList();

				assertEquals( userQuantity - firstRow , results.size() );

				for (User user : results) {
					assertTrue(user.getId() >= firstId );
					assertTrue(user.getUserAge() >= firstRow );
				}

				return null;
			}
		});
	}


	@Test
	public void testFirstRowPaginationWithOrderDesc() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int firstRow = new Random().nextInt(userQuantity);

				List<User> results = session.findQuery(User.class).firstRow(firstRow).where().ge("id", firstId).orderBy().desc("id").getList();

				assertEquals( userQuantity - firstRow , results.size() );

				for (User user : results) {
					assertTrue(user.getId() >= firstId );
					assertTrue(user.getUserAge() < (userQuantity-firstRow) );

				}

				return null;
			}
		});
	}

	@Test
	public void testPaginationWithOrderAsc() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int firstRow = new Random().nextInt(userQuantity);
				int maxRows = new Random().nextInt(userQuantity - firstRow) + 1;

				List<User> results = session.findQuery(User.class).maxRows(maxRows).firstRow(firstRow).where().ge("id", firstId).orderBy().asc("id").getList();

				assertEquals( maxRows , results.size() );

				for (User user : results) {
					assertTrue(user.getId() >= firstId );
					assertTrue(user.getUserAge() >= firstRow );
					assertTrue(user.getUserAge() < (firstRow + maxRows) );
				}

				return null;
			}
		});
	}

	@Test
	public void testPaginationWithOrderDesc() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int firstRow = new Random().nextInt(userQuantity);
				int maxRows = new Random().nextInt(userQuantity - firstRow) + 1;

				final List<User> results = new ArrayList<User>();
				OrmRowMapper<User> rsr = new OrmRowMapper<User>() {
					@Override
					public void read(final User user, final int rowCount) {
						results.add(user);
					}
				};
				session.findQuery(User.class).maxRows(maxRows).firstRow(firstRow).where().ge("id", firstId).orderBy().desc("id").get(rsr);

				assertEquals( maxRows , results.size() );

				for (User user : results) {
					assertTrue(user.getId() >= firstId );
					assertTrue(user.getUserAge() < (userQuantity-firstRow) );
					assertTrue(user.getUserAge() >= ((userQuantity-firstRow) - maxRows) );

				}

				return null;
			}
		});
	}

}
