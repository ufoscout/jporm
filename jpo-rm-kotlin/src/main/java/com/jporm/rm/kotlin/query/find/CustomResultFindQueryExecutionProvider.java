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
package com.jporm.rm.kotlin.query.find;

import java.math.BigDecimal;
import java.util.Optional;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.rm.kotlin.session.SqlExecutor;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.types.io.ResultEntry;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

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
    default <T> Observable<T> fetchAll(IntBiFunction<ResultEntry, T> resultSetRowReader) {
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
    default Maybe<BigDecimal> fetchBigDecimal() {
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
    default Single<Optional<BigDecimal>> fetchBigDecimalOptional() {
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
    default Single<BigDecimal> fetchBigDecimalUnique() {
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
    default Maybe<Boolean> fetchBoolean() {
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
    default Single<Optional<Boolean>> fetchBooleanOptional() {
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
    default Single<Boolean> fetchBooleanUnique() {
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
    default Maybe<Double> fetchDouble() {
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
    default Single<Optional<Double>> fetchDoubleOptional() {
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
    default Single<Double> fetchDoubleUnique() {
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
    default Maybe<Float> fetchFloat() {
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
    default Single<Optional<Float>> fetchFloatOptional() {
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
    default Single<Float> fetchFloatUnique() {
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
    default Maybe<Integer> fetchInt() {
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
    default Single<Optional<Integer>> fetchIntOptional() {
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
    default Single<Integer> fetchIntUnique() {
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
    default Maybe<Long> fetchLong() {
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
    default Single<Optional<Long>> fetchLongOptional() {
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
    default Single<Long> fetchLongUnique() {
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
    default Maybe<String> fetchString() {
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
    default Single<Optional<String>> fetchStringOptional() {
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
    default Single<String> fetchStringUnique() {
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
    default <T> Single<T> fetchOneUnique(IntBiFunction<ResultEntry, T> resultSetRowReader) {
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
    default <T> Single<Optional<T>> fetchOneOptional(final IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException {
        return getSqlExecutor().queryForOptional(sqlQuery(), sqlValues(), resultSetRowReader);
    }

    /**
     * Return the count of entities this query should return.
     *
     * @return
     */
    default Single<Integer> fetchRowCount() {
        return getSqlExecutor().queryForIntUnique(sqlRowCountQuery(), sqlValues());
    }

    SqlExecutor getSqlExecutor();

}
