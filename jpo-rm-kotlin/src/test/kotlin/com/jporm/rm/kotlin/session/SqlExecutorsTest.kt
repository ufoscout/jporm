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
package com.jporm.rm.kotlin.session

import org.junit.Assert.assertEquals

import java.util.ArrayList
import java.util.Date
import java.util.function.Function

import org.junit.Test

import com.jporm.rm.kotlin.BaseTestApi
import com.jporm.rm.kotlin.JpoRm
import com.jporm.types.io.GeneratedKeyReader
import com.jporm.types.io.ResultSet

/**

 * @author Francesco Cina
 * *
 * *         02/lug/2011
 */
class SqlExecutorsTest : BaseTestApi() {

    private fun checkExistAll(peopleIds: List<Long>, sqlExecutor: SqlExecutor, exist: Boolean) {
        var sql = "select * from people where id in ( " //$NON-NLS-1$
        for (i in 0..peopleIds.size - 1 - 1) {
            sql += "?, " //$NON-NLS-1$
        }
        sql += "? ) " //$NON-NLS-1$

        val rse = Function<ResultSet, List<Long>> { resultSet ->
            val result = ArrayList<Long>()
            while (resultSet.hasNext()) {
                result.add(resultSet.next().getLong("ID")) //$NON-NLS-1$
            }
            result
        }

        val result = sqlExecutor.query(sql, peopleIds.toTypedArray(), rse)
        for (id in peopleIds) {
            println("Check id: " + id + " exists? " + result.contains(id)) //$NON-NLS-1$ //$NON-NLS-2$
            assertEquals(exist, result.contains(id))
        }
    }

    private fun sqlExecutorDelete(ids: List<Long>, sqlExecutor: SqlExecutor) {
        val sql = "delete from people where id = ?" //$NON-NLS-1$

        val args = ArrayList<Array<Any>>()

        for (id in ids) {
            args.add(arrayOf<Any>(id))
        }

        sqlExecutor.batchUpdate(sql, args)

    }

    private fun sqlExecutorInsert(sqlExec: SqlExecutor): List<Long> {
        val results = ArrayList<Long>()

        var idMain = Date().time

        val id1 = idMain++
        results.add(id1)
        val sqlFixed = "insert into people (id, firstname, lastname) values ( $id1 , 'fixed name' , 'fixed surname' )" //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(1, sqlExec.update(sqlFixed, *arrayOfNulls<Any>(0)).toLong())

        val sql1 = "insert into people (id, firstname, lastname) values ( ? , ? , ? )" //$NON-NLS-1$
        val id2 = idMain++
        results.add(id2)
        assertEquals(1, sqlExec.update(sql1, *arrayOf(id2, "name-" + id2, "surname-" + id2)).toLong()) //$NON-NLS-1$ //$NON-NLS-2$

        val args = ArrayList<Array<Any>>()
        val id3 = idMain++
        val id4 = idMain++
        val id5 = idMain++
        results.add(id3)
        results.add(id4)
        results.add(id5)
        args.add(arrayOf(id3, "name-" + id3, "batchUpdate(sql1, args) " + id3)) //$NON-NLS-1$ //$NON-NLS-2$
        args.add(arrayOf(id4, "name-" + id4, "batchUpdate(sql1, args) " + id4)) //$NON-NLS-1$ //$NON-NLS-2$
        args.add(arrayOf(id5, "name-" + id5, "batchUpdate(sql1, args) " + id5)) //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(3, sqlExec.batchUpdate(sql1, args).size.toLong())

        val sqlsFixed = ArrayList<String>()
        val id6 = idMain++
        val id7 = idMain++
        results.add(id6)
        results.add(id7)
        sqlsFixed.add("insert into people (id, firstname, lastname) values ( $id6 , 'batchUpdate(sqlsFixed)' , '1' )") //$NON-NLS-1$ //$NON-NLS-2$
        sqlsFixed.add("insert into people (id, firstname, lastname) values ( $id7 , 'batchUpdate(sqlsFixed)' , '2' )") //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(2, sqlExec.batchUpdate(sqlsFixed).size.toLong())

        val sqlKeyExtractor = "insert into people (id, firstname, lastname) values ( SEQ_PEOPLE.nextval , ? , ? )" //$NON-NLS-1$
        val generatedKeyExtractor = object : GeneratedKeyReader<Int> {

            override fun generatedColumnNames(): Array<String> {
                return arrayOf("ID") //$NON-NLS-1$
            }

            override fun read(generatedKeyResultSet: ResultSet, affectedRows: Int): Int? {
                generatedKeyResultSet.hasNext()
                val gk = generatedKeyResultSet.next().getLong(0)
                println("Generated key: " + gk) //$NON-NLS-1$
                results.add(gk)
                return affectedRows
            }
        }
        assertEquals(1,
                sqlExec.update(sqlKeyExtractor, arrayOf<Any>("sqlExec.update(sqlKeyExtractor, generatedKeyExtractor, args", "1"), generatedKeyExtractor).toInt().toLong()) //$NON-NLS-1$ //$NON-NLS-2$

        return results
    }

    @Test
    fun testExecuteAll() {
        val jpOrm = jpo

        val ids = jpOrm.tx().execute<List<Long>> { _session -> sqlExecutorInsert(_session.sql().executor()) }

        jpOrm.tx().execute { _session -> checkExistAll(ids, _session.sql().executor(), true) }

        jpOrm.tx().execute { _session -> sqlExecutorDelete(ids, _session.sql().executor()) }

        jpOrm.tx().execute { _session -> checkExistAll(ids, _session.sql().executor(), false) }


    }

    //    @Test
    //    public void fetchStream() {
    //        getJPO().transaction().execute(session -> {
    //            SqlExecutor sqlExecutor = session.sql().executor();
    //
    //            sqlExecutor
    //
    //        });
    //    }
}
