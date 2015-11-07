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

import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.test.domain.section01.EmployeeName;
import com.jporm.test.domain.section01.EmployeeSurname;
import com.jporm.test.domain.section01.EmployeeWithEnum;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class EmployeeWithEnumTest extends BaseTestAllDB {

    public EmployeeWithEnumTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    private CompletableFuture<EmployeeWithEnum> create(final Session session) {

        final int id = new Random().nextInt(Integer.MAX_VALUE);

        final EmployeeWithEnum employee = new EmployeeWithEnum();
        employee.setId(id);
        employee.setAge(44);
        employee.setEmployeeNumber("empNumber" + id); //$NON-NLS-1$
        employee.setName(EmployeeName.FRANCESCO);
        employee.setSurname(EmployeeSurname.UFO);

        return session.save(employee);
    }

    private CompletableFuture<EmployeeWithEnum> delete(final Session session, final EmployeeWithEnum employee) {
        return session.delete(employee).thenApply(deleteResult -> {
            assertTrue(deleteResult.deleted() == 1);
            return employee;
        });
    }

    private CompletableFuture<EmployeeWithEnum> load(final Session session, final EmployeeWithEnum employee) {
        return session.findById(EmployeeWithEnum.class, employee.getId()).fetch().thenApply(employeeLoad -> {
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
            CompletableFuture<EmployeeWithEnum> action = create(session).thenCompose(created -> load(session, created))
                    .thenCompose(loaded -> update(session, loaded)).thenApply(updated -> {
                assertEquals(EmployeeName.MARK, updated.getName());
                assertEquals(EmployeeSurname.TWAIN, updated.getSurname());
                return updated;
            }).thenCompose(updated -> load(session, updated)).thenApply(loaded -> {
                assertEquals(EmployeeName.MARK, loaded.getName());
                assertEquals(EmployeeSurname.TWAIN, loaded.getSurname());
                return loaded;
            }).thenCompose(loaded -> delete(session, loaded)).thenCompose(deleted -> {
                return session.findById(Employee.class, deleted.getId()).fetchOptional();
            }).thenApply(loaded -> {
                assertFalse(loaded.isPresent());
                return null;
            });

            return action;
        });

    }

    private CompletableFuture<EmployeeWithEnum> update(final Session session, final EmployeeWithEnum employee) {
        employee.setName(EmployeeName.MARK);
        employee.setSurname(EmployeeSurname.TWAIN);
        return session.update(employee);
    }

}
