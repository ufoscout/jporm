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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Random;

import org.junit.Test;

import com.jporm.rm.JpoRm;
import com.jporm.rm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class EmployeeTest extends BaseTestAllDB {

    public EmployeeTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testCrudEmployee() {
        final JpoRm jpOrm = getJPO();

        final int id = new Random().nextInt(Integer.MAX_VALUE);
        final Employee employee = new Employee();
        employee.setId(id);
        employee.setAge(44);
        employee.setEmployeeNumber("empNumber" + id); //$NON-NLS-1$
        employee.setName("Wizard"); //$NON-NLS-1$
        employee.setSurname("Cina"); //$NON-NLS-1$

        // CREATE
        final Session conn = jpOrm.session();
        jpOrm.transaction().execute((_session) -> {
            conn.save(employee);
        });

        Employee employeeLoad1 = jpOrm.transaction().execute((_session) -> {
            // LOAD
            final Employee employeeLoad = conn.findById(Employee.class, id).fetchUnique();
            assertNotNull(employeeLoad);
            assertEquals(employee.getId(), employeeLoad.getId());
            assertEquals(employee.getName(), employeeLoad.getName());
            assertEquals(employee.getSurname(), employeeLoad.getSurname());
            assertEquals(employee.getEmployeeNumber(), employeeLoad.getEmployeeNumber());

            // UPDATE
            employeeLoad.setName("Wizard"); //$NON-NLS-1$
            return conn.update(employeeLoad);
        });

        jpOrm.transaction().execute((_session) -> {
            // LOAD
            final Employee employeeLoad = conn.findById(Employee.class, id).fetchUnique();
            assertNotNull(employeeLoad);
            assertEquals(employeeLoad1.getId(), employeeLoad.getId());
            assertEquals(employeeLoad1.getName(), employeeLoad.getName());
            assertEquals(employeeLoad1.getSurname(), employeeLoad.getSurname());
            assertEquals(employeeLoad1.getEmployeeNumber(), employeeLoad.getEmployeeNumber());

            // DELETE
            conn.delete(employeeLoad);
            assertFalse(conn.findById(Employee.class, id).fetchOptional().isPresent());
        });

    }

}
