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
package com.jporm.rx.session.impl;

import java.util.UUID;

import org.junit.Test;

import com.jporm.rx.BaseTestApi;
import com.jporm.rx.session.Session;
import com.jporm.test.domain.section08.CommonUser;

public class SessionSaveQueryTest extends BaseTestApi {

    @Test
    public void testOne() throws Throwable {
        final String firstname = UUID.randomUUID().toString();
        final String lastname = UUID.randomUUID().toString();

        Session session = newJpo().session();
        session.save(CommonUser.class, "firstname", "lastname").values(firstname, lastname).execute().thenAccept(updateResult -> {
            threadAssertTrue(updateResult.updated() == 1);

            session.find(CommonUser.class).where("firstname = ?", firstname).fetch().thenAccept(foundUser -> {
                threadAssertEquals(lastname, foundUser.getLastname());
                resume();
            });

        });
        await(2000, 1);
    }

}
