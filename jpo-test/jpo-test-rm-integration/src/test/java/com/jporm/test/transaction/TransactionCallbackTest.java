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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
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
public class TransactionCallbackTest extends BaseTestAllDB {

    private final int repeatTests = 50;

    private JpoRm jpo;

    public TransactionCallbackTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Before
    public void setUp() {
        jpo = getJPO();
    }

    @Test
    public void testTransactionCommitted() {

        final Random random = new Random();
        final List<Employee> employees = new ArrayList<Employee>();

        for (int i = 0; i < repeatTests; i++) {
            jpo.tx().execute((Session session) -> {
                    final Employee employee = new Employee();
                    employee.setId(random.nextInt(Integer.MAX_VALUE));
                    employees.add(employee);
                    session.save(employee);
            });
        }

        for (Employee employee : employees) {
            assertNotNull(jpo.session().findById(Employee.class, employee.getId()).fetchOneOptional());
        }
    }

    @Test
    public void testTransactionRolledback() {

        final Random random = new Random();
        final List<Employee> employees = new ArrayList<Employee>();

        for (int i = 0; i < repeatTests; i++) {
            try {
                jpo.tx().execute((Session session) -> {
                        final Employee employee = new Employee();
                        employee.setId(random.nextInt(Integer.MAX_VALUE));
                        employees.add(employee);
                        session.save(employee);
                        throw new RuntimeException("manually thrown exception"); //$NON-NLS-1$
                });
            } catch (RuntimeException e) {
                // nothing to do
            }
        }

        for (Employee employee : employees) {
            assertFalse(jpo.session().findById(Employee.class, employee.getId()).fetchOneOptional().isPresent());
        }
    }

}
