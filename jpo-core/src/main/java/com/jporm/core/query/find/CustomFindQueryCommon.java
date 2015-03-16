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
import com.jporm.commons.core.query.find.CommonFindQueryRoot;
import com.jporm.core.query.ResultSetReader;
import com.jporm.core.query.ResultSetRowReader;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public interface CustomFindQueryCommon extends CommonFindQueryRoot {

	/**
	 * Execute the query and read the result creating an ordered array with the
	 * extracted column values.
	 *
	 * @return
	 */
	Object[] get();

	/**
	 * Execute the query reading the ResultSet with a {@link ResultSetReader}.
	 *
	 * @param rse
	 *            object that will extract all rows of results
	 * @return an arbitrary result object, as returned by the
	 *         {@link ResultSetReader}
	 */
	<T> T get(ResultSetReader<T> rsr) throws JpoException;

	/**
	 * Execute the query reading the ResultSet with a {@link ResultSetRowReader}
	 * .
	 *
	 * @param rsrr
	 *            object that will extract all rows of results
	 * @return a List of result objects returned by the
	 *         {@link ResultSetRowReader}
	 */
	<T> List<T> get(ResultSetRowReader<T> rsrr) throws JpoException;

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
	BigDecimal getBigDecimal() throws JpoException;

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
	Optional<BigDecimal> getBigDecimalOptional() throws JpoException;

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
	BigDecimal getBigDecimalUnique() throws JpoException;

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
	Boolean getBoolean() throws JpoException;

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
	Optional<Boolean> getBooleanOptional() throws JpoException;

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
	Boolean getBooleanUnique() throws JpoException;

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
	Double getDouble();

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
	Optional<Double> getDoubleOptional();

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
	Double getDoubleUnique() throws JpoException;

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
	Float getFloat();

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
	Optional<Float> getFloatOptional();

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
	Float getFloatUnique() throws JpoException;

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
	Integer getInt();

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
	Optional<Integer> getIntOptional();

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
	Integer getIntUnique() throws JpoException;

	/**
	 * Execute the query and read the result creating a List of all the ordered
	 * arrays with the extracted column values for every row.
	 *
	 * @return
	 */
	List<Object[]> getList() throws JpoException;

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
	Long getLong();

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
	Optional<Long> getLongOptional();

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
	Long getLongUnique() throws JpoException;

	/**
	 * Execute the query and read the result creating an ordered array with the
	 * extracted column values.
	 *
	 * @return
	 */
	Optional<Object[]> getOptional();

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
	String getString();

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
	Optional<String> getStringOptional();

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
	String getStringUnique() throws JpoException;

	/**
	 * Execute the query and read the result creating an ordered array with the
	 * extracted column values.
	 *
	 * @return
	 */
	Object[] getUnique();

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
	<T> T getUnique(ResultSetRowReader<T> rsrr) throws JpoException, JpoNotUniqueResultException;

}
