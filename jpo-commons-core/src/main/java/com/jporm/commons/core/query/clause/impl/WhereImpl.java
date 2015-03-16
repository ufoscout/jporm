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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jporm.commons.core.query.AQuerySubElement;
import com.jporm.commons.core.query.clause.Where;
import com.jporm.commons.core.query.find.CommonFindQueryRoot;
import com.jporm.commons.core.query.find.SelectFindQueryRootAdatpter;

/**
 *
 * @author Francesco Cina
 *
 *         19/giu/2011
 */
public abstract class WhereImpl<T extends Where<?>> extends AQuerySubElement implements Where<T> {

	private final com.jporm.sql.query.clause.Where sqlWhere;

	public WhereImpl(com.jporm.sql.query.clause.Where sqlWhere) {
		this.sqlWhere = sqlWhere;
	}

	@Override
	public T allEq(Map<String, Object> propertyMap) {
		sqlWhere.allEq(propertyMap);
		return where();
	}

	@Override
	public T and(com.jporm.sql.query.clause.WhereExpressionElement... WhereExpressionElements) {
		sqlWhere.and(WhereExpressionElements);
		return where();
	}

	@Override
	public T and(List<com.jporm.sql.query.clause.WhereExpressionElement> WhereExpressionElements) {
		sqlWhere.and(WhereExpressionElements);
		return where();
	}

	@Override
	public T and(String customClause, Object... args) {
		sqlWhere.and(customClause, args);
		return where();
	}

	@Override
	public T eq(String property, Object value) {
		sqlWhere.eq(property, value);
		return where();
	}

	@Override
	public T eqProperties(String firstProperty, String secondProperty) {
		sqlWhere.eqProperties(firstProperty, secondProperty);
		return where();
	}

	@Override
	public T ge(String property, Object value) {
		sqlWhere.ge(property, value);
		return where();
	}

	@Override
	public T geProperties(String firstProperty, String secondProperty) {
		sqlWhere.geProperties(firstProperty, secondProperty);
		return where();
	}

	@Override
	public T gt(String property, Object value) {
		sqlWhere.gt(property, value);
		return where();
	}

	@Override
	public T gtProperties(String firstProperty, String secondProperty) {
		sqlWhere.gtProperties(firstProperty, secondProperty);
		return where();
	}

	@Override
	public T ieq(String property, String value) {
		sqlWhere.ieq(property, value);
		return where();
	}

	@Override
	public T ieqProperties(String firstProperty, String secondProperty) {
		sqlWhere.ieqProperties(firstProperty, secondProperty);
		return where();
	}

	@Override
	public T ilike(String property, String value) {
		sqlWhere.ilike(property, value);
		return where();
	}

	@Override
	public T in(String property, Collection<?> values) {
		sqlWhere.in(property, values);
		return where();
	}

	@Override
	public T in(String property, Object[] values) {
		sqlWhere.in(property, values);
		return where();
	}

	@Override
	public T isNotNull(String property) {
		sqlWhere.isNotNull(property);
		return where();
	}

	@Override
	public T isNull(String property) {
		sqlWhere.isNull(property);
		return where();
	}

	@Override
	public T le(String property, Object value) {
		sqlWhere.le(property, value);
		return where();
	}

	@Override
	public T leProperties(String firstProperty, String secondProperty) {
		sqlWhere.leProperties(firstProperty, secondProperty);
		return where();
	}

	@Override
	public T like(String property, String value) {
		sqlWhere.like(property, value);
		return where();
	}

	@Override
	public T lt(String property, Object value) {
		sqlWhere.lt(property, value);
		return where();
	}

	@Override
	public T ltProperties(String firstProperty, String secondProperty) {
		sqlWhere.ltProperties(firstProperty, secondProperty);
		return where();
	}

	@Override
	public T ne(String property, Object value) {
		sqlWhere.ne(property, value);
		return where();
	}

	@Override
	public T neProperties(String firstProperty, String secondProperty) {
		sqlWhere.neProperties(firstProperty, secondProperty);
		return where();
	}

	@Override
	public T nin(String property, Collection<?> values) {
		sqlWhere.nin(property, values);
		return where();
	}

	@Override
	public T nin(String property, Object[] values) {
		sqlWhere.nin(property, values);
		return where();
	}

	@Override
	public T nlike(String property, String value) {
		sqlWhere.nlike(property, value);
		return where();
	}

	@Override
	public T not(com.jporm.sql.query.clause.WhereExpressionElement... expression) {
		sqlWhere.not(expression);
		return where();
	}

	@Override
	public T not(List<com.jporm.sql.query.clause.WhereExpressionElement> whereExpressionElements) {
		sqlWhere.not(whereExpressionElements);
		return where();
	}

	@Override
	public T not(String customClause, Object... args) {
		sqlWhere.not(customClause, args);
		return where();
	}

	@Override
	public T or(com.jporm.sql.query.clause.WhereExpressionElement... whereExpressionElements) {
		sqlWhere.or(whereExpressionElements);
		return where();
	}

	@Override
	public T or(List<com.jporm.sql.query.clause.WhereExpressionElement> whereExpressionElements) {
		sqlWhere.or(whereExpressionElements);
		return where();
	}

	@Override
	public T or(String customClause, Object... args) {
		sqlWhere.or(customClause, args);
		return where();
	}

	protected abstract T where();

	@Override
	public T in(String property, CommonFindQueryRoot subQuery) {
		sqlWhere.in(property, new SelectFindQueryRootAdatpter(subQuery));
		return where();
	}

	@Override
	public T nin(String property, CommonFindQueryRoot subQuery) {
		sqlWhere.nin(property, new SelectFindQueryRootAdatpter(subQuery));
		return where();
	}

}
