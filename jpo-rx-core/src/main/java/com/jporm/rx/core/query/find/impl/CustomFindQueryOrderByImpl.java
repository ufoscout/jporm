/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rx.core.query.find.impl;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.find.impl.CommonFindQueryOrderByImpl;
import com.jporm.rx.core.query.find.CustomFindQuery;
import com.jporm.rx.core.query.find.CustomFindQueryGroupBy;
import com.jporm.rx.core.query.find.CustomFindQueryOrderBy;
import com.jporm.rx.core.query.find.CustomFindQueryWhere;

/**
 * @author ufo
 */
public class CustomFindQueryOrderByImpl extends CommonFindQueryOrderByImpl<CustomFindQuery, CustomFindQueryWhere, CustomFindQueryOrderBy> implements CustomFindQueryOrderBy {

	public CustomFindQueryOrderByImpl(com.jporm.sql.query.clause.OrderBy sqlOrderBy, final CustomFindQuery customFindQuery) {
		super(sqlOrderBy, customFindQuery);
	}

	@Override
	public CustomFindQueryGroupBy groupBy(final String... fields) throws JpoException {
		return root().groupBy(fields);
	}

}
