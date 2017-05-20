/*******************************************************************************
 * Copyright 2013 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.jporm.rx.query.find;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.function.IntBiConsumer;
import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public interface CustomResultFindQueryExecutionProvider extends SelectCommon {

    /**
     * Execute the query reading the ResultSet with a {@link ResultSetReader}.
     *
     * @param rse
     *            object that will extract all rows of results
     * @return an arbitrary result object, as returned by the
     *         {@link ResultSetReader}
     */
    default <T> CompletableFuture<T> fetch(Function<ResultSet, T> resultSetReader) {
        return getSqlExecutor().query(sqlQuery(), sqlValues(), resultSetReader);
    }

    /**
     * Execute the query reading the ResultSet with a {@link ResultSetReader}.
     *
     * @param rse
     *            object that will extract all rows of results
     * @return an arbitrary result object, as returned by the
     *         {@link ResultSetReader}
     */
    default CompletableFuture<Void> fetch(Consumer<ResultSet> resultSetReader) {
        return getSqlExecutor().query(sqlQuery(), sqlValues(), resultSetReader);
    }

    /**
     * Execute the query reading the ResultSet with a {@link ResultSetRowReader}
     * .
     *
     * @param rsrr
     *            object that will extract all rows of results
     * @return a List of result objects returned by the
     *         {@link ResultSetRowReader}
     */
    default <T> CompletableFuture<List<T>> fetch(IntBiFunction<ResultEntry, T> resultSetRowReader) {
        return getSqlExecutor().query(sqlQuery(), sqlValues(), resultSetRowReader);
    }

    /**
     * Execute the query reading the ResultSet with a {@link ResultSetRowReader}
     * .
     *
     * @param rsrr
     *            object that will extract all rows of results
     * @return a List of result objects returned by the
     *         {@link ResultSetRowReader}
     */
    default CompletableFuture<Void> fetch(IntBiConsumer<ResultEntry> resultSetRowReader) {
        return getSqlExecutor().query(sqlQuery(), sqlValues(), resultSetRowReader);
    }

    /**
     * Execute the query and read the result as an {@link BigDecimal} value. If
     * more than one rows are returned by the query, the first value is
     * returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<BigDecimal> fetchBigDecimal() {
        return getSqlExecutor().queryForBigDecimal(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query and read the result as an {@link BigDecimal} value. If
     * more than one rows are returned by the query, the first value is
     * returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<Optional<BigDecimal>> fetchBigDecimalOptional() {
        return fetchBigDecimal().thenApply(value -> Optional.ofNullable(value));
    }

    /**
     * Execute the query and read the result as a BigDecimal value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @throws JpoNotUniqueResultException
     *             if the results of the query executions are not exactly 1
     * @return
     */
    default CompletableFuture<BigDecimal> fetchBigDecimalUnique() {
        return getSqlExecutor().queryForBigDecimalUnique(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query and read the result as an {@link Boolean} value. If
     * more than one rows are returned by the query, the first value is
     * returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<Boolean> fetchBoolean() {
        return getSqlExecutor().queryForBoolean(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query and read the result as an {@link Boolean} value. If
     * more than one rows are returned by the query, the first value is
     * returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<Optional<Boolean>> fetchBooleanOptional() {
        return fetchBoolean().thenApply(value -> Optional.ofNullable(value));
    }

    /**
     * Execute the query and read the result as a boolean value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @throws JpoNotUniqueResultException
     *             if the results of the query executions are not exactly 1
     * @return
     */
    default CompletableFuture<Boolean> fetchBooleanUnique() {
        return getSqlExecutor().queryForBooleanUnique(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query and read the result as an {@link Double} value. If more
     * than one rows are returned by the query, the first value is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<Double> fetchDouble() {
        return getSqlExecutor().queryForDouble(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query and read the result as an {@link Double} value. If more
     * than one rows are returned by the query, the first value is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<Optional<Double>> fetchDoubleOptional() {
        return fetchDouble().thenApply(value -> Optional.ofNullable(value));
    }

    /**
     * Execute the query and read the result as a double value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @throws JpoNotUniqueResultException
     *             if the results of the query executions are not exactly 1
     * @return
     */
    default CompletableFuture<Double> fetchDoubleUnique() {
        return getSqlExecutor().queryForDoubleUnique(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query and read the result as an {@link Float} value. If more
     * than one rows are returned by the query, the first value is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<Float> fetchFloat() {
        return getSqlExecutor().queryForFloat(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query and read the result as an {@link Float} value. If more
     * than one rows are returned by the query, the first value is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<Optional<Float>> fetchFloatOptional() {
        return fetchFloat().thenApply(value -> Optional.ofNullable(value));
    }

    /**
     * Execute the query and read the result as a float value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @throws JpoNotUniqueResultException
     *             if the results of the query executions are not exactly 1
     * @return
     */
    default CompletableFuture<Float> fetchFloatUnique() {
        return getSqlExecutor().queryForFloatUnique(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query and read the result as an {@link Integer} value. If
     * more than one rows are returned by the query, the first value is
     * returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<Integer> fetchInt() {
        return getSqlExecutor().queryForInt(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query and read the result as an {@link Integer} value. If
     * more than one rows are returned by the query, the first value is
     * returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<Optional<Integer>> fetchIntOptional() {
        return fetchInt().thenApply(value -> Optional.ofNullable(value));
    }

    /**
     * Execute the query and read the result as an {@link Integer} value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @throws JpoNotUniqueResultException
     *             if the results of the query executions are not exactly 1
     * @return
     */
    default CompletableFuture<Integer> fetchIntUnique() {
        return getSqlExecutor().queryForIntUnique(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query and read the result as an {@link Long} value. If more
     * than one rows are returned by the query, the first value is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<Long> fetchLong() {
        return getSqlExecutor().queryForLong(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query and read the result as an {@link Long} value. If more
     * than one rows are returned by the query, the first value is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<Optional<Long>> fetchLongOptional() {
        return fetchLong().thenApply(value -> Optional.ofNullable(value));
    }

    /**
     * Execute the query and read the result as an {@link Long} value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @throws JpoNotUniqueResultException
     *             if the results of the query executions are not exactly 1
     * @return
     */
    default CompletableFuture<Long> fetchLongUnique() {
        return getSqlExecutor().queryForLongUnique(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query and read the result as an {@link String} value. If more
     * than one rows are returned by the query, the first value is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<String> fetchString() {
        return getSqlExecutor().queryForString(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query and read the result as an {@link String} value. If more
     * than one rows are returned by the query, the first value is returned.
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @return
     */
    default CompletableFuture<Optional<String>> fetchStringOptional() {
        return fetchString().thenApply(value -> Optional.ofNullable(value));
    }

    /**
     * Execute the query and read the result as a String value
     *
     * @param sql
     *            SQL query to execute
     * @param args
     *            arguments to bind to the query
     * @throws JpoNotUniqueResultException
     *             if the results of the query executions are not exactly 1
     * @return
     */
    default CompletableFuture<String> fetchStringUnique() {
        return getSqlExecutor().queryForStringUnique(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query reading the ResultSet with a {@link ResultSetRowReader}
     * .
     *
     * @param rsrr
     *            object that will extract the row of result
     * @return
     * @throws JpoException
     * @throws JpoNotUniqueResultException
     *             if the results of the query executions are not exactly 1
     */
    default <T> CompletableFuture<T> fetchUnique(IntBiFunction<ResultEntry, T> resultSetRowReader) {
        return getSqlExecutor().queryForUnique(sqlQuery(), sqlValues(), resultSetRowReader);
    }

    /**
     * Execute the query reading the ResultSet with a {@link ResultSetRowReader}. If more
     * than one rows are returned by the query, the first value is returned.
     *
     * @param rsrr
     *            object that will extract the row of result
     * @return
     * @throws JpoException
     */
    default <T> CompletableFuture<Optional<T>> fetchOptional(final IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException {
        return getSqlExecutor().queryForOptional(sqlQuery(), sqlValues(), resultSetRowReader);
    }

    /**
     * Return the count of entities this query should return.
     *
     * @return
     */
    default CompletableFuture<Integer> fetchRowCount() {
        return getSqlExecutor().queryForInt(sqlRowCountQuery(), sqlValues());
    }

    SqlExecutor getSqlExecutor();

}
