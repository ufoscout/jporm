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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 14, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.rm.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.jporm.rm.BaseTestApi;
import com.jporm.test.domain.section01.Employee;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 14, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class SessionCRUDWithOptionalTest extends BaseTestApi {

	@Before
	public void setUp() {
		getJPO().tx().execute((Session session) -> {
			session.delete(Employee.class).execute();
		});
	}

	@Test
	public void testCRUDwithNullOptional() {
		getJPO().tx().readOnly(true).execute((Session session) -> {

			Employee employee =  new Employee();

			// Save with null Optional
			{
				employee.setId(10000);
				assertNull(employee.getEmployeeNumber());
				employee = session.save(employee);
			}

			// Fetch with null Optional
			{
				employee = session.find(Employee.class).where().eq("id", employee.getId()).fetchOne();
				assertNotNull(employee);
				assertNotNull(employee.getEmployeeNumber());
				assertFalse(employee.getEmployeeNumber().isPresent());
			}

			// Update with null Optional
			{
				employee.setEmployeeNumber(null);
				employee = session.update(employee);
				employee = session.find(Employee.class).where().eq("id", employee.getId()).fetchOne();
				assertNotNull(employee.getEmployeeNumber());
				assertFalse(employee.getEmployeeNumber().isPresent());
			}

		});

	}

	@Test
	public void testCRUDwithEmptyOptional() {
		getJPO().tx().readOnly(true).execute((Session session) -> {

			Employee employee =  new Employee();

			// Save with null Optional
			{
				employee.setId(10000);
				employee.setEmployeeNumber(Optional.empty());
				employee = session.save(employee);
			}

			// Fetch with null Optional
			{
				employee = session.find(Employee.class).where().eq("id", employee.getId()).fetchOne();
				assertNotNull(employee);
				assertNotNull(employee.getEmployeeNumber());
				assertFalse(employee.getEmployeeNumber().isPresent());
			}

			// Update with null Optional
			{
				employee.setEmployeeNumber(Optional.empty());
				employee = session.update(employee);
				employee = session.find(Employee.class).where().eq("id", employee.getId()).fetchOne();
				assertNotNull(employee.getEmployeeNumber());
				assertFalse(employee.getEmployeeNumber().isPresent());
			}

		});

	}

	@Test
	public void testCRUDwithNotEmptyOptional() {
		getJPO().tx().readOnly(true).execute((Session session) -> {

			Employee employee =  new Employee();
			final String employeeNumber = UUID.randomUUID().toString();

			// Save with valid Optional
			{
				employee.setId(10000);
				employee.setEmployeeNumber(Optional.of(employeeNumber));
				employee = session.save(employee);
			}

			// Fetch with valid Optional
			{
				employee = session.find(Employee.class).where().eq("id", employee.getId()).fetchOne();
				assertNotNull(employee);
				assertNotNull(employee.getEmployeeNumber());
				assertTrue(employee.getEmployeeNumber().isPresent());
				assertEquals(employeeNumber, employee.getEmployeeNumber().get());
			}

			// Update with empty Optional
			{
				employee.setEmployeeNumber(Optional.empty());
				employee = session.update(employee);
				employee = session.find(Employee.class).where().eq("id", employee.getId()).fetchOne();
				assertNotNull(employee.getEmployeeNumber());
				assertFalse(employee.getEmployeeNumber().isPresent());
			}

			final String newEmployeeNumber = UUID.randomUUID().toString();

			// Update with valid Optional
			{
				employee.setEmployeeNumber(Optional.of(newEmployeeNumber));
				employee = session.update(employee);
				employee = session.find(Employee.class).where().eq("id", employee.getId()).fetchOne();
				assertNotNull(employee.getEmployeeNumber());
				assertTrue(employee.getEmployeeNumber().isPresent());
				assertEquals(newEmployeeNumber, employee.getEmployeeNumber().get());

			}

		});
	}

}
