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
package com.jporm.test.exception;

import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.jporm.commons.core.exception.sql.JpoSqlDataIntegrityViolationException;
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
public class ConstraintViolationExceptionTest extends BaseTestAllDB {

    private JpoRm jpOrm;

    public ConstraintViolationExceptionTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Before
    public void setUp() {
        jpOrm = getJPO();
    }

    @Test
    public void testConstraintViolationException() {

        final int id = new Random().nextInt(Integer.MAX_VALUE);
        final Employee employee = new Employee();
        employee.setId(id);
        employee.setAge(44);
        employee.setEmployeeNumber(("empNumber_" + id)); //$NON-NLS-1$
        employee.setName("Wizard"); //$NON-NLS-1$
        employee.setSurname("Cina"); //$NON-NLS-1$

        // CREATE
        final Session conn = jpOrm.session();
        try {
            jpOrm.transaction().execute((_session) -> {
                conn.save(employee);
                conn.save(employee);
            });
        } catch (JpoSqlDataIntegrityViolationException e) {
            System.out.println("Constraint violation intercepted. Message [" + e.getMessage() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (Exception e) {
            fail("A specific exception should be thrown, but is " + e); //$NON-NLS-1$
        }
    }

}
