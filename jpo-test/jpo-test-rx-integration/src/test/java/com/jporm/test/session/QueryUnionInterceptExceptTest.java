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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CommonUser;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

/**
 *
 * @author Francesco Cina
 *
 *         05/giu/2011
 */
public class QueryUnionInterceptExceptTest extends BaseTestAllDB {

    public QueryUnionInterceptExceptTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    private CommonUser createUser(String firstName) {
        CommonUser commonUser = new CommonUser();
        commonUser.setUserAge(10l);
        commonUser.setFirstname(firstName);
        commonUser.setLastname("surname");
        return commonUser;
    }

    private boolean contains(String firstname, List<CommonUser> users) {
        for (CommonUser user : users) {
            if (user.getFirstname().equals(firstname)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testUnion() throws Exception {
        TestObserver<Object> subscriber = new TestObserver<>();

        getJPO().tx().timeout(2).execute((Session session) -> {

                session.delete(CommonUser.class).execute().blockingGet();

                session.save(createUser("one")).blockingGet();
                session.save(createUser("two")).blockingGet();
                session.save(createUser("three")).blockingGet();

                List<CommonUser> users;
                users = session.find(CommonUser.class).where().eq("firstname", "one").union(session.find(CommonUser.class).where().eq("firstname", "two"))
                        .fetchAll().buffer(1000).blockingFirst();

                assertEquals(2, users.size());
                assertTrue(contains("one", users));
                assertTrue(contains("two", users));
                assertFalse(contains("three", users));

                return Single.just("");
        })
        .subscribe(subscriber);
        subscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        subscriber.assertComplete();
    }

    @Test
    public void testUnionWithDuplicates() throws Exception {
        TestObserver<Object> subscriber = new TestObserver<>();
        getJPO().tx().timeout(2).execute((Session session) -> {

                session.delete(CommonUser.class).execute().blockingGet();

                session.save(createUser("one")).blockingGet();
                session.save(createUser("two")).blockingGet();
                session.save(createUser("three")).blockingGet();

                List<CommonUser> users = session.find(CommonUser.class).where().eq("firstname", "one")
                        .union(session.find(CommonUser.class).where().eq("firstname", "one").or().eq("firstname", "two")).fetchAll().buffer(1000).blockingFirst();

                assertEquals(2, users.size());
                assertTrue(contains("one", users));
                assertTrue(contains("two", users));
                assertFalse(contains("three", users));
                return Single.just("");
        })
        .subscribe(subscriber);
        subscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        subscriber.assertComplete();
    }

    @Test
    public void testUnionAll() throws Exception {
        TestObserver<Object> subscriber = new TestObserver<>();
        getJPO().tx().timeout(2).execute((Session session) -> {

                session.delete(CommonUser.class).execute().blockingGet();

                session.save(createUser("one")).blockingGet();
                session.save(createUser("two")).blockingGet();
                session.save(createUser("three")).blockingGet();

                List<CommonUser> users = session.find(CommonUser.class).where().eq("firstname", "one")
                        .unionAll(session.find(CommonUser.class).where().eq("firstname", "one").or().eq("firstname", "two")).fetchAll().buffer(1000).blockingFirst();

                assertEquals(3, users.size());
                assertTrue(contains("one", users));
                assertTrue(contains("two", users));
                assertFalse(contains("three", users));

                return Single.just("");
        })
        .subscribe(subscriber);
        subscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        subscriber.assertComplete();
    }

    // @Test
    // public void testIntersect() {
    // getJPO().transaction().executeVoid((Session session) -> {
    //
    // session.delete(CommonUser.class).execute().get();
    //
    // session.save(createUser("one"));
    // session.save(createUser("two"));
    // session.save(createUser("three"));
    //
    // List<CommonUser> users = session.find(CommonUser.class)
    // .intersect(session.find(CommonUser.class).where().eq("firstname", "two"))
    // .fetchList();
    //
    // assertEquals(1, users.size());
    // assertFalse(contains("one", users));
    // assertTrue(contains("two", users));
    // assertFalse(contains("three", users));
    // });
    // }

    // @Test
    // public void testExcept() {
    // getJPO().transaction().executeVoid((Session session) -> {
    //
    // session.delete(CommonUser.class).execute().get();
    //
    // session.save(createUser("one"));
    // session.save(createUser("two"));
    // session.save(createUser("three"));
    //
    // List<CommonUser> users = session.find(CommonUser.class)
    // .except(session.find(CommonUser.class).where().eq("firstname", "three"))
    // .fetchList();
    //
    // assertEquals(2, users.size());
    // assertTrue(contains("one", users));
    // assertTrue(contains("two", users));
    // assertFalse(contains("three", users));
    // });
    // }
}
