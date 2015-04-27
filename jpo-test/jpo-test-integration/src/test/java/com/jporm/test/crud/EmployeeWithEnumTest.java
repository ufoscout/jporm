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

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import com.jporm.core.JPO;
import com.jporm.core.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.EmployeeName;
import com.jporm.test.domain.section01.EmployeeSurname;
import com.jporm.test.domain.section01.EmployeeWithEnum;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class EmployeeWithEnumTest extends BaseTestAllDB {

	public EmployeeWithEnumTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testCrudEmployee() {
		final JPO jpOrm = getJPO();

		final int id = new Random().nextInt(Integer.MAX_VALUE);
		final EmployeeWithEnum employee = new EmployeeWithEnum();
		employee.setId( id );
		employee.setAge( 44 );
		employee.setEmployeeNumber( "empNumber" + id ); //$NON-NLS-1$
		employee.setName(EmployeeName.FRANCESCO);
		employee.setSurname(EmployeeSurname.UFO);


		final Session conn = jpOrm.session();
		conn.txVoidNow((_session) -> {
			// CREATE
			conn.save(employee);
		});

		EmployeeWithEnum employeeLoad1 = conn.txNow((_session) -> {
			// LOAD
			final EmployeeWithEnum employeeLoad = conn.find(EmployeeWithEnum.class, id).fetchUnique();
			assertNotNull(employeeLoad);
			assertEquals( employee.getId(), employeeLoad.getId() );
			assertEquals( employee.getName(), employeeLoad.getName() );
			assertEquals( employee.getSurname(), employeeLoad.getSurname() );
			assertEquals( employee.getEmployeeNumber(), employeeLoad.getEmployeeNumber() );

			//UPDATE
			employeeLoad.setName(EmployeeName.MARK);
			employeeLoad.setSurname(EmployeeSurname.TWAIN);
			return conn.update(employeeLoad);
		});

		conn.txVoidNow((_session) -> {
			// LOAD
			final EmployeeWithEnum employeeLoad2 = conn.find(EmployeeWithEnum.class, id).fetchUnique();
			assertNotNull(employeeLoad2);
			assertEquals( employeeLoad1.getId(), employeeLoad2.getId() );
			assertEquals( employeeLoad1.getName(), employeeLoad2.getName() );
			assertEquals( employeeLoad1.getSurname(), employeeLoad2.getSurname() );
			assertEquals( employeeLoad1.getEmployeeNumber(), employeeLoad2.getEmployeeNumber() );

			//DELETE
			conn.delete(employeeLoad2);
			assertFalse(conn.find(EmployeeWithEnum.class, id).fetchOptional().isPresent());
		});



	}

}
