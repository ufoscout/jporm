/*******************************************************************************
 * Copyright 2015 Francesco Cina'

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jporm.rm.kotlin.query.save

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

import java.util.ArrayList

import org.junit.Test

import com.jporm.core.domain.Employee
import com.jporm.rm.kotlin.BaseTestApi

class CustomSaveQueryImplTest : BaseTestApi() {

    @Test
    fun testSaveQuerySintax() {

        jpo.tx { session ->

            val save = session.save(Employee::class.java, "id", "employeeNumber", "name")

            save.values("idValue", "employeeNumberValue", null)

            println(save.sqlQuery())
            val expectedSql = "INSERT INTO EMPLOYEE (ID, EMPLOYEE_NUMBER, NAME) VALUES (?, ?, ?) "
            assertEquals(expectedSql, save.sqlQuery())

            val values = ArrayList<Any>()
            save.sqlValues(values)

            assertEquals(3, values.size.toLong())

            assertEquals("idValue", values[0]) //$NON-NLS-1$
            assertEquals("employeeNumberValue", values[1])
            assertNull(values[2])
        }

    }

}
