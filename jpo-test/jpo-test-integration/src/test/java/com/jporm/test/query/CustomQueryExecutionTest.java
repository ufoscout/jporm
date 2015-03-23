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
package com.jporm.test.query;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jporm.core.JPO;
import com.jporm.core.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.ResultSetRowReader;

/**
 * @author Francesco Cina 23/giu/2011
 */
public class CustomQueryExecutionTest extends BaseTestAllDB {

	public CustomQueryExecutionTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	private Employee employee1;
	private Employee employee2;
	private Session session;

	@Before
	public void setUp() {
		final JPO jpOrm = getJPOrm();

		session = jpOrm.session();

		session.txVoidNow((_session) -> {
			session.deleteQuery(Employee.class).now();

			final Random random = new Random();
			employee1 = new Employee();
			employee1.setId(random.nextInt(Integer.MAX_VALUE));
			employee1.setAge(44);
			employee1.setEmployeeNumber("a"); //$NON-NLS-1$
			employee1 = session.save(employee1);

			employee2 = new Employee();
			employee2.setId(random.nextInt(Integer.MAX_VALUE));
			employee2.setAge(44);
			employee2.setEmployeeNumber("b"); //$NON-NLS-1$
			employee2 = session.save(employee2);
		});

	}

	@After
	public void tearDown() {
		session.txVoidNow((_session) -> {
			session.delete(employee1);
			session.delete(employee2);
			// session.delete(employee3);
		});
	}

	@Test
	public void testOrderByAsc() {
		ResultSetRowReader<String> rsrr = new ResultSetRowReader<String>() {
			@Override
			public String readRow(final ResultSet rs, final int rowNum) {
				return rs.getString("emp.employeeNumber"); //$NON-NLS-1$
			}
		};
		List<String> results = session
				.findQuery(new String[] { "emp.id", "emp.employeeNumber", "emp2.employeeNumber" }, Employee.class, "emp").join(Employee.class, "emp2").orderBy().asc("emp.employeeNumber").get(rsrr); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		assertEquals(4, results.size());
		assertEquals("a", results.get(0)); //$NON-NLS-1$
		assertEquals("a", results.get(1)); //$NON-NLS-1$
		assertEquals("b", results.get(2)); //$NON-NLS-1$
		assertEquals("b", results.get(3)); //$NON-NLS-1$

	}

	@Test
	public void testOrderByDesc() {
		ResultSetRowReader<String> rsrr = new ResultSetRowReader<String>() {
			@Override
			public String readRow(final ResultSet rs, final int rowNum) {
				return rs.getString("emp.employeeNumber"); //$NON-NLS-1$
			}
		};
		List<String> results = session
				.findQuery(new String[] { "emp.id", "emp.employeeNumber", "emp2.employeeNumber" }, Employee.class, "emp").join(Employee.class, "emp2").orderBy().desc("emp.employeeNumber").get(rsrr); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		assertEquals(4, results.size());
		assertEquals("b", results.get(0)); //$NON-NLS-1$
		assertEquals("b", results.get(1)); //$NON-NLS-1$
		assertEquals("a", results.get(2)); //$NON-NLS-1$
		assertEquals("a", results.get(3)); //$NON-NLS-1$

	}

}
