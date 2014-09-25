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
package com.jporm.test.crud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.junit.Test;

import com.jporm.dialect.DBType;
import com.jporm.session.ResultSetReader;
import com.jporm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section07.WrapperTypeTable;

/**
 *
 * @author Francesco Cina'
 *
 * Apr 17, 2012
 */
public class WrapperTypeTableTest extends BaseTestAllDB {

	public WrapperTypeTableTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testCrudWithWrapperType() {
		//Mysql timestamp doesn't store millis
		if (getTestData().getDBType().equals( DBType.MYSQL )) {
			return;
		}

		final Session conn = getJPOrm().session();

		conn.doInTransactionVoid((_session) -> {
			LocalDate endDate = LocalDate.now();
			LocalDateTime startDate = LocalDateTime.now();
			final Date now = new Date();

			WrapperTypeTable wrapper1 = new WrapperTypeTable();
			wrapper1.setNow(now);
			wrapper1.setEndDate(endDate);
			wrapper1.setStartDate(startDate);

			assertEquals( Long.valueOf(-1l), wrapper1.getId() );

			// CREATE
			wrapper1 = conn.save(wrapper1);

			System.out.println("wrapper1 id: " + wrapper1.getId()); //$NON-NLS-1$
			assertTrue( wrapper1.getId() >= Long.valueOf(0) );

			seeDBValues(conn, wrapper1.getId());

			// LOAD
			WrapperTypeTable wrapperLoad1 = conn.find(WrapperTypeTable.class, wrapper1.getId() ).get();
			assertNotNull(wrapperLoad1);
			assertEquals( wrapper1.getId(), wrapperLoad1.getId() );
			assertNull( wrapperLoad1.getValid() );
			assertEquals( now, wrapperLoad1.getNow() );
			assertEquals( startDate, wrapperLoad1.getStartDate() );
			assertEquals( endDate, wrapperLoad1.getEndDate() );

			//UPDATE
			endDate = LocalDate.now();
			startDate = LocalDateTime.now();
			final boolean valid = true;

			wrapperLoad1.setEndDate(endDate);
			wrapperLoad1.setStartDate(startDate);
			wrapperLoad1.setValid(valid);
			wrapperLoad1 = conn.update(wrapperLoad1);


			// LOAD
			final WrapperTypeTable wrapperLoad2 = conn.find(WrapperTypeTable.class, wrapper1.getId() ).get();
			assertNotNull(wrapperLoad2);
			assertEquals( wrapperLoad1.getId(), wrapperLoad2.getId() );
			assertEquals( valid, wrapperLoad2.getValid() );
			assertEquals( startDate, wrapperLoad2.getStartDate() );
			assertEquals( endDate, wrapperLoad2.getEndDate() );
			assertEquals( now, wrapperLoad1.getNow() );

			//DELETE
			conn.delete(wrapperLoad2);
			final WrapperTypeTable wrapperLoad3 = conn.find(WrapperTypeTable.class, wrapper1.getId() ).get();
			assertNull(wrapperLoad3);
		});


	}

	@Test
	public void testQueryWithWrapperType() {
		//Mysql timestamp doesn't store millis
		if (getTestData().getDBType().equals( DBType.MYSQL )) {
			return;
		}

		final Session conn = getJPOrm().session();
		conn.doInTransactionVoid((_session) -> {
			LocalDate endDate = LocalDate.now();
			LocalDateTime startDate = LocalDateTime.now();
			final Date now = new Date();

			WrapperTypeTable wrapper1 = new WrapperTypeTable();
			wrapper1.setNow(now);
			wrapper1.setEndDate(endDate);
			wrapper1.setStartDate(startDate);

			assertEquals( Long.valueOf(-1l), wrapper1.getId() );

			// CREATE
			wrapper1 = conn.save(wrapper1);

			System.out.println("wrapper1 id: " + wrapper1.getId()); //$NON-NLS-1$
			assertTrue( wrapper1.getId() >= Long.valueOf(0) );

			seeDBValues(conn, wrapper1.getId());

			// LOAD
			final WrapperTypeTable wrapperLoad1 = conn.findQuery(WrapperTypeTable.class).where().eq("startDate", startDate).eq("now", now).eq("endDate", endDate).getUnique(); //$NON-NLS-1$
			assertNotNull(wrapperLoad1);
			assertEquals( wrapper1.getId(), wrapperLoad1.getId() );
			assertNull( wrapperLoad1.getValid() );
			assertEquals( now, wrapperLoad1.getNow() );
			assertEquals( startDate, wrapperLoad1.getStartDate() );
			assertEquals( endDate, wrapperLoad1.getEndDate() );

			//UPDATE
			endDate = LocalDate.now();
			startDate = LocalDateTime.now();
			final boolean valid = true;

			//		conn.updateQuery(clazz)
			final int updated = conn.updateQuery(WrapperTypeTable.class)
					.set().eq("startDate", startDate).eq("valid", valid).eq("endDate", endDate) //$NON-NLS-1$
					.where().eq("id", wrapper1.getId()).now();

			assertEquals(1, updated);


			// LOAD
			final WrapperTypeTable wrapperLoad2 = conn.findQuery(WrapperTypeTable.class).where().eq("id", wrapper1.getId()).getUnique(); //$NON-NLS-1$

			assertNotNull(wrapperLoad2);
			assertEquals( wrapperLoad1.getId(), wrapperLoad2.getId() );
			assertEquals( valid, wrapperLoad2.getValid() );
			assertEquals( startDate, wrapperLoad2.getStartDate() );
			assertEquals( endDate, wrapperLoad2.getEndDate() );
			assertEquals( now, wrapperLoad1.getNow() );

			//DELETE
			final int deleted = conn.deleteQuery(WrapperTypeTable.class).where().eq("id", wrapper1.getId()).now(); //$NON-NLS-1$
			assertEquals(1, deleted);

			assertTrue( conn.findQuery(WrapperTypeTable.class).where().eq("id", wrapper1.getId()).getList().isEmpty() ); //$NON-NLS-1$
		});


	}

	private void seeDBValues(final Session conn, final Long id) {
		final ResultSetReader<Object> rse = new ResultSetReader<Object>() {

			@Override
			public Object read(final ResultSet resultSet) throws SQLException {

				while(resultSet.next()) {
					System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++"); //$NON-NLS-1$
					System.out.println("Object found:"); //$NON-NLS-1$
					System.out.println("id: " + resultSet.getLong("id")); //$NON-NLS-1$ //$NON-NLS-2$
					System.out.println("now: " + resultSet.getTimestamp("now")); //$NON-NLS-1$ //$NON-NLS-2$
					System.out.println("start_date: " + resultSet.getTimestamp("start_date")); //$NON-NLS-1$ //$NON-NLS-2$
					System.out.println("end_date: " + resultSet.getTimestamp("end_date")); //$NON-NLS-1$ //$NON-NLS-2$
					System.out.println("valid: " + resultSet.getBigDecimal("valid")); //$NON-NLS-1$ //$NON-NLS-2$
					System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++"); //$NON-NLS-1$
				}

				return null;
			}
		};
		conn.sqlExecutor().query("select * from WRAPPER_TYPE_TABLE", rse); //$NON-NLS-1$

	}



}
