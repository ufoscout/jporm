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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.Random;

import org.junit.Test;

import com.jporm.rx.query.find.CustomFindQuery;
import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.test.domain.section04.Zoo_People;

import io.reactivex.Single;

/**
 *
 * @author Francesco Cina
 *
 *         23/giu/2011
 */
public class QueryExecutionMultipleSchemaTest extends BaseTestAllDB {

	public QueryExecutionMultipleSchemaTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	private Single<Employee> createEmployee(final Session session, final int id) {
		final Employee employee = new Employee();
		employee.setId(id);
		employee.setAge(44);
		employee.setEmployeeNumber(Optional.of("empNumber" + id)); //$NON-NLS-1$
		employee.setName("Wizard"); //$NON-NLS-1$
		employee.setSurname("Cina"); //$NON-NLS-1$
		return session.save(employee);
	}

	private Single<Employee> deleteEmployee(final Session session, final int id) {
		final Employee employee = new Employee();
		employee.setId(id);
		return session.delete(employee).map(fn -> {
			assertTrue(fn.deleted() > 0);
			return employee;
		});
	}

	@Test
	public void testQuery2() {

		if (!getTestData().isSupportMultipleSchemas()) {
			return;
		}

		final int maxRows = 4;
		final int id = new Random().nextInt(Integer.MAX_VALUE);

		transaction((Session session) -> {
			return createEmployee(session, id).flatMapObservable(employee -> {

				final CustomFindQuery<Employee> query = session.find(Employee.class, "em");
				query.join(Zoo_People.class, "zp"); //$NON-NLS-1$
				query.limit(maxRows);
				query.where().not().le("em.id", 0); //$NON-NLS-1$
				query.where().ilike("zp.firstname", "%"); //$NON-NLS-1$ //$NON-NLS-2$
				return query.fetchAll();
			}).buffer(1000).flatMap(employees -> {
				assertNotNull(employees);

				getLogger().info("found employees: " + employees.size()); //$NON-NLS-1$
				assertTrue(employees.size() <= maxRows);

				return deleteEmployee(session, id).toObservable();
			}).buffer(Integer.MAX_VALUE).singleElement();
		});

	}

}
