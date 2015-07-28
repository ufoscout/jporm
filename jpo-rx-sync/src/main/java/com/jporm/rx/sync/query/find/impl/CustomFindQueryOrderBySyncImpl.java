/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rx.sync.query.find.impl;

import java.util.List;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rx.query.find.CustomFindQuery;
import com.jporm.rx.query.find.CustomFindQueryCommon;
import com.jporm.rx.query.find.CustomFindQueryOrderBy;
import com.jporm.rx.sync.query.find.CustomFindQueryGroupBySync;
import com.jporm.rx.sync.query.find.CustomFindQueryOrderBySync;
import com.jporm.rx.sync.query.find.CustomFindQuerySync;
import com.jporm.rx.sync.query.find.CustomFindQueryWhereSync;
import com.jporm.sql.query.clause.WhereExpressionElement;

import co.paralleluniverse.fibers.Suspendable;

@Suspendable
public class CustomFindQueryOrderBySyncImpl extends CustomFindQueryCommonSyncAbstract implements CustomFindQueryOrderBySync {

	private final CustomFindQueryOrderBy orderBy;
	private final CustomFindQuerySync customFindQuerySync;
	private final CustomFindQuery customFindQuery;

	public CustomFindQueryOrderBySyncImpl(CustomFindQuerySync customFindQuerySync, CustomFindQuery customFindQuery, CustomFindQueryOrderBy orderBy) {
		this.customFindQuerySync = customFindQuerySync;
		this.customFindQuery = customFindQuery;
		this.orderBy = orderBy;
	}

	@Override
	public CustomFindQueryWhereSync where(WhereExpressionElement... expressionElements) {
		return customFindQuerySync.where(expressionElements);
	}

	@Override
	public CustomFindQueryWhereSync where(List<WhereExpressionElement> expressionElements) {
		return customFindQuerySync.where(expressionElements);
	}

	@Override
	public CustomFindQueryWhereSync where(String customClause, Object... args) {
		return customFindQuerySync.where(customClause, args);
	}

	@Override
	public CustomFindQuerySync root() {
		return customFindQuerySync;
	}

	@Override
	public CustomFindQuerySync distinct() throws JpoException {
		return customFindQuerySync.distinct();
	}

	@Override
	public CustomFindQuerySync forUpdate() {
		return customFindQuerySync.forUpdate();
	}

	@Override
	public CustomFindQuerySync forUpdateNoWait() {
		return customFindQuerySync.forUpdateNoWait();
	}

	@Override
	public CustomFindQuerySync limit(int limit) throws JpoException {
		return customFindQuerySync.limit(limit);
	}

	@Override
	public CustomFindQuerySync offset(int offset) throws JpoException {
		return customFindQuerySync.offset(offset);
	}


	@Override
	protected CustomFindQueryCommon getCustomFindQuery() {
		return customFindQuery;
	}

	@Override
	public CustomFindQueryOrderBySync asc(String property) {
		orderBy.asc(property);
		return this;
	}

	@Override
	public CustomFindQueryOrderBySync ascNullsFirst(String property) {
		orderBy.ascNullsFirst(property);
		return this;
	}

	@Override
	public CustomFindQueryOrderBySync ascNullsLast(String property) {
		orderBy.ascNullsLast(property);
		return this;
	}

	@Override
	public CustomFindQueryOrderBySync desc(String property) {
		orderBy.desc(property);
		return this;
	}

	@Override
	public CustomFindQueryOrderBySync descNullsFirst(String property) {
		orderBy.descNullsFirst(property);
		return this;
	}

	@Override
	public CustomFindQueryOrderBySync descNullsLast(String property) {
		orderBy.descNullsLast(property);
		return this;
	}

	@Override
	public CustomFindQueryGroupBySync groupBy(String... fields) throws JpoException {
		return customFindQuerySync.groupBy(fields);
	}

}
