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
import java.util.concurrent.atomic.AtomicInteger;

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
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author Francesco Cina
 *
 * 02/lug/2011
 */
public class CustomQueryResultSetReaderTest extends BaseTestAllDB {

    public CustomQueryResultSetReaderTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    private Employee employee1;
    private Employee employee2;
    private Employee employee3;
    private Session session;

    @Before
    public void testSetUp() throws InterruptedException, ExecutionException {
        session = getJPO().session();
        session.deleteQuery(Employee.class).execute().get();

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

    @Test
    public void testResultSetReaderWithTwoResults() {

        transaction(session -> {
            return session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 44)
                    .fetch(new ResultSetReader<List<Integer>>() {
                        @Override
                        public List<Integer> read(final ResultSet resultSet) {
                            List<Integer> results = new ArrayList<Integer>();
                            while (resultSet.next()) {
                                results.add(resultSet.getInt("emp.id")); //$NON-NLS-1$
                            }
                            return results;
                        }
                    })
                    .thenApply(findResults -> {
                        System.out.println("Result is " + findResults); //$NON-NLS-1$
                        assertEquals(2, findResults.size());
                        assertTrue(findResults.contains(employee1.getId()));
                        assertTrue(findResults.contains(employee2.getId()));
                        return null;
                    });

        });

    }

    @Test
    public void testResultSetRowReaderWithTwoResults() {

        transaction(session -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 44)
                    .fetch(new ResultSetRowReader<Integer>() {
                        @Override
                        public Integer readRow(final ResultEntry rs, final int rowNum) {
                            atomicRownNum.set(rowNum);
                            return rs.getInt("emp.id"); //$NON-NLS-1$
                        }
                    })
                    .thenApply(results -> {
                        System.out.println("Result is " + results); //$NON-NLS-1$
                        System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
                        assertEquals(2, results.size());
                        assertEquals(1, atomicRownNum.get());
                        assertTrue(results.contains(employee1.getId()));
                        assertTrue(results.contains(employee2.getId()));
                        return null;
                    });

        });

    }

    @Test
    public void testResultSetRowReaderWithOneResult() {

        transaction(session -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 45)
                    .fetch(new ResultSetRowReader<Integer>() {
                        @Override
                        public Integer readRow(final ResultEntry rs, final int rowNum) {
                            atomicRownNum.set(rowNum);
                            return rs.getInt("emp.id"); //$NON-NLS-1$
                        }
                    })
                    .thenApply(results -> {
                        System.out.println("Result is " + results); //$NON-NLS-1$
                        System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
                        assertEquals(1, results.size());
                        assertEquals(0, atomicRownNum.get());
                        assertTrue(results.contains(employee3.getId()));
                        return null;
                    });

        });
    }

    @Test
    public void testResultSetRowReaderWithNoResult() {

        transaction(session -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 46)
                    .fetch(new ResultSetRowReader<Integer>() {
                        @Override
                        public Integer readRow(final ResultEntry rs, final int rowNum) {
                            atomicRownNum.set(rowNum);
                            return rs.getInt("emp.id"); //$NON-NLS-1$
                        }
                    })
                    .thenApply(results -> {
                        System.out.println("Result is " + results); //$NON-NLS-1$
                        System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
                        assertEquals(0, results.size());
                        assertEquals(-1, atomicRownNum.get());
                        return null;
                    });

        });

    }

    @Test
    public void testResultSetRowReaderUniqueWithTwoResults() {

        CompletableFuture<Integer> result = transaction(true, session -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 44)
                    .fetchUnique(new ResultSetRowReader<Integer>() {
                        @Override
                        public Integer readRow(final ResultEntry rs, final int rowNum) {
                            atomicRownNum.set(rowNum);
                            return rs.getInt("emp.id"); //$NON-NLS-1$
                        }
                    });

        });
        try {
            result.get();
            fail("an exception should be thrown before"); //$NON-NLS-1$
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof JpoNotUniqueResultManyResultsException);
            assertTrue(e.getCause().getMessage().contains("higher")); //$NON-NLS-1$
        }
    }

    @Test
    public void testResultSetRowReaderUniqueWithNoResults() {

        CompletableFuture<Integer> result = transaction(true, session -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 46)
                    .fetchUnique(new ResultSetRowReader<Integer>() {
                        @Override
                        public Integer readRow(final ResultEntry rs, final int rowNum) {
                            atomicRownNum.set(rowNum);
                            return rs.getInt("emp.id"); //$NON-NLS-1$
                        }
                    });

        });
        try {
            result.get();
            fail("an exception should be thrown before"); //$NON-NLS-1$
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof JpoNotUniqueResultNoResultException);
            assertTrue(e.getCause().getMessage().contains("zero")); //$NON-NLS-1$
        }

    }

    @Test
    public void testResultSetRowReaderUniqueWithOneResult() {

        transaction(session -> {
            final AtomicInteger atomicRownNum = new AtomicInteger(-1);

            return session.findQuery(new String[]{"emp.id"}, Employee.class, "emp").where().eq("emp.age", 45)
                    .fetchUnique(new ResultSetRowReader<Integer>() {
                        @Override
                        public Integer readRow(final ResultEntry rs, final int rowNum) {
                            atomicRownNum.set(rowNum);
                            return rs.getInt("emp.id"); //$NON-NLS-1$
                        }
                    })
                    .thenApply(result -> {
                        assertEquals(employee3.getId(), result);
                        assertEquals(0, atomicRownNum.get());
                        return null;
                    });

        });
    }

    @Test
    public void testCustomQueryWithMoreFields() {

        transaction(session -> {
            return session.findQuery(new String[]{"emp.id", "emp.age"}, Employee.class, "emp").where().eq("emp.age", 44)
                    .fetch(new ResultSetReader<List<Integer>>() {
                        @Override
                        public List<Integer> read(final ResultSet resultSet) {
                            List<Integer> results = new ArrayList<Integer>();
                            while (resultSet.next()) {
                                results.add(resultSet.getInt("emp.id")); //$NON-NLS-1$
                                assertTrue(resultSet.getInt("emp.age") > 0); //$NON-NLS-1$
                            }
                            return results;
                        }
                    })
                    .thenApply(results -> {
                        System.out.println("Result is " + results); //$NON-NLS-1$
                        assertEquals(2, results.size());
                        assertTrue(results.contains(employee1.getId()));
                        assertTrue(results.contains(employee2.getId()));
                        return null;
                    });

        });

    }

    @Test
    public void testCustomQueryWithMoreFieldsAndAlias() {

        transaction(session -> {
            return session.findQuery(new String[]{"emp.id as empIdAlias", "emp.age"}, Employee.class, "emp").where().eq("emp.age", 44)
                    .fetch(new ResultSetReader<List<Integer>>() {
                        @Override
                        public List<Integer> read(final ResultSet resultSet) {
                            List<Integer> results = new ArrayList<Integer>();
                            while (resultSet.next()) {
                                results.add(resultSet.getInt("empIdAlias")); //$NON-NLS-1$
                                assertTrue(resultSet.getInt("emp.age") > 0); //$NON-NLS-1$
                            }
                            return results;
                        }
                    })
                    .thenApply(results -> {
                        System.out.println("Result is " + results); //$NON-NLS-1$
                        assertEquals(2, results.size());
                        assertTrue(results.contains(employee1.getId()));
                        assertTrue(results.contains(employee2.getId()));
                        return null;
                    });

        });

    }

    @Test
    public void testCustomQueryWithMoreFieldsCommaSeparatedAndFunctions() {

        transaction(session -> {
            return session.findQuery(new String[]{"emp.id as empId, MOD(emp.age, 10) as empAge"}, Employee.class, "emp").where().eq("emp.age", 44)
                    .fetch(new ResultSetReader<List<Integer>>() {
                        @Override
                        public List<Integer> read(final ResultSet resultSet) {
                            List<Integer> results = new ArrayList<Integer>();
                            while (resultSet.next()) {
                                results.add(resultSet.getInt("empId")); //$NON-NLS-1$
                                assertTrue(resultSet.getInt("empAge") > 0); //$NON-NLS-1$
                            }
                            return results;
                        }
                    })
                    .thenApply(findResults -> {
                        System.out.println("Result is " + findResults); //$NON-NLS-1$
                        assertEquals(2, findResults.size());
                        assertTrue(findResults.contains(employee1.getId()));
                        assertTrue(findResults.contains(employee2.getId()));
                        return null;
                    });

        });
    }
}
