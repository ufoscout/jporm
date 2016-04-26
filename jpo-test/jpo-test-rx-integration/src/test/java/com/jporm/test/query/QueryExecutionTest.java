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
import java.util.concurrent.CompletableFuture;

import org.junit.Test;

import com.jporm.rx.query.find.CustomFindQuery;
import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;

/**
 *
 * @author Francesco Cina
 *
 *         23/giu/2011
 */
public class QueryExecutionTest extends BaseTestAllDB {

    public QueryExecutionTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    private CompletableFuture<Employee> createEmployee(final Session session, final int id) {
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
        return session.delete(employee).thenApply(fn -> {
            threadAssertTrue(fn.deleted() > 0);
            return employee;
        });
    }

    @Test
    public void testQuery1() {
        final int id = new Random().nextInt(Integer.MAX_VALUE);
        transaction(session -> {
            CompletableFuture<Employee> result = createEmployee(session, id).thenCompose(emp -> {
                return session.find(Employee.class).fetchAll();
            }).thenCompose(employees -> {
                threadAssertNotNull(employees);

                return session.find(Employee.class).fetchRowCount().thenApply(count -> {
                    threadAssertTrue(employees.size() > 0);
                    threadAssertEquals(employees.size(), count.intValue());
                    return null;
                });
            }).thenCompose(employee -> {

                return deleteEmployee(session, id);
            });
            return result;
        });

    }

    @Test
    public void testQuery3() {
        final int id = new Random().nextInt(Integer.MAX_VALUE);
        transaction(session -> {
            CompletableFuture<Employee> result = createEmployee(session, id).thenCompose(employee -> {
                final int maxRows = 4;
                final CustomFindQuery<Employee> query = session.find(Employee.class, "e"); //$NON-NLS-1$
                query.limit(maxRows);
                query.where().ge("e.id", 0);
                return query.fetchAll().thenApply(employees -> {
                    threadAssertTrue(employees.size() > 0);
                    threadAssertTrue(employees.size() <= maxRows);
                    return employee;
                });
            }).thenCompose(employee -> {
                return deleteEmployee(session, id);
            });
            return result;
        });

    }

    @Test
    public void testQuery4() {
        final int id = new Random().nextInt(Integer.MAX_VALUE);
        transaction(session -> {
            CompletableFuture<Employee> result = createEmployee(session, id).thenCompose(employee -> {
                // find list with one result
                final CustomFindQuery<Employee> query1 = session.find(Employee.class);
                query1.where().eq("id", employee.getId()); //$NON-NLS-1$
                return query1.fetchAll().thenCompose(list1 -> {
                    threadAssertEquals(1, list1.size());

                    final CustomFindQuery<Employee> query2 = session.find(Employee.class);
                    query2.where().eq("id", (-employee.getId()));
                    return query2.fetchAll().thenCompose(list2 -> {
                        threadAssertEquals(0, list2.size());

                        final CustomFindQuery<Employee> query3 = session.find(Employee.class);
                        query3.where().eq("id", employee.getId());
                        return query3.fetchOneOptional().thenCompose(result3 -> {

                            threadAssertTrue(result3.isPresent());

                            final CustomFindQuery<Employee> query4 = session.find(Employee.class);
                            query4.where().eq("id", -employee.getId());
                            return query4.fetchOneOptional().thenApply(result4 -> {
                                threadAssertFalse(result4.isPresent());
                                return result4;
                            });
                        });
                    });
                });

            }).thenCompose(employee -> {
                return deleteEmployee(session, id);
            });
            return result;
        });

    }

}
