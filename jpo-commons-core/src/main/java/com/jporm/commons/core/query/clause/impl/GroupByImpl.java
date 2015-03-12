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
package com.jporm.commons.core.query.clause.impl;

import com.jporm.commons.core.query.AQuerySubElement;
import com.jporm.commons.core.query.clause.GroupBy;
import com.jporm.commons.core.query.clause.QueryClause;

/**
 *
 * @author Francesco Cina
 *
 * 24/giu/2011
 */
public abstract class GroupByImpl<T extends QueryClause<?>> extends AQuerySubElement implements GroupBy<T> {

	private final com.jporm.sql.query.clause.GroupBy sqlGroupBy;

	public GroupByImpl(com.jporm.sql.query.clause.GroupBy sqlGroupBy) {
		this.sqlGroupBy = sqlGroupBy;
	}


	@Override
	public final T having(final String havingClause, final Object... args) {
		sqlGroupBy.having(havingClause, args);
		return sqlQuery();
	}

	@Override
	public final T fields(final String... fields) {
		sqlGroupBy.fields(fields);
		return sqlQuery();
	}

	/**
	 * @return
	 */
	protected abstract T sqlQuery();

}
