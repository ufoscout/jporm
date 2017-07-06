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
package com.jporm.rm.kotlin.connection

import com.jporm.commons.core.exception.JpoException
import com.jporm.commons.core.transaction.TransactionIsolation
import com.jporm.types.io.BatchPreparedStatementSetter
import com.jporm.types.io.GeneratedKeyReader
import com.jporm.types.io.ResultSet
import com.jporm.types.io.Statement

/**

 * @author Francesco Cina'
 * *
 * *         Dec 20, 2011
 * *
 * *         The implementations of this class MUST be stateless and Thread safe.
 */
interface Connection {

    @Throws(JpoException::class)
    fun batchUpdate(sqls: Collection<String>, sqlPreProcessor: (String) -> String): IntArray

    @Throws(JpoException::class)
    fun batchUpdate(sql: String, psc: BatchPreparedStatementSetter): IntArray

    @Throws(JpoException::class)
    fun batchUpdate(sql: String, statementSetters: Collection<(Statement) -> Unit>): IntArray

    @Throws(JpoException::class)
    fun execute(sql: String)

    @Throws(JpoException::class)
    fun <T> query(sql: String, statementSetter: (Statement) -> Unit, resultSetReader: (ResultSet) -> T): T

    fun setReadOnly(readOnly: Boolean)

    fun setTimeout(timeout: Int)

    fun setTransactionIsolation(isolationLevel: TransactionIsolation)

    fun update(sql: String, statementSetter: (Statement) -> Unit): Int

    fun <R> update(sql: String, generatedKeyReader: GeneratedKeyReader<R>, statementSetter: (Statement) -> Unit): R

}
