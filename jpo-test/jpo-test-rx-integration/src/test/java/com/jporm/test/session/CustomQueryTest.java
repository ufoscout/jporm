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

import java.util.UUID;

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
public class CustomQueryTest extends BaseTestAllDB {

    public CustomQueryTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testCustomFetchOptionalQuery() {
        transaction((Session session) -> {

                String random = UUID.randomUUID().toString();

                assertEquals(Integer.valueOf(0), session.find(CommonUser.class).where().eq("firstname", random).fetchRowCount().blockingGet());
                assertFalse(session.find("u.firstname").from(CommonUser.class, "u").where().eq("u.firstname", random).fetchOneOptional((rs, i) -> {
                    return rs.getString(0);
                }).blockingGet().isPresent());

                CommonUser user1 = new CommonUser();
                user1.setFirstname(random);
                user1.setLastname(random);
                user1 = session.save(user1).blockingGet();

                assertEquals(Integer.valueOf(1), session.find(CommonUser.class).where().eq("firstname", random).fetchRowCount().blockingGet());
                assertEquals(random, session.find("u.firstname").from(CommonUser.class, "u").where().eq("u.firstname", random).fetchOneOptional((rs, i) -> {
                    return rs.getString(0);
                }).blockingGet().get());

                CommonUser user2 = new CommonUser();
                user2.setFirstname(random);
                user2.setLastname(random);
                user2 = session.save(user2).blockingGet();

                assertEquals(Integer.valueOf(2), session.find(CommonUser.class).where().eq("firstname", random).fetchRowCount().blockingGet());
                assertTrue(session.find("u.firstname")
                        .from(CommonUser.class, "u")
                        .where()
                            .eq("u.firstname", random)
                            .fetchOneOptional((rs, i) -> {
                                return rs.getString(0);
                            })
                         .blockingGet().isPresent()
                );

                return Single.just("");
        });
    }

}
