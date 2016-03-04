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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.jporm.commons.core.io.RowMapper;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;

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
    public void setUp() {
        getJPO().transaction().execute(session -> {
                for (int i = 0; i < CommonUserQuantity; i++) {
                    CommonUser CommonUser = new CommonUser();
                    CommonUser.setUserAge(Long.valueOf(i));
                    CommonUser.setFirstname("name");
                    CommonUser.setLastname("surname");
                    CommonUser = session.save(CommonUser);

                    if (i == 0) {
                        firstId = CommonUser.getId();
                    }

                }

                return null;
        });
        assertNotNull(firstId);
    }

    @Test
    public void testFirstRowPaginationWithOrderAsc() {
        getJPO().transaction().execute(session -> {

                int firstRow = new Random().nextInt(CommonUserQuantity);

                List<CommonUser> results = session.find(CommonUser.class).where().ge("id", firstId).orderBy().asc("id").offset(firstRow).fetchList();

                assertEquals(CommonUserQuantity - firstRow, results.size());

                for (CommonUser CommonUser : results) {
                    assertTrue(CommonUser.getId() >= firstId);
                    assertTrue(CommonUser.getUserAge() >= firstRow);
                }

                return null;
        });
    }

    @Test
    public void testFirstRowPaginationWithOrderDesc() {
        getJPO().transaction().execute(session -> {

                int firstRow = new Random().nextInt(CommonUserQuantity);

                List<CommonUser> results = session.find(CommonUser.class).where().ge("id", firstId).orderBy().desc("id").offset(firstRow).fetchList();

                assertEquals(CommonUserQuantity - firstRow, results.size());

                for (CommonUser CommonUser : results) {
                    assertTrue(CommonUser.getId() >= firstId);
                    assertTrue(CommonUser.getUserAge() < (CommonUserQuantity - firstRow));

                }

                return null;
        });
    }

    @Test
    public void testMaxRowsPaginationWithOrderAsc() {
        getJPO().transaction().execute(session -> {

                int maxRows = new Random().nextInt(CommonUserQuantity) + 1;

                List<CommonUser> results = session.find(CommonUser.class).where().ge("id", firstId).orderBy().asc("id").limit(maxRows).fetchList();

                assertEquals(maxRows, results.size());

                for (CommonUser CommonUser : results) {
                    assertTrue(CommonUser.getId() >= firstId);
                    assertTrue(CommonUser.getUserAge() < maxRows);
                }

                return null;
        });
    }

    @Test
    public void testMaxRowsPaginationWithOrderDesc() {
        getJPO().transaction().execute(session -> {

                int maxRows = new Random().nextInt(CommonUserQuantity) + 1;

                List<CommonUser> results = session.find(CommonUser.class).where().ge("id", firstId).orderBy().desc("id").limit(maxRows).fetchList();

                assertEquals(maxRows, results.size());

                for (CommonUser CommonUser : results) {
                    assertTrue(CommonUser.getId() >= firstId);
                    assertTrue(CommonUser.getUserAge() >= (CommonUserQuantity - maxRows));
                }

                return null;
        });
    }

    @Test
    public void testPaginationWithOrderAsc() {
        getJPO().transaction().execute(session -> {

                int firstRow = new Random().nextInt(CommonUserQuantity);
                int maxRows = new Random().nextInt(CommonUserQuantity - firstRow) + 1;

                List<CommonUser> results = session.find(CommonUser.class).where().ge("id", firstId).orderBy().asc("id")
                        .limit(maxRows).offset(firstRow).fetchList();

                assertEquals(maxRows, results.size());

                for (CommonUser CommonUser : results) {
                    assertTrue(CommonUser.getId() >= firstId);
                    assertTrue(CommonUser.getUserAge() >= firstRow);
                    assertTrue(CommonUser.getUserAge() < (firstRow + maxRows));
                }

                return null;
        });
    }

    @Test
    public void testPaginationWithOrderDesc() {
        getJPO().transaction().execute(session -> {

                int firstRow = new Random().nextInt(CommonUserQuantity);
                int maxRows = new Random().nextInt(CommonUserQuantity - firstRow) + 1;

                final List<CommonUser> results = new ArrayList<CommonUser>();
                RowMapper<CommonUser> rsr = new RowMapper<CommonUser>() {
                    @Override
                    public void read(final CommonUser CommonUser, final int rowCount) {
                        results.add(CommonUser);
                    }
                };
                session.find(CommonUser.class).where().ge("id", firstId).orderBy().desc("id").limit(maxRows).offset(firstRow).fetch(rsr);

                assertEquals(maxRows, results.size());

                for (CommonUser CommonUser : results) {
                    assertTrue(CommonUser.getId() >= firstId);
                    assertTrue(CommonUser.getUserAge() < (CommonUserQuantity - firstRow));
                    assertTrue(CommonUser.getUserAge() >= ((CommonUserQuantity - firstRow) - maxRows));

                }

                return null;
        });
    }

}
