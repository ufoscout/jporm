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

import com.jporm.sql.query.clause.impl.where.Exp;
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
public class QueryWithCustomExpressionTest extends BaseTestAllDB {

    public QueryWithCustomExpressionTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    private final int userQuantity = 100;
    private Long firstId;

    @Before
    public void testSetUp() throws InterruptedException, ExecutionException {
        getJPO().transaction().execute(session -> {
            for (int i = 0; i < userQuantity; i++) {
                try {
                    CommonUser user = new CommonUser();
                    user.setUserAge(Long.valueOf(i));
                    user.setFirstname("name");
                    user.setLastname("surname");
                    user = session.save(user).get();

                    if (i == 0) {
                        firstId = user.getId();
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
    public void testCustomExpression1() {
        transaction(session -> {
            int module = new Random().nextInt(10);
            return session.find(CommonUser.class).where("MOD(CommonUser.id, 10) = ?", module).fetchList()
                    .thenApply(results -> {
                        assertFalse(results.isEmpty());
                        for (CommonUser user : results) {
                            assertTrue((user.getId() % 10) == module);
                        }
                        return null;
                    });
        });
    }

    @Test
    public void testCustomExpression2() {

        transaction(session -> {
            int max = new Random().nextInt(19) + 1;
            int module = new Random().nextInt(max);

            return session.find(CommonUser.class).where(Exp.gt("id", 0)).and("CommonUser.id >= 0").and("MOD(CommonUser.id, ?) = ?", max, module).fetchList()
                    .thenApply(results -> {
                        assertFalse(results.isEmpty());
                        for (CommonUser user : results) {
                            assertTrue((user.getId() % max) == module);
                        }
                        return null;
                    });
        });
    }

}
