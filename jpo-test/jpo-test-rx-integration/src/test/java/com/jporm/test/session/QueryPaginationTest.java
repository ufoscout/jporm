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

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author Francesco Cina
 *
 * 05/giu/2011
 */
public class QueryPaginationTest extends BaseTestAllDB {

    public QueryPaginationTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    private final int CommonUserQuantity = 100;
    private Long firstId;

    @Before
    public void testSetUp() throws InterruptedException, ExecutionException {
        getJPO().transaction().execute(session -> {
            for (int i = 0; i < CommonUserQuantity; i++) {
                try {
                    CommonUser commonUser = new CommonUser();
                    commonUser.setUserAge(Long.valueOf(i));
                    commonUser.setFirstname("name");
                    commonUser.setLastname("surname");
                    commonUser = session.save(commonUser).get();

                    if (i == 0) {
                        firstId = commonUser.getId();
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    throw new RuntimeException(ex);
                }
            }

            return CompletableFuture.completedFuture(null);
        }).get();
        assertNotNull(firstId);
    }

    @Test
    public void testMaxRowsPaginationWithOrderAsc() {
        transaction(session -> {
            int maxRows = new Random().nextInt(CommonUserQuantity) + 1;
            return session.find(CommonUser.class).limit(maxRows).where().ge("id", firstId).orderBy().asc("id").fetchList()
                    .thenApply(results -> {
                        assertEquals(maxRows, results.size());
                        for (CommonUser commonUser : results) {
                            assertTrue(commonUser.getId() >= firstId);
                            assertTrue(commonUser.getUserAge() < maxRows);
                        }
                        return null;
                    });
        });
    }

    @Test
    public void testMaxRowsPaginationWithOrderDesc() {

        transaction(session -> {
            int maxRows = new Random().nextInt(CommonUserQuantity) + 1;
            return session.find(CommonUser.class).limit(maxRows).where().ge("id", firstId).orderBy().desc("id").fetchList()
                    .thenApply(results -> {
                        assertEquals(maxRows, results.size());
                        for (CommonUser commonUser : results) {
                            assertTrue(commonUser.getId() >= firstId);
                            assertTrue(commonUser.getUserAge() >= (CommonUserQuantity - maxRows));
                        }
                        return null;
                    });
        });
    }

    @Test
    public void testFirstRowPaginationWithOrderAsc() {
        transaction(session -> {
            int firstRow = new Random().nextInt(CommonUserQuantity);
            return session.find(CommonUser.class).offset(firstRow).where().ge("id", firstId).orderBy().asc("id").fetchList()
                    .thenApply(results -> {
                        assertEquals(CommonUserQuantity - firstRow, results.size());

                        for (CommonUser CommonUser : results) {
                            assertTrue(CommonUser.getId() >= firstId);
                            assertTrue(CommonUser.getUserAge() >= firstRow);
                        }
                        return null;
                    });
        });
    }

    @Test
    public void testFirstRowPaginationWithOrderDesc() {
        transaction(session -> {
            int firstRow = new Random().nextInt(CommonUserQuantity);
            return session.find(CommonUser.class).offset(firstRow).where().ge("id", firstId).orderBy().desc("id").fetchList()
                    .thenApply(results -> {
                        assertEquals(CommonUserQuantity - firstRow, results.size());

                        for (CommonUser CommonUser : results) {
                            assertTrue(CommonUser.getId() >= firstId);
                            assertTrue(CommonUser.getUserAge() < (CommonUserQuantity - firstRow));

                        }
                        return null;
                    });
        });

    }

    @Test
    public void testPaginationWithOrderAsc() {

        transaction(session -> {
            int firstRow = new Random().nextInt(CommonUserQuantity);
            int maxRows = new Random().nextInt(CommonUserQuantity - firstRow) + 1;
            return session.find(CommonUser.class).limit(maxRows).offset(firstRow).where().ge("id", firstId).orderBy().asc("id").fetchList()
                    .thenApply(results -> {
                        assertEquals(maxRows, results.size());

                        for (CommonUser CommonUser : results) {
                            assertTrue(CommonUser.getId() >= firstId);
                            assertTrue(CommonUser.getUserAge() >= firstRow);
                            assertTrue(CommonUser.getUserAge() < (firstRow + maxRows));
                        }

                        return null;
                    });
        });
    }

    @Test
    public void testPaginationWithOrderDesc() {

        transaction(session -> {
            int firstRow = new Random().nextInt(CommonUserQuantity);
            int maxRows = new Random().nextInt(CommonUserQuantity - firstRow) + 1;
            return session.find(CommonUser.class).limit(maxRows).offset(firstRow).where().ge("id", firstId).orderBy().desc("id").fetchList()
                    .thenApply(results -> {
                        assertEquals(maxRows, results.size());

                        for (CommonUser CommonUser : results) {
                            assertTrue(CommonUser.getId() >= firstId);
                            assertTrue(CommonUser.getUserAge() < (CommonUserQuantity - firstRow));
                            assertTrue(CommonUser.getUserAge() >= ((CommonUserQuantity - firstRow) - maxRows));

                        }
                        return null;
                    });
        });
    }

}
