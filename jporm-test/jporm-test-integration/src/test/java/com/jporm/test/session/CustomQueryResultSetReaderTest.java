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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jporm.core.JPO;
import com.jporm.exception.OrmNotUniqueResultManyResultsException;
import com.jporm.exception.OrmNotUniqueResultNoResultException;
import com.jporm.query.find.CustomFindQueryWhere;
import com.jporm.session.ResultSetReader;
import com.jporm.session.ResultSetRowReader;
import com.jporm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;

/**
 *
 * @author Francesco Cina
 *
 * 02/lug/2011
 */
public class CustomQueryResultSetReaderTest extends BaseTestAllDB {

	public CustomQueryResultSetReaderTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	private Employee employee1;
	private Employee employee2;
	private Employee employee3;
	private Session session;

	@Before
	public void setUp() {
		final JPO jpOrm = getJPOrm();
		session = jpOrm.session();
		session.txVoidNow((_session) -> {
			session.deleteQuery(Employee.class).now();

			final Random random = new Random();
			employee1 = new Employee();
			employee1.setId( random.nextInt(Integer.MAX_VALUE) );
			employee1.setAge( 44 );
			employee1 = session.save(employee1);

			employee2 = new Employee();
			employee2.setId( random.nextInt(Integer.MAX_VALUE) );
			employee2.setAge( 44 );
			employee2 = session.save(employee2);

			employee3 = new Employee();
			employee3.setId( random.nextInt(Integer.MAX_VALUE) );
			employee3.setAge( 45 );
			employee3 = session.save(employee3);
		});
	}

	@After
	public void tearDown() {
		session.txVoidNow((_session) -> {
			session.delete(employee1);
			session.delete(employee2);
			session.delete(employee3);
		});
	}

	@Test
	public void testResultSetReaderWithTwoResults() {
		CustomFindQueryWhere findQuery = session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 44); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		List<Integer> findResults = findQuery.get(new ResultSetReader<List<Integer>>() {
			@Override
			public List<Integer> read(final ResultSet resultSet) throws SQLException {
				List<Integer> results = new ArrayList<Integer>();
				while (resultSet.next()) {
					results.add( resultSet.getInt("emp.id") ); //$NON-NLS-1$
				}
				return results;
			}
		});
		System.out.println("Result is " + findResults); //$NON-NLS-1$
		assertEquals( 2, findResults.size() );
		assertTrue( findResults.contains( employee1.getId() ) );
		assertTrue( findResults.contains( employee2.getId() ) );
	}

	@Test
	public void testResultSetRowReaderWithTwoResults() {
		final AtomicInteger atomicRownNum = new AtomicInteger(-1);
		CustomFindQueryWhere findQuery = session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 44); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		List<Integer> results = findQuery.get(new ResultSetRowReader<Integer>() {
			@Override
			public Integer readRow(final ResultSet rs, final int rowNum) throws SQLException {
				atomicRownNum.set(rowNum);
				return rs.getInt("emp.id"); //$NON-NLS-1$
			}
		});
		System.out.println("Result is " + results); //$NON-NLS-1$
		System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
		assertEquals( 2, results.size() );
		assertEquals( 1, atomicRownNum.get() );
		assertTrue( results.contains( employee1.getId() ) );
		assertTrue( results.contains( employee2.getId() ) );
	}

	@Test
	public void testResultSetRowReaderWithOneResult() {
		final AtomicInteger atomicRownNum = new AtomicInteger(-1);
		CustomFindQueryWhere findQuery = session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 45); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		List<Integer> results = findQuery.get(new ResultSetRowReader<Integer>() {
			@Override
			public Integer readRow(final ResultSet rs, final int rowNum) throws SQLException {
				atomicRownNum.set(rowNum);
				return rs.getInt("emp.id"); //$NON-NLS-1$
			}
		});
		System.out.println("Result is " + results); //$NON-NLS-1$
		System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
		assertEquals( 1, results.size() );
		assertEquals( 0, atomicRownNum.get() );
		assertTrue( results.contains( employee3.getId() ) );
	}

	@Test
	public void testResultSetRowReaderWithNoResult() {
		final AtomicInteger atomicRownNum = new AtomicInteger(-1);
		CustomFindQueryWhere findQuery = session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 46); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		List<Integer> results = findQuery.get(new ResultSetRowReader<Integer>() {
			@Override
			public Integer readRow(final ResultSet rs, final int rowNum) throws SQLException {
				atomicRownNum.set(rowNum);
				return rs.getInt("emp.id"); //$NON-NLS-1$
			}
		});
		System.out.println("Result is " + results); //$NON-NLS-1$
		System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
		assertEquals( 0, results.size() );
		assertEquals( -1, atomicRownNum.get() );
	}

	@Test
	public void testResultSetRowReaderUniqueWithTwoResults() {
		final AtomicInteger atomicRownNum = new AtomicInteger(-1);
		CustomFindQueryWhere findQuery = session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 44); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		try {
			findQuery.getUnique(new ResultSetRowReader<Integer>() {
				@Override
				public Integer readRow(final ResultSet rs, final int rowNum) throws SQLException {
					atomicRownNum.set(rowNum);
					return rs.getInt("emp.id"); //$NON-NLS-1$
				}
			});
			fail("an exception should be thrown before"); //$NON-NLS-1$
		} catch (OrmNotUniqueResultManyResultsException e) {
			assertTrue( e.getMessage().contains("higher") ); //$NON-NLS-1$
		}
	}

	@Test
	public void testResultSetRowReaderUniqueWithNoResults() {
		final AtomicInteger atomicRownNum = new AtomicInteger(-1);
		CustomFindQueryWhere findQuery = session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 46); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		try {
			findQuery.getUnique(new ResultSetRowReader<Integer>() {
				@Override
				public Integer readRow(final ResultSet rs, final int rowNum) throws SQLException {
					atomicRownNum.set(rowNum);
					return rs.getInt("emp.id"); //$NON-NLS-1$
				}
			});
			fail("an exception should be thrown before"); //$NON-NLS-1$
		} catch (OrmNotUniqueResultNoResultException e) {
			assertTrue( e.getMessage().contains("zero") ); //$NON-NLS-1$
		}
	}

	@Test
	public void testResultSetRowReaderUniqueWithOneResult() {
		final AtomicInteger atomicRownNum = new AtomicInteger(-1);
		CustomFindQueryWhere findQuery = session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 45); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		Integer result = findQuery.getUnique(new ResultSetRowReader<Integer>() {
			@Override
			public Integer readRow(final ResultSet rs, final int rowNum) throws SQLException {
				atomicRownNum.set(rowNum);
				return rs.getInt("emp.id"); //$NON-NLS-1$
			}
		});
		assertEquals( employee3.getId(), result );
		assertEquals( 0, atomicRownNum.get() );
	}


	@Test
	public void testCustomQueryWithMoreFields() {
		CustomFindQueryWhere findQuery = session.findQuery(new String[]{"emp.id", "emp.age"}, Employee.class, "emp").where().eq("emp.age", 44); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		List<Integer> findResults = findQuery.get(new ResultSetReader<List<Integer>>() {
			@Override
			public List<Integer> read(final ResultSet resultSet) throws SQLException {
				List<Integer> results = new ArrayList<Integer>();
				while (resultSet.next()) {
					results.add( resultSet.getInt("emp.id") ); //$NON-NLS-1$
					assertTrue( resultSet.getInt("emp.age") > 0 ); //$NON-NLS-1$
				}
				return results;
			}
		});
		System.out.println("Result is " + findResults); //$NON-NLS-1$
		assertEquals( 2, findResults.size() );
		assertTrue( findResults.contains( employee1.getId() ) );
		assertTrue( findResults.contains( employee2.getId() ) );
	}

	@Test
	public void testCustomQueryWithMoreFieldsAndAlias() {
		CustomFindQueryWhere findQuery = session.findQuery(new String[]{"emp.id as empIdAlias", "emp.age"}, Employee.class, "emp").where().eq("emp.age", 44); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		List<Integer> findResults = findQuery.get(new ResultSetReader<List<Integer>>() {
			@Override
			public List<Integer> read(final ResultSet resultSet) throws SQLException {
				List<Integer> results = new ArrayList<Integer>();
				while (resultSet.next()) {
					results.add( resultSet.getInt("empIdAlias") ); //$NON-NLS-1$
					assertTrue( resultSet.getInt("emp.age") > 0 ); //$NON-NLS-1$
				}
				return results;
			}
		});
		System.out.println("Result is " + findResults); //$NON-NLS-1$
		assertEquals( 2, findResults.size() );
		assertTrue( findResults.contains( employee1.getId() ) );
		assertTrue( findResults.contains( employee2.getId() ) );
	}

	@Test
	public void testCustomQueryWithMoreFieldsCommaSeparatedAndFunctions() {
		CustomFindQueryWhere findQuery = session.findQuery(new String[]{"emp.id as empId, MOD(emp.age, 10) as empAge"}, Employee.class, "emp").where().eq("emp.age", 44); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		List<Integer> findResults = findQuery.get(new ResultSetReader<List<Integer>>() {
			@Override
			public List<Integer> read(final ResultSet resultSet) throws SQLException {
				List<Integer> results = new ArrayList<Integer>();
				while (resultSet.next()) {
					results.add( resultSet.getInt("empId") ); //$NON-NLS-1$
					assertTrue( resultSet.getInt("empAge") > 0 ); //$NON-NLS-1$
				}
				return results;
			}
		});
		System.out.println("Result is " + findResults); //$NON-NLS-1$
		assertEquals( 2, findResults.size() );
		assertTrue( findResults.contains( employee1.getId() ) );
		assertTrue( findResults.contains( employee2.getId() ) );
	}

}
