/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rm.query.find;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.function.IntBiConsumer;
import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;

/**
 * @author Francesco Cina 20/giu/2011
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
    default <T> T fetch(final Function<ResultSet, T> resultSetReader) throws JpoException {
        return getSqlExecutor().query(sqlQuery(), sqlValues(), resultSetReader);
    }

    /**
     * Execute the query reading the ResultSet with a {@link ResultSetReader}.
     *
     * @param rse
     *            object that will extract all rows of results
     */
    default void fetch(final Consumer<ResultSet> resultSetReader) throws JpoException {
        getSqlExecutor().query(sqlQuery(), sqlValues(), resultSetReader);
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
    default <T> List<T> fetch(final IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException {
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
    default void fetch(final IntBiConsumer<ResultEntry> resultSetRowReader) throws JpoException {
        getSqlExecutor().query(sqlQuery(), sqlValues(), resultSetRowReader);
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
    default BigDecimal fetchBigDecimal() throws JpoException {
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
    default Optional<BigDecimal> fetchBigDecimalOptional() throws JpoException {
        return Optional.ofNullable(fetchBigDecimal());
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
    default BigDecimal fetchBigDecimalUnique() throws JpoException {
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
    default Boolean fetchBoolean() throws JpoException {
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
    default Optional<Boolean> fetchBooleanOptional() throws JpoException {
        return Optional.ofNullable(fetchBoolean());
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
    default Boolean fetchBooleanUnique() throws JpoException {
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
    default Double fetchDouble() {
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
    default Optional<Double> fetchDoubleOptional() {
        return Optional.ofNullable(fetchDouble());
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
    default Double fetchDoubleUnique() throws JpoException {
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
    default Float fetchFloat() {
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
    default Optional<Float> fetchFloatOptional() {
        return Optional.ofNullable(fetchFloat());
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
    default Float fetchFloatUnique() throws JpoException {
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
     * @returnRowReader<
     */
    default Integer fetchInt() {
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
    default Optional<Integer> fetchIntOptional() {
        return Optional.ofNullable(fetchInt());
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
    default Integer fetchIntUnique() throws JpoException {
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
    default Long fetchLong() {
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
    default Optional<Long> fetchLongOptional() {
        return Optional.ofNullable(fetchLong());
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
    default Long fetchLongUnique() throws JpoException {
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
    default String fetchString() {
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
    default Optional<String> fetchStringOptional() {
        return Optional.ofNullable(fetchString());
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
    default String fetchStringUnique() throws JpoException {
        return getSqlExecutor().queryForStringUnique(sqlQuery(), sqlValues());
    }

    /**
     * Execute the query reading the ResultSet with a {@RowReader<link ResultSetRowReader}
     *
     * @param rsrr
     *            object that will extract the row of result
     * @return
     * @throws JpoException
     * @throws JpoNotUniqueResultException
     *             if the results of the query executions are not exactly 1
     */
    default <T> T fetchUnique(final IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException, JpoNotUniqueResultException {
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
    default <T> Optional<T> fetchOptional(final IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException {
        return getSqlExecutor().queryForOptional(sqlQuery(), sqlValues(), resultSetRowReader);
    }

    /**
     * Return the count of entities this query should return.
     *
     * @return
     */
    public default int fetchRowCount() {
        return getSqlExecutor().queryForIntUnique(sqlRowCountQuery(), sqlValues());
    }

	SqlExecutor getSqlExecutor();

}
