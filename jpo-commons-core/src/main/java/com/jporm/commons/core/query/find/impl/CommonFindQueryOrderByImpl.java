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

import java.util.List;

import com.jporm.annotation.LockMode;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.clause.impl.OrderByImpl;
import com.jporm.commons.core.query.find.CommonFindQuery;
import com.jporm.commons.core.query.find.CommonFindQueryOrderBy;
import com.jporm.commons.core.query.find.CommonFindQueryWhere;
import com.jporm.sql.query.clause.WhereExpressionElement;

/**
 *
 * @author ufo
 *
 * @param <BEAN>
 */
public class CommonFindQueryOrderByImpl<FIND extends CommonFindQuery<FIND, WHERE, ORDER_BY>,
								WHERE extends CommonFindQueryWhere<FIND, WHERE, ORDER_BY>,
								ORDER_BY extends CommonFindQueryOrderBy<FIND, WHERE, ORDER_BY>>
	extends OrderByImpl<ORDER_BY> implements CommonFindQueryOrderBy<FIND, WHERE, ORDER_BY> {

	private final FIND findQuery;

	public CommonFindQueryOrderByImpl(com.jporm.sql.query.clause.OrderBy sqlOrderBy, final FIND findQuery) {
		super(sqlOrderBy);
		this.findQuery = findQuery;
	}

	@Override
	public final void appendValues(final List<Object> values) {
		this.findQuery.appendValues(values);
	}

	@Override
	public final FIND distinct(final boolean distinct) throws JpoException {
		return this.findQuery.distinct(distinct);
	}

	@Override
	public final FIND firstRow(final int firstRow) throws JpoException {
		return this.findQuery.firstRow(firstRow);
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
	public final FIND query() {
		return this.findQuery;
	}

	@Override
	public final String renderRowCountSql() throws JpoException {
		return this.findQuery.renderRowCountSql();
	}

	@Override
	public final String renderSql() {
		return this.findQuery.renderSql();
	}

	@Override
	public final void renderSql(final StringBuilder stringBuilder) {
		this.findQuery.renderSql(stringBuilder);
	}

	@Override
	public final WHERE where(final List<WhereExpressionElement> expressionElements) {
		return findQuery.where(expressionElements);
	}

	@Override
	public final WHERE where(final String customClause, final Object... args) {
		return findQuery.where(customClause, args);
	}

	@Override
	public final WHERE where(final WhereExpressionElement... expressionElements) {
		return findQuery.where(expressionElements);
	}

	@Override
	public final int getVersion() {
		return findQuery.getVersion();
	}

	@Override
	protected final ORDER_BY orderBy() {
		return findQuery.orderBy();
	}

}
