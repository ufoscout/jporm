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
package com.jporm.rm.kotlin.query.delete

import org.junit.Assert.assertEquals

import org.junit.Test

import com.jporm.rm.kotlin.BaseTestApi

class DeleteQueryListDecoratorTest : BaseTestApi() {

    private inner class TestDeleteQuery internal constructor(internal var value: Int) : DeleteQuery {

        override fun execute(): Int {
            return value
        }

    }

    @Test
    fun testDeleteQueryListDecorator() {
        val query1 = TestDeleteQuery(1)
        val query2 = TestDeleteQuery(2)
        val query3 = TestDeleteQuery(3)
        val queryList = DeleteQueryListDecorator()
        queryList.add(query1)
        queryList.add(query2)
        queryList.add(query3)

        val result = queryList.execute()
        assertEquals((1 + 2 + 3).toLong(), result.toLong())

    }

}
