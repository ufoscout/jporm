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


import java.util.Random;

import org.junit.Test;

import com.jporm.rx.query.find.FindQuery;
import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;

import java.util.concurrent.CompletableFuture;

/**
 *
 * @author Francesco Cina
 *
 * 23/giu/2011
 */
public class QueryExecutionTest extends BaseTestAllDB {

    public QueryExecutionTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

   @Test
    public void testQuery1() {
        final int id = new Random().nextInt(Integer.MAX_VALUE);
        transaction(session -> {
        	CompletableFuture<Employee> result = createEmployee(session, id)
                    .thenCompose(emp -> {
                        return session.findQuery(Employee.class).fetchList();
                    })
                    .thenCompose(employees -> {
                        assertNotNull(employees);

                        return session.findQuery(Employee.class).fetchRowCount()
                        .thenApply(count -> {
                            assertTrue(employees.size() > 0);
                            assertEquals(employees.size(), count.intValue());
                            return null;
                        });
                    })
                    .thenCompose(employee -> {

                        return deleteEmployee(session, id);
                    });
        	return result;
        });

    }

   @Test
    public void testQuery3() {
        final int id = new Random().nextInt(Integer.MAX_VALUE);
        transaction(session -> {
            CompletableFuture<Employee> result = createEmployee(session, id)
                    .thenCompose(employee -> {
                        final int maxRows = 4;
                        final FindQuery<Employee> query = session.findQuery(Employee.class, "e"); //$NON-NLS-1$
                        query.limit(maxRows);
                        query.where().ge("e.id", 0);
                        return query.fetchList()
                        .thenApply(employees -> {
                            assertTrue(employees.size() > 0);
                            assertTrue(employees.size() <= maxRows);
                            return employee;
                        });
                    })
                    .thenCompose(employee -> {
                        return deleteEmployee(session, id);
                    });
            return result;
        });

    }

   @Test
    public void testQuery4() {
        final int id = new Random().nextInt(Integer.MAX_VALUE);
        transaction(session -> {
        	CompletableFuture<Employee> result = createEmployee(session, id)
                    .thenCompose(employee -> {
                            //find list with one result
                            final FindQuery<Employee> query1 = session.findQuery(Employee.class);
                            query1.where().eq("id", employee.getId()); //$NON-NLS-1$
                            return query1.fetchList()
                                    .thenCompose(list1 -> {
                                        assertEquals(1, list1.size());
                                        
                                        final FindQuery<Employee> query2 = session.findQuery(Employee.class);
                                        query2.where().eq("id", (-employee.getId()));
                                        return query2.fetchList()
                                                .thenCompose(list2 -> {
                                                    assertEquals(0, list2.size());
                                                    
                                                    final FindQuery<Employee> query3 = session.findQuery(Employee.class);
                                                    query3.where().eq("id", employee.getId()); 
                                                    return query3.fetchOptional()
                                                        .thenCompose(result3 -> {
                                                            
                                                            assertTrue(result3.isPresent());
                                                            
                                                            final FindQuery<Employee> query4 = session.findQuery(Employee.class);
                                                            query4.where().eq("id", -employee.getId());
                                                            return query4.fetchOptional()
                                                                .thenApply(result4 -> {
                                                                    assertFalse(result4.isPresent());
                                                                    return result4;
                                                                });
                                                        });
                                                });
                                    });

                    })
                    .thenCompose(employee -> {
                        return deleteEmployee(session, id);
                    });
        	return result;
        });

    }

    private CompletableFuture<Employee> createEmployee(final Session session, int id) {
        final Employee employee = new Employee();
        employee.setId(id);
        employee.setAge(44);
        employee.setEmployeeNumber("empNumber" + id); //$NON-NLS-1$
        employee.setName("Wizard"); //$NON-NLS-1$
        employee.setSurname("Cina"); //$NON-NLS-1$
        return session.save(employee);
    }

    private CompletableFuture<Employee> deleteEmployee(final Session session, final int id) {
        final Employee employee = new Employee();
        employee.setId(id);
        return session.delete(employee)
                .thenApply(fn -> {
                    assertTrue(fn.deleted() > 0);
                    return employee;
                });
    }

}
