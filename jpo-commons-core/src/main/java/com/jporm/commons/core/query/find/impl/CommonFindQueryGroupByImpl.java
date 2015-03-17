/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.commons.core.query.find.impl;

import java.util.List;

import com.jporm.annotation.LockMode;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.clause.impl.GroupByImpl;
import com.jporm.commons.core.query.find.CommonFindQuery;
import com.jporm.commons.core.query.find.CommonFindQueryGroupBy;
import com.jporm.commons.core.query.find.CommonFindQueryOrderBy;
import com.jporm.commons.core.query.find.CommonFindQueryWhere;
import com.jporm.sql.query.clause.WhereExpressionElement;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Mar 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class CommonFindQueryGroupByImpl<FIND extends CommonFindQuery<FIND, WHERE, ORDER_BY>,
										WHERE extends CommonFindQueryWhere<FIND, WHERE, ORDER_BY>,
										ORDER_BY extends CommonFindQueryOrderBy<FIND, WHERE, ORDER_BY>,
										GROUP_BY extends CommonFindQueryGroupBy<FIND, WHERE, ORDER_BY, GROUP_BY>>
								extends GroupByImpl<GROUP_BY> implements CommonFindQueryGroupBy<FIND, WHERE, ORDER_BY, GROUP_BY> {

	private final FIND customFindQuery;

	public CommonFindQueryGroupByImpl(com.jporm.sql.query.clause.GroupBy sqlGroupBy, final FIND customFindQuery) {
		super(sqlGroupBy);
		this.customFindQuery = customFindQuery;
	}

	@Override
	public final void appendValues(final List<Object> values) {
		customFindQuery.appendValues(values);
	}

	@Override
	public final FIND distinct(final boolean distinct) throws JpoException {
		return customFindQuery.distinct(distinct);
	}

	@Override
	public final FIND firstRow(final int firstRow) throws JpoException {
		return customFindQuery.firstRow(firstRow);
	}

	@Override
	public final FIND lockMode(final LockMode lockMode) {
		return customFindQuery.lockMode(lockMode);
	}

	@Override
	public final FIND maxRows(final int maxRows) throws JpoException {
		return customFindQuery.maxRows(maxRows);
	}

	@Override
	public final ORDER_BY orderBy() throws JpoException {
		return customFindQuery.orderBy();
	}

	@Override
	public final String renderSql() {
		return customFindQuery.renderSql();
	}

	@Override
	public final void renderSql(final StringBuilder queryBuilder) {
		customFindQuery.renderSql(queryBuilder);
	}

	@Override
	public final WHERE where(final List<WhereExpressionElement> expressionElements) {
		return customFindQuery.where(expressionElements);
	}

	@Override
	public final WHERE where(final String customClause, final Object... args) {
		return customFindQuery.where(customClause, args);
	}

	@Override
	public final WHERE where(final WhereExpressionElement... expressionElements) {
		return customFindQuery.where(expressionElements);
	}

	@Override
	public final int getVersion() {
		return customFindQuery.getVersion();
	}

	@Override
	public final String renderRowCountSql() throws JpoException {
		return customFindQuery.renderRowCountSql();
	}

	@Override
	public final FIND query() {
		return customFindQuery;
	}

	@Override
	protected GROUP_BY sqlQuery() {
		return (GROUP_BY) this;
	}

}