/*******************************************************************************
 * Copyright 2013 Francesco Cina'

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
package com.jporm.rm.kotlin.query.update

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

import java.sql.Date
import java.util.ArrayList
import java.util.UUID

import org.junit.Before
import org.junit.Test

import com.jporm.core.domain.Employee
import com.jporm.core.domain.Zoo_People
import com.jporm.rm.kotlin.BaseTestApi
import com.jporm.rm.kotlin.JpoRm
import com.jporm.rm.kotlin.JpoRmBuilder
import com.jporm.rm.kotlin.connection.NullTransactionProvider

/**

 * @author Francesco Cina
 * *
 * *         23/giu/2011
 */
class CustomUpdateQueryTest : BaseTestApi() {

    private var jpOrm: JpoRm? = null

    @Before
    fun setUp() {
        jpOrm = JpoRmBuilder.get().build(NullTransactionProvider())
    }

    @Test
    fun testOnlineSqlWriting() {
        JpoRmBuilder.get().build(NullTransactionProvider()).tx { nullSession ->

            // METHOD ONE
            val date = Date(java.util.Date().time)
            val update = nullSession.update(Zoo_People::class.java)
            update.where().eq("birthdate", date) //$NON-NLS-1$
            update.where().eq("deathdate", date) //$NON-NLS-1$
            update.set("id", 1) //$NON-NLS-1$

            val methodOneRendering = update.sqlQuery()

            // SAME QUERY WITH OLD ONLINE WRITING
            val oldOnlineMethodWriting = nullSession.update(Zoo_People::class.java).set("id", 1) //$NON-NLS-1$
                    .where().eq("birthdate", date).eq("deathdate", date).sqlQuery()

            logger.info("Method one query    : " + methodOneRendering) //$NON-NLS-1$
            logger.info("online writing query: " + oldOnlineMethodWriting) //$NON-NLS-1$

            assertEquals(methodOneRendering, oldOnlineMethodWriting)

            // SAME QUERY WITH ONLINE WRITING
            val onlineMethodWriting = nullSession.update(Zoo_People::class.java).set("id", 1).where().eq("birthdate", date).eq("deathdate", date).sqlQuery()

            logger.info("Method one query    : " + methodOneRendering) //$NON-NLS-1$
            logger.info("online writing query: " + onlineMethodWriting) //$NON-NLS-1$

            assertEquals(methodOneRendering, onlineMethodWriting)
        }
    }

    @Test
    fun testUpdate1() {

        jpOrm!!.tx { session ->

            val update = session.update(Employee::class.java)
            update.set("age", "12") //$NON-NLS-1$ //$NON-NLS-2$
            update.where().eq("id", 1) //$NON-NLS-1$
            logger.info(update.sqlQuery())
            val expectedSql = "UPDATE EMPLOYEE SET AGE = ? WHERE ID = ? " //$NON-NLS-1$
            assertEquals(expectedSql, update.sqlQuery())

            val values = ArrayList<Any>()
            update.sqlValues(values)

            assertEquals(2, values.size.toLong())

            assertEquals("12", values[0]) //$NON-NLS-1$
            assertEquals(Integer.valueOf(1), values[1])
        }

    }

    @Test
    fun testUpdate2() {

        jpOrm!!.tx { session ->

            val date = Date(java.util.Date().time)
            val update = session.update(Zoo_People::class.java)
            update.set("birthdate", date) //$NON-NLS-1$
            update.set("deathdate", date) //$NON-NLS-1$
            update.where().eq("id", 1) //$NON-NLS-1$
            logger.info(update.sqlQuery())
            val expectedSql = "UPDATE ZOO.PEOPLE SET BIRTHDATE = ? , DEATHDATE = ? WHERE ID = ? " //$NON-NLS-1$
            assertEquals(expectedSql, update.sqlQuery())

            val values = ArrayList<Any>()
            update.sqlValues(values)

            assertEquals(3, values.size.toLong())

            assertEquals(date, values[0])
            assertEquals(date, values[1])
            assertEquals(Integer.valueOf(1), values[2])
        }
    }

    @Test
    fun testUpdateSetNull() {

        jpOrm!!.tx { session ->

            val update = session.update(Employee::class.java)
            update.set("employeeNumber", null)
            update.set("employeeNumber", UUID.randomUUID().toString())

            // This is to check that a NullPointerException is not thrown
            // see: https://github.com/ufoscout/jporm/issues/86
            assertTrue(update.execute() >= 0)
        }
    }
}
