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
package com.jporm.test.transaction;

import java.util.Random;

import org.junit.Test;

import com.jporm.rx.JpoRx;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class EmployeeTransactionTest extends BaseTestAllDB {

    public EmployeeTransactionTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testTransaction1() throws Exception {
        final JpoRx jpOrm = getJPO();

        final int id = new Random().nextInt(Integer.MAX_VALUE);
        final Employee employee = new Employee();
        employee.setId(id);
        employee.setAge(44);
        employee.setEmployeeNumber(("empNumber_" + id)); //$NON-NLS-1$
        employee.setName("Wizard"); //$NON-NLS-1$
        employee.setSurname("Cina"); //$NON-NLS-1$

        // CREATE
        try {
            jpOrm.transaction().execute(session -> {
                return session.save(employee).map(empl -> {
                    throw new RuntimeException();
                });
            }).get();
            threadFail("It should throw an exception before");
        } catch (Exception e) {
            // ok!
        }

        // LOAD
        assertFalse(jpOrm.session().findById(Employee.class, id).fetchOneOptional().get().isPresent());

    }

}
