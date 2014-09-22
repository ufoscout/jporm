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

import java.util.Random;

import org.junit.Test;

import com.jporm.JPO;
import com.jporm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.transaction.Transaction;

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
		final JPO jpOrm = getJPOrm();

		jpOrm.register(Employee.class);

		final int id = new Random().nextInt(Integer.MAX_VALUE);
		final Employee employee = new Employee();
		employee.setId( id );
		employee.setAge( 44 );
		employee.setEmployeeNumber( "empNumber" + id ); //$NON-NLS-1$
		employee.setName("Wizard"); //$NON-NLS-1$
		employee.setSurname("Cina"); //$NON-NLS-1$

		// CREATE
		final Session conn = jpOrm.session();
		Transaction tx = conn.transaction();
		conn.save(employee);
		tx.commit();

		// LOAD
		tx = conn.transaction();
		final Employee employeeLoad1 = conn.find(Employee.class, new Object[]{id}).get();
		assertNotNull(employeeLoad1);
		assertEquals( employee.getId(), employeeLoad1.getId() );
		assertEquals( employee.getName(), employeeLoad1.getName() );
		assertEquals( employee.getSurname(), employeeLoad1.getSurname() );
		assertEquals( employee.getEmployeeNumber(), employeeLoad1.getEmployeeNumber() );

		//UPDATE
		employeeLoad1.setName("Wizard"); //$NON-NLS-1$
		conn.update(employeeLoad1);
		tx.commit();

		// LOAD
		tx = conn.transaction();
		final Employee employeeLoad2 = conn.find(Employee.class, new Object[]{id}).get();
		assertNotNull(employeeLoad2);
		assertEquals( employeeLoad1.getId(), employeeLoad2.getId() );
		assertEquals( employeeLoad1.getName(), employeeLoad2.getName() );
		assertEquals( employeeLoad1.getSurname(), employeeLoad2.getSurname() );
		assertEquals( employeeLoad1.getEmployeeNumber(), employeeLoad2.getEmployeeNumber() );

		//DELETE
		conn.delete(employeeLoad2);
		final Employee employeeLoad3 = conn.find(Employee.class, new Object[]{id}).get();
		assertNull(employeeLoad3);
		tx.commit();

	}

}
