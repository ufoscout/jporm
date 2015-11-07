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

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.query.find.CommonFindQueryRoot;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public interface CustomFindQueryCommon extends CommonFindQueryRoot {

    /**
     * Execute the query reading the ResultSet with a {@link ResultSetReader}.
     *
     * @param rse
     *            object that will extract all rows of results
     * @return an arbitrary result object, as returned by the
     *         {@link ResultSetReader}
     */
    <T> CompletableFuture<T> fetch(ResultSetReader<T> rsr);

    /**
     * Execute the query reading the ResultSet with a {@link ResultSetRowReader}
     * .
     *
     * @param rsrr
     *            object that will extract all rows of results
     * @return a List of result objects returned by the
     *         {@link ResultSetRowReader}
     */
    <T> CompletableFuture<List<T>> fetch(ResultSetRowReader<T> rsrr);

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
    CompletableFuture<BigDecimal> fetchBigDecimal();

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
    CompletableFuture<Optional<BigDecimal>> fetchBigDecimalOptional();

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
    CompletableFuture<BigDecimal> fetchBigDecimalUnique();

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
    CompletableFuture<Boolean> fetchBoolean();

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
    CompletableFuture<Optional<Boolean>> fetchBooleanOptional();

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
    CompletableFuture<Boolean> fetchBooleanUnique();

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
    CompletableFuture<Double> fetchDouble();

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
    CompletableFuture<Optional<Double>> fetchDoubleOptional();

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
    CompletableFuture<Double> fetchDoubleUnique();

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
    CompletableFuture<Float> fetchFloat();

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
    CompletableFuture<Optional<Float>> fetchFloatOptional();

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
    CompletableFuture<Float> fetchFloatUnique();

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
    CompletableFuture<Integer> fetchInt();

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
    CompletableFuture<Optional<Integer>> fetchIntOptional();

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
    CompletableFuture<Integer> fetchIntUnique();

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
    CompletableFuture<Long> fetchLong();

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
    CompletableFuture<Optional<Long>> fetchLongOptional();

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
    CompletableFuture<Long> fetchLongUnique();

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
    CompletableFuture<String> fetchString();

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
    CompletableFuture<Optional<String>> fetchStringOptional();

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
    CompletableFuture<String> fetchStringUnique();

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
    <T> CompletableFuture<T> fetchUnique(ResultSetRowReader<T> rsrr);

}
