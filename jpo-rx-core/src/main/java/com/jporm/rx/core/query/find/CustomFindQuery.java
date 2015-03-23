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

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.find.CommonFindQuery;

/**
 *
 * @author Francesco Cina
 *
 * 07/lug/2011
 */
public interface CustomFindQuery extends CustomFindQueryCommon, CommonFindQuery<CustomFindQuery, CustomFindQueryWhere, CustomFindQueryOrderBy> {

	/**
	 * Set the GROUP BY clause
	 * @param fields the fields to group by
	 * @return
	 * @throws JpoException
	 */

	CustomFindQueryGroupBy groupBy(String... fields) throws JpoException;

}
