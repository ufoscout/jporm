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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;
import com.jporm.types.io.ResultSet;

/**
 *
 * @author Francesco Cina
 *
 *         05/giu/2011
 */
public class QueryGroupByHavingTest extends BaseTestAllDB {

    private final int firstnameOneQuantity = 50;

    private final String firstnameOne = UUID.randomUUID().toString();
    private final int firstnameTwoQuantity = 60;

    private final String firstnameTwo = UUID.randomUUID().toString();
    private final int firstnameThreeQuantity = 70;

    private final String firstnameThree = UUID.randomUUID().toString();

    public QueryGroupByHavingTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testGroupBy() {

        transaction(session -> {

            return session.find("u.firstname", "count(*) as countName").from(CommonUser.class, "u").groupBy("u.firstname")
                    .fetch((final ResultSet resultSet) -> {
                final Map<String, Integer> firstnameCount = new HashMap<>();
                while (resultSet.next()) {
                    String rsFirstname = resultSet.getString("u.firstname");
                    Integer rsCount = resultSet.getInt("countName");
                    getLogger().debug("Found firstname [{}] count [{}]", rsFirstname, rsCount);
                    firstnameCount.put(rsFirstname, rsCount);
                }
                return firstnameCount;
            }).thenApply(firstnameCount -> {
                threadAssertFalse(firstnameCount.isEmpty());
                threadAssertEquals(3, firstnameCount.size());
                threadAssertTrue(firstnameCount.containsKey(firstnameOne));
                threadAssertTrue(firstnameCount.containsKey(firstnameTwo));
                threadAssertTrue(firstnameCount.containsKey(firstnameThree));
                threadAssertEquals(Integer.valueOf(firstnameOneQuantity), firstnameCount.get(firstnameOne));
                threadAssertEquals(Integer.valueOf(firstnameTwoQuantity), firstnameCount.get(firstnameTwo));
                threadAssertEquals(Integer.valueOf(firstnameThreeQuantity), firstnameCount.get(firstnameThree));
                return null;
            });
        });
    }

    @Test
    public void testGroupByHaving() {

        transaction(session -> {

            return session.find("u.firstname", "count(*) as countName").from(CommonUser.class, "u").groupBy("u.firstname")
                    .having("count(*) > ?", firstnameOneQuantity).fetch((final ResultSet resultSet) -> {
                final Map<String, Integer> firstnameCount = new HashMap<>();
                while (resultSet.next()) {
                    String rsFirstname = resultSet.getString("u.firstname");
                    Integer rsCount = resultSet.getInt("countName");
                    getLogger().debug("Found firstname [{}] count [{}]", rsFirstname, rsCount);
                    firstnameCount.put(rsFirstname, rsCount);
                }
                return firstnameCount;
            }).thenApply(firstnameCount -> {
                threadAssertFalse(firstnameCount.isEmpty());
                threadAssertEquals(2, firstnameCount.size());
                threadAssertFalse(firstnameCount.containsKey(firstnameOne));
                threadAssertTrue(firstnameCount.containsKey(firstnameTwo));
                threadAssertTrue(firstnameCount.containsKey(firstnameThree));
                threadAssertEquals(Integer.valueOf(firstnameTwoQuantity), firstnameCount.get(firstnameTwo));
                threadAssertEquals(Integer.valueOf(firstnameThreeQuantity), firstnameCount.get(firstnameThree));
                return null;
            });
        });

    }

    @Test
    public void testGroupByHavingWithAlias() {

        transaction(session -> {

            return session.find("u.firstname", "sum(userAge) as sumAge").from(CommonUser.class, "u").groupBy("u.firstname").having("sum(userAge) > ?", 100)
                    .fetch((final ResultSet resultSet) -> {
                final Map<String, Integer> firstnameAge = new HashMap<>();
                while (resultSet.next()) {
                    String rsFirstname = resultSet.getString("u.firstname");
                    Integer rsCount = resultSet.getInt("sumAge");
                    getLogger().info("Found firstname [{}] sumAge [{}]", rsFirstname, rsCount);
                    firstnameAge.put(rsFirstname, rsCount);
                }
                return firstnameAge;
            }).thenApply(firstnameAge -> {
                threadAssertFalse(firstnameAge.isEmpty());
                threadAssertEquals(3, firstnameAge.size());
                threadAssertTrue(firstnameAge.containsKey(firstnameOne));
                threadAssertTrue(firstnameAge.containsKey(firstnameTwo));
                threadAssertTrue(firstnameAge.containsKey(firstnameThree));
                threadAssertTrue(firstnameAge.get(firstnameOne) > 100);
                threadAssertTrue(firstnameAge.get(firstnameTwo) > 100);
                threadAssertTrue(firstnameAge.get(firstnameThree) > 100);
                return null;
            });
        });
    }

    @Test
    public void testGroupByWithOrderBy() {

        transaction(session -> {

            return session.find("u.firstname", "count(*) as countName").from(CommonUser.class, "u").groupBy("u.firstname").orderBy().asc("u.firstname")
                    .fetch((final ResultSet resultSet) -> {
                final Map<String, Integer> firstnameCount = new HashMap<>();
                while (resultSet.next()) {
                    String rsFirstname = resultSet.getString("u.firstname");
                    Integer rsCount = resultSet.getInt("countName");
                    getLogger().debug("Found firstname [{}] count [{}]", rsFirstname, rsCount);
                    firstnameCount.put(rsFirstname, rsCount);
                }
                return firstnameCount;
            }).thenApply(firstnameCount -> {
                threadAssertFalse(firstnameCount.isEmpty());
                threadAssertEquals(3, firstnameCount.size());
                threadAssertTrue(firstnameCount.containsKey(firstnameOne));
                threadAssertTrue(firstnameCount.containsKey(firstnameTwo));
                threadAssertTrue(firstnameCount.containsKey(firstnameThree));
                threadAssertEquals(Integer.valueOf(firstnameOneQuantity), firstnameCount.get(firstnameOne));
                threadAssertEquals(Integer.valueOf(firstnameTwoQuantity), firstnameCount.get(firstnameTwo));
                threadAssertEquals(Integer.valueOf(firstnameThreeQuantity), firstnameCount.get(firstnameThree));
                return null;
            });
        });
    }

    @Before
    public void testSetUp() throws InterruptedException, ExecutionException {
        getJPO().transaction().execute(session -> {
            try {

                session.delete(CommonUser.class).execute().get();

                for (int i = 0; i < firstnameOneQuantity; i++) {
                    CommonUser user = new CommonUser();
                    user.setUserAge(Long.valueOf(i));
                    user.setFirstname(firstnameOne);
                    user.setLastname("surname");
                    session.save(user).get();
                }

                for (int i = 0; i < firstnameTwoQuantity; i++) {
                    CommonUser user = new CommonUser();
                    user.setUserAge(Long.valueOf(i));
                    user.setFirstname(firstnameTwo);
                    user.setLastname("surname");
                    session.save(user).get();
                }

                for (int i = 0; i < firstnameThreeQuantity; i++) {
                    CommonUser user = new CommonUser();
                    user.setUserAge(Long.valueOf(i));
                    user.setFirstname(firstnameThree);
                    user.setLastname("surname");
                    session.save(user).get();
                }
            } catch (InterruptedException | ExecutionException ex) {
                getLogger().error("", ex);
            }

            return CompletableFuture.completedFuture(null);
        }).get();
    }

}
