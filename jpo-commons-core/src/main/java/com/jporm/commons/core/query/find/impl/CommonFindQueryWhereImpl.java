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
package com.jporm.commons.core.query.find.impl;

import com.jporm.annotation.LockMode;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.clause.impl.WhereImpl;
import com.jporm.commons.core.query.find.CommonFindQuery;
import com.jporm.commons.core.query.find.CommonFindQueryOrderBy;
import com.jporm.commons.core.query.find.CommonFindQueryWhere;

public class CommonFindQueryWhereImpl<FIND extends CommonFindQuery<FIND, WHERE, ORDER_BY>,
										WHERE extends CommonFindQueryWhere<FIND, WHERE, ORDER_BY>,
										ORDER_BY extends CommonFindQueryOrderBy<FIND, WHERE, ORDER_BY>>
									extends WhereImpl<WHERE> implements CommonFindQueryWhere<FIND, WHERE, ORDER_BY> {

	private final FIND findQuery;

	public CommonFindQueryWhereImpl(com.jporm.sql.query.clause.Where sqlWhere, final FIND findQuery) {
		super(sqlWhere);
		this.findQuery = findQuery;
	}

	@Override
	public final FIND distinct(final boolean distinct) throws JpoException {
		return this.findQuery.distinct(distinct);
	}

	@Override
	public final FIND lockMode(final LockMode lockMode) {
		return this.findQuery.lockMode(lockMode);
	}

	@Override
	public final FIND maxRows(final int maxRows) throws JpoException {
		return this.findQuery.maxRows(maxRows);
	}

	@Override
	public final ORDER_BY orderBy() throws JpoException {
		return this.findQuery.orderBy();
	}

	@Override
	public final FIND root() {
		return this.findQuery;
	}

	@Override
	public final FIND firstRow(final int firstRow) throws JpoException {
		return this.findQuery.firstRow(firstRow);
	}

	@Override
	protected final WHERE where() {
		return findQuery.where();
	}
}
