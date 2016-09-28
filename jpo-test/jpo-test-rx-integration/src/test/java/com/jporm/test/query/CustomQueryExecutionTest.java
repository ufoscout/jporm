/**
 * *****************************************************************************
 * Copyright 2013 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ****************************************************************************
 */
package com.jporm.test.query;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.types.io.ResultEntry;

/**
 * @author Francesco Cina 23/giu/2011
 */
public class CustomQueryExecutionTest extends BaseTestAllDB {

    private Employee employee1;

    private Employee employee2;
    private Session session;

    public CustomQueryExecutionTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testOrderByAsc() {
        transaction(session -> {
            IntBiFunction<ResultEntry, String> rsrr = new IntBiFunction<ResultEntry, String>() {
                @Override
                public String apply(final ResultEntry rs, final int rowNum) {
                    return rs.getString("emp.employeeNumber"); //$NON-NLS-1$
                }
            };
            return session.find("emp.id", "emp.employeeNumber", "emp2.employeeNumber").from(Employee.class, "emp").join(Employee.class, "emp2").orderBy()
                    .asc("emp.employeeNumber").fetchAll(rsrr).buffer(100).map(results -> {
                assertEquals(4, results.size());
                assertEquals("a", results.get(0)); //$NON-NLS-1$
                assertEquals("a", results.get(1)); //$NON-NLS-1$
                assertEquals("b", results.get(2)); //$NON-NLS-1$
                assertEquals("b", results.get(3)); //$NON-NLS-1$
                return results;
            }).buffer(Integer.MAX_VALUE).first().toSingle();

        });

    }

    @Test
    public void testOrderByDesc() {
        transaction(session -> {
            IntBiFunction<ResultEntry, String> rsrr = new IntBiFunction<ResultEntry, String>() {
                @Override
                public String apply(final ResultEntry rs, final int rowNum) {
                    return rs.getString("emp.employeeNumber"); //$NON-NLS-1$
                }
            };
            return session.find("emp.id", "emp.employeeNumber", "emp2.employeeNumber").from(Employee.class, "emp").join(Employee.class, "emp2").orderBy()
                    .desc("emp.employeeNumber").fetchAll(rsrr).buffer(100).map(results -> {
                assertEquals(4, results.size());
                assertEquals("b", results.get(0)); //$NON-NLS-1$
                assertEquals("b", results.get(1)); //$NON-NLS-1$
                assertEquals("a", results.get(2)); //$NON-NLS-1$
                assertEquals("a", results.get(3)); //$NON-NLS-1$
                return results;
            }).buffer(Integer.MAX_VALUE).first().toSingle();

        });

    }

    @Before
    public void testSetUp() throws InterruptedException, ExecutionException {

        session = getJPO().session();

        session.delete(Employee.class).execute().toBlocking().value();

        final Random random = new Random();
        employee1 = new Employee();
        employee1.setId(random.nextInt(Integer.MAX_VALUE));
        employee1.setAge(44);
        employee1.setEmployeeNumber("a"); //$NON-NLS-1$
        employee1 = session.save(employee1).toBlocking().value();

        employee2 = new Employee();
        employee2.setId(random.nextInt(Integer.MAX_VALUE));
        employee2.setAge(44);
        employee2.setEmployeeNumber("b"); //$NON-NLS-1$
        employee2 = session.save(employee2).toBlocking().value();

    }

    @After
    public void testTearDown() throws InterruptedException, ExecutionException {
        session.delete(employee1).toBlocking().value();
        session.delete(employee2).toBlocking().value();
    }
}
