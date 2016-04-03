/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rx.session;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.function.IntBiConsumer;
import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.rx.query.update.UpdateResult;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

/**
 * @author Francesco Cina 02/lug/2011 An executor to perform plain SQL queries
 */
public interface SqlExecutor {

    /**
     * Issue multiple SQL updates on a single JDBC Statement using batching.
     *
     * @param sql
     *            defining a List of SQL statements that will be executed.
     * @return an array of the number of rows affected by each statement
     */
    CompletableFuture<int[]> batchUpdate(Collection<String> sqls);

    /**
     * Issue multiple SQL updates on a single JDBC Statement using batching. The
     * values on the generated PreparedStatement are set using an
     * IPreparedStatementCreator.
     *
     * @param sql
     *            defining a List of SQL statements that will be executed.
     * @param psc
     *            the creator to bind values on the PreparedStatement
     * @return an array of the number of rows affected by each statement
     */
    CompletableFuture<int[]> batchUpdate(String sql, BatchPreparedStatementSetter psc);

    /**
     * Issue multiple SQL updates on a single JDBC Statement using batching. The
     * same query is executed for every Object array present in the args list
     * which is the list of arguments to bind to the query.
     *
     * @param sql
     *            defining a List of SQL statements that will be executed.
     * @param args
     *            defining a List of Object arrays to bind to the query.
     * @return an array of the number of rows affected by each statement
     */
    CompletableFuture<int[]> batchUpdate(String sql, Collection<Object[]> args);

    /**
     * Issue a single SQL execute, typically a DDL statement.
     *
     * @param sql
     *            static SQL to execute
     */
    CompletableFuture<Void> execute(String sql);

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * IResultSetReader.
     *
     * @param sql
     *            SQL query to execute
     * @param rse
     *            object that will extract all rows of results
     * @param args
     *            arguments to bind to the query
     * @return an arbitrary result object, as returned by the
     *         IResultSetExtractor
     */
    <T> CompletableFuture<T> query(String sql, Collection<?> args, Function<ResultSet, T> resultSetReader);

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * IResultSetReader.
     *
     * @param sql
     *            SQL query to execute
     * @param rse
     *            object that will extract all rows of results
     * @param args
     *            arguments to bind to the query
     * @return an arbitrary result object, as returned by the
     *         IResultSetExtractor
     */
    CompletableFuture<Void> query(String sql, Collection<?> args, Consumer<ResultSet> resultSetReader);

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * {@link ResultSetRowReader}.
     *
     * @param sql
     *            SQL query to execute
     * @param rsrr
     *            object that will extract all rows of results
     * @param args
     *            arguments to bind to the query
     * @return an arbitrary result object, as returned by the
     *         {@link ResultSetRowReader}
     */
    <T> CompletableFuture<List<T>> query(String sql, Collection<?> args, IntBiFunction<ResultEntry, T> resultSetRowReader);

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * {@link ResultSetRowReader}.
     *
     * @param sql
     *            SQL query to execute
     * @param rsrr
     *            object that will extract all rows of results
     * @param args
     *            arguments to bind to the query
     * @return an arbitrary result object, as returned by the
     *         {@link ResultSetRowReader}
     */
    CompletableFuture<Void> query(String sql, Collection<?> args, IntBiConsumer<ResultEntry> resultSetRowReader);


    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * IResultSetReader.
     *
     * @param sql
     *            SQL query to execute
     * @param rse
     *            object that will extract all rows of results
     * @param args
     *            arguments to bind to the query
     * @return an arbitrary result object, as returned by the
     *         IResultSetExtractor
     */
    <T> CompletableFuture<T> query(String sql, Object[] args, Function<ResultSet, T> resultSetReader);

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * IResultSetReader.
     *
     * @param sql
     *            SQL query to execute
     * @param rse
     *            object that will extract all rows of results
     * @param args
     *            arguments to bind to the query
     * @return an arbitrary result object, as returned by the
     *         IResultSetExtractor
     */
    CompletableFuture<Void> query(String sql, Object[] args, Consumer<ResultSet> resultSetReader);

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * {@link ResultSetRowReader}.
     *
     * @param sql
     *            SQL query to execute
     * @param rsrr
     *            object that will extract all rows of results
     * @param args
     *            arguments to bind to the query
     * @return an arbitrary result object, as returned by the
     *         {@link ResultSetRowReader}
     */
    <T> CompletableFuture<List<T>> query(String sql, Object[] args, IntBiFunction<ResultEntry, T> resultSetRowReader);

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * {@link ResultSetRowReader}.
     *
     * @param sql
     *            SQL query to execute
     * @param rsrr
     *            object that will extract all rows of results
     * @param args
     *            arguments to bind to the query
     * @return an arbitrary result object, as returned by the
     *         {@link ResultSetRowReader}
     */
    CompletableFuture<Void> query(String sql, Object[] args, IntBiConsumer<ResultEntry> resultSetRowReader);

    /**
     * Execute a query given static SQL and read the result as an bigDecimal
     * value. It returns null if no rows are returned. It returns the first
     * value if more than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<BigDecimal> queryForBigDecimal(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as an BigDecimal
     * value. It returns null if no rows are returned. It returns the first
     * value if more than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<BigDecimal> queryForBigDecimal(String sql, Object... args);

    /**
     * Execute a query given static SQL and read the result as a BigDecimal
     * value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<BigDecimal> queryForBigDecimalUnique(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as a BigDecimal
     * value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<BigDecimal> queryForBigDecimalUnique(String sql, Object... args);

    /**
     * Execute a query given static SQL and read the result as an Boolean value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<Boolean> queryForBoolean(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as an Boolean value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<Boolean> queryForBoolean(String sql, Object... args);

    /**
     * Execute a query given static SQL and read the result as a boolean value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<Boolean> queryForBooleanUnique(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as a boolean value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<Boolean> queryForBooleanUnique(String sql, Object... args);

    /**
     * Execute a query given static SQL and read the result as an double value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<Double> queryForDouble(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as an Double value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<Double> queryForDouble(String sql, Object... args);

    /**
     * Execute a query given static SQL and read the result as a double value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<Double> queryForDoubleUnique(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as a double value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<Double> queryForDoubleUnique(String sql, Object... args);

    /**
     * Execute a query given static SQL and read the result as an Float value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<Float> queryForFloat(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as an float value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<Float> queryForFloat(String sql, Object... args);

    /**
     * Execute a query given static SQL and read the result as a float value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<Float> queryForFloatUnique(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as a float value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<Float> queryForFloatUnique(String sql, Object... args);

    /**
     * Execute a query given static SQL and read the result as an Integer value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<Integer> queryForInt(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as an Integer value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<Integer> queryForInt(String sql, Object... args);

    /**
     * Execute a query given static SQL and read the result as an int value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<Integer> queryForIntUnique(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as an int value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<Integer> queryForIntUnique(String sql, Object... args);

    /**
     * Execute a query given static SQL and read the result as an long value. It
     * returns null if no rows are returned. It returns the first value if more
     * than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<Long> queryForLong(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as an long value. It
     * returns null if no rows are returned. It returns the first value if more
     * than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<Long> queryForLong(String sql, Object... args);

    /**
     * Execute a query given static SQL and read the result as an long value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<Long> queryForLongUnique(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as an long value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<Long> queryForLongUnique(String sql, Object... args);

    /**
     * Execute a query given static SQL and read the result as an String value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<String> queryForString(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as an String value.
     * It returns null if no rows are returned. It returns the first value if
     * more than one row is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    CompletableFuture<String> queryForString(String sql, Object... args);

    /**
     * Execute a query given static SQL and read the result as a String value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<String> queryForStringUnique(String sql, Collection<?> args);

    /**
     * Execute a query given static SQL and read the result as a String value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     * @throws JpoNotUniqueResultException
     *             if no results or more than one result is returned by the
     *             query
     */
    CompletableFuture<String> queryForStringUnique(String sql, Object... args);

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * {@link ResultSetRowReader}.
     *
     * @param sql
     *            SQL query to execute
     * @param rsrr
     *            object that will extract th result's row
     * @param args
     *            arguments to bind to the query
     * @return an arbitrary result object, as returned by the
     *         {@link ResultSetRowReader}
     * @throws JpoNotUniqueResultException
     *             if not exactly one row is returned by the query execution
     */
    <T> CompletableFuture<T> queryForUnique(String sql, Collection<?> args, IntBiFunction<ResultEntry, T> resultSetRowReader);

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * {@link ResultSetRowReader}.
     *
     * @param sql
     *            SQL query to execute
     * @param rsrr
     *            object that will extract th result's row
     * @param args
     *            arguments to bind to the query
     * @return an arbitrary result object, as returned by the
     *         {@link ResultSetRowReader}
     * @throws JpoNotUniqueResultException
     *             if not exactly one row is returned by the query execution
     */
    <T> CompletableFuture<T> queryForUnique(String sql, Object[] args, IntBiFunction<ResultEntry, T> resultSetRowReader);

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * {@link ResultSetRowReader}. If more
     * than one rows are returned by the query, the first value is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param rsrr
     *            object that will extract th result's row
     * @param args
     *            arguments to bind to the query
     * @return an arbitrary result object, as returned by the
     *         {@link ResultSetRowReader}
     */
    <T> CompletableFuture<Optional<T>> queryForOptional(String sql, Collection<?> args, IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException;

    /**
     * Execute a query given static SQL, reading the ResultSet with a
     * {@link ResultSetRowReader}. If more
     * than one rows are returned by the query, the first value is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param rsrr
     *            object that will extract the result's row
     * @param args
     *            arguments to bind to the query
     * @return an arbitrary result object, as returned by the
     *         {@link ResultSetRowReader}
     */
    <T> CompletableFuture<Optional<T>> queryForOptional(String sql, Object[] args, IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException;


    /**
     * Perform a single SQL update operation (such as an insert, update or
     * delete statement).
     *
     * @param sql
     *            static SQL to execute
     * @param args
     *            arguments to bind to the query
     * @return the number of rows affected
     */
    CompletableFuture<UpdateResult> update(String sql, Collection<?> args);

    /**
     * Issue an update statement using a PreparedStatementCreator to provide SQL
     * and any required parameters. Generated keys can be read using the
     * IGeneratedKeyReader.
     *
     * @param psc
     *            object that provides SQL and any necessary parameters
     * @param generatedKeyReader
     *            IGeneratedKeyReader to read the generated key
     * @return the number of rows affected
     */
    <R> CompletableFuture<R> update(String sql, Collection<?> args, GeneratedKeyReader<R> generatedKeyReader);

    /**
     * Perform a single SQL update operation (such as an insert, update or
     * delete statement).
     *
     * @param sql
     *            static SQL to execute
     * @param args
     *            arguments to bind to the query
     * @return the number of rows affected
     */
    CompletableFuture<UpdateResult> update(String sql, Object... args);

    /**
     * Issue an update statement using a PreparedStatementCreator to provide SQL
     * and any required parameters. Generated keys can be read using the
     * IGeneratedKeyReader.
     *
     * @param psc
     *            object that provides SQL and any necessary parameters
     * @param generatedKeyReader
     *            IGeneratedKeyReader to read the generated key
     * @return the number of rows affected
     */
    <R> CompletableFuture<R> update(String sql, Object[] args, GeneratedKeyReader<R> generatedKeyReader);

    /**
     * Perform a single SQL update operation (such as an insert, update or
     * delete statement).
     *
     * @param sql
     *            static SQL to execute
     * @param psc
     * @return the number of rows affected
     */
    CompletableFuture<UpdateResult> update(String sql, Consumer<Statement> statementSetter);

    /**
     * Issue an update statement using a PreparedStatementCreator to provide SQL
     * and any required parameters. Generated keys can be read using the
     * GeneratedKeyReader.
     *
     * @param sql
     *            static SQL to execute
     * @param psc
     * @return the number of rows affected
     */
    <R> CompletableFuture<R> update(String sql, Consumer<Statement> statementSetter, GeneratedKeyReader<R> generatedKeyReader);
}
