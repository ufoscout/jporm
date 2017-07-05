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
import java.util.ArrayList

import org.junit.Before
import org.junit.Test

import com.jporm.commons.core.util.GenericWrapper
import com.jporm.rm.kotlin.BaseTestApi
import com.jporm.rm.kotlin.session.script.Parser
import com.jporm.rm.kotlin.session.script.StreamParser

/**

 * @author Francesco Cina
 * *
 * *         01/lug/2011
 */
class StreamParserTest : BaseTestApi() {

    private var filename: String? = null

    @Before
    fun setUp() {
        filename = testInputBasePath + "/StreamParserTest_1.sql" 
        assertTrue(File(filename!!).exists())
    }

    @Test
    @Throws(Exception::class)
    fun testParser() {

        val expectedList = ArrayList<String>()

        expectedList.add("create table STRANGE_TABLE ()") 
        expectedList.add("drop table STRANGE_TABLE") 
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (1, 'one')") 
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (2, 'two')") 
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (3, 'three')") 
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (4, 'four;')") 
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (5, 'f''ive;')") 
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (6, 's''ix;')") 
        expectedList.add("insert\ninto TEMP_TABLE (ID, NAME) values (7, 'seven'';{--ix;')") 
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (8, 'height'';{--ix;')") 
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (9, 'ni'';ne'';{--ix;')") 
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (10, 'ten'';{--ix;')") 
        expectedList.add(" insert\ninto TEMP_TABLE (ID, NAME) values (11, 'e''le;{--ven;')") 

        val fis = FileInputStream(filename!!)
        val parser = StreamParser(fis, true)
        val countWrapper = GenericWrapper(0)

        parser.parse { text: String ->
            var count = countWrapper.value
            println("------- BEGIN -------------") 
            println("Received: " + text) 
            println("expected: " + expectedList[count]) 
            assertEquals(expectedList[count], text)
            println("------- END ---------------") 
            countWrapper.setValue(++count)
        }
    }
}
