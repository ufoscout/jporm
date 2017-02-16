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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import com.jporm.rm.JpoRm;
import com.jporm.rm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.ImmutableUserValue;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class ImmutablesCRUDTest extends BaseTestAllDB {

	public ImmutablesCRUDTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testCrud() {
		final JpoRm jpOrm = getJPO();

		final ImmutableUserValue user = ImmutableUserValue.builder()
				.firstname("first")
				.lastname("last")
				.userAge(new Random().nextLong())
				.build();

		// CREATE
		final Session conn = jpOrm.session();
		final ImmutableUserValue savedUser = jpOrm.tx().execute((_session) -> {
			return conn.save(user);
		});

		assertNotNull(savedUser);
		assertNotSame(user, savedUser);
		assertTrue(savedUser.id().isPresent());
		assertEquals(user.firstname(), savedUser.firstname());
		assertEquals(user.lastname(), savedUser.lastname());
		assertEquals(user.userAge(), savedUser.userAge());
		assertTrue(savedUser.version().isPresent());

		// Add missing CRUS pieces
		final int completesMe;

		/*
		final Employee employeeLoad1 = jpOrm.tx().execute((_session) -> {
			// LOAD
			final Employee employeeLoad = conn.findById(Employee.class, id).fetchOneUnique();
			assertNotNull(employeeLoad);
			assertEquals(employee.getId(), employeeLoad.getId());
			assertEquals(employee.getName(), employeeLoad.getName());
			assertEquals(employee.getSurname(), employeeLoad.getSurname());
			assertEquals(employee.getEmployeeNumber().get(), employeeLoad.getEmployeeNumber().get());

			// UPDATE
			employeeLoad.setName("Wizard"); //$NON-NLS-1$
			return conn.update(employeeLoad);
		});

		jpOrm.tx().execute((_session) -> {
			// LOAD
			final Employee employeeLoad = conn.findById(Employee.class, id).fetchOneUnique();
			assertNotNull(employeeLoad);
			assertEquals(employeeLoad1.getId(), employeeLoad.getId());
			assertEquals(employeeLoad1.getName(), employeeLoad.getName());
			assertEquals(employeeLoad1.getSurname(), employeeLoad.getSurname());
			assertEquals(employeeLoad1.getEmployeeNumber().get(), employeeLoad.getEmployeeNumber().get());

			// DELETE
			conn.delete(employeeLoad);
			assertFalse(conn.findById(Employee.class, id).fetchOneOptional().isPresent());
		});
		 */
	}

}
