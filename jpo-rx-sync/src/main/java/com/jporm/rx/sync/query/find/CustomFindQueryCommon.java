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
package com.jporm.rx.sync.query.find;

import co.paralleluniverse.fibers.Suspendable;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
@Suspendable
public interface CustomFindQueryCommon {

	/**
	 * Execute the query reading the ResultSet with a {@link ResultSetReader}.
	 *
	 * @param rsr
	 *            object that will extract all rows of results
	 * @return an arbitrary result object, as returned by the
	 *         {@link ResultSetReader}
	 */
	<T> T fetch(ResultSetReader<T> rsr);

	/**
	 * Execute the query reading the ResultSet with a {@link ResultSetRowReader}
	 * .
	 *
	 * @param rsrr
	 *            object that will extract all rows of results
	 * @return a List of result objects returned by the
	 *         {@link ResultSetRowReader}
	 */
	<T> List<T> fetch(ResultSetRowReader<T> rsrr);

	/**
	 * Execute the query and read the result as an {@link BigDecimal} value. If
	 * more than one rows are returned by the query, the first value is
	 * returned.
	 * @return
	 */
	BigDecimal fetchBigDecimal();

	/**
	 * Execute the query and read the result as an {@link BigDecimal} value. If
	 * more than one rows are returned by the query, the first value is
	 * returned.
	 * @return
	 */
	Optional<BigDecimal> fetchBigDecimalOptional();

	/**
	 * Execute the query and read the result as a BigDecimal value
	 *
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *             if the results of the query executions are not exactly 1
	 * @return
	 */
	BigDecimal fetchBigDecimalUnique();

	/**
	 * Execute the query and read the result as an {@link Boolean} value. If
	 * more than one rows are returned by the query, the first value is
	 * returned.
	 *
	 * @return
	 */
	Boolean fetchBoolean();

	/**
	 * Execute the query and read the result as an {@link Boolean} value. If
	 * more than one rows are returned by the query, the first value is
	 * returned.
	 *
	 * @return
	 */
	Optional<Boolean> fetchBooleanOptional();

	/**
	 * Execute the query and read the result as a boolean value
	 *
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *             if the results of the query executions are not exactly 1
	 * @return
	 */
	Boolean fetchBooleanUnique();

	/**
	 * Execute the query and read the result as an {@link Double} value. If more
	 * than one rows are returned by the query, the first value is returned.
	 *
	 * @return
	 */
	Double fetchDouble();

	/**
	 * Execute the query and read the result as an {@link Double} value. If more
	 * than one rows are returned by the query, the first value is returned.
	 *
	 * @return
	 */
	Optional<Double> fetchDoubleOptional();

	/**
	 * Execute the query and read the result as a double value
	 *
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *             if the results of the query executions are not exactly 1
	 * @return
	 */
	Double fetchDoubleUnique();

	/**
	 * Execute the query and read the result as an {@link Float} value. If more
	 * than one rows are returned by the query, the first value is returned.
	 *
	 * @return
	 */
	Float fetchFloat();

	/**
	 * Execute the query and read the result as an {@link Float} value. If more
	 * than one rows are returned by the query, the first value is returned.
	 *
	 * @return
	 */
	Optional<Float> fetchFloatOptional();

	/**
	 * Execute the query and read the result as a float value
	 *
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *             if the results of the query executions are not exactly 1
	 * @return
	 */
	Float fetchFloatUnique();

	/**
	 * Execute the query and read the result as an {@link Integer} value. If
	 * more than one rows are returned by the query, the first value is
	 * returned.
	 *
	 * @return
	 */
	Integer fetchInt();

	/**
	 * Execute the query and read the result as an {@link Integer} value. If
	 * more than one rows are returned by the query, the first value is
	 * returned.
	 *
	 * @return
	 */
	Optional<Integer> fetchIntOptional();

	/**
	 * Execute the query and read the result as an {@link Integer} value
	 *
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *             if the results of the query executions are not exactly 1
	 * @return
	 */
	Integer fetchIntUnique();

	/**
	 * Execute the query and read the result as an {@link Long} value. If more
	 * than one rows are returned by the query, the first value is returned.
	 *
	 * @return
	 */
	Long fetchLong();

	/**
	 * Execute the query and read the result as an {@link Long} value. If more
	 * than one rows are returned by the query, the first value is returned.
	 *
	 * @return
	 */
	Optional<Long> fetchLongOptional();

	/**
	 * Execute the query and read the result as an {@link Long} value
	 *
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *             if the results of the query executions are not exactly 1
	 * @return
	 */
	Long fetchLongUnique();

	/**
	 * Execute the query and read the result as an {@link String} value. If more
	 * than one rows are returned by the query, the first value is returned.
	 *
	 * @return
	 */
	String fetchString();

	/**
	 * Execute the query and read the result as an {@link String} value. If more
	 * than one rows are returned by the query, the first value is returned.
	 *
	 * @return
	 */
	Optional<String> fetchStringOptional();

	/**
	 * Execute the query and read the result as a String value
	 *
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *             if the results of the query executions are not exactly 1
	 * @return
	 */
	String fetchStringUnique();

	/**
	 * Execute the query reading the ResultSet with a {@link ResultSetRowReader}
	 * .
	 *
	 * @param rsrr
	 *            object that will extract the row of result
	 * @return
	 * @throws com.jporm.commons.core.exception.JpoNotUniqueResultException
	 *             if the results of the query executions are not exactly 1
	 */
	<T> T fetchUnique(ResultSetRowReader<T> rsrr);

}
