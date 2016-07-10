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

import org.junit.Before;
import org.junit.Test;

import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;
import com.jporm.types.io.ResultEntry;

import rx.Completable;

/**
 *
 * @author Francesco Cina
 *
 *         05/giu/2011
 */
public class CustomQueryPaginationTest extends BaseTestAllDB {

    private final int userQuantity = 100;

    private Long firstId;

    public CustomQueryPaginationTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testFirstRowPaginationWithOrderAsc() {
        transaction(session -> {

            int firstRow = new Random().nextInt(userQuantity);

            IntBiFunction<ResultEntry, Integer> rsrr = new IntBiFunction<ResultEntry, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final int rowNum) {
                    return rs.getInt("userAge");
                }
            };
            return session.find("userAge").from(CommonUser.class, "user").where().ge("id", firstId).orderBy().asc("id").offset(firstRow).fetchAll(rsrr)
                    .buffer(100).map(results -> {
                assertEquals(userQuantity - firstRow, results.size());

                for (Integer age : results) {
                    assertTrue(age >= firstRow);
                }
                return results;
            });

        });
    }

    @Test
    public void testFirstRowPaginationWithOrderDesc() {
        transaction(session -> {

            int firstRow = new Random().nextInt(userQuantity);

            IntBiFunction<ResultEntry, Integer> rsrr = new IntBiFunction<ResultEntry, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final int rowNum) {
                    return rs.getInt("userAge");
                }
            };
            return session.find("userAge").from(CommonUser.class, "user").where().ge("id", firstId).orderBy().desc("id").offset(firstRow).fetchAll(rsrr)
                    .buffer(100).map(results -> {
                assertEquals(userQuantity - firstRow, results.size());

                for (Integer age : results) {
                    assertTrue(age < (userQuantity - firstRow));

                }

                return results;

            });

        });
    }

    @Test
    public void testMaxRowsPaginationWithOrderAsc() {
        transaction(session -> {

            int maxRows = new Random().nextInt(userQuantity) + 1;

            IntBiFunction<ResultEntry, Integer> rsrr = new IntBiFunction<ResultEntry, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final int rowNum) {
                    return rs.getInt("userAge");
                }
            };
            return session.find("userAge").from(CommonUser.class, "user").where().ge("id", firstId).orderBy().asc("id").limit(maxRows).fetchAll(rsrr)
                    .buffer(100).map(results -> {
                assertEquals(maxRows, results.size());
                for (Integer age : results) {
                    assertTrue(age < maxRows);
                }
                return results;
            });

        });
    }

    @Test
    public void testMaxRowsPaginationWithOrderDesc() {
        transaction(session -> {

            int maxRows = new Random().nextInt(userQuantity) + 1;

            IntBiFunction<ResultEntry, Integer> rsrr = new IntBiFunction<ResultEntry, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final int rowNum) {
                    return rs.getInt("userAge");
                }
            };
            return session.find("userAge").from(CommonUser.class, "user").where().ge("id", firstId).orderBy().desc("id").limit(maxRows).fetchAll(rsrr)
                    .buffer(100).map(results -> {
                assertEquals(maxRows, results.size());

                for (Integer age : results) {
                    assertTrue(age >= (userQuantity - maxRows));
                }
                return results;
            });
        });
    }

    @Test
    public void testPaginationWithOrderAsc() {
        transaction(session -> {

            int firstRow = new Random().nextInt(userQuantity);
            int maxRows = new Random().nextInt(userQuantity - firstRow) + 1;

            IntBiFunction<ResultEntry, Integer> rsrr = new IntBiFunction<ResultEntry, Integer>() {
                @Override
                public Integer apply(final ResultEntry rs, final int rowNum) {
                    return rs.getInt("userAge");
                }
            };
            return session.find("userAge").from(CommonUser.class, "user").where().ge("id", firstId).orderBy().asc("id")
                    .limit(maxRows).offset(firstRow).fetchAll(rsrr).buffer(100).map(results -> {
                assertEquals(maxRows, results.size());

                for (Integer age : results) {
                    assertTrue(age >= firstRow);
                    assertTrue(age < (firstRow + maxRows));
                }

                return results;

            });

        });
    }

    @Test
    public void testPaginationWithOrderDesc() {
        transaction(session -> {

            int firstRow = new Random().nextInt(userQuantity);
            int maxRows = new Random().nextInt(userQuantity - firstRow) + 1;

            IntBiFunction<ResultEntry, Integer> rsrr = new IntBiFunction<ResultEntry, Integer>() {
                @Override
                public Integer apply(final ResultEntry resultSet, int count) {
                    return resultSet.getInt("userAge");
                }
            };
            return session.find("userAge").from(CommonUser.class, "user").where().ge("id", firstId).orderBy().desc("id")
                    .limit(maxRows).offset(firstRow).fetchAll(rsrr).buffer(100).map(results -> {
                assertEquals(maxRows, results.size());

                for (Integer age : results) {
                    assertTrue(age < (userQuantity - firstRow));
                    assertTrue(age >= ((userQuantity - firstRow) - maxRows));

                }

                return results;

            });

        });
    }

    @Before
    public void testSetUp() throws Exception {

        transaction((Session session) -> {
            for (int i = 0; i < userQuantity; i++) {
                try {
                    CommonUser user = new CommonUser();
                    user.setUserAge(Long.valueOf(i));
                    user.setFirstname("name");
                    user.setLastname("surname");
                    user = session.save(user).toBlocking().value();

                    if (i == 0) {
                        firstId = user.getId();
                    }
                } catch (Exception ex) {
                    getLogger().error("", ex);
                }
            }
            return Completable.complete().toObservable();
        });

        assertNotNull(firstId);
    }
}
