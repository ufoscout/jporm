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
package com.jporm.rx.sync.query.find.impl;

import co.paralleluniverse.fibers.Suspendable;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.rx.query.find.FindQuery;
import com.jporm.rx.sync.query.find.FindQueryOrderBySync;
import com.jporm.rx.sync.query.find.FindQuerySync;
import com.jporm.rx.sync.query.find.FindQueryWhereSync;
import com.jporm.sql.query.clause.WhereExpressionElement;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author ufo
 *
 * @param <BEAN>
 */
@Suspendable
public class FindQueryOrderBySyncImpl<BEAN> implements FindQueryOrderBySync<BEAN> {

	private FindQuerySync<BEAN> findQuerySync;
	private FindQuery<BEAN> findQuery;

	public FindQueryOrderBySyncImpl(FindQuerySync<BEAN> findQuerySync, FindQuery<BEAN> findQuery) {
		this.findQuerySync = findQuerySync;
		this.findQuery = findQuery;
	}

	@Override
	public FindQuerySync<BEAN> root() {
		return findQuerySync;
	}

	@Override
	public FindQueryWhereSync<BEAN> where(WhereExpressionElement... expressionElements) {
		return findQuerySync.where(expressionElements);
	}

	@Override
	public FindQueryWhereSync<BEAN> where(List<WhereExpressionElement> expressionElements) {
		return findQuerySync.where(expressionElements);
	}

	@Override
	public FindQueryWhereSync<BEAN> where(String customClause, Object... args) {
		return findQuerySync.where(customClause, args);
	}

	@Override
	public FindQuerySync<BEAN> distinct() throws JpoException {
		return findQuerySync.distinct();
	}

	@Override
	public FindQuerySync<BEAN> forUpdate() {
		return findQuerySync.forUpdate();
	}

	@Override
	public FindQuerySync<BEAN> forUpdateNoWait() {
		return findQuerySync.forUpdateNoWait();
	}

	@Override
	public FindQuerySync<BEAN> limit(int limit) throws JpoException {
		return findQuerySync.limit(limit);
	}

	@Override
	public FindQuerySync<BEAN> offset(int offset) throws JpoException {
		return findQuerySync.offset(offset);
	}

	@Override
	public BEAN fetch() {
		return findQuerySync.fetch();
	}

	@Override
	public Optional<BEAN> fetchOptional() {
		return findQuerySync.fetchOptional();
	}

	@Override
	public BEAN fetchUnique() {
		return findQuerySync.fetchUnique();
	}

	@Override
	public Boolean exist() {
		return exist();
	}

	@Override
	public List<BEAN> fetchList() {
		return findQuerySync.fetchList();
	}

	@Override
	public Integer fetchRowCount() {
		return findQuerySync.fetchRowCount();
	}

	@Override
	public FindQueryOrderBySync<BEAN> asc(String property) {
		findQuery.orderBy().asc(property);
		return this;
	}

	@Override
	public FindQueryOrderBySync<BEAN> ascNullsFirst(String property) {
		findQuery.orderBy().ascNullsFirst(property);
		return this;
	}

	@Override
	public FindQueryOrderBySync<BEAN> ascNullsLast(String property) {
		findQuery.orderBy().ascNullsLast(property);
		return this;
	}

	@Override
	public FindQueryOrderBySync<BEAN> desc(String property) {
		findQuery.orderBy().desc(property);
		return this;
	}

	@Override
	public FindQueryOrderBySync<BEAN> descNullsFirst(String property) {
		findQuery.orderBy().descNullsFirst(property);
		return this;
	}

	@Override
	public FindQueryOrderBySync<BEAN> descNullsLast(String property) {
		findQuery.orderBy().descNullsLast(property);
		return this;
	}
}
