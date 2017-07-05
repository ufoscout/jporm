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
package com.jporm.rm.quasar.connection.datasource

import java.util.function.Consumer
import java.util.function.Function

import com.jporm.commons.core.async.AsyncTaskExecutor
import com.jporm.commons.core.exception.JpoException
import com.jporm.commons.core.transaction.TransactionIsolation
import com.jporm.rm.connection.datasource.DataSourceConnection
import com.jporm.rm.quasar.connection.JpoCompletableWrapper
import com.jporm.types.io.BatchPreparedStatementSetter
import com.jporm.types.io.GeneratedKeyReader
import com.jporm.types.io.ResultSet
import com.jporm.types.io.Statement

class KotlinDataSourceConnection(private val connection: DataSourceConnection, private val executor: AsyncTaskExecutor) : DataSourceConnection {

    @Override
    @Throws(JpoException::class)
    fun batchUpdate(sqls: Collection<String>, sqlPreProcessor: Function<String, String>): IntArray {
        return JpoCompletableWrapper.get(executor.execute({ connection.batchUpdate(sqls, sqlPreProcessor) }))

    }

    @Override
    @Throws(JpoException::class)
    fun batchUpdate(sql: String, psc: BatchPreparedStatementSetter): IntArray {
        return JpoCompletableWrapper.get(executor.execute({ connection.batchUpdate(sql, psc) }))

    }

    @Override
    @Throws(JpoException::class)
    fun batchUpdate(sql: String, args: Collection<Consumer<Statement>>): IntArray {
        return JpoCompletableWrapper.get(executor.execute({ connection.batchUpdate(sql, args) }))
    }

    @Override
    @Throws(JpoException::class)
    fun execute(sql: String) {
        JpoCompletableWrapper.get(executor.execute({ connection.execute(sql) }))
    }

    @Override
    @Throws(JpoException::class)
    fun <T> query(sql: String, pss: Consumer<Statement>, rse: Function<ResultSet, T>): T {
        return JpoCompletableWrapper.get(executor.execute({ connection.query(sql, pss, rse) }))
    }

    @Override
    fun setReadOnly(readOnly: Boolean) {
        connection.setReadOnly(readOnly)
    }

    @Override
    fun setTimeout(timeout: Int) {
        connection.setTimeout(timeout)
    }

    @Override
    fun setTransactionIsolation(isolationLevel: TransactionIsolation) {
        connection.setTransactionIsolation(isolationLevel)
    }

    @Override
    @Throws(JpoException::class)
    fun <R> update(sql: String, generatedKeyReader: GeneratedKeyReader<R>, pss: Consumer<Statement>): R {
        return JpoCompletableWrapper.get(executor.execute({ connection.update(sql, generatedKeyReader, pss) }))
    }

    @Override
    fun update(sql: String, pss: Consumer<Statement>): Int {
        return JpoCompletableWrapper.get(executor.execute({ connection.update(sql, pss) }))
    }

    @Override
    fun close() {
        JpoCompletableWrapper.get(executor.execute({ connection.close() }))
    }

    @Override
    fun commit() {
        JpoCompletableWrapper.get(executor.execute({ connection.commit() }))
    }

    @Override
    fun rollback() {
        JpoCompletableWrapper.get(executor.execute({ connection.rollback() }))
    }

    @Override
    fun setAutoCommit(autoCommit: Boolean) {
        connection.setAutoCommit(autoCommit)
    }

    val isClosed: Boolean
        @Override
        get() = connection.isClosed()

}
