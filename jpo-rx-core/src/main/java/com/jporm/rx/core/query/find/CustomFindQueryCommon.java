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
package com.jporm.rx.core.query.find;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public interface CustomFindQueryCommon {

	/**
	 * Execute the query reading the ResultSet with a {@link ResultSetReader}.
	 *
	 * @param rse
	 *            object that will extract all rows of results
	 * @return an arbitrary result object, as returned by the
	 *         {@link ResultSetReader}
	 */
	<T> CompletableFuture<T> get(ResultSetReader<T> rsr);

	/**
	 * Execute the query reading the ResultSet with a {@link ResultSetRowReader}
	 * .
	 *
	 * @param rsrr
	 *            object that will extract all rows of results
	 * @return a List of result objects returned by the
	 *         {@link ResultSetRowReader}
	 */
	<T> CompletableFuture<List<T>> get(ResultSetRowReader<T> rsrr);

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
	CompletableFuture<BigDecimal> getBigDecimal();

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
	CompletableFuture<Optional<BigDecimal>> getBigDecimalOptional();

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
	CompletableFuture<BigDecimal> getBigDecimalUnique();

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
	CompletableFuture<Boolean> getBoolean();

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
	CompletableFuture<Optional<Boolean>> getBooleanOptional();

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
	CompletableFuture<Boolean> getBooleanUnique();

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
	CompletableFuture<Double> getDouble();

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
	CompletableFuture<Optional<Double>> getDoubleOptional();

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
	CompletableFuture<Double> getDoubleUnique();

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
	CompletableFuture<Float> getFloat();

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
	CompletableFuture<Optional<Float>> getFloatOptional();

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
	CompletableFuture<Float> getFloatUnique();

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
	CompletableFuture<Integer> getInt();

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
	CompletableFuture<Optional<Integer>> getIntOptional();

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
	CompletableFuture<Integer> getIntUnique();

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
	CompletableFuture<Long> getLong();

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
	CompletableFuture<Optional<Long>> getLongOptional();

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
	CompletableFuture<Long> getLongUnique();

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
	CompletableFuture<String> getString();

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
	CompletableFuture<Optional<String>> getStringOptional();

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
	CompletableFuture<String> getStringUnique();

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
	<T> CompletableFuture<T> getUnique(ResultSetRowReader<T> rsrr);

}
