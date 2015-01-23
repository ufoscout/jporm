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

import com.jporm.exception.OrmException;
import com.jporm.query.BaseFindQuery;
import com.jporm.query.CustomQuery;
import com.jporm.query.LockMode;

/**
 *
 * @author Francesco Cina
 *
 * 07/lug/2011
 */
public interface CustomFindQueryCommon extends BaseFindQuery, CustomQuery {

	/**
	 * Whether to use Distinct in the select clause
	 * @return
	 */
	CustomFindQuery distinct(boolean distinct) throws OrmException;

	/**
	 * Set the {@link LockMode} for the query
	 * @param lockMode
	 * @return
	 */
	CustomFindQuery lockMode(LockMode lockMode);

	/**
	 * Set the maximum number of rows to retrieve.
	 * @param maxRows
	 * @return
	 */
	CustomFindQuery maxRows(int maxRows) throws OrmException;

	/**
	 * Set the first row to retrieve. If not set, rows will be
	 * retrieved beginning from row <tt>0</tt>.
	 * @param firstRow the first row to retrieve starting from 0.
	 * @return
	 */
	CustomFindQuery firstRow(int firstRow) throws OrmException;

	/**
	 * Set the query timeout in seconds.
	 */
	CustomFindQuery timeout(int seconds);

	/**
	 * Return the query timeout seconds.
	 */
	int getTimeout();

}
