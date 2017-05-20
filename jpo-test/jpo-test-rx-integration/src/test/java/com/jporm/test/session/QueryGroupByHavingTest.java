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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import com.jporm.rx.rxjava2.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;
import com.jporm.types.io.ResultEntry;

import io.reactivex.Single;

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

        transaction((Session session) -> {

            return session.find("u.firstname", "count(*) as countName").from(CommonUser.class, "u").groupBy("u.firstname")
                    .fetchAll((final ResultEntry entry, int count) -> {
                        String rsFirstname = entry.getString("u.firstname");
                        Integer rsCount = entry.getInt("countName");
                        getLogger().debug("Found firstname [{}] count [{}]", rsFirstname, rsCount);
                        return new AbstractMap.SimpleEntry<>(rsFirstname, rsCount);
                    })
                    .toMap(entry -> entry.getKey(), entry -> entry.getValue())
                    .map(firstnameCount -> {
                        getLogger().debug("Map: [{}]", firstnameCount);
                        assertFalse(firstnameCount.isEmpty());
                        assertEquals(3, firstnameCount.size());
                        assertTrue(firstnameCount.containsKey(firstnameOne));
                        assertTrue(firstnameCount.containsKey(firstnameTwo));
                        assertTrue(firstnameCount.containsKey(firstnameThree));
                        assertEquals(Integer.valueOf(firstnameOneQuantity), firstnameCount.get(firstnameOne));
                        assertEquals(Integer.valueOf(firstnameTwoQuantity), firstnameCount.get(firstnameTwo));
                        assertEquals(Integer.valueOf(firstnameThreeQuantity), firstnameCount.get(firstnameThree));
                        return firstnameCount;
                    });
        });
    }

    @Test
    public void testGroupByHaving() {

        transaction((Session session) -> {

            return session.find("u.firstname", "count(*) as countName").from(CommonUser.class, "u").groupBy("u.firstname")
                    .having("count(*) > ?", firstnameOneQuantity)
                    .fetchAll((final ResultEntry entry, int count) -> {
                        String rsFirstname = entry.getString("u.firstname");
                        Integer rsCount = entry.getInt("countName");
                        getLogger().debug("Found firstname [{}] count [{}]", rsFirstname, rsCount);
                        return new AbstractMap.SimpleEntry<>(rsFirstname, rsCount);
                    })
                    .toMap(entry -> entry.getKey(), entry -> entry.getValue())
                    .map(firstnameCount -> {
                        assertFalse(firstnameCount.isEmpty());
                        assertEquals(2, firstnameCount.size());
                        assertFalse(firstnameCount.containsKey(firstnameOne));
                        assertTrue(firstnameCount.containsKey(firstnameTwo));
                        assertTrue(firstnameCount.containsKey(firstnameThree));
                        assertEquals(Integer.valueOf(firstnameTwoQuantity), firstnameCount.get(firstnameTwo));
                        assertEquals(Integer.valueOf(firstnameThreeQuantity), firstnameCount.get(firstnameThree));
                        return firstnameCount;
                    });
        });

    }

    @Test
    public void testGroupByHavingWithAlias() {

        transaction((Session session) -> {

            return session.find("u.firstname", "sum(userAge) as sumAge").from(CommonUser.class, "u").groupBy("u.firstname").having("sum(userAge) > ?", 100)
                    .fetchAll((final ResultEntry entry, int count) -> {
                        String rsFirstname = entry.getString("u.firstname");
                        Integer rsCount = entry.getInt("sumAge");
                        getLogger().debug("Found firstname [{}] count [{}]", rsFirstname, rsCount);
                        return new AbstractMap.SimpleEntry<>(rsFirstname, rsCount);
                    })
                    .toMap(entry -> entry.getKey(), entry -> entry.getValue())
                    .map(firstnameAge -> {
                        assertFalse(firstnameAge.isEmpty());
                        assertEquals(3, firstnameAge.size());
                        assertTrue(firstnameAge.containsKey(firstnameOne));
                        assertTrue(firstnameAge.containsKey(firstnameTwo));
                        assertTrue(firstnameAge.containsKey(firstnameThree));
                        assertTrue(firstnameAge.get(firstnameOne) > 100);
                        assertTrue(firstnameAge.get(firstnameTwo) > 100);
                        assertTrue(firstnameAge.get(firstnameThree) > 100);
                        return firstnameAge;
                    });
        });
    }

    @Test
    public void testGroupByWithOrderBy() {

        transaction((Session session) -> {

            return session.find("u.firstname", "count(*) as countName").from(CommonUser.class, "u").groupBy("u.firstname").orderBy().asc("u.firstname")
                    .fetchAll((final ResultEntry entry, int count) -> {
                        String rsFirstname = entry.getString("u.firstname");
                        Integer rsCount = entry.getInt("countName");
                        getLogger().debug("Found firstname [{}] count [{}]", rsFirstname, rsCount);
                        return new AbstractMap.SimpleEntry<>(rsFirstname, rsCount);
                    })
                    .toMap(entry -> entry.getKey(), entry -> entry.getValue())
                    .map(firstnameCount -> {
                        assertFalse(firstnameCount.isEmpty());
                        assertEquals(3, firstnameCount.size());
                        assertTrue(firstnameCount.containsKey(firstnameOne));
                        assertTrue(firstnameCount.containsKey(firstnameTwo));
                        assertTrue(firstnameCount.containsKey(firstnameThree));
                        assertEquals(Integer.valueOf(firstnameOneQuantity), firstnameCount.get(firstnameOne));
                        assertEquals(Integer.valueOf(firstnameTwoQuantity), firstnameCount.get(firstnameTwo));
                        assertEquals(Integer.valueOf(firstnameThreeQuantity), firstnameCount.get(firstnameThree));
                        return firstnameCount;
                    });
        });
    }

    @Before
    public void testSetUp() throws InterruptedException, ExecutionException {
        transaction((Session session) -> {

            session.delete(CommonUser.class).execute().blockingGet();

            for (int i = 0; i < firstnameOneQuantity; i++) {
                CommonUser user = new CommonUser();
                user.setUserAge(Long.valueOf(i));
                user.setFirstname(firstnameOne);
                user.setLastname("surname");
                session.save(user).blockingGet();
            }

            for (int i = 0; i < firstnameTwoQuantity; i++) {
                CommonUser user = new CommonUser();
                user.setUserAge(Long.valueOf(i));
                user.setFirstname(firstnameTwo);
                user.setLastname("surname");
                session.save(user).blockingGet();
            }

            for (int i = 0; i < firstnameThreeQuantity; i++) {
                CommonUser user = new CommonUser();
                user.setUserAge(Long.valueOf(i));
                user.setFirstname(firstnameThree);
                user.setLastname("surname");
                session.save(user).blockingGet();
            }

            return Single.just("");
        });
    }

}
