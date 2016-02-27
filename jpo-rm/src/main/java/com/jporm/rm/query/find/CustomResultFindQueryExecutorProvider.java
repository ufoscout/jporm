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

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

/**
 * @author Francesco Cina 20/giu/2011
 */
public interface CustomResultFindQueryExecutorProvider extends SelectCommon {

    /**
     * Execute the query reading the ResultSet with a {@link ResultSetReader}.
     *
     * @param rse
     *            object that will extract all rows of results
     * @return an arbitrary result object, as returned by the
     *         {@link ResultSetReader}
     */
    default <T> T fetch(final ResultSetReader<T> rse) throws JpoException {
        return getExecutionEnvProvider().getSqlExecutor().query(sqlQuery(), sqlValues(), rse);
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
    default <T> List<T> fetch(final ResultSetRowReader<T> rsrr) throws JpoException {
        return getExecutionEnvProvider().getSqlExecutor().query(sqlQuery(), sqlValues(), rsrr);
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
        return getExecutionEnvProvider().getSqlExecutor().queryForBigDecimal(sqlQuery(), sqlValues());
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
        return getExecutionEnvProvider().getSqlExecutor().queryForBigDecimalUnique(sqlQuery(), sqlValues());
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
        return getExecutionEnvProvider().getSqlExecutor().queryForBoolean(sqlQuery(), sqlValues());
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
        return getExecutionEnvProvider().getSqlExecutor().queryForBooleanUnique(sqlQuery(), sqlValues());
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
        return getExecutionEnvProvider().getSqlExecutor().queryForDouble(sqlQuery(), sqlValues());
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
        return getExecutionEnvProvider().getSqlExecutor().queryForDoubleUnique(sqlQuery(), sqlValues());
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
        return getExecutionEnvProvider().getSqlExecutor().queryForFloat(sqlQuery(), sqlValues());
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
        return getExecutionEnvProvider().getSqlExecutor().queryForFloatUnique(sqlQuery(), sqlValues());
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
    default Integer fetchInt() {
        return getExecutionEnvProvider().getSqlExecutor().queryForInt(sqlQuery(), sqlValues());
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
        return getExecutionEnvProvider().getSqlExecutor().queryForIntUnique(sqlQuery(), sqlValues());
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
        return getExecutionEnvProvider().getSqlExecutor().queryForLong(sqlQuery(), sqlValues());
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
        return getExecutionEnvProvider().getSqlExecutor().queryForLongUnique(sqlQuery(), sqlValues());
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
        return getExecutionEnvProvider().getSqlExecutor().queryForString(sqlQuery(), sqlValues());
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
        return getExecutionEnvProvider().getSqlExecutor().queryForStringUnique(sqlQuery(), sqlValues());
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
    default <T> T fetchUnique(final ResultSetRowReader<T> rsrr) throws JpoException, JpoNotUniqueResultException {
        return getExecutionEnvProvider().getSqlExecutor().queryForUnique(sqlQuery(), sqlValues(), rsrr);
    }

	ExecutionEnvProvider<?> getExecutionEnvProvider();

}