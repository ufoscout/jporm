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
package com.jporm.rx.reactor.query.find;

import java.math.BigDecimal;
import java.util.Optional;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.function.IntBiConsumer;
import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.rx.reactor.session.SqlExecutor;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.types.io.ResultEntry;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public interface CustomResultFindQueryExecutionProvider extends SelectCommon {

    /**
     * Execute the query reading the ResultSet with a {@link ResultSetRowReader}
     * .
     *
     * @param rsrr
     *            object that will extract all rows of results
     * @return a List of result objects returned by the
     *         {@link ResultSetRowReader}
     */
    default <T> Flux<T> fetchAll(IntBiFunction<ResultEntry, T> resultSetRowReader) {
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
    default Flux<Void> fetchAll(IntBiConsumer<ResultEntry> resultSetRowReader) {
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
    default Mono<BigDecimal> fetchBigDecimal() {
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
    default Mono<Optional<BigDecimal>> fetchBigDecimalOptional() {
        return getSqlExecutor().queryForBigDecimalOptional(sqlQuery(), sqlValues());
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
    default Mono<BigDecimal> fetchBigDecimalUnique() {
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
    default Mono<Boolean> fetchBoolean() {
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
    default Mono<Optional<Boolean>> fetchBooleanOptional() {
        return getSqlExecutor().queryForBooleanOptional(sqlQuery(), sqlValues());
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
    default Mono<Boolean> fetchBooleanUnique() {
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
    default Mono<Double> fetchDouble() {
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
    default Mono<Optional<Double>> fetchDoubleOptional() {
        return getSqlExecutor().queryForDoubleOptional(sqlQuery(), sqlValues());
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
    default Mono<Double> fetchDoubleUnique() {
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
    default Mono<Float> fetchFloat() {
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
    default Mono<Optional<Float>> fetchFloatOptional() {
        return getSqlExecutor().queryForFloatOptional(sqlQuery(), sqlValues());
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
    default Mono<Float> fetchFloatUnique() {
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
    default Mono<Integer> fetchInt() {
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
    default Mono<Optional<Integer>> fetchIntOptional() {
        return getSqlExecutor().queryForIntOptional(sqlQuery(), sqlValues());
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
    default Mono<Integer> fetchIntUnique() {
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
    default Mono<Long> fetchLong() {
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
    default Mono<Optional<Long>> fetchLongOptional() {
        return getSqlExecutor().queryForLongOptional(sqlQuery(), sqlValues());
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
    default Mono<Long> fetchLongUnique() {
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
    default Mono<String> fetchString() {
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
    default Mono<Optional<String>> fetchStringOptional() {
        return getSqlExecutor().queryForStringOptional(sqlQuery(), sqlValues());
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
    default Mono<String> fetchStringUnique() {
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
    default <T> Mono<T> fetchOneUnique(IntBiFunction<ResultEntry, T> resultSetRowReader) {
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
    default <T> Mono<Optional<T>> fetchOneOptional(final IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException {
        return getSqlExecutor().queryForOptional(sqlQuery(), sqlValues(), resultSetRowReader);
    }

    /**
     * Return the count of entities this query should return.
     *
     * @return
     */
    default Mono<Integer> fetchRowCount() {
        return getSqlExecutor().queryForInt(sqlRowCountQuery(), sqlValues());
    }

    SqlExecutor getSqlExecutor();

}
