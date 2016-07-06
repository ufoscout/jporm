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

import java.util.Random;

import org.junit.Test;

import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;

import rx.Single;

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

    private Single<Employee> create(final Session session) {

        final int id = new Random().nextInt(Integer.MAX_VALUE);
        final Employee employee = new Employee();
        employee.setId(id);
        employee.setAge(44);
        employee.setEmployeeNumber("empNumber" + id); //$NON-NLS-1$
        employee.setName("Wizard"); //$NON-NLS-1$
        employee.setSurname("Cina"); //$NON-NLS-1$

        return session.save(employee);
    }

    private Single<Employee> delete(final Session session, final Employee employee) {
        return session.delete(employee).map(deleteResult -> {
            assertTrue(deleteResult.deleted() == 1);
            return employee;
        });
    }

    private Single<Employee> load(final Session session, final Employee employee) {
        return session.findById(Employee.class, employee.getId()).fetchOneUnique().map(employeeLoad -> {
            assertNotNull(employeeLoad);
            assertEquals(employee.getId(), employeeLoad.getId());
            assertEquals(employee.getName(), employeeLoad.getName());
            assertEquals(employee.getSurname(), employeeLoad.getSurname());
            assertEquals(employee.getEmployeeNumber(), employeeLoad.getEmployeeNumber());
            return employeeLoad;
        });
    }

    @Test
    public void testCrudEmployee() {
        transaction(session -> {
            Single<?> action = create(session).flatMap(created -> load(session, created)).flatMap(loaded -> update(session, loaded))
                    .map(updated -> {
                assertEquals("Mage", updated.getName());
                return updated;
            }).flatMap(updated -> load(session, updated)).map(loaded -> {
                assertEquals("Mage", loaded.getName());
                return loaded;
            }).flatMap(loaded -> delete(session, loaded)).flatMap(deleted -> {
                return session.findById(Employee.class, deleted.getId()).fetchOneOptional();
            }).map(loaded -> {
                assertFalse(loaded.isPresent());
                return null;
            });
            return action.toObservable();
        });
    }

    private Single<Employee> update(final Session session, final Employee employee) {
        employee.setName("Mage");
        return session.update(employee);
    }

}
