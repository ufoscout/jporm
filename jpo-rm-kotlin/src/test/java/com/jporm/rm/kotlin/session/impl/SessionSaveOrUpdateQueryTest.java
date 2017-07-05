/**
 * *****************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rm.kotlin.session.impl;

import static org.junit.Assert.*;

import java.util.UUID;

import com.jporm.rm.kotlin.BaseTestApi;
import com.jporm.rm.kotlin.session.Session;
import org.junit.Test;

import com.jporm.test.domain.section08.CommonUser;

import io.reactivex.Single;

public class SessionSaveOrUpdateQueryTest extends BaseTestApi {

    @Test
    public void testOne() throws Throwable {
        final String firstname = UUID.randomUUID().toString();
        final String lastname = UUID.randomUUID().toString();

        CommonUser newUser = new CommonUser();
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);

        Single<CommonUser> saveOrUpdateUser = newJpo().tx((Session session) -> {
            return session.saveOrUpdate(newUser).flatMap(savedUser -> {

                assertNotNull(savedUser);

                final String newfirstname = UUID.randomUUID().toString();
                savedUser.setFirstname(newfirstname);

                return session.saveOrUpdate(savedUser).flatMap(updatedUser -> {
                    assertEquals(updatedUser.getFirstname(), newfirstname);
                    assertEquals(savedUser.getLastname(), updatedUser.getLastname());
                    assertEquals(savedUser.getId(), updatedUser.getId());

                    return session.findById(CommonUser.class, savedUser.getId()).fetchOneUnique().map(foundUser -> {
                        assertEquals(newfirstname, foundUser.getFirstname());
                        return foundUser;
                    });
                });
            });
        });

        assertNotNull(saveOrUpdateUser.blockingGet());
    }

}
