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
package com.jporm.test.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jporm.commons.core.exception.JpoNotUniqueResultManyResultsException;
import com.jporm.commons.core.exception.JpoNotUniqueResultNoResultException;
import com.jporm.rm.JpoRm;
import com.jporm.rm.query.find.CustomResultFindQueryWhere;
import com.jporm.rm.session.Session;
import com.jporm.sql.dialect.DBType;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

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

    @Before
    public void setUp() {
        final JpoRm jpOrm = getJPO();
        session = jpOrm.session();
        jpOrm.transaction().executeVoid((_session) -> {
            session.delete(Employee.class).execute();

            final Random random = new Random();
            employee1 = new Employee();
            employee1.setId(random.nextInt(Integer.MAX_VALUE));
            employee1.setAge(44);
            employee1 = session.save(employee1);

            employee2 = new Employee();
            employee2.setId(random.nextInt(Integer.MAX_VALUE));
            employee2.setAge(44);
            employee2 = session.save(employee2);

            employee3 = new Employee();
            employee3.setId(random.nextInt(Integer.MAX_VALUE));
            employee3.setAge(45);
            employee3 = session.save(employee3);
        });
    }

    @After
    public void tearDown() {
        getJPO().transaction().executeVoid((_session) -> {
            session.delete(employee1);
            session.delete(employee2);
            session.delete(employee3);
        });
    }

    @Test
    public void testCustomQueryWithMoreFields() {
        CustomResultFindQueryWhere findQuery = session.find("emp.id", "emp.age").from(Employee.class, "emp").where().eq("emp.age", 44); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        List<Integer> findResults = findQuery.fetch(new ResultSetReader<List<Integer>>() {
            @Override
            public List<Integer> read(final ResultSet resultSet) {
                List<Integer> results = new ArrayList<Integer>();
                while (resultSet.next()) {
                    results.add(resultSet.getInt("emp.id")); //$NON-NLS-1$
                    assertTrue(resultSet.getInt("emp.age") > 0); //$NON-NLS-1$
                }
                return results;
            }
        });
        System.out.println("Result is " + findResults); //$NON-NLS-1$
        assertEquals(2, findResults.size());
        assertTrue(findResults.contains(employee1.getId()));
        assertTrue(findResults.contains(employee2.getId()));
    }

    @Test
    public void testCustomQueryWithMoreFieldsAndAlias() {
        CustomResultFindQueryWhere findQuery = session.find("emp.id as empIdAlias", "emp.age").from(Employee.class, "emp").where().eq("emp.age", 44); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        List<Integer> findResults = findQuery.fetch(new ResultSetReader<List<Integer>>() {
            @Override
            public List<Integer> read(final ResultSet resultSet) {
                List<Integer> results = new ArrayList<Integer>();
                while (resultSet.next()) {
                    results.add(resultSet.getInt("empIdAlias")); //$NON-NLS-1$
                    assertTrue(resultSet.getInt("emp.age") > 0); //$NON-NLS-1$
                }
                return results;
            }
        });
        System.out.println("Result is " + findResults); //$NON-NLS-1$
        assertEquals(2, findResults.size());
        assertTrue(findResults.contains(employee1.getId()));
        assertTrue(findResults.contains(employee2.getId()));
    }

    @Test
    public void testCustomQueryWithMoreFieldsCommaSeparatedAndFunctions() {
        //SQL Server does not support the MOD function
        if (DBType.SQLSERVER12.equals(getTestData().getDBType())) {
            return;
        }
        CustomResultFindQueryWhere findQuery = session.find("emp.id as empId, MOD(emp.age, 10) as empAge").from(Employee.class, "emp").where().eq("emp.age", 44); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        List<Integer> findResults = findQuery.fetch(new ResultSetReader<List<Integer>>() {
            @Override
            public List<Integer> read(final ResultSet resultSet) {
                List<Integer> results = new ArrayList<Integer>();
                while (resultSet.next()) {
                    results.add(resultSet.getInt("empId")); //$NON-NLS-1$
                    assertTrue(resultSet.getInt("empAge") > 0); //$NON-NLS-1$
                }
                return results;
            }
        });
        System.out.println("Result is " + findResults); //$NON-NLS-1$
        assertEquals(2, findResults.size());
        assertTrue(findResults.contains(employee1.getId()));
        assertTrue(findResults.contains(employee2.getId()));
    }

    @Test
    public void testResultSetReaderWithTwoResults() {
        CustomResultFindQueryWhere findQuery = session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 44); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        List<Integer> findResults = findQuery.fetch(new ResultSetReader<List<Integer>>() {
            @Override
            public List<Integer> read(final ResultSet resultSet) {
                List<Integer> results = new ArrayList<Integer>();
                while (resultSet.next()) {
                    results.add(resultSet.getInt("emp.id")); //$NON-NLS-1$
                }
                return results;
            }
        });
        System.out.println("Result is " + findResults); //$NON-NLS-1$
        assertEquals(2, findResults.size());
        assertTrue(findResults.contains(employee1.getId()));
        assertTrue(findResults.contains(employee2.getId()));
    }

    @Test
    public void testResultSetRowReaderUniqueWithNoResults() {
        final AtomicInteger atomicRownNum = new AtomicInteger(-1);
        CustomResultFindQueryWhere findQuery = session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 46); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        try {
            findQuery.fetchUnique(new ResultSetRowReader<Integer>() {
                @Override
                public Integer readRow(final ResultEntry rs, final int rowNum) {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
                }
            });
            fail("an exception should be thrown before"); //$NON-NLS-1$
        } catch (JpoNotUniqueResultNoResultException e) {
            assertTrue(e.getMessage().contains("zero")); //$NON-NLS-1$
        }
    }

    @Test
    public void testResultSetRowReaderUniqueWithOneResult() {
        final AtomicInteger atomicRownNum = new AtomicInteger(-1);
        CustomResultFindQueryWhere findQuery = session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 45); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Integer result = findQuery.fetchUnique(new ResultSetRowReader<Integer>() {
            @Override
            public Integer readRow(final ResultEntry rs, final int rowNum) {
                atomicRownNum.set(rowNum);
                return rs.getInt("emp.id"); //$NON-NLS-1$
            }
        });
        assertEquals(employee3.getId(), result);
        assertEquals(0, atomicRownNum.get());
    }

    @Test
    public void testResultSetRowReaderUniqueWithTwoResults() {
        final AtomicInteger atomicRownNum = new AtomicInteger(-1);
        CustomResultFindQueryWhere findQuery = session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 44); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        try {
            findQuery.fetchUnique(new ResultSetRowReader<Integer>() {
                @Override
                public Integer readRow(final ResultEntry rs, final int rowNum) {
                    atomicRownNum.set(rowNum);
                    return rs.getInt("emp.id"); //$NON-NLS-1$
                }
            });
            fail("an exception should be thrown before"); //$NON-NLS-1$
        } catch (JpoNotUniqueResultManyResultsException e) {
            assertTrue(e.getMessage().contains("higher")); //$NON-NLS-1$
        }
    }

    @Test
    public void testResultSetRowReaderWithNoResult() {
        final AtomicInteger atomicRownNum = new AtomicInteger(-1);
        CustomResultFindQueryWhere findQuery = session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 46); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        List<Integer> results = findQuery.fetch(new ResultSetRowReader<Integer>() {
            @Override
            public Integer readRow(final ResultEntry rs, final int rowNum) {
                atomicRownNum.set(rowNum);
                return rs.getInt("emp.id"); //$NON-NLS-1$
            }
        });
        System.out.println("Result is " + results); //$NON-NLS-1$
        System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
        assertEquals(0, results.size());
        assertEquals(-1, atomicRownNum.get());
    }

    @Test
    public void testResultSetRowReaderWithOneResult() {
        final AtomicInteger atomicRownNum = new AtomicInteger(-1);
        CustomResultFindQueryWhere findQuery = session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 45); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        List<Integer> results = findQuery.fetch(new ResultSetRowReader<Integer>() {
            @Override
            public Integer readRow(final ResultEntry rs, final int rowNum) {
                atomicRownNum.set(rowNum);
                return rs.getInt("emp.id"); //$NON-NLS-1$
            }
        });
        System.out.println("Result is " + results); //$NON-NLS-1$
        System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
        assertEquals(1, results.size());
        assertEquals(0, atomicRownNum.get());
        assertTrue(results.contains(employee3.getId()));
    }

    @Test
    public void testResultSetRowReaderWithTwoResults() {
        final AtomicInteger atomicRownNum = new AtomicInteger(-1);
        CustomResultFindQueryWhere findQuery = session.find("emp.id").from(Employee.class, "emp").where().eq("emp.age", 44); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        List<Integer> results = findQuery.fetch(new ResultSetRowReader<Integer>() {
            @Override
            public Integer readRow(final ResultEntry rs, final int rowNum) {
                atomicRownNum.set(rowNum);
                return rs.getInt("emp.id"); //$NON-NLS-1$
            }
        });
        System.out.println("Result is " + results); //$NON-NLS-1$
        System.out.println("atomicRownNum is " + atomicRownNum); //$NON-NLS-1$
        assertEquals(2, results.size());
        assertEquals(1, atomicRownNum.get());
        assertTrue(results.contains(employee1.getId()));
        assertTrue(results.contains(employee2.getId()));
    }

}
