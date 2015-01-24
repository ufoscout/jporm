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
package com.jporm.query.find;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import com.jporm.exception.OrmException;
import com.jporm.exception.OrmNotUniqueResultException;
import com.jporm.query.LockMode;
import com.jporm.query.OrmRowMapper;

/**
 *
 * @author Francesco Cina
 *
 * 18/giu/2011
 */
public interface FindQueryCommon<BEAN> extends BaseFindQuery {

	/**
	 * Execute the query returning the list of beans.
	 * @return
	 */
	List<BEAN> getList() throws OrmException;

	/**
	 * Return the count of entities this query should return.
	 * @return
	 */
	int getRowCount() throws OrmException;

	/**
	 * Execute the query and return the first entry of the {@link ResultSet}, if the {@link ResultSet} contains at least one row,
	 * null otherwise.
	 * @return
	 */
	BEAN get() throws OrmException;

	/**
	 * Execute the query returning either a single bean or an Exception.
	 * @return
	 * @throws OrmNotUniqueResultException if zero or more than one row are returned by the query
	 */
	BEAN getUnique() throws OrmException, OrmNotUniqueResultException;

	/**
	 * Execute the query and return the first entry of the {@link ResultSet}, if the {@link ResultSet} contains at least one row,
	 * null otherwise.
	 * @return
	 */
	Optional<BEAN> getOptional() throws OrmException;

	/**
	 * Execute the query and for each bean returned the callback method of {@link OrmRowMapper} is called.
	 * No references to created Beans are hold by the orm; in addition, one bean at time is created just before calling
	 * the callback method. This method permits to handle big amount of data with a minimum memory footprint.
	 * @param orm
	 * @throws OrmException
	 */
	void get(OrmRowMapper<BEAN> orm) throws OrmException;

	/**
	 * Return the sql used to calculate the row count of the execution of this query.
	 * @return
	 */
	String renderRowCountSql() throws OrmException;

	/**
	 * Whether to use Distinct in the select clause
	 * @return
	 */
	FindQuery<BEAN> distinct(boolean distinct) throws OrmException;

	/**
	 * Set the {@link LockMode} for the query
	 * @param lockMode
	 * @return
	 */
	FindQuery<BEAN> lockMode(LockMode lockMode);

	/**
	 * Set the maximum number of rows to retrieve.
	 * @param maxRows
	 * @return
	 */
	FindQuery<BEAN> maxRows(int maxRows) throws OrmException;

	/**
	 * Set the first row to retrieve. If not set, rows will be
	 * retrieved beginning from row <tt>0</tt>.
	 * @param firstRow the first row to retrieve starting from 0.
	 * @return
	 */
	FindQuery<BEAN> firstRow(int firstRow) throws OrmException;

	/**
	 * Set the query timeout in seconds.
	 */
	FindQuery<BEAN> timeout(int seconds);

	/**
	 * Return the query timeout seconds.
	 */
	int getTimeout();

}
