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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jporm.commons.core.exception.JpoNotUniqueResultManyResultsException;
import com.jporm.commons.core.exception.JpoNotUniqueResultNoResultException;
import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;

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

        transaction(session -> {
            return session.find("emp.id", "emp.age").from(Employee.class, "emp").where().eq("emp.age", 44).fetch(new Function<ResultSet, List<Integer>>() {
                @Override
                public List<Integer> apply(final ResultSet resultSet) {
                    List<Integer> results = new ArrayList<Integer>();
                    while (resultSet.next()) {
                        results.add(resultSet.getInt("emp.id")); //$NON-NLS-1$
                        threadAssertTrue(resultSet.getInt("emp.age") > 0); //$NON-NLS-1$
                    }
                    return results;
                }
            }).thenApply(results -> {
                System.out.println("Result is " + results); //$NON-NLS-1$
                threadAssertEquals(2, results.size());
                threadAssertTrue(results.contains(employee1.getId()));
                threadAssertTrue(results.contains(employee2.getId()));
                return null;
            });

        });

    }

    @Test
    public void testCustomQueryWithMoreFieldsAndAlias() {

        transaction(session -> {
            return session.find("emp.id as empIdAlias", "emp.age").from(Employee.class, "emp").where().eq("emp.age", 44)
                    .fetch(new Function<ResultSet, List<Integer>>() {
                @Override
                public List<Integer> apply(final ResultSet resultSet) {
                    List<Integer> results = new ArrayList<Integer>();
                    while (resultSet.next()) {
                        results.add(resultSet.getInt("empIdAlias")); //$NON-NLS-1$
                        threadAssertTrue(resultSet.getInt("emp.age") > 0); //$NON-NLS-1$
                    }
                    return results;
                }
            }).thenApply(results -> {
                System.out.println("Result is " + results); //$NON-NLS-1$
                threadAssertEquals(2, results.size());
                threadAssertTrue(results.contains(employee1.getId()));
                threadAssertTrue(results.contains(employee2.getId()));
                return null;
            });

        });

    }

    @Test
    public void testCustomQueryWithMoreFieldsCommaSeparatedAndFunctions() {

        transaction(session -> {
            return session.find("emp.id as empId, MOD(emp.age, 10) as empAge").from(Employee.class, "emp").where().eq("emp.age", 44)
                    .fetch(new Function<ResultSet, List<Integer>>() {
                @Override
                public List<Integer> apply(final ResultSet resultSet) {
                    List<Integer> results = new ArrayList<Integer>();
                    while (resultSet.next()) {
                        results.add(resultSet.getInt("empId")); //$NON-NLS-1$
                        threadAssertTrue(resultSet.getInt("empAge") > 0); //$NON-NLS-1$
                    }
                    return results;
                }
            }).thenApply(findResults -> {
                System.out.println("Result is " + findResults); //$NON-NLS-1$
                threadAssertEquals(2, findResults.size());
                threadAssertTrue(findResults.contains(employee1.getId()));
                threadAssertTrue(findResults.contains(employee2.getId()));
                return null;
            });

        });
    }

    @Test
    public void testResultSetReaderWithTwoResults() {

        transaction(session -> {
            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 44).fetch(new Function<ResultSet, List<Integer>>() {
                @Override
                public List<Integer> apply(final ResultSet resultSet) {
                    List<Integer> results = new ArrayList<Integer>();
                    while (resultSet.next()) {
                        results.add(resultSet.getInt("emp.id")); //$NON-NLS-1$
                    }
                    return results;
                }
            }).thenApply(findResults -> {
                System.out.println("Result is " + findResults); //$NON-NLS-1$
                threadAssertEquals(2, findResults.size());
                threadAssertTrue(findResults.contains(employee1.getId()));
                threadAssertTrue(findResults.contains(employee2.getId()));
                return null;
            });

        });

    }

    @Test
    public void testResultSetRowReaderUniqueWithNoResults() {

        CompletableFuture<Integer> result = transaction(true, session -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 46).fetchUnique(new BiFunction<ResultEntry, Integer, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final Integer rowNum) {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
                }
            });

        });
        try {
            result.get();
            threadFail("an exception should be thrown before"); //$NON-NLS-1$
        } catch (Exception e) {
            threadAssertTrue(e.getCause() instanceof JpoNotUniqueResultNoResultException);
            threadAssertTrue(e.getCause().getMessage().contains("zero")); //$NON-NLS-1$
        }

    }

    @Test
    public void testResultSetRowReaderUniqueWithOneResult() {

        transaction(session -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 45).fetchUnique(new BiFunction<ResultEntry, Integer, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final Integer rowNum) {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
                }
            }).thenApply(result -> {
                threadAssertEquals(employee3.getId(), result);
                threadAssertEquals(0, atomicRownNum.get());
                return null;
            });

        });
    }

    @Test
    public void testResultSetRowReaderUniqueWithTwoResults() {

        CompletableFuture<Integer> result = transaction(true, session -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 44).fetchUnique(new BiFunction<ResultEntry, Integer, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final Integer rowNum) {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
                }
            });

        });
        try {
            result.get();
            threadFail("an exception should be thrown before"); //$NON-NLS-1$
        } catch (Exception e) {
            threadAssertTrue(e.getCause() instanceof JpoNotUniqueResultManyResultsException);
            threadAssertTrue(e.getCause().getMessage().contains("higher")); //$NON-NLS-1$
        }
    }

    @Test
    public void testResultSetRowReaderWithNoResult() {

        transaction(session -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 46).fetch(new BiFunction<ResultEntry, Integer, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final Integer rowNum) {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
                }
            }).thenApply(results -> {
                System.out.println("Result is " + results); //$NON-NLS-1$
                System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
                threadAssertEquals(0, results.size());
                threadAssertEquals(-1, atomicRownNum.get());
                return null;
            });

        });

    }

    @Test
    public void testResultSetRowReaderWithOneResult() {

        transaction(session -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 45).fetch(new BiFunction<ResultEntry, Integer, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final Integer rowNum) {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
                }
            }).thenApply(results -> {
                System.out.println("Result is " + results); //$NON-NLS-1$
                System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
                threadAssertEquals(1, results.size());
                threadAssertEquals(0, atomicRownNum.get());
                threadAssertTrue(results.contains(employee3.getId()));
                return null;
            });

        });
    }

    @Test
    public void testResultSetRowReaderWithTwoResults() {

        transaction(session -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 44).fetch(new BiFunction<ResultEntry, Integer, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final Integer rowNum) {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
                }
            }).thenApply(results -> {
                System.out.println("Result is " + results); //$NON-NLS-1$
                System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
                threadAssertEquals(2, results.size());
                threadAssertEquals(1, atomicRownNum.get());
                threadAssertTrue(results.contains(employee1.getId()));
                threadAssertTrue(results.contains(employee2.getId()));
                return null;
            });

        });

    }

    @Before
    public void testSetUp() throws InterruptedException, ExecutionException {
        session = getJPO().session();
        session.delete(Employee.class).execute().get();

        final Random random = new Random();
        employee1 = new Employee();
        employee1.setId(random.nextInt(Integer.MAX_VALUE));
        employee1.setAge(44);
        employee1 = session.save(employee1).get();

        employee2 = new Employee();
        employee2.setId(random.nextInt(Integer.MAX_VALUE));
        employee2.setAge(44);
        employee2 = session.save(employee2).get();

        employee3 = new Employee();
        employee3.setId(random.nextInt(Integer.MAX_VALUE));
        employee3.setAge(45);
        employee3 = session.save(employee3).get();
    }

    @After
    public void testTearDown() throws InterruptedException, ExecutionException {
        session.delete(employee1).get();
        session.delete(employee2).get();
        session.delete(employee3).get();
    }
}
