/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rx.session.impl;

import static org.junit.Assert.*;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.jporm.rx.BaseTestApi;
import com.jporm.rx.JpoRx;
import com.jporm.rx.session.Session;
import com.jporm.test.domain.section08.CommonUser;

import io.reactivex.observers.TestObserver;

public class SessionImplCRUDTest extends BaseTestApi {

    @Test
    public void testOne() throws Throwable {

        TestObserver<Optional<CommonUser>> subscriber = new TestObserver<>();

        JpoRx jpo = newJpo();
        final String firstname = UUID.randomUUID().toString();
        final String lastname = UUID.randomUUID().toString();

        CommonUser newUser = new CommonUser();
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);

        jpo.tx((Session session) -> {
            // SAVE
            return session.save(newUser).flatMap(savedUser -> {

                assertNotNull(savedUser);
                assertNotNull(savedUser.getId());
                assertNotNull(savedUser.getVersion());

                // FIND
                return session.findById(CommonUser.class, savedUser.getId()).fetchOneUnique().flatMap(foundUser -> {

                    getLogger().info("Found bean {}", foundUser);
                    assertNotNull(foundUser);
                    getLogger().info("Found bean with id {}", foundUser.getId());
                    getLogger().info("Found bean with firstname {}", foundUser.getFirstname());
                    getLogger().info("Found bean with lastname {}", foundUser.getLastname());
                    getLogger().info("Found bean with version {}", foundUser.getVersion());

                    assertEquals(savedUser.getId(), foundUser.getId());
                    assertEquals(firstname, foundUser.getFirstname());

                    // jpo.session().findQuery("u.firstname, u.id", User.class,
                    // "u").where().eq("u.id", userId).getList(customQueryResult
                    // ->
                    // {
                    // threadAssertTrue(customQueryResult.succeeded());
                    // threadAssertEquals(1, customQueryResult.result().size()
                    // );
                    // getLogger().info("Found with custom query {}",
                    // customQueryResult.result().get(0));
                    // threadAssertEquals(firstname,
                    // customQueryResult.result().get(0).getString("u.firstname")
                    // );
                    // });

                    // UPDATE
                    foundUser.setFirstname(UUID.randomUUID().toString());
                    return session.update(foundUser).flatMap(updatedUser -> {

                        getLogger().info("Update bean {}", updatedUser);
                        assertNotNull(updatedUser);
                        getLogger().info("Update bean with id {}", updatedUser.getId());
                        getLogger().info("Update bean with firstname {}", updatedUser.getFirstname());
                        getLogger().info("Update bean with lastname {}", updatedUser.getLastname());
                        getLogger().info("Update bean with version {}", updatedUser.getVersion());

                        assertEquals(foundUser.getId(), updatedUser.getId());
                        assertEquals(foundUser.getFirstname(), updatedUser.getFirstname());

                        // The bean version should be increased
                        assertEquals(foundUser.getVersion().longValue() + 1, updatedUser.getVersion().longValue());

                        // FIND THE UPDATED USER TO VERIFY THAT DATA HAS BEEN
                        // PERSISTED
                        return session.findById(CommonUser.class, updatedUser.getId()).fetchOneUnique().flatMap(foundUpdatedUser -> {

                            getLogger().info("Found Updated bean {}", foundUpdatedUser);
                            assertNotNull(foundUpdatedUser);
                            getLogger().info("Found Updated bean with id {}", foundUpdatedUser.getId());
                            getLogger().info("Found Updated bean with firstname {}", foundUpdatedUser.getFirstname());
                            getLogger().info("Found Updated bean with lastname {}", foundUpdatedUser.getLastname());
                            getLogger().info("Found Updated bean with version {}", foundUpdatedUser.getVersion());

                            assertEquals(updatedUser.getId(), foundUpdatedUser.getId());
                            assertEquals(updatedUser.getFirstname(), foundUpdatedUser.getFirstname());
                            assertEquals(updatedUser.getVersion(), foundUpdatedUser.getVersion());

                            // DELETE
                            return session.delete(savedUser).flatMap(deleteResult -> {

                                getLogger().info("User deleted");
                                assertNotNull(deleteResult);
                                assertEquals(1, deleteResult.deleted());

                                // FIND DELETED USER
                                return session.findById(CommonUser.class, savedUser.getId()).fetchOneOptional().map(deletedUser -> {
                                    getLogger().info("Found bean {}", deletedUser);
                                    assertFalse(deletedUser.isPresent());
                                    return deletedUser;
                                });

                            });

                        });

                    });

                });
            }).toMaybe();
        }).subscribe(subscriber);

        subscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        subscriber.assertComplete();
    }

}
