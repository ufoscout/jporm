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
package com.jporm.rm.session;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.jporm.rm.BaseTestApi;
import com.jporm.test.domain.section08.CommonUser;
import com.jporm.types.io.ResultEntry;

public class SessionSaveQueryTest extends BaseTestApi {

    @Test
    public void testOne() {
        getJPO().tx(session -> {
            final String firstname1 = UUID.randomUUID().toString();
            final String firstname2 = UUID.randomUUID().toString();
            final String lastname = UUID.randomUUID().toString();

            int updateResult = session.save(CommonUser.class, "firstname", "lastname").values(firstname1, lastname).values(firstname2, lastname).execute();
            assertTrue(updateResult == 2);

            List<String> foundUsers = session.find("u.firstname").from(CommonUser.class, "u").where("u.lastname = ?", lastname)
                    .fetchAll((final ResultEntry rs, final int rowNum) -> {
                        return rs.getString("u.firstname");
                    });
            assertTrue(foundUsers.size() == 2);
            assertTrue(foundUsers.contains(firstname1));
            assertTrue(foundUsers.contains(firstname2));
        });
    }

}
