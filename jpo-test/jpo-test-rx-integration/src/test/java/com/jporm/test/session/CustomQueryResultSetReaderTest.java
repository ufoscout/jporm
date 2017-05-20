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
package com.jporm.test.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.rx.rxjava2.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.types.io.ResultEntry;

import io.reactivex.observers.TestObserver;

/**
 *
 * @author Francesco Cina
 *
 *         02/lug/2011
 */
public class CustomQueryResultSetReaderTest extends BaseTestAllDB {

    private Employee employee1;

    private Employee employee2;
    private Employee employee3;
    private Session session;

    public CustomQueryResultSetReaderTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testCustomQueryWithMoreFields() {

        transaction((Session session) -> {
            return session.find("emp.id", "emp.age").from(Employee.class, "emp").where().eq("emp.age", 44)
                    .fetchAll((entry, count) -> {
                        assertTrue(entry.getInt("emp.age") > 0); //$NON-NLS-1$
                        return entry.getInt("emp.id"); //$NON-NLS-1$
            }).buffer(100).map(results -> {
                System.out.println("Result is " + results); //$NON-NLS-1$
                assertEquals(2, results.size());
                assertTrue(results.contains(employee1.getId()));
                assertTrue(results.contains(employee2.getId()));
                return results;
            }).buffer(Integer.MAX_VALUE).firstElement();

        });

    }

    @Test
    public void testCustomQueryWithMoreFieldsAndAlias() {

        transaction((Session session) -> {
            return session.find("emp.id as empIdAlias", "emp.age").from(Employee.class, "emp").where().eq("emp.age", 44)
                    .fetchAll((entry, count) -> {
                        assertTrue(entry.getInt("emp.age") > 0); //$NON-NLS-1$
                        return entry.getInt("empIdAlias");
            }).buffer(100).map(results -> {
                System.out.println("Result is " + results); //$NON-NLS-1$
                assertEquals(2, results.size());
                assertTrue(results.contains(employee1.getId()));
                assertTrue(results.contains(employee2.getId()));
                return results;
            }).buffer(Integer.MAX_VALUE).firstOrError();

        });

    }

    @Test
    public void testCustomQueryWithMoreFieldsCommaSeparatedAndFunctions() {

        transaction((Session session) -> {
            return session.find("emp.id as empId, MOD(emp.age, 10) as empAge").from(Employee.class, "emp").where().eq("emp.age", 44)
                    .fetchAll((entry, count) -> {
                        assertTrue(entry.getInt("empAge") > 0); //$NON-NLS-1$
                        return entry.getInt("empId"); //$NON-NLS-1$
            }).buffer(100).map(findResults -> {
                System.out.println("Result is " + findResults); //$NON-NLS-1$
                assertEquals(2, findResults.size());
                assertTrue(findResults.contains(employee1.getId()));
                assertTrue(findResults.contains(employee2.getId()));
                return findResults;
            }).buffer(Integer.MAX_VALUE).firstElement();

        });
    }

    @Test
    public void testResultSetReaderWithTwoResults() {

        transaction((Session session) -> {
            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 44).fetchAll((resultEntry, count) -> {
                    return resultEntry.getInt("emp.id");
            }).buffer(100).map(findResults -> {
                System.out.println("Result is " + findResults); //$NON-NLS-1$
                assertEquals(2, findResults.size());
                assertTrue(findResults.contains(employee1.getId()));
                assertTrue(findResults.contains(employee2.getId()));
                return findResults;
            }).buffer(Integer.MAX_VALUE).firstOrError();

        });

    }

    @Test
    public void testResultSetRowReaderUniqueWithNoResults() {

        TestObserver<Integer> result = transaction(true, (Session session) -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 46)
                    .fetchOneUnique((final ResultEntry rs, final int rowNum) -> {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
            });

        });
        result.assertError(NoSuchElementException.class);
//        try {
//            result.get();
//            threadFail("an exception should be thrown before"); //$NON-NLS-1$
//        } catch (Exception e) {
//            assertTrue(e.getCause() instanceof JpoNotUniqueResultNoResultException);
//            assertTrue(e.getCause().getMessage().contains("zero")); //$NON-NLS-1$
//        }

    }

    @Test
    public void testResultSetRowReaderUniqueWithOneResult() {

        transaction((Session session) -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 45)
                    .fetchOneUnique((rs, rowNum) -> {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
            }).map(result -> {
                assertEquals(employee3.getId(), result);
                assertEquals(0, atomicRownNum.get());
                return result;
            });

        });
    }

    @Test
    public void testResultSetRowReaderUniqueWithTwoResults() {

        TestObserver<Integer> result = transaction(true, (Session session) -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 44).fetchOneUnique(new IntBiFunction<ResultEntry, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final int rowNum) {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
                }
            });

        });
        result.assertError(IllegalArgumentException.class);
//        try {
//            result.assertError(JpoNotUniqueResultManyResultsException.class);toBlocking().first();
//            fail("an exception should be thrown before"); //$NON-NLS-1$
//        } catch (Exception e) {
//            assertTrue(e.getCause() instanceof JpoNotUniqueResultManyResultsException);
//            assertTrue(e.getCause().getMessage().contains("higher")); //$NON-NLS-1$
//        }
    }

    @Test
    public void testResultSetRowReaderWithNoResult() {

        transaction((Session session) -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 46).fetchAll(new IntBiFunction<ResultEntry, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final int rowNum) {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
                }
            }).buffer(100).map(results -> {
                System.out.println("Result is " + results); //$NON-NLS-1$
                System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
                assertEquals(0, results.size());
                assertEquals(-1, atomicRownNum.get());
                return results;
            }).buffer(Integer.MAX_VALUE).firstElement();

        });

    }

    @Test
    public void testResultSetRowReaderWithOneResult() {

        transaction((Session session) -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 45).fetchAll(new IntBiFunction<ResultEntry, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final int rowNum) {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
                }
            }).buffer(100).map(results -> {
                System.out.println("Result is " + results); //$NON-NLS-1$
                System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
                assertEquals(1, results.size());
                assertEquals(0, atomicRownNum.get());
                assertTrue(results.contains(employee3.getId()));
                return results;
            }).buffer(Integer.MAX_VALUE).firstElement();

        });
    }

    @Test
    public void testResultSetRowReaderWithTwoResults() {

        transaction((Session session) -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 44).fetchAll(new IntBiFunction<ResultEntry, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final int rowNum) {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
                }
            }).buffer(100).map(results -> {
                System.out.println("Result is " + results); //$NON-NLS-1$
                System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
                assertEquals(2, results.size());
                assertEquals(1, atomicRownNum.get());
                assertTrue(results.contains(employee1.getId()));
                assertTrue(results.contains(employee2.getId()));
                return results;
            }).buffer(Integer.MAX_VALUE).firstElement();

        });

    }

    @Before
    public void testSetUp() throws InterruptedException, ExecutionException {
        session = getJPO().session();
        session.delete(Employee.class).execute().blockingGet();

        final Random random = new Random();
        employee1 = new Employee();
        employee1.setId(random.nextInt(Integer.MAX_VALUE));
        employee1.setAge(44);
        employee1 = session.save(employee1).blockingGet();

        employee2 = new Employee();
        employee2.setId(random.nextInt(Integer.MAX_VALUE));
        employee2.setAge(44);
        employee2 = session.save(employee2).blockingGet();

        employee3 = new Employee();
        employee3.setId(random.nextInt(Integer.MAX_VALUE));
        employee3.setAge(45);
        employee3 = session.save(employee3).blockingGet();
    }

    @After
    public void testTearDown() throws InterruptedException, ExecutionException {
        session.delete(employee1).blockingGet();
        session.delete(employee2).blockingGet();
        session.delete(employee3).blockingGet();
    }
}
