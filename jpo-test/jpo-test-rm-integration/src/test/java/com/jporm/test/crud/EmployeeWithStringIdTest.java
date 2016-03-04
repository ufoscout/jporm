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
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;

import com.jporm.rm.JpoRm;
import com.jporm.rm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.EmployeeWithStringId;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class EmployeeWithStringIdTest extends BaseTestAllDB {

    public EmployeeWithStringIdTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    public boolean isUUID(final String string) {
        try {
            UUID.fromString(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Test
    public void testCrudEmployee() {
        final JpoRm jpOrm = getJPO();

        final EmployeeWithStringId employee = new EmployeeWithStringId();
        employee.setId(UUID.randomUUID().toString());
        employee.setName("FRANCESCO");
        employee.setSurname("UFO");

        //assertNull(employee.getId());

        final Session conn = jpOrm.session();
        EmployeeWithStringId saved = jpOrm.transaction().execute((_session) -> {
            // CREATE
            return conn.save(employee);

        });

        //assertNull(employee.getId());
        assertNotNull(saved.getId());
        assertTrue(isUUID(saved.getId()));

        EmployeeWithStringId employeeLoad1 = jpOrm.transaction().execute((_session) -> {
            // LOAD
            final EmployeeWithStringId employeeLoad = conn.findById(EmployeeWithStringId.class, saved.getId()).fetchUnique();
            assertNotNull(employeeLoad);
            assertEquals(employee.getName(), employeeLoad.getName());
            assertEquals(employee.getSurname(), employeeLoad.getSurname());

            // UPDATE
            employeeLoad.setName("MARK");
            employeeLoad.setSurname("TWAIN");
            return conn.update(employeeLoad);
        });

        jpOrm.transaction().execute((_session) -> {
            // LOAD
            final EmployeeWithStringId employeeLoad2 = conn.findById(EmployeeWithStringId.class, saved.getId()).fetchUnique();
            assertNotNull(employeeLoad2);
            assertEquals(employeeLoad1.getId(), employeeLoad2.getId());
            assertEquals(employeeLoad1.getName(), employeeLoad2.getName());
            assertEquals(employeeLoad1.getSurname(), employeeLoad2.getSurname());

            // DELETE
            conn.delete(employeeLoad2);
            assertFalse(conn.findById(EmployeeWithStringId.class, saved.getId()).fetchOptional().isPresent());
        });

    }
}
