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
package com.jporm.rm.kotlin.query.delete

import org.junit.Assert.assertEquals

import java.sql.Date
import java.util.ArrayList

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
class CustomDeleteQueryTest : BaseTestApi() {

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
            val delete = nullSession.delete(Zoo_People::class.java)
            delete.where().eq("id", 1).eq("birthdate", date).eq("deathdate", date) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            val methodOneRendering = delete.sqlQuery()

            // SAME QUERY WITH OLD ONLINE WRITING
            val oldOnlineMethodWriting = nullSession.delete(Zoo_People::class.java).where().eq("id", 1).eq("birthdate", date).eq("deathdate", date) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    .sqlQuery()

            logger.info("Method one query        : " + methodOneRendering) //$NON-NLS-1$
            logger.info("old online writing query: " + oldOnlineMethodWriting) //$NON-NLS-1$

            assertEquals(methodOneRendering, oldOnlineMethodWriting)

            // SAME QUERY WITH ONLINE WRITING
            val onlineMethodWriting = nullSession.delete(Zoo_People::class.java).where().eq("id", 1).eq("birthdate", date).eq("deathdate", date) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    .sqlQuery()

            logger.info("Method one query    : " + methodOneRendering) //$NON-NLS-1$
            logger.info("online writing query: " + onlineMethodWriting) //$NON-NLS-1$

            assertEquals(methodOneRendering, onlineMethodWriting)
        }
    }

    @Test
    fun testUpdate1() {

        jpOrm!!.tx { session ->
            val delete = session.delete(Employee::class.java)
            delete.where().eq("id", 1) //$NON-NLS-1$
            logger.info(delete.sqlQuery())
            val expectedSql = "DELETE FROM EMPLOYEE WHERE ID = ? " //$NON-NLS-1$
            assertEquals(expectedSql, delete.sqlQuery())

            val values = ArrayList<Any>()
            delete.sqlValues(values)

            assertEquals(1, values.size.toLong())

            assertEquals(Integer.valueOf(1), values[0])
        }

    }

    @Test
    fun testUpdate2() {

        jpOrm!!.tx { session ->

            val date = Date(java.util.Date().time)
            val delete = session.delete(Zoo_People::class.java)
            delete.where().eq("id", 1).eq("birthdate", date).eq("deathdate", date) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            logger.info(delete.sqlQuery())
            val expectedSql = "DELETE FROM ZOO.PEOPLE WHERE ID = ? AND BIRTHDATE = ? AND DEATHDATE = ? " //$NON-NLS-1$
            assertEquals(expectedSql, delete.sqlQuery())

            val values = ArrayList<Any>()
            delete.sqlValues(values)

            assertEquals(3, values.size.toLong())

            assertEquals(Integer.valueOf(1), values[0])
            assertEquals(date, values[1])
            assertEquals(date, values[2])
        }

    }

}
