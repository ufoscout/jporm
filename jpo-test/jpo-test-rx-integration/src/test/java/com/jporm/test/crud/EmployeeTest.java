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

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;

import com.jporm.rx.core.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class EmployeeTest extends BaseTestAllDB {


	public EmployeeTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testCrudEmployee() {
		transaction(session -> {
			CompletableFuture<?> action = create(session)
					.thenCompose(created -> load(session, created))
					.thenCompose(loaded -> update(session, loaded))
					.thenApply(updated -> {
						threadAssertEquals( "Mage", updated.getName() );
						return updated;
					})
					.thenCompose(updated -> load(session, updated))
					.thenApply(loaded -> {
						threadAssertEquals( "Mage", loaded.getName() );
						return loaded;
					})
					.thenCompose(loaded -> delete(session, loaded))
					.thenCompose(deleted -> {
						return session.find(deleted).getOptional();
					})
					.thenApply(loaded -> {
						threadAssertFalse(loaded.isPresent());
						return null;
					});
			return action;
		});
	}

	private CompletableFuture<Employee> create(Session session) {

		final int id = new Random().nextInt(Integer.MAX_VALUE);
		final Employee employee = new Employee();
		employee.setId( id );
		employee.setAge( 44 );
		employee.setEmployeeNumber( "empNumber" + id ); //$NON-NLS-1$
		employee.setName("Wizard"); //$NON-NLS-1$
		employee.setSurname("Cina"); //$NON-NLS-1$

		return session.save(employee);
	}

	private CompletableFuture<Employee> load(Session session, Employee employee) {
		return session.find(employee).get().thenApply(employeeLoad -> {
			threadAssertNotNull(employeeLoad);
			threadAssertEquals( employee.getId(), employeeLoad.getId() );
			threadAssertEquals( employee.getName(), employeeLoad.getName() );
			threadAssertEquals( employee.getSurname(), employeeLoad.getSurname() );
			threadAssertEquals( employee.getEmployeeNumber(), employeeLoad.getEmployeeNumber() );
			return employeeLoad;
		});
	}

	private CompletableFuture<Employee> update(Session session, Employee employee) {
		employee.setName("Mage");
		return session.update(employee);
	}

	private CompletableFuture<Employee> delete(Session session, Employee employee) {
		return session.delete(employee)
				.thenApply(deleteResult -> {
					threadAssertTrue(deleteResult.deleted() == 1);
					return employee;
				});
	}

}
