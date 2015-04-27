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
package com.jporm.core.query.find;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
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
	<T> T fetch(ResultSetReader<T> rsr) throws JpoException;

	/**
	 * Execute the query reading the ResultSet with a {@link ResultSetRowReader}
	 * .
	 *
	 * @param rsrr
	 *            object that will extract all rows of results
	 * @return a List of result objects returned by the
	 *         {@link ResultSetRowReader}
	 */
	<T> List<T> fetch(ResultSetRowReader<T> rsrr) throws JpoException;

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
	BigDecimal fetchBigDecimal() throws JpoException;

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
	Optional<BigDecimal> fetchBigDecimalOptional() throws JpoException;

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
	BigDecimal fetchBigDecimalUnique() throws JpoException;

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
	Boolean fetchBoolean() throws JpoException;

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
	Optional<Boolean> fetchBooleanOptional() throws JpoException;

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
	Boolean fetchBooleanUnique() throws JpoException;

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
	Double fetchDouble();

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
	Optional<Double> fetchDoubleOptional();

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
	Double fetchDoubleUnique() throws JpoException;

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
	Float fetchFloat();

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
	Optional<Float> fetchFloatOptional();

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
	Float fetchFloatUnique() throws JpoException;

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
	Integer fetchInt();

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
	Optional<Integer> fetchIntOptional();

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
	Integer fetchIntUnique() throws JpoException;

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
	Long fetchLong();

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
	Optional<Long> fetchLongOptional();

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
	Long fetchLongUnique() throws JpoException;

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
	String fetchString();

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
	Optional<String> fetchStringOptional();

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
	String fetchStringUnique() throws JpoException;

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
	<T> T fetchUnique(ResultSetRowReader<T> rsrr) throws JpoException, JpoNotUniqueResultException;

}
