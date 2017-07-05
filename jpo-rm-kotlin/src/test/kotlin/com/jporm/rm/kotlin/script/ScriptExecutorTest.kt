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
package com.jporm.rm.kotlin.script

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList

import org.junit.Before
import org.junit.Test

import com.jporm.commons.core.exception.JpoException
import com.jporm.core.domain.TempTable
import com.jporm.rm.kotlin.BaseTestApi
import com.jporm.rm.kotlin.JpoRm
import com.jporm.rm.kotlin.query.find.CustomFindQuery
import com.jporm.rm.kotlin.session.ScriptExecutor

/**

 * @author Francesco Cina
 * *
 * *         02/lug/2011
 */
class ScriptExecutorTest : BaseTestApi() {

    private var filename: String? = null

    @Throws(Exception::class)
    private fun executeScript(jpOrm: JpoRm) {

        jpOrm.tx().execute<Any> { session ->
            val scriptExecutor = session.scriptExecutor()

            try {
                FileInputStream(filename!!).use { scriptStream -> scriptExecutor.execute(scriptStream) }
            } catch (e: JpoException) {
                throw RuntimeException(e)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

            null
        }

    }

    @Before
    fun setUp() {

        filename = testInputBasePath + "/StreamParserTest_1.sql" //$NON-NLS-1$
        assertTrue(File(filename!!).exists())
    }

    @Test
    @Throws(Exception::class)
    fun testScript() {
        val jpo = jpo
        executeScript(jpo)
        verifyData(jpo)
    }

    private fun verifyData(jpOrm: JpoRm) {

        jpOrm.tx { session ->
            val query = session.find(TempTable::class.java, "TempTable") //$NON-NLS-1$
            query.orderBy().asc("TempTable.id") //$NON-NLS-1$
            val result = query.fetchAll()

            logger.info("result.size() = " + result.size) //$NON-NLS-1$

            for (i in result.indices) {
                val temp = result[i]
                logger.info("Found element id: " + temp.id + " - name: " + temp.name) //$NON-NLS-1$ //$NON-NLS-2$
            }

            val expectedResult = ArrayList<String>()
            expectedResult.add("one") //$NON-NLS-1$
            expectedResult.add("two") //$NON-NLS-1$
            expectedResult.add("three") //$NON-NLS-1$
            expectedResult.add("four;") //$NON-NLS-1$
            expectedResult.add("f'ive;") //$NON-NLS-1$
            expectedResult.add("s'ix;") //$NON-NLS-1$
            expectedResult.add("seven';{--ix;") //$NON-NLS-1$
            expectedResult.add("height';{--ix;") //$NON-NLS-1$
            expectedResult.add("ni';ne';{--ix;") //$NON-NLS-1$
            expectedResult.add("ten';{--ix;") //$NON-NLS-1$
            expectedResult.add("e'le;{--ven;") //$NON-NLS-1$

            assertEquals(expectedResult.size.toLong(), result.size.toLong())

            for (i in result.indices) {
                val temp = result[i]
                logger.info("check element id: " + temp.id + " - name: " + temp.name) //$NON-NLS-1$ //$NON-NLS-2$
                assertEquals(expectedResult[i], temp.name)
            }
        }

    }

}
