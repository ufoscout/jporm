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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.jporm.JPO;
import com.jporm.core.JPOrm;
import com.jporm.exception.OrmNotUniqueResultException;
import com.jporm.query.find.FindQuery;
import com.jporm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;

/**
 *
 * @author Francesco Cina
 *
 * 23/giu/2011
 */
public class QueryExecutionTest extends BaseTestAllDB {

	public QueryExecutionTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testQuery1() {
		final JPOrm jpOrm = getJPOrm();
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(Employee.class);

		//		jpOrm.register(classes, true);

		final Session session =  jpOrm.session();
		final Employee employee = createEmployee(jpOrm);

		final FindQuery<Employee> query = session.findQuery(Employee.class);
		System.out.println(query.renderSql());
		System.out.println(query.renderRowCountSql());

		final List<Employee> employeeList = query.getList();
		assertNotNull( employeeList );

		final long countRowQueryResult = query.getRowCount();

		System.out.println("found employees: " + employeeList.size()); //$NON-NLS-1$
		System.out.println("count row query result: " + countRowQueryResult); //$NON-NLS-1$
		assertTrue( employeeList.size()>0 );
		assertEquals( employeeList.size(), countRowQueryResult );

		deleteEmployee(jpOrm, employee);
	}


	@Test
	public void testQuery3() {
		final JPOrm jpOrm = getJPOrm();
		jpOrm.register(Employee.class);

		final Session session =  jpOrm.session();
		final Employee employee = createEmployee(jpOrm);

		final int maxRows = 4;
		final FindQuery<Employee> query = session.findQuery(Employee.class, "e"); //$NON-NLS-1$
		query.maxRows(maxRows);
		query.where().ge("e.id", Integer.valueOf(0)); //$NON-NLS-1$
		System.out.println(query.renderSql());

		final List<Employee> employeeList = query.getList();
		assertNotNull( employeeList );

		System.out.println("found employees: " + employeeList.size()); //$NON-NLS-1$
		assertTrue( employeeList.size()>0 );
		assertTrue( employeeList.size()<=maxRows );

		deleteEmployee(jpOrm, employee);
	}

	@Test
	public void testQuery4() {
		final JPOrm jpOrm = getJPOrm();
		jpOrm.register(Employee.class);

		final Session session =  jpOrm.session();
		final Employee employee = createEmployee(jpOrm);

		session.txVoidNow((_session) -> {
			//find list with one result
			final FindQuery<Employee> query1 = session.findQuery(Employee.class);
			query1.where().eq("id", employee.getId()); //$NON-NLS-1$
			assertEquals( 1 , query1.getList().size() );

			//find list with zero result
			final FindQuery<Employee> query2 = session.findQuery(Employee.class);
			query2.where().eq("id", (-employee.getId()) ); //$NON-NLS-1$
			assertEquals( 0 , query2.getList().size() );

			//find unique query
			final FindQuery<Employee> query3 = session.findQuery(Employee.class);
			query3.where().eq("id", employee.getId()); //$NON-NLS-1$
			assertNotNull( query3.getUnique() );

			//find unique query exception
			final FindQuery<Employee> query4 = session.findQuery(Employee.class);
			query4.where().eq("id", -employee.getId()); //$NON-NLS-1$
			boolean notUniqueResultException = false;
			try{
				assertNull( query4.getUnique() );
			} catch (final OrmNotUniqueResultException e) {
				notUniqueResultException = true;
			}
			assertTrue(notUniqueResultException);

			//find unique
			assertNotNull( session.find(Employee.class, employee.getId()).getUnique() );

			//find unique exception
			notUniqueResultException = false;
			try{
				assertNull( session.find(Employee.class, -employee.getId()).getUnique() );
			} catch (final OrmNotUniqueResultException e) {
				notUniqueResultException = true;
			}
			assertTrue(notUniqueResultException);

		});

		deleteEmployee(jpOrm, employee);

	}

	private Employee createEmployee(final JPO jpOrm) {
		final Session ormSession = jpOrm.session();
		return ormSession.txNow((_session) -> {
			final int id = new Random().nextInt(Integer.MAX_VALUE);
			final Employee employee = new Employee();
			employee.setId( id );
			employee.setAge( 44 );
			employee.setEmployeeNumber( "empNumber" + id ); //$NON-NLS-1$
			employee.setName("Wizard"); //$NON-NLS-1$
			employee.setSurname("Cina"); //$NON-NLS-1$
			ormSession.save(employee);
			return employee;
		});
	}

	private void deleteEmployee(final JPO jpOrm, final Employee employee) {
		final Session ormSession = jpOrm.session();
		ormSession.txVoidNow((_session) -> {
			ormSession.delete(employee).now();
		});
	}

}
