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
package com.jporm.commons.core.query.find;

import com.jporm.annotation.LockMode;
import com.jporm.commons.core.exception.JpoException;

/**
 *
 * @author Francesco Cina
 *
 * 18/giu/2011
 */
public interface CommonFindQueryBase<FIND extends CommonFindQuery<FIND, WHERE, ORDER_BY>,
								WHERE extends CommonFindQueryWhere<FIND, WHERE, ORDER_BY>,
								ORDER_BY extends CommonFindQueryOrderBy<FIND, WHERE, ORDER_BY>> {

	/**
	 * Whether to use Distinct in the select clause
	 * @return
	 */
	FIND distinct(boolean distinct) throws JpoException;

	/**
	 * Set the {@link LockMode} for the query
	 * @param lockMode
	 * @return
	 */
	FIND lockMode(LockMode lockMode);

	/**
	 * Set the maximum number of rows to retrieve.
	 * @param maxRows
	 * @return
	 */
	FIND maxRows(int maxRows) throws JpoException;

	/**
	 * Set the first row to retrieve. If not set, rows will be
	 * retrieved beginning from row <tt>0</tt>.
	 * @param firstRow the first row to retrieve starting from 0.
	 * @return
	 */
	FIND firstRow(int firstRow) throws JpoException;

}
