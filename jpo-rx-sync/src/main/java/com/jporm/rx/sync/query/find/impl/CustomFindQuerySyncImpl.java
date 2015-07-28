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
import com.jporm.rx.sync.query.find.CustomFindQueryGroupBySync;
import com.jporm.rx.sync.query.find.CustomFindQueryOrderBySync;
import com.jporm.rx.sync.query.find.CustomFindQuerySync;
import com.jporm.rx.sync.query.find.CustomFindQueryWhereSync;
import com.jporm.sql.query.clause.SelectCommon;
import com.jporm.sql.query.clause.WhereExpressionElement;

import co.paralleluniverse.fibers.Suspendable;

@Suspendable
public class CustomFindQuerySyncImpl extends CustomFindQueryCommonSyncAbstract implements CustomFindQuerySync {

	private final CustomFindQuery customFindQuery;
	private final CustomFindQueryOrderBySync customFindQueryOrderBySync;
	private final CustomFindQueryWhereSync customFindQueryWhereSync;

	public CustomFindQuerySyncImpl(CustomFindQuery customFindQuery) {
		this.customFindQuery = customFindQuery;
		customFindQueryOrderBySync = new CustomFindQueryOrderBySyncImpl(this, customFindQuery, customFindQuery.orderBy());
		customFindQueryWhereSync = new CustomFindQueryWhereSyncImpl(this, customFindQuery);
	}

	@Override
	public SelectCommon sql() {
		return getCustomFindQuery().sql();
	}

	@Override
	public CustomFindQueryWhereSync where(WhereExpressionElement... expressionElements) {
		getCustomFindQuery().where(expressionElements);
		return customFindQueryWhereSync;
	}

	@Override
	public CustomFindQueryWhereSync where(List<WhereExpressionElement> expressionElements) {
		getCustomFindQuery().where(expressionElements);
		return customFindQueryWhereSync;
	}

	@Override
	public CustomFindQueryWhereSync where(String customClause, Object... args) {
		getCustomFindQuery().where(customClause, args);
		return customFindQueryWhereSync;
	}

	@Override
	public CustomFindQueryOrderBySync orderBy() throws JpoException {
		return customFindQueryOrderBySync;
	}

	@Override
	public CustomFindQueryGroupBySync groupBy(String... fields) throws JpoException {
		return new CustomFindQueryGroupBySyncImpl(this, customFindQuery, getCustomFindQuery().groupBy(fields));
	}

	@Override
	public CustomFindQuerySync cache(String cache) {
		getCustomFindQuery().cache(cache);
		return this;
	}

	@Override
	public CustomFindQuerySync ignore(String... fields) {
		getCustomFindQuery().ignore(fields);
		return this;
	}

	@Override
	public CustomFindQuerySync ignore(boolean ignoreFieldsCondition, String... fields) {
		getCustomFindQuery().ignore(ignoreFieldsCondition, fields);
		return this;
	}

	@Override
	public CustomFindQuerySync join(Class<?> joinClass) {
		getCustomFindQuery().join(joinClass);
		return this;
	}

	@Override
	public CustomFindQuerySync join(Class<?> joinClass, String joinClassAlias) {
		getCustomFindQuery().join(joinClass, joinClassAlias);
		return this;
	}

	@Override
	public CustomFindQuerySync naturalJoin(Class<?> joinClass) {
		getCustomFindQuery().naturalJoin(joinClass);
		return this;
	}

	@Override
	public CustomFindQuerySync naturalJoin(Class<?> joinClass, String joinClassAlias) {
		getCustomFindQuery().naturalJoin(joinClass, joinClassAlias);
		return this;
	}

	@Override
	public CustomFindQuerySync innerJoin(Class<?> joinClass) {
		getCustomFindQuery().innerJoin(joinClass);
		return this;
	}

	@Override
	public CustomFindQuerySync innerJoin(Class<?> joinClass, String joinClassAlias) {
		getCustomFindQuery().innerJoin(joinClass, joinClassAlias);
		return this;
	}

	@Override
	public CustomFindQuerySync innerJoin(Class<?> joinClass, String onLeftProperty, String onRigthProperty) {
		getCustomFindQuery().innerJoin(joinClass, onLeftProperty, onRigthProperty);
		return this;
	}

	@Override
	public CustomFindQuerySync innerJoin(Class<?> joinClass, String joinClassAlias, String onLeftProperty, String onRigthProperty) {
		getCustomFindQuery().innerJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
		return this;
	}

	@Override
	public CustomFindQuerySync leftOuterJoin(Class<?> joinClass) {
		getCustomFindQuery().leftOuterJoin(joinClass);
		return this;
	}

	@Override
	public CustomFindQuerySync leftOuterJoin(Class<?> joinClass, String joinClassAlias) {
		getCustomFindQuery().leftOuterJoin(joinClass, joinClassAlias);
		return this;
	}

	@Override
	public CustomFindQuerySync leftOuterJoin(Class<?> joinClass, String onLeftProperty, String onRigthProperty) {
		getCustomFindQuery().leftOuterJoin(joinClass, onLeftProperty, onRigthProperty);
		return this;
	}

	@Override
	public CustomFindQuerySync leftOuterJoin(Class<?> joinClass, String joinClassAlias, String onLeftProperty, String onRigthProperty) {
		getCustomFindQuery().leftOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
		return this;
	}

	@Override
	public CustomFindQuerySync rightOuterJoin(Class<?> joinClass) {
		getCustomFindQuery().rightOuterJoin(joinClass);
		return this;
	}

	@Override
	public CustomFindQuerySync rightOuterJoin(Class<?> joinClass, String joinClassAlias) {
		getCustomFindQuery().rightOuterJoin(joinClass, joinClassAlias);
		return this;
	}

	@Override
	public CustomFindQuerySync rightOuterJoin(Class<?> joinClass, String onLeftProperty, String onRigthProperty) {
		getCustomFindQuery().rightOuterJoin(joinClass, onLeftProperty, onRigthProperty);
		return this;
	}

	@Override
	public CustomFindQuerySync rightOuterJoin(Class<?> joinClass, String joinClassAlias, String onLeftProperty, String onRigthProperty) {
		getCustomFindQuery().rightOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
		return this;
	}

	@Override
	public CustomFindQuerySync fullOuterJoin(Class<?> joinClass) {
		getCustomFindQuery().fullOuterJoin(joinClass);
		return this;
	}

	@Override
	public CustomFindQuerySync fullOuterJoin(Class<?> joinClass, String joinClassAlias) {
		getCustomFindQuery().fullOuterJoin(joinClass, joinClassAlias);
		return this;
	}

	@Override
	public CustomFindQuerySync fullOuterJoin(Class<?> joinClass, String onLeftProperty, String onRigthProperty) {
		getCustomFindQuery().fullOuterJoin(joinClass, onLeftProperty, onRigthProperty);
		return this;
	}

	@Override
	public CustomFindQuerySync fullOuterJoin(Class<?> joinClass, String joinClassAlias, String onLeftProperty, String onRigthProperty) {
		getCustomFindQuery().fullOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
		return this;
	}

	@Override
	public CustomFindQuerySync distinct() throws JpoException {
		getCustomFindQuery().distinct();
		return this;
	}

	@Override
	public CustomFindQuerySync forUpdate() {
		getCustomFindQuery().forUpdate();
		return this;
	}

	@Override
	public CustomFindQuerySync forUpdateNoWait() {
		getCustomFindQuery().forUpdateNoWait();
		return this;
	}

	@Override
	public CustomFindQuerySync limit(int limit) throws JpoException {
		getCustomFindQuery().limit(limit);
		return this;
	}

	@Override
	public CustomFindQuerySync offset(int offset) throws JpoException {
		getCustomFindQuery().offset(offset);
		return this;
	}

	@Override
	protected CustomFindQuery getCustomFindQuery() {
		return customFindQuery;
	}

}
