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
import com.jporm.types.io.*

import java.math.BigDecimal
import java.util.Optional

/**
 * @author Francesco Cina 02/lug/2011 An executor to perform plain SQL queries
 */
interface SqlExecutor {

    /**
     * Issue multiple SQL updates on a single JDBC Statement using batching.

     * @param sql
     * *            defining a List of SQL statements that will be executed.
     * *
     * @return an array of the number of rows affected by each statement
     */
    @Throws(JpoException::class)
    fun batchUpdate(sqls: Collection<String>): IntArray

    /**
     * Issue multiple SQL updates on a single JDBC Statement using batching. The
     * args on the generated PreparedStatement are set using an
     * IPreparedStatementCreator.

     * @param sql
     * *            defining a List of SQL statements that will be executed.
     * *
     * @param psc
     * *            the creator to bind args on the PreparedStatement
     * *
     * @return an array of the number of rows affected by each statement
     */
    @Throws(JpoException::class)
    fun batchUpdate(sql: String, psc: BatchPreparedStatementSetter): IntArray

    /**
     * Issue multiple SQL updates on a single JDBC Statement using batching. The
     * same query is executed for every Object array present in the args list
     * which is the list of arguments to bind to the query.

     * @param sql
     * *            defining a List of SQL statements that will be executed.
     * *
     * @param args
     * *            defining a List of Object arrays to bind to the query.
     * *
     * @return an array of the number of rows affected by each statement
     */
    @Throws(JpoException::class)
    fun batchUpdate(sql: String, args: Collection<Array<Any>>): IntArray

    /**
     * Issue a single SQL execute, typically a DDL statement.

     * @param sql
     * *            static SQL to execute
     */
    @Throws(JpoException::class)
    fun execute(sql: String)

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * IResultSetReader.

     * @param sql
     * *            SQL query to execute
     * *
     * @param rse
     * *            object that will extract all rows of results
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return an arbitrary result object, as returned by the
     * *         IResultSetExtractor
     */
    @Throws(JpoException::class)
    fun <T> query(sql: String, args: Collection<*>, resultSetReader: (rs: ResultSet) -> T): T

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * IResultSetReader.

     * @param sql
     * *            SQL query to execute
     * *
     * @param rse
     * *            object that will extract all rows of results
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return an arbitrary result object, as returned by the
     * *         IResultSetExtractor
     */
    @Throws(JpoException::class)
    fun query(sql: String, args: Collection<*>, resultSetReader: (rs: ResultSet) -> Unit)

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * [ResultSetRowReader].

     * @param sql
     * *            SQL query to execute
     * *
     * @param rsrr
     * *            object that will extract all rows of results
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return an arbitrary result object, as returned by the
     * *         [ResultSetRowReader]
     */
    @Throws(JpoException::class)
    fun <T> query(sql: String, args: Collection<*>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> T): List<T>

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * [ResultSetRowReader].

     * @param sql
     * *            SQL query to execute
     * *
     * @param rsrr
     * *            object that will extract all rows of results
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return an arbitrary result object, as returned by the
     * *         [ResultSetRowReader]
     */
    @Throws(JpoException::class)
    fun query(sql: String, args: Collection<*>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> Unit)

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * IResultSetReader.

     * @param sql
     * *            SQL query to execute
     * *
     * @param rse
     * *            object that will extract all rows of results
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return an arbitrary result object, as returned by the
     * *         IResultSetExtractor
     */
    @Throws(JpoException::class)
    fun <T> query(sql: String, args: Array<Any>, resultSetReader: (ResultSet) -> T): T

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * IResultSetReader.

     * @param sql
     * *            SQL query to execute
     * *
     * @param rse
     * *            object that will extract all rows of results
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return an arbitrary result object, as returned by the
     * *         IResultSetExtractor
     */
    @Throws(JpoException::class)
    fun query(sql: String, args: Array<Any>, resultSetReader: (ResultSet) -> Unit)

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * [ResultSetRowReader].

     * @param sql
     * *            SQL query to execute
     * *
     * @param rsrr
     * *            object that will extract all rows of results
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return an arbitrary result object, as returned by the
     * *         [ResultSetRowReader]
     */
    @Throws(JpoException::class)
    fun query(sql: String, args: Array<Any>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> Unit)

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * [ResultSetRowReader].

     * @param sql
     * *            SQL query to execute
     * *
     * @param rsrr
     * *            object that will extract all rows of results
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return an arbitrary result object, as returned by the
     * *         [ResultSetRowReader]
     */
    @Throws(JpoException::class)
    fun <T> query(sql: String, args: Array<Any>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> T): List<T>

    /**
     * Execute a query given static SQL and read the result as an bigDecimal
     * value. It returns null if no rows are returned. It returns the first
     * value if more than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForBigDecimal(sql: String, args: Collection<*>): BigDecimal?

    /**
     * Execute a query given static SQL and read the result as an BigDecimal
     * value. It returns null if no rows are returned. It returns the first
     * value if more than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForBigDecimal(sql: String, vararg args: Any): BigDecimal?

    /**
     * Execute a query given static SQL and read the result as a BigDecimal
     * value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForBigDecimalUnique(sql: String, args: Collection<*>): BigDecimal

    /**
     * Execute a query given static SQL and read the result as a BigDecimal
     * value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForBigDecimalUnique(sql: String, vararg args: Any): BigDecimal

    /**
     * Execute a query given static SQL and read the result as an Boolean value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForBoolean(sql: String, args: Collection<*>): Boolean?

    /**
     * Execute a query given static SQL and read the result as an Boolean value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForBoolean(sql: String, vararg args: Any): Boolean?

    /**
     * Execute a query given static SQL and read the result as a boolean value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForBooleanUnique(sql: String, args: Collection<*>): Boolean

    /**
     * Execute a query given static SQL and read the result as a boolean value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForBooleanUnique(sql: String, vararg args: Any): Boolean

    /**
     * Execute a query given static SQL and read the result as an double value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForDouble(sql: String, args: Collection<*>): Double?

    /**
     * Execute a query given static SQL and read the result as an Double value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForDouble(sql: String, vararg args: Any): Double?

    /**
     * Execute a query given static SQL and read the result as a double value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForDoubleUnique(sql: String, args: Collection<*>): Double

    /**
     * Execute a query given static SQL and read the result as a double value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForDoubleUnique(sql: String, vararg args: Any): Double

    /**
     * Execute a query given static SQL and read the result as an Float value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForFloat(sql: String, args: Collection<*>): Float?

    /**
     * Execute a query given static SQL and read the result as an float value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForFloat(sql: String, vararg args: Any): Float?

    /**
     * Execute a query given static SQL and read the result as a float value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForFloatUnique(sql: String, args: Collection<*>): Float

    /**
     * Execute a query given static SQL and read the result as a float value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForFloatUnique(sql: String, vararg args: Any): Float

    /**
     * Execute a query given static SQL and read the result as an Integer value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForInt(sql: String, args: Collection<*>): Int?

    /**
     * Execute a query given static SQL and read the result as an Integer value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForInt(sql: String, vararg args: Any): Int?

    /**
     * Execute a query given static SQL and read the result as an int value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForIntUnique(sql: String, args: Collection<*>): Int

    /**
     * Execute a query given static SQL and read the result as an int value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForIntUnique(sql: String, vararg args: Any): Int

    /**
     * Execute a query given static SQL and read the result as an long value. It
     * returns null if no rows are returned. It returns the first value if more
     * than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForLong(sql: String, args: Collection<*>): Long?

    /**
     * Execute a query given static SQL and read the result as an long value. It
     * returns null if no rows are returned. It returns the first value if more
     * than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForLong(sql: String, vararg args: Any): Long?

    /**
     * Execute a query given static SQL and read the result as an long value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForLongUnique(sql: String, args: Collection<*>): Long

    /**
     * Execute a query given static SQL and read the result as an long value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForLongUnique(sql: String, vararg args: Any): Long

    /**
     * Execute a query given static SQL and read the result as an String value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForString(sql: String, args: Collection<*>): String?

    /**
     * Execute a query given static SQL and read the result as an String value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForString(sql: String, vararg args: Any): String?

    /**
     * Execute a query given static SQL and read the result as a String value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForStringUnique(sql: String, args: Collection<*>): String

    /**
     * Execute a query given static SQL and read the result as a String value

     * @param sql
     * *            SQL query to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return
     * *
     * @throws JpoNotUniqueResultException
     * *             if no results or more than one result is returned by the
     * *             query
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun queryForStringUnique(sql: String, vararg args: Any): String

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * [ResultSetRowReader].

     * @param sql
     * *            SQL query to execute
     * *
     * @param rsrr
     * *            object that will extract th result's row
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return an arbitrary result object, as returned by the
     * *         [ResultSetRowReader]
     * *
     * @throws JpoNotUniqueResultException
     * *             if not exactly one row is returned by the query execution
     */
    @Throws(JpoException::class)
    fun <T> queryForUnique(sql: String, args: Collection<*>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> T): T

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * [ResultSetRowReader].

     * @param sql
     * *            SQL query to execute
     * *
     * @param rsrr
     * *            object that will extract th result's row
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return an arbitrary result object, as returned by the
     * *         [ResultSetRowReader]
     * *
     * @throws JpoNotUniqueResultException
     * *             if not exactly one row is returned by the query execution
     */
    @Throws(JpoException::class, JpoNotUniqueResultException::class)
    fun <T> queryForUnique(sql: String, args: Array<Any>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> T): T

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * [ResultSetRowReader]. If more
     * than one rows are returned by the query, the first value is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param rsrr
     * *            object that will extract th result's row
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return an arbitrary result object, as returned by the
     * *         [ResultSetRowReader]
     */
    @Throws(JpoException::class)
    fun <T> queryForOptional(sql: String, args: Collection<*>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> T): Optional<T>

    /**
     * Perform a single SQL update operation (such as an insert, update or
     * delete statement).

     * @param sql
     * *            static SQL to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return the number of rows affected
     */
    @Throws(JpoException::class)
    fun update(sql: String, args: Collection<*>): Int


    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * [ResultSetRowReader]. If more
     * than one rows are returned by the query, the first value is returned.

     * @param sql
     * *            SQL query to execute
     * *
     * @param rsrr
     * *            object that will extract the result's row
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return an arbitrary result object, as returned by the
     * *         [ResultSetRowReader]
     */
    @Throws(JpoException::class)
    fun <T> queryForOptional(sql: String, args: Array<Any>, resultSetRowReader: (entry: ResultEntry, rowCount: Int) -> T): Optional<T>

    /**
     * Issue an update statement using a PreparedStatementCreator to provide SQL
     * and any required parameters. Generated keys can be read using the
     * IGeneratedKeyReader.

     * @param psc
     * *            object that provides SQL and any necessary parameters
     * *
     * @param generatedKeyReader
     * *            IGeneratedKeyReader to read the generated key
     * *
     * @return the number of rows affected
     */
    @Throws(JpoException::class)
    fun <R> update(sql: String, args: Collection<*>, generatedKeyReader: GeneratedKeyReader<R>): R

    /**
     * Perform a single SQL update operation (such as an insert, update or
     * delete statement).

     * @param sql
     * *            static SQL to execute
     * *
     * @param args
     * *            arguments to bind to the query
     * *
     * @return the number of rows affected
     */
    @Throws(JpoException::class)
    fun update(sql: String, vararg args: Any): Int

    /**
     * Issue an update statement using a PreparedStatementCreator to provide SQL
     * and any required parameters. Generated keys can be read using the
     * IGeneratedKeyReader.

     * @param psc
     * *            object that provides SQL and any necessary parameters
     * *
     * @param generatedKeyReader
     * *            IGeneratedKeyReader to read the generated key
     * *
     * @return the number of rows affected
     */
    @Throws(JpoException::class)
    fun <R> update(sql: String, args: Array<Any>, generatedKeyReader: GeneratedKeyReader<R>): R

    /**
     * Perform a single SQL update operation (such as an insert, update or
     * delete statement).

     * @param sql
     * *            static SQL to execute
     * *
     * @param psc
     * *
     * @return the number of rows affected
     */
    @Throws(JpoException::class)
    fun update(sql: String, statementSetter: (Statement) -> Unit): Int

    /**
     * Issue an update statement using a PreparedStatementCreator to provide SQL
     * and any required parameters. Generated keys can be read using the
     * GeneratedKeyReader.

     * @param sql
     * *            static SQL to execute
     * *
     * @param psc
     * *
     * @return the number of rows affected
     */
    @Throws(JpoException::class)
    fun <R> update(sql: String, generatedKeyReader: GeneratedKeyReader<R>, statementSetter: (Statement) -> Unit): R

}
