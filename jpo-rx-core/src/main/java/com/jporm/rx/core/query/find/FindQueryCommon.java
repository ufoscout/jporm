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

import com.jporm.annotation.LockMode;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.find.CommonFindQueryRoot;

/**
 *
 * @author Francesco Cina
 *
 * 18/giu/2011
 */
public interface FindQueryCommon<BEAN> extends CommonFindQueryRoot, FindQueryBase<BEAN> {

//	/**
//	 * Execute the query returning the list of beans.
//	 * @return
//	 */
//	List<BEAN> getList() throws JpoException;
//
//	/**
//	 * Return the count of entities this query should return.
//	 * @return
//	 */
//	int getRowCount() throws JpoException;

	/**
	 * Return the sql used to calculate the row count of the execution of this query.
	 * @return
	 */
	String renderRowCountSql() throws JpoException;

	/**
	 * Whether to use Distinct in the select clause
	 * @return
	 */
	FindQuery<BEAN> distinct(boolean distinct) throws JpoException;

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
	FindQuery<BEAN> maxRows(int maxRows) throws JpoException;

	/**
	 * Set the first row to retrieve. If not set, rows will be
	 * retrieved beginning from row <tt>0</tt>.
	 * @param firstRow the first row to retrieve starting from 0.
	 * @return
	 */
	FindQuery<BEAN> firstRow(int firstRow) throws JpoException;

}
