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
package com.jporm.rx.reactor.session.impl;

import java.util.UUID;

import org.junit.Test;

import com.jporm.rx.reactor.BaseTestApi;
import com.jporm.rx.reactor.session.Session;
import com.jporm.test.domain.section08.CommonUser;

public class SessionUpdateQueryTest extends BaseTestApi {

    @Test
    public void testOne() throws Throwable {
        final String firstname = UUID.randomUUID().toString();
        final String lastname = UUID.randomUUID().toString();

        CommonUser newUser = new CommonUser();
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);

        Session session = newJpo().session();
        session.save(newUser).thenAccept(savedUser -> {

            threadAssertNotNull(savedUser);

            final String newfirstname = UUID.randomUUID().toString();
            session.update(CommonUser.class).set("firstname", newfirstname).where().eq("firstname", firstname).execute().thenAccept(updateResult -> {
                threadAssertTrue(updateResult.updated() == 1);

                session.findById(CommonUser.class, savedUser.getId()).fetchOne().thenAccept(foundUser -> {
                    threadAssertEquals(newfirstname, foundUser.getFirstname());
                    resume();
                });
            });

        });
        await(2000, 1);
    }

}
