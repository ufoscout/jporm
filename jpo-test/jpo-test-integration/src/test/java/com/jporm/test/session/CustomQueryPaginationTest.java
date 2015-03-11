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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.jporm.core.query.ResultSetReader;
import com.jporm.core.query.ResultSetRowReader;
import com.jporm.core.session.Session;
import com.jporm.core.transaction.TransactionCallback;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;

/**
 *
 * @author Francesco Cina
 *
 * 05/giu/2011
 */
public class CustomQueryPaginationTest extends BaseTestAllDB {

	public CustomQueryPaginationTest(final String testName, final TestData testData) {
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
		getJPOrm().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int maxRows = new Random().nextInt(userQuantity) + 1;

				ResultSetRowReader<Integer> rsrr = new ResultSetRowReader<Integer>() {
					@Override
					public Integer readRow(final ResultSet rs, final int rowNum) throws SQLException {
						return rs.getInt("userAge");
					}
				};
				List<Integer> results = session.findQuery(new String[]{"userAge"}, CommonUser.class, "user").maxRows(maxRows).where().ge("id", firstId).orderBy().asc("id").get(rsrr);

				assertEquals( maxRows , results.size() );

				for (Integer age : results) {
					assertTrue(age < maxRows );
				}

				return null;
			}
		});
	}

	@Test
	public void testMaxRowsPaginationWithOrderDesc() {
		getJPOrm().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int maxRows = new Random().nextInt(userQuantity) + 1;

				ResultSetRowReader<Integer> rsrr = new ResultSetRowReader<Integer>() {
					@Override
					public Integer readRow(final ResultSet rs, final int rowNum) throws SQLException {
						return rs.getInt("userAge");
					}
				};
				List<Integer> results = session.findQuery(new String[]{"userAge"}, CommonUser.class, "user").maxRows(maxRows).where().ge("id", firstId).orderBy().desc("id").get(rsrr);

				assertEquals( maxRows , results.size() );

				for (Integer age : results) {
					assertTrue(age >= (userQuantity-maxRows) );
				}

				return null;
			}
		});
	}

	@Test
	public void testFirstRowPaginationWithOrderAsc() {
		getJPOrm().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int firstRow = new Random().nextInt(userQuantity);

				ResultSetRowReader<Integer> rsrr = new ResultSetRowReader<Integer>() {
					@Override
					public Integer readRow(final ResultSet rs, final int rowNum) throws SQLException {
						return rs.getInt("userAge");
					}
				};
				List<Integer> results = session.findQuery(new String[]{"userAge"}, CommonUser.class, "user").firstRow(firstRow).where().ge("id", firstId).orderBy().asc("id").get(rsrr);

				assertEquals( userQuantity - firstRow , results.size() );

				for (Integer age : results) {
					assertTrue(age >= firstRow );
				}

				return null;
			}
		});
	}

	@Test
	public void testFirstRowPaginationWithOrderDesc() {
		getJPOrm().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int firstRow = new Random().nextInt(userQuantity);

				ResultSetRowReader<Integer> rsrr = new ResultSetRowReader<Integer>() {
					@Override
					public Integer readRow(final ResultSet rs, final int rowNum) throws SQLException {
						return rs.getInt("userAge");
					}
				};
				List<Integer> results = session.findQuery(new String[]{"userAge"}, CommonUser.class, "user").firstRow(firstRow).where().ge("id", firstId).orderBy().desc("id").get(rsrr);

				assertEquals( userQuantity - firstRow , results.size() );

				for (Integer age : results) {
					assertTrue(age < (userQuantity-firstRow) );

				}

				return null;
			}
		});
	}

	@Test
	public void testPaginationWithOrderAsc() {
		getJPOrm().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int firstRow = new Random().nextInt(userQuantity);
				int maxRows = new Random().nextInt(userQuantity - firstRow) + 1;

				ResultSetRowReader<Integer> rsrr = new ResultSetRowReader<Integer>() {
					@Override
					public Integer readRow(final ResultSet rs, final int rowNum) throws SQLException {
						return rs.getInt("userAge");
					}
				};
				List<Integer> results = session.findQuery(new String[]{"userAge"}, CommonUser.class, "user").maxRows(maxRows).firstRow(firstRow).where().ge("id", firstId).orderBy().asc("id").get(rsrr);

				assertEquals( maxRows , results.size() );

				for (Integer age : results) {
					assertTrue(age >= firstRow );
					assertTrue(age < (firstRow + maxRows) );
				}

				return null;
			}
		});
	}

	@Test
	public void testPaginationWithOrderDesc() {
		getJPOrm().session().txNow(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				int firstRow = new Random().nextInt(userQuantity);
				int maxRows = new Random().nextInt(userQuantity - firstRow) + 1;

				ResultSetReader<List<Integer>> rsrr = new ResultSetReader<List<Integer>>() {
					@Override
					public List<Integer> read(final ResultSet resultSet) throws SQLException {
						final List<Integer> results = new ArrayList<Integer>();
						while (resultSet.next()) {
							results.add(resultSet.getInt("userAge"));
						}
						return results;
					}
				};
				final List<Integer> results = session.findQuery(new String[]{"userAge"}, CommonUser.class, "user").maxRows(maxRows).firstRow(firstRow).where().ge("id", firstId).orderBy().desc("id").get(rsrr);

				assertEquals( maxRows , results.size() );

				for (Integer age : results) {
					assertTrue(age < (userQuantity-firstRow) );
					assertTrue(age >= ((userQuantity-firstRow) - maxRows) );

				}

				return null;
			}
		});
	}

}
