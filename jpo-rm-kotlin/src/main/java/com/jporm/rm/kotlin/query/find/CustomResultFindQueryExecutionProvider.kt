/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.jporm.rm.kotlin.query.find

import com.jporm.commons.core.exception.JpoException
import com.jporm.commons.core.exception.JpoNotUniqueResultException
import com.jporm.rm.kotlin.session.SqlExecutor
import com.jporm.sql.query.select.SelectCommon
import com.jporm.types.io.ResultEntry
import com.jporm.types.io.ResultSet

import java.math.BigDecimal
import java.util.Optional

/**
 * @author Francesco Cina 20/giu/2011
 */
interface CustomResultFindQueryExecutionProvider : SelectCommon {

    /**
     * Execute the query reading the ResultSet with a [ResultSetReader].

     * @param rse
     * *            object that will extract all rows of results
     * *
     * @return an arbitrary result object, as returned by the
     * *         [ResultSetReader]
     */
    @Throws(JpoException::class)
    fun <T> fetchAll(resultSetReader: (ResultSet) -> T): T {
        return sqlExecutor.query(sqlQuery(), sqlValues(), resultSetReader)
    }

    /**
     * Execute the query reading the ResultSet with a [ResultSetReader].

     * @param rse
     * *            object that will extract all rows of results
     */
    @Throws(JpoException::class)
    fun fetchAll(resultSetReader: (ResultSet) -> Unit) {
        sqlExecutor.query(sqlQuery(), sqlValues(), resultSetReader)
    }

    /**
     * Execute the query reading the ResultSet with a [ResultSetRowReader]
     * .

     * @param rsrr
     * *            object that will extract all rows of results
     * *
     * @return a List of result objects returned by the
     * *         [ResultSetRowReader]
     */
    @Throws(JpoException::class)
    fun <T> fetchAll(resultSetRowReader: (ResultEntry, Int) -> T): List<T> {
        return sqlExecutor.query(sqlQuery(), sqlValues(), resultSetRowReader)
    }

    /**
     * Execute the query reading the ResultSet with a [ResultSetRowReader]
     * .

     * @param rsrr
     * *            object that will extract all rows of results
     * *
     * @return a List of result objects returned by the
     * *         [ResultSetRowReader]
     */
    @Throws(JpoException::class)
    fun fetchAll(resultSetRowReader: (ResultEntry, Int) -> Unit) {
        sqlExecutor.query(sqlQuery(), sqlValues(), resultSetRowReader)
    }

    /**
     * Execute the query and read the result as an [BigDecimal] value. If
     * more than one rows are returned by the query, the first value is
     * returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class)
    fun fetchBigDecimal(): BigDecimal? {
        return sqlExecutor.queryForBigDecimal(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query and read the result as an [BigDecimal] value. If
     * more than one rows are returned by the query, the first value is
     * returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class)
    fun fetchBigDecimalOptional(): Optional<BigDecimal> {
        return Optional.ofNullable(fetchBigDecimal())
    }

    /**
     * Execute the query and read the result as a BigDecimal value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @throws JpoNotUniqueResultException
     * *             if the results of the query executions are not exactly 1
     * *
     * @return
     */
    @Throws(JpoException::class)
    fun fetchBigDecimalUnique(): BigDecimal {
        return sqlExecutor.queryForBigDecimalUnique(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query and read the result as an [Boolean] value. If
     * more than one rows are returned by the query, the first value is
     * returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class)
    fun fetchBoolean(): Boolean? {
        return sqlExecutor.queryForBoolean(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query and read the result as an [Boolean] value. If
     * more than one rows are returned by the query, the first value is
     * returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class)
    fun fetchBooleanOptional(): Optional<Boolean> {
        return Optional.ofNullable(fetchBoolean())
    }

    /**
     * Execute the query and read the result as a boolean value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @throws JpoNotUniqueResultException
     * *             if the results of the query executions are not exactly 1
     * *
     * @return
     */
    @Throws(JpoException::class)
    fun fetchBooleanUnique(): Boolean {
        return sqlExecutor.queryForBooleanUnique(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query and read the result as an [Double] value. If more
     * than one rows are returned by the query, the first value is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    fun fetchDouble(): Double? {
        return sqlExecutor.queryForDouble(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query and read the result as an [Double] value. If more
     * than one rows are returned by the query, the first value is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    fun fetchDoubleOptional(): Optional<Double> {
        return Optional.ofNullable(fetchDouble())
    }

    /**
     * Execute the query and read the result as a double value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @throws JpoNotUniqueResultException
     * *             if the results of the query executions are not exactly 1
     * *
     * @return
     */
    @Throws(JpoException::class)
    fun fetchDoubleUnique(): Double {
        return sqlExecutor.queryForDoubleUnique(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query and read the result as an [Float] value. If more
     * than one rows are returned by the query, the first value is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    fun fetchFloat(): Float? {
        return sqlExecutor.queryForFloat(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query and read the result as an [Float] value. If more
     * than one rows are returned by the query, the first value is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    fun fetchFloatOptional(): Optional<Float> {
        return Optional.ofNullable(fetchFloat())
    }

    /**
     * Execute the query and read the result as a float value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @throws JpoNotUniqueResultException
     * *             if the results of the query executions are not exactly 1
     * *
     * @return
     */
    @Throws(JpoException::class)
    fun fetchFloatUnique(): Float {
        return sqlExecutor.queryForFloatUnique(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query and read the result as an [Integer] value. If
     * more than one rows are returned by the query, the first value is
     * returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @returnRowReader<
     */
    fun fetchInt(): Int? {
        return sqlExecutor.queryForInt(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query and read the result as an [Integer] value. If
     * more than one rows are returned by the query, the first value is
     * returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    fun fetchIntOptional(): Optional<Int> {
        return Optional.ofNullable(fetchInt())
    }

    /**
     * Execute the query and read the result as an [Integer] value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @throws JpoNotUniqueResultException
     * *             if the results of the query executions are not exactly 1
     * *
     * @return
     */
    @Throws(JpoException::class)
    fun fetchIntUnique(): Int {
        return sqlExecutor.queryForIntUnique(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query and read the result as an [Long] value. If more
     * than one rows are returned by the query, the first value is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    fun fetchLong(): Long? {
        return sqlExecutor.queryForLong(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query and read the result as an [Long] value. If more
     * than one rows are returned by the query, the first value is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    fun fetchLongOptional(): Optional<Long> {
        return Optional.ofNullable(fetchLong())
    }

    /**
     * Execute the query and read the result as an [Long] value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @throws JpoNotUniqueResultException
     * *             if the results of the query executions are not exactly 1
     * *
     * @return
     */
    @Throws(JpoException::class)
    fun fetchLongUnique(): Long {
        return sqlExecutor.queryForLongUnique(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query and read the result as an [String] value. If more
     * than one rows are returned by the query, the first value is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    fun fetchString(): String? {
        return sqlExecutor.queryForString(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query and read the result as an [String] value. If more
     * than one rows are returned by the query, the first value is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    fun fetchStringOptional(): Optional<String> {
        return Optional.ofNullable(fetchString())
    }

    /**
     * Execute the query and read the result as a String value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @throws JpoNotUniqueResultException
     * *             if the results of the query executions are not exactly 1
     * *
     * @return
     */
    @Throws(JpoException::class)
    fun fetchStringUnique(): String {
        return sqlExecutor.queryForStringUnique(sqlQuery(), sqlValues())
    }

    /**
     * Execute the query reading the ResultSet with a {@RowReader<link ResultSetRowReader} @param rsrr * object that will extract the row of result * @return * @throws JpoException * @throws JpoNotUniqueResultException * if the results of the query executions are not exactly 1></link>
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun <T> fetchOneUnique(resultSetRowReader: (ResultEntry, Int) -> T): T {
        return sqlExecutor.queryForUnique(sqlQuery(), sqlValues(), resultSetRowReader)
    }

    /**
     * Execute the query reading the ResultSet with a [ResultSetRowReader]. If more
     * than one rows are returned by the query, the first value is returned.

     * @param rsrr
     * *            object that will extract the row of result
     * *
     * @return
     * *
     * @throws JpoException
     */
    @Throws(JpoException::class)
    fun <T> fetchOneOptional(resultSetRowReader: (ResultEntry, Int) -> T): Optional<T> {
        return sqlExecutor.queryForOptional(sqlQuery(), sqlValues(), resultSetRowReader)
    }

    /**
     * Return the count of entities this query should return.

     * @return
     */
    fun fetchRowCount(): Int {
        return sqlExecutor.queryForIntUnique(sqlRowCountQuery(), sqlValues())!!
    }

    val sqlExecutor: SqlExecutor

}
