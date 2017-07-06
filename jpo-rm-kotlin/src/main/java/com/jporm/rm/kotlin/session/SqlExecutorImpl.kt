/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.jporm.rm.kotlin.session

import com.jporm.commons.core.exception.JpoException
import com.jporm.commons.core.exception.JpoNotUniqueResultException
import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReader
import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReaderUnique
import com.jporm.commons.core.session.ASqlExecutor
import com.jporm.commons.core.util.BigDecimalUtil
import com.jporm.rm.kotlin.connection.Connection
import com.jporm.rm.kotlin.connection.ConnectionProvider
import com.jporm.types.TypeConverterFactory
import com.jporm.types.io.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.math.BigDecimal
import java.util.ArrayList
import java.util.Optional

/**
 * @author Francesco Cina 02/lug/2011
 */
class SqlExecutorImpl
/**
 * @param sqlPerformerStrategy2
 * *
 * @param serviceCatalog
 */
@JvmOverloads constructor(private val connectionProvider: ConnectionProvider<out Connection>, typeFactory: TypeConverterFactory,
                          private val sqlPreProcessor: (String) -> String = SQL_PRE_PROCESSOR_DEFAULT) : ASqlExecutor(typeFactory), SqlExecutor {

    @Throws(JpoException::class)
    override fun batchUpdate(sqls: Collection<String>): IntArray {
        return connectionProvider.connection(true) { connection -> connection.batchUpdate(sqls, sqlPreProcessor) }
    }

    @Throws(JpoException::class)
    override fun batchUpdate(sql: String, psc: BatchPreparedStatementSetter): IntArray {
        val processedSql = preProcessSql(sql)
        return connectionProvider.connection(true) { connection -> connection.batchUpdate(processedSql, psc) }
    }

    @Throws(JpoException::class)
    override fun batchUpdate(sql: String, args: Collection<Array<Any>>): IntArray {
        val processedSql = preProcessSql(sql)
        val statements = ArrayList<(Statement) -> Unit>()
        args.forEach { array -> statements.add(ASqlExecutor.PrepareStatementSetterArrayWrapper(array)) }
        return connectionProvider.connection(true) { connection -> connection.batchUpdate(processedSql, statements) }
    }

    @Throws(JpoException::class)
    override fun execute(sql: String) {
        val processedSql = preProcessSql(sql)
        connectionProvider.connection<Any>(true) { connection ->
            connection.execute(processedSql)
            null
        }
    }

    override fun getLogger(): Logger {
        return LOGGER
    }

    @Throws(JpoException::class)
    override fun <T> query(sql: String, args: Collection<*>, rse: (ResultSet) -> T): T {
        val processedSql = preProcessSql(sql)
        val pss = ASqlExecutor.PrepareStatementSetterCollectionWrapper(args)
        return connectionProvider.connection(true) { connection -> connection.query(processedSql, pss, rse) }
    }

    @Throws(JpoException::class)
    override fun query(sql: String, args: Collection<*>, rse: (ResultSet) -> Unit) {
        query<Unit>(sql, args) { resultSet ->
            rse(resultSet)
        }
    }

    @Throws(JpoException::class)
    override fun <T> query(sql: String, args: Collection<*>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> T): List<T> {
        return query(sql, args, ResultSetRowReaderToResultSetReader(resultSetRowReader))
    }

    @Throws(JpoException::class)
    override fun query(sql: String, args: Collection<*>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> Unit) {
        query(sql, args) { resultSet: ResultSet ->
            var rowNum = 0
            while (resultSet.hasNext()) {
                val entry = resultSet.next()
                resultSetRowReader(entry, rowNum++)
            }
        }
    }

    @Throws(JpoException::class)
    override fun <T> query(sql: String, args: Array<Any>, rse: (ResultSet) -> T): T {
        val processedSql = preProcessSql(sql)
        val pss = ASqlExecutor.PrepareStatementSetterArrayWrapper(args)
        return connectionProvider.connection(true) { connection -> connection.query(processedSql, pss, rse) }
    }

    @Throws(JpoException::class)
    override fun query(sql: String, args: Array<Any>, rse: (ResultSet) -> Unit) {
        query<Unit>(sql, args) { resultSet ->
            rse(resultSet)
        }
    }

    @Throws(JpoException::class)
    override fun <T> query(sql: String, args: Array<Any>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> T): List<T> {
        return query(sql, args, ResultSetRowReaderToResultSetReader(resultSetRowReader))
    }

    @Throws(JpoException::class)
    override fun query(sql: String, args: Array<Any>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> Unit) {
        query(sql, args) { resultSet: ResultSet ->
            var rowNum = 0
            while (resultSet.hasNext()) {
                val entry = resultSet.next()
                resultSetRowReader(entry, rowNum++)
            }
        }
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBigDecimal(sql: String, args: Collection<*>): BigDecimal? {
        return this.query(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBigDecimal(sql: String, vararg args: Any): BigDecimal? {
        return this.query<BigDecimal>(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBigDecimalUnique(sql: String, args: Collection<*>): BigDecimal {
        return this.query(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL_UNIQUE)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBigDecimalUnique(sql: String, vararg args: Any): BigDecimal {
        return this.query<BigDecimal>(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL_UNIQUE)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBoolean(sql: String, args: Collection<*>): Boolean? {
        val result = this.query(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL)
        return BigDecimalUtil.toBoolean(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBoolean(sql: String, vararg args: Any): Boolean? {
        val result = this.query<BigDecimal>(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL)
        return BigDecimalUtil.toBoolean(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBooleanUnique(sql: String, args: Collection<*>): Boolean {
        val result = this.query(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL_UNIQUE)
        return BigDecimalUtil.toBoolean(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBooleanUnique(sql: String, vararg args: Any): Boolean {
        val result = this.query<BigDecimal>(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL_UNIQUE)
        return BigDecimalUtil.toBoolean(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForDouble(sql: String, args: Collection<*>): Double? {
        val result = this.query(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL)
        return BigDecimalUtil.toDouble(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForDouble(sql: String, vararg args: Any): Double? {
        val result = this.query<BigDecimal>(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL)
        return BigDecimalUtil.toDouble(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForDoubleUnique(sql: String, args: Collection<*>): Double {
        val result = this.query(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL_UNIQUE)
        return BigDecimalUtil.toDouble(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForDoubleUnique(sql: String, vararg args: Any): Double {
        val result = this.query<BigDecimal>(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL_UNIQUE)
        return BigDecimalUtil.toDouble(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForFloat(sql: String, args: Collection<*>): Float? {
        val result = this.query(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL)
        return BigDecimalUtil.toFloat(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForFloat(sql: String, vararg args: Any): Float? {
        val result = this.query<BigDecimal>(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL)
        return BigDecimalUtil.toFloat(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForFloatUnique(sql: String, args: Collection<*>): Float {
        val result = this.query(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL_UNIQUE)
        return BigDecimalUtil.toFloat(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForFloatUnique(sql: String, vararg args: Any): Float {
        val result = this.query<BigDecimal>(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL_UNIQUE)
        return BigDecimalUtil.toFloat(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForInt(sql: String, args: Collection<*>): Int? {
        val result = this.query(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL)
        return BigDecimalUtil.toInteger(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForInt(sql: String, vararg args: Any): Int? {
        val result = this.query<BigDecimal>(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL)
        return BigDecimalUtil.toInteger(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForIntUnique(sql: String, args: Collection<*>): Int {
        val result = this.query(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL_UNIQUE)
        return BigDecimalUtil.toInteger(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForIntUnique(sql: String, vararg args: Any): Int {
        val result = this.query<BigDecimal>(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL_UNIQUE)
        return BigDecimalUtil.toInteger(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForLong(sql: String, args: Collection<*>): Long? {
        val result = this.query(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL)
        return BigDecimalUtil.toLong(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForLong(sql: String, vararg args: Any): Long? {
        val result = this.query<BigDecimal>(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL)
        return BigDecimalUtil.toLong(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForLongUnique(sql: String, args: Collection<*>): Long {
        val result = this.query(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL_UNIQUE)
        return BigDecimalUtil.toLong(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForLongUnique(sql: String, vararg args: Any): Long {
        val result = this.query<BigDecimal>(sql, args, ASqlExecutor.RESULT_SET_READER_BIG_DECIMAL_UNIQUE)
        return BigDecimalUtil.toLong(result)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForString(sql: String, args: Collection<*>): String? {
        return this.query(sql, args, ASqlExecutor.RESULT_SET_READER_STRING)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForString(sql: String, vararg args: Any): String? {
        return this.query<String>(sql, args, ASqlExecutor.RESULT_SET_READER_STRING)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForStringUnique(sql: String, args: Collection<*>): String {
        return this.query(sql, args, ASqlExecutor.RESULT_SET_READER_STRING_UNIQUE)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForStringUnique(sql: String, vararg args: Any): String {
        return this.query<String>(sql, args, ASqlExecutor.RESULT_SET_READER_STRING_UNIQUE)
    }

    @Throws(JpoException::class)
    override fun <T> queryForUnique(sql: String, args: Collection<*>, rsrr: (entry: ResultEntry, rowCount: Int) -> T): T {
        return query(sql, args, ResultSetRowReaderToResultSetReaderUnique(rsrr))
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun <T> queryForUnique(sql: String, args: Array<Any>, rsrr: (entry: ResultEntry, rowCount: Int) -> T): T {
        return query(sql, args, ResultSetRowReaderToResultSetReaderUnique(rsrr))
    }

    @Throws(JpoException::class)
    override fun update(sql: String, args: Collection<*>): Int {
        val pss = ASqlExecutor.PrepareStatementSetterCollectionWrapper(args)
        return update(sql, pss)
    }

    @Throws(JpoException::class)
    override fun <R> update(sql: String, args: Collection<*>, generatedKeyReader: GeneratedKeyReader<R>): R {
        val pss = ASqlExecutor.PrepareStatementSetterCollectionWrapper(args)
        return update<R>(sql, pss, generatedKeyReader)
    }

    @Throws(JpoException::class)
    override fun update(sql: String, vararg args: Any): Int {
        val pss = ASqlExecutor.PrepareStatementSetterArrayWrapper(args)
        return update(sql, pss)
    }

    @Throws(JpoException::class)
    override fun <R> update(sql: String, args: Array<Any>, generatedKeyReader: GeneratedKeyReader<R>): R {
        val pss = ASqlExecutor.PrepareStatementSetterArrayWrapper(args)
        return update<R>(sql, pss, generatedKeyReader)
    }

    @Throws(JpoException::class)
    override fun update(sql: String, psc: (Statement) -> Unit): Int {
        val processedSql = preProcessSql(sql)
        return connectionProvider.connection(true) { connection -> connection.update(processedSql, psc) }
    }

    @Throws(JpoException::class)
    override fun <R> update(sql: String, generatedKeyReader: GeneratedKeyReader<R>, psc: (Statement) -> Unit): R {
        val processedSql = preProcessSql(sql)
        return connectionProvider.connection(true) { connection -> connection.update(processedSql, generatedKeyReader, psc) }
    }

    @Throws(JpoException::class)
    override fun <T> queryForOptional(sql: String, args: Collection<*>, rsrr: (entry: ResultEntry, rowCount: Int) -> T): Optional<T> {
        val result = query<T>(sql, args) { rs ->
            if (rs.hasNext()) {
                return@query rsrr(rs.next(), 0)
            }
            null
        }
        return Optional.ofNullable(result)
    }

    @Throws(JpoException::class)
    override fun <T> queryForOptional(sql: String, args: Array<Any>, rsrr: (entry: ResultEntry, rowCount: Int) -> T): Optional<T> {
        val result = query<T>(sql, args) { rs ->
            if (rs.hasNext()) {
                return@query rsrr(rs.next(), 0)
            }
            null
        }
        return Optional.ofNullable(result)
    }

    private fun preProcessSql(sql: String): String {
        return sqlPreProcessor(sql)
    }

    companion object {

        val SQL_PRE_PROCESSOR_DEFAULT = { sql:String -> sql }
        val LOGGER = LoggerFactory.getLogger(SqlExecutorImpl::class.java)
    }

}