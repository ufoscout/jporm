/**
 * *****************************************************************************
 * Copyright 2015 Francesco Cina'

 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ****************************************************************************
 */
package com.jporm.rm.kotlin.session

import org.junit.Assert.assertTrue
import java.util.UUID

import org.junit.Test

import com.jporm.rm.kotlin.BaseTestApi
import com.jporm.test.domain.section08.CommonUser
import com.jporm.types.io.ResultEntry

class SessionSaveQueryTest : BaseTestApi() {

    @Test
    fun testOne() {
        jpo.tx { session ->
            val firstname1 = UUID.randomUUID().toString()
            val firstname2 = UUID.randomUUID().toString()
            val lastname = UUID.randomUUID().toString()

            val updateResult = session.save(CommonUser::class.java, "firstname", "lastname").values(firstname1, lastname).values(firstname2, lastname).execute()
            assertTrue(updateResult == 2)

            val foundUsers = session.find<Any>("u.firstname").from(CommonUser::class.java, "u").where("u.lastname = ?", lastname)
                    .fetchAll<String> { rs: ResultEntry, rowNum: Int -> rs.getString("u.firstname") }
            assertTrue(foundUsers.size == 2)
            assertTrue(foundUsers.contains(firstname1))
            assertTrue(foundUsers.contains(firstname2))
        }
    }

}
