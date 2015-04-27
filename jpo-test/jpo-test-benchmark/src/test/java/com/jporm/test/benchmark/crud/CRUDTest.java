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
package com.jporm.test.benchmark.crud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import com.jporm.core.JPO;
import com.jporm.core.JPOBuilder;
import com.jporm.core.query.find.FindQuery;
import com.jporm.core.session.Session;
import com.jporm.test.benchmark.BaseTestBenchmark;
import com.jporm.test.benchmark.BenchmarkData;
import com.jporm.test.domain.section01.Employee;

/**
 *
 * @author cinafr
 *
 */
public class CRUDTest extends BaseTestBenchmark {

	final int howManyEmployee = 1000;
	final int tries = 2;

	@Test
	public void testCRUD() {
		if (!isEnabled()) {
			return;
		}

		Date now = new Date();

		for (BenchmarkData data : getBenchmarkData()) {
			for (int i=0; i<tries; i++) {
				now = new Date();

				final JPO jdbcTemplateH2 = new JPOBuilder().build(data.getJdbcTemplateSessionProvider());
				now = new Date();
				doCRUD(jdbcTemplateH2, howManyEmployee);
				System.out.println(data.getDbData().getDBType() + " - JPOrm - JdbcTemplate - Execution time for " + howManyEmployee + " employee = " + (new Date().getTime() - now.getTime()) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

				final JPO datasourceH2 = new JPOBuilder().build(data.getDataSourceSessionProvider()) ;
				now = new Date();
				doCRUD(datasourceH2, howManyEmployee);
				System.out.println(data.getDbData().getDBType() + " - JPOrm - DataSource - Execution time for " + howManyEmployee + " employee = " + (new Date().getTime() - now.getTime()) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
	}

	private <T> void doCRUD(final JPO jpOrm, final int howMany) {
		StopWatch stopWatch = new Log4JStopWatch();

		final int baseId = new Random().nextInt();
		final int age = new Random().nextInt();
		final String employeeNumber = "empNumber" + baseId; //$NON-NLS-1$
		final String employeeName = "Wizard" + age; //$NON-NLS-1$
		final String surname = "Cina" + age; //$NON-NLS-1$

		final List<Employee> employees = new ArrayList<Employee>();
		final Integer[] ids = new Integer[howMany];

		for (int i=0; i<howMany; i++) {
			final int id = baseId + i;
			ids[i] = id;
			final Employee employee = new Employee();
			employee.setId( id );
			employee.setAge( age );
			employee.setEmployeeNumber( employeeNumber );
			employee.setName( employeeName );
			employee.setSurname( surname );

			employees.add(employee);
		}
		stopWatch.lap("JPO_prepare"); //$NON-NLS-1$
		// CREATE
		final Session conn = jpOrm.session();
		conn.txVoidNow((session) -> {
			conn.save(employees);
		});

		stopWatch.lap("JPO_save"); //$NON-NLS-1$

		// LOAD
		final String newName = "newName"; //$NON-NLS-1$
		conn.txVoidNow((session) -> {
			final List<Employee> employeesLoaded = new ArrayList<Employee>();
			for (final Integer id : ids) {
				final Employee empl = conn.find(Employee.class, id ).fetchUnique();
				assertNotNull(empl);
				assertEquals( id , empl.getId() );
				assertEquals( employeeName , empl.getName() );
				assertEquals( surname, empl.getSurname() );
				assertEquals( employeeNumber, empl.getEmployeeNumber() );
				empl.setName(newName);
				employeesLoaded.add( empl );
			}
			stopWatch.lap("JPO_load1"); //$NON-NLS-1$

			//UPDATE
			conn.update(employeesLoaded);
			stopWatch.lap("JPO_update1"); //$NON-NLS-1$
		});



		conn.txVoidNow((session) -> {
			// LOAD WITH QUERY
			FindQuery<Employee> query = conn.findQuery(Employee.class);
			query.where().in("id", ids); //$NON-NLS-1$
			final List<Employee> employeesLoaded2 = query.fetchList();

			assertEquals(howMany, employeesLoaded2.size());

			for (final Employee empl : employeesLoaded2) {
				assertNotNull(empl);
				assertEquals( newName , empl.getName() );
				assertEquals( surname, empl.getSurname() );
				assertEquals( employeeNumber, empl.getEmployeeNumber() );
			}
			stopWatch.lap("JPO_load2"); //$NON-NLS-1$

			//DELETE
			conn.delete(employeesLoaded2);
		});

		stopWatch.lap("JPO_delete"); //$NON-NLS-1$

		conn.txVoidNow((session) -> {
			FindQuery<Employee> query = conn.findQuery(Employee.class);
			query.where().in("id", ids); //$NON-NLS-1$
			final List<Employee> employeesLoaded3 = query.fetchList();
			assertTrue(employeesLoaded3.isEmpty());
		});

		stopWatch.lap("JPO_verify"); //$NON-NLS-1$
	}

}
