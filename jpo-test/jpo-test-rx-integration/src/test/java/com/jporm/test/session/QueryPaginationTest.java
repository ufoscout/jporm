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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;

import io.reactivex.Single;

/**
 *
 * @author Francesco Cina
 *
 *         05/giu/2011
 */
public class QueryPaginationTest extends BaseTestAllDB {

    private final int CommonUserQuantity = 100;

    private Long firstId;

    public QueryPaginationTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Before
    public void testSetUp() throws InterruptedException, ExecutionException {
        transaction((Session session) -> {
            for (int i = 0; i < CommonUserQuantity; i++) {
                CommonUser commonUser = new CommonUser();
                commonUser.setUserAge(Long.valueOf(i));
                commonUser.setFirstname("name");
                commonUser.setLastname("surname");
                commonUser = session.save(commonUser).blockingGet();

                if (i == 0) {
                    firstId = commonUser.getId();
                }
            }

            return Single.just("");
        });
        assertNotNull(firstId);
    }

    @Test
    public void testFirstRowPaginationWithOrderAsc() {
        transaction((Session session) -> {
            int firstRow = new Random().nextInt(CommonUserQuantity);
            return session.find(CommonUser.class).where().ge("id", firstId).orderBy().asc("id").offset(firstRow).fetchAll().buffer(1000).map(results -> {
                assertEquals(CommonUserQuantity - firstRow, results.size());

                for (CommonUser CommonUser : results) {
                    assertTrue(CommonUser.getId() >= firstId);
                    assertTrue(CommonUser.getUserAge() >= firstRow);
                }
                return results;
            }).buffer(Integer.MAX_VALUE).singleElement();
        });
    }

    @Test
    public void testFirstRowPaginationWithOrderDesc() {
        transaction((Session session) -> {
            int firstRow = new Random().nextInt(CommonUserQuantity);
            return session.find(CommonUser.class).where().ge("id", firstId).orderBy().desc("id").offset(firstRow).fetchAll().buffer(1000).map(results -> {
                assertEquals(CommonUserQuantity - firstRow, results.size());

                for (CommonUser CommonUser : results) {
                    assertTrue(CommonUser.getId() >= firstId);
                    assertTrue(CommonUser.getUserAge() < (CommonUserQuantity - firstRow));

                }
                return results;
            }).buffer(Integer.MAX_VALUE).singleElement();
        });

    }

    @Test
    public void testMaxRowsPaginationWithOrderAsc() {
        transaction((Session session) -> {
            int maxRows = new Random().nextInt(CommonUserQuantity) + 1;
            return session.find(CommonUser.class).where().ge("id", firstId).orderBy().asc("id").limit(maxRows).fetchAll().buffer(1000).map(results -> {
                assertEquals(maxRows, results.size());
                for (CommonUser commonUser : results) {
                    assertTrue(commonUser.getId() >= firstId);
                    assertTrue(commonUser.getUserAge() < maxRows);
                }
                return results;
            }).buffer(Integer.MAX_VALUE).singleElement();
        });
    }

    @Test
    public void testMaxRowsPaginationWithOrderDesc() {

        transaction((Session session) -> {
            int maxRows = new Random().nextInt(CommonUserQuantity) + 1;
            return session.find(CommonUser.class).where().ge("id", firstId).orderBy().desc("id").limit(maxRows).fetchAll().buffer(1000).map(results -> {
                assertEquals(maxRows, results.size());
                for (CommonUser commonUser : results) {
                    assertTrue(commonUser.getId() >= firstId);
                    assertTrue(commonUser.getUserAge() >= (CommonUserQuantity - maxRows));
                }
                return results;
            }).buffer(Integer.MAX_VALUE).singleElement();
        });
    }

    @Test
    public void testPaginationWithOrderAsc() {

        transaction((Session session) -> {
            int firstRow = new Random().nextInt(CommonUserQuantity);
            int maxRows = new Random().nextInt(CommonUserQuantity - firstRow) + 1;
            return session.find(CommonUser.class).where().ge("id", firstId).orderBy().asc("id").limit(maxRows).offset(firstRow).fetchAll()
                    .buffer(1000).map(results -> {
                assertEquals(maxRows, results.size());

                for (CommonUser CommonUser : results) {
                    assertTrue(CommonUser.getId() >= firstId);
                    assertTrue(CommonUser.getUserAge() >= firstRow);
                    assertTrue(CommonUser.getUserAge() < (firstRow + maxRows));
                }

                return results;
            }).buffer(Integer.MAX_VALUE).singleElement();
        });
    }

    @Test
    public void testPaginationWithOrderDesc() {

        transaction((Session session) -> {
            int firstRow = new Random().nextInt(CommonUserQuantity);
            int maxRows = new Random().nextInt(CommonUserQuantity - firstRow) + 1;
            return session.find(CommonUser.class).where().ge("id", firstId).orderBy().desc("id").limit(maxRows).offset(firstRow).fetchAll()
                    .buffer(1000).map(results -> {
                assertEquals(maxRows, results.size());

                for (CommonUser CommonUser : results) {
                    assertTrue(CommonUser.getId() >= firstId);
                    assertTrue(CommonUser.getUserAge() < (CommonUserQuantity - firstRow));
                    assertTrue(CommonUser.getUserAge() >= ((CommonUserQuantity - firstRow) - maxRows));

                }
                return results;
            }).buffer(Integer.MAX_VALUE).singleElement();
        });
    }

}
