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
import com.jporm.types.io.*
import java.math.BigDecimal

/**
 * @author Francesco Cina 02/lug/2011
 */
class SqlExecutorImpl
/**
 * @param sqlPerformerStrategy2
 * *
 * @param serviceCatalog
 */
@JvmOverloads constructor(private val rmExecutor: com.jporm.rm.session.SqlExecutor) : SqlExecutor {

    @Throws(JpoException::class)
    override fun batchUpdate(sqls: Collection<String>): IntArray {
        return rmExecutor.batchUpdate(sqls);
    }

    @Throws(JpoException::class)
    override fun batchUpdate(sql: String, psc: BatchPreparedStatementSetter): IntArray {
        return rmExecutor.batchUpdate(sql, psc);
    }

    @Throws(JpoException::class)
    override fun batchUpdate(sql: String, args: Collection<Array<Any>>): IntArray {
        return rmExecutor.batchUpdate(sql, args);
    }

    @Throws(JpoException::class)
    override fun execute(sql: String) {
        rmExecutor.execute(sql);
    }

    @Throws(JpoException::class)
    override fun <T> query(sql: String, args: Collection<*>, rse: (ResultSet) -> T): T {
        return rmExecutor.query(sql, args, rse);
    }

    @Throws(JpoException::class)
    override fun query(sql: String, args: Collection<*>, rse: (ResultSet) -> Unit) {
        rmExecutor.query(sql, args, rse);
    }

    @Throws(JpoException::class)
    override fun <T> query(sql: String, args: Collection<*>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> T): List<T> {
        return rmExecutor.query(sql, args, ResultSetRowReaderToResultSetReader(resultSetRowReader))
    }

    @Throws(JpoException::class)
    override fun query(sql: String, args: Collection<*>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> Unit) {
        rmExecutor.query(sql, args, resultSetRowReader);
    }

    @Throws(JpoException::class)
    override fun <T> query(sql: String, args: Array<Any>, rse: (ResultSet) -> T): T {
        return rmExecutor.query(sql, args, rse)
    }

    @Throws(JpoException::class)
    override fun query(sql: String, args: Array<Any>, rse: (ResultSet) -> Unit) {
        rmExecutor.query(sql, args, rse)
    }

    @Throws(JpoException::class)
    override fun <T> query(sql: String, args: Array<Any>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> T): List<T> {
        return rmExecutor.query(sql, args, resultSetRowReader)
    }

    @Throws(JpoException::class)
    override fun query(sql: String, args: Array<Any>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> Unit) {
        rmExecutor.query(sql, args, resultSetRowReader)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBigDecimal(sql: String, args: Collection<*>): BigDecimal? {
        return rmExecutor.queryForBigDecimal(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBigDecimal(sql: String, vararg args: Any): BigDecimal? {
        return rmExecutor.queryForBigDecimal(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBigDecimalUnique(sql: String, args: Collection<*>): BigDecimal {
        return rmExecutor.queryForBigDecimalUnique(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBigDecimalUnique(sql: String, vararg args: Any): BigDecimal {
        return rmExecutor.queryForBigDecimal(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBoolean(sql: String, args: Collection<*>): Boolean? {
        return rmExecutor.queryForBoolean(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBoolean(sql: String, vararg args: Any): Boolean? {
        return rmExecutor.queryForBoolean(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBooleanUnique(sql: String, args: Collection<*>): Boolean {
        return rmExecutor.queryForBooleanUnique(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForBooleanUnique(sql: String, vararg args: Any): Boolean {
        return rmExecutor.queryForBooleanUnique(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForDouble(sql: String, args: Collection<*>): Double? {
        return rmExecutor.queryForDouble(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForDouble(sql: String, vararg args: Any): Double? {
        return rmExecutor.queryForDouble(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForDoubleUnique(sql: String, args: Collection<*>): Double {
        return rmExecutor.queryForDoubleUnique(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForDoubleUnique(sql: String, vararg args: Any): Double {
        return rmExecutor.queryForDoubleUnique(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForFloat(sql: String, args: Collection<*>): Float? {
        return rmExecutor.queryForFloat(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForFloat(sql: String, vararg args: Any): Float? {
        return rmExecutor.queryForFloat(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForFloatUnique(sql: String, args: Collection<*>): Float {
        return rmExecutor.queryForFloatUnique(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForFloatUnique(sql: String, vararg args: Any): Float {
        return rmExecutor.queryForFloatUnique(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForInt(sql: String, args: Collection<*>): Int? {
        return rmExecutor.queryForInt(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForInt(sql: String, vararg args: Any): Int? {
        return rmExecutor.queryForInt(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForIntUnique(sql: String, args: Collection<*>): Int {
        return rmExecutor.queryForIntUnique(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForIntUnique(sql: String, vararg args: Any): Int {
        return rmExecutor.queryForIntUnique(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForLong(sql: String, args: Collection<*>): Long? {
        return rmExecutor.queryForLong(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForLong(sql: String, vararg args: Any): Long? {
        return rmExecutor.queryForLong(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForLongUnique(sql: String, args: Collection<*>): Long {
        return rmExecutor.queryForLongUnique(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForLongUnique(sql: String, vararg args: Any): Long {
        return rmExecutor.queryForLongUnique(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForString(sql: String, args: Collection<*>): String? {
        return rmExecutor.queryForString(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForString(sql: String, vararg args: Any): String? {
        return rmExecutor.queryForString(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForStringUnique(sql: String, args: Collection<*>): String {
        return rmExecutor.queryForStringUnique(sql, args)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun queryForStringUnique(sql: String, vararg args: Any): String {
        return rmExecutor.queryForStringUnique(sql, args)
    }

    @Throws(JpoException::class)
    override fun <T> queryForUnique(sql: String, args: Collection<*>, rsrr: (entry: ResultEntry, rowCount: Int) -> T): T {
        return rmExecutor.queryForUnique(sql, args, rsrr)
    }

    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    override fun <T> queryForUnique(sql: String, args: Array<Any>, rsrr: (entry: ResultEntry, rowCount: Int) -> T): T {
        return rmExecutor.queryForUnique(sql, args, rsrr)
    }

    @Throws(JpoException::class)
    override fun update(sql: String, args: Collection<*>): Int {
        return rmExecutor.update(sql, args)
    }

    @Throws(JpoException::class)
    override fun <R> update(sql: String, args: Collection<*>, generatedKeyReader: GeneratedKeyReader<R>): R {
        return rmExecutor.update(sql, args, generatedKeyReader)
    }

    @Throws(JpoException::class)
    override fun update(sql: String, vararg args: Any): Int {
        return rmExecutor.update(sql, args)
    }

    @Throws(JpoException::class)
    override fun <R> update(sql: String, args: Array<Any>, generatedKeyReader: GeneratedKeyReader<R>): R {
        return rmExecutor.update(sql, args, generatedKeyReader)
    }

    @Throws(JpoException::class)
    override fun update(sql: String, psc: (Statement) -> Unit): Int {
        return rmExecutor.update(sql, psc)
    }

    @Throws(JpoException::class)
    override fun <R> update(sql: String, generatedKeyReader: GeneratedKeyReader<R>, psc: (Statement) -> Unit): R {
        return rmExecutor.update(sql, psc, generatedKeyReader)
    }

}