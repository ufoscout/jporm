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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.jporm.annotation.LockMode;
import com.jporm.annotation.exception.JpoWrongPropertyNameException;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.AQueryRoot;
import com.jporm.commons.core.query.clause.From;
import com.jporm.commons.core.query.find.CommonFindQuery;
import com.jporm.commons.core.query.find.CommonFindQueryOrderBy;
import com.jporm.commons.core.query.find.CommonFindQueryWhere;
import com.jporm.sql.query.clause.Select;
import com.jporm.sql.query.clause.WhereExpressionElement;

/**
 *
 * @author Francesco Cina
 *
 * 20/giu/2011
 */
public class CommonFindQueryImpl<FIND extends CommonFindQuery<FIND, WHERE, ORDER_BY>,
								WHERE extends CommonFindQueryWhere<FIND, WHERE, ORDER_BY>,
								ORDER_BY extends CommonFindQueryOrderBy<FIND, WHERE, ORDER_BY>>
						extends AQueryRoot implements CommonFindQuery<FIND, WHERE, ORDER_BY> {

	private final Class<?> clazz;
	private final Select select;
	private final String[] allColumnNames;
	private WHERE where;
	private ORDER_BY orderBy;
	private From<FIND> from;
	private List<String> _ignoredFields = Collections.EMPTY_LIST;
	private String cacheName;

	public CommonFindQueryImpl(final ServiceCatalog<?> serviceCatalog, final Class<?> clazz, final String alias) {
		super(serviceCatalog.getSqlCache());
		this.clazz = clazz;
		select = serviceCatalog.getSqlFactory().select(clazz, alias);
		allColumnNames = serviceCatalog.getClassToolMap().get(clazz).getDescriptor().getAllColumnJavaNames();
	}

	@Override
	public final void appendValues(final List<Object> values) {
		getSelect().appendValues(values);
	}

	@Override
	public final FIND cache(final String cache) {
		this.cacheName = cache;
		return query();
	}

	@Override
	public final FIND distinct(final boolean distinct) {
		getSelect().distinct(distinct);
		return query();
	}

	@Override
	public final FIND firstRow(final int firstRow) throws JpoException {
		getSelect().firstRow(firstRow);
		return query();
	}

	@Override
	public final FIND fullOuterJoin(final Class<?> joinClass) {
		return this.from.fullOuterJoin(joinClass);
	}

	@Override
	public final FIND fullOuterJoin(final Class<?> joinClass,
			final String joinClassAlias) {
		return this.from.fullOuterJoin(joinClass, joinClassAlias);
	}

	@Override
	public final FIND fullOuterJoin(final Class<?> joinClass,
			final String onLeftProperty, final String onRigthProperty) {
		return this.from.fullOuterJoin(joinClass, onLeftProperty, onRigthProperty);
	}

	@Override
	public final FIND fullOuterJoin(final Class<?> joinClass,
			final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		return this.from.fullOuterJoin(joinClass, joinClassAlias, onLeftProperty,
				onRigthProperty);
	}

	/**
	 * @return the cacheName
	 */
	public final String getCacheName() {
		return cacheName;
	}

	@Override
	public final int getVersion() {
		return getSelect().getVersion();

	}

	@Override
	public final FIND ignore(final boolean ignoreFieldsCondition, final String... fields) {
		if(ignoreFieldsCondition && (fields.length>0)) {
			_ignoredFields = Arrays.asList(fields);
			List<String> selectedColumns = new ArrayList<>();
			for (int i=0; i<getAllColumns().length; i++) {
				selectedColumns.add(getAllColumns()[i]);
			}
			selectedColumns.removeAll(_ignoredFields);
			if (getAllColumns().length != (selectedColumns.size() + fields.length)) {
				throw new JpoWrongPropertyNameException("One of the specified fields is not a property of [" + clazz.getName() + "]");
			}
			getSelect().selectFields(selectedColumns.toArray(new String[0]));
		}
		return query();
	}

	@Override
	public final FIND ignore(final String... fields) {
		return ignore(true, fields);
	}

	@Override
	public final FIND innerJoin(final Class<?> joinClass) {
		return this.from.innerJoin(joinClass);
	}

	@Override
	public final FIND innerJoin(final Class<?> joinClass, final String joinClassAlias) {
		return this.from.innerJoin(joinClass, joinClassAlias);
	}

	@Override
	public final FIND innerJoin(final Class<?> joinClass, final String onLeftProperty,
			final String onRigthProperty) {
		return this.from.innerJoin(joinClass, onLeftProperty, onRigthProperty);
	}

	@Override
	public final FIND innerJoin(final Class<?> joinClass, final String joinClassAlias,
			final String onLeftProperty, final String onRigthProperty) {
		return this.from.innerJoin(joinClass, joinClassAlias, onLeftProperty,
				onRigthProperty);
	}

	@Override
	public final FIND join(final Class<?> joinClass) {
		return this.from.join(joinClass);
	}

	@Override
	public final FIND join(final Class<?> joinClass, final String joinClassAlias) {
		return this.from.join(joinClass, joinClassAlias);
	}

	@Override
	public final FIND leftOuterJoin(final Class<?> joinClass) {
		return this.from.leftOuterJoin(joinClass);
	}

	@Override
	public final FIND leftOuterJoin(final Class<?> joinClass,
			final String joinClassAlias) {
		return this.from.leftOuterJoin(joinClass, joinClassAlias);
	}

	@Override
	public final FIND leftOuterJoin(final Class<?> joinClass,
			final String onLeftProperty, final String onRigthProperty) {
		return this.from.leftOuterJoin(joinClass, onLeftProperty, onRigthProperty);
	}

	@Override
	public final FIND leftOuterJoin(final Class<?> joinClass,
			final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		return this.from.leftOuterJoin(joinClass, joinClassAlias, onLeftProperty,
				onRigthProperty);
	}

	@Override
	public final FIND lockMode(final LockMode lockMode) {
		getSelect().lockMode(lockMode);
		return query();
	}

	@Override
	public final FIND maxRows(final int maxRows) throws JpoException {
		getSelect().maxRows(maxRows);
		return query();
	}

	protected final FIND query() {
		return (FIND) this;
	}

	@Override
	public final FIND naturalJoin(final Class<?> joinClass) {
		return this.from.naturalJoin(joinClass);
	}

	@Override
	public final FIND naturalJoin(final Class<?> joinClass, final String joinClassAlias) {
		return this.from.naturalJoin(joinClass, joinClassAlias);
	}

	@Override
	public final ORDER_BY orderBy() throws JpoException {
		return this.orderBy;
	}

	@Override
	public final String renderRowCountSql() {
		return getSelect().renderRowCountSql();
	}

	@Override
	public final void renderSql(final StringBuilder queryBuilder) {
		this.getSelect().renderSql(queryBuilder);
	}

	@Override
	public final FIND rightOuterJoin(final Class<?> joinClass) {
		return this.from.rightOuterJoin(joinClass);
	}

	@Override
	public final FIND rightOuterJoin(final Class<?> joinClass,
			final String joinClassAlias) {
		return this.from.rightOuterJoin(joinClass, joinClassAlias);
	}

	@Override
	public final FIND rightOuterJoin(final Class<?> joinClass,
			final String onLeftProperty, final String onRigthProperty) {
		return this.from.rightOuterJoin(joinClass, onLeftProperty, onRigthProperty);
	}

	@Override
	public final FIND rightOuterJoin(final Class<?> joinClass,
			final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		return this.from.rightOuterJoin(joinClass, joinClassAlias, onLeftProperty,
				onRigthProperty);
	}

	@Override
	public final WHERE where(final List<WhereExpressionElement> expressionElements) {
		where.and(expressionElements);
		return where;
	}

	@Override
	public final WHERE where(final String customClause, final Object... args) {
		where.and(customClause, args);
		return where;
	}

	@Override
	public final WHERE where(final WhereExpressionElement... expressionElements) {
		if (expressionElements.length > 0) {
			where.and(expressionElements);
		}
		return where;
	}

	/**
	 * @return the select
	 */
	public Select getSelect() {
		return select;
	}

	/**
	 * @return the where
	 */
	public WHERE getWhere() {
		return where;
	}

	/**
	 * @param where the where to set
	 */
	public void setWhere(WHERE where) {
		this.where = where;
	}

	/**
	 * @return the orderBy
	 */
	public ORDER_BY getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(ORDER_BY orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the from
	 */
	public From<FIND> getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(From<FIND> from) {
		this.from = from;
	}

	/**
	 * @return the _ignoredFields
	 */
	public List<String> getIgnoredFields() {
		return _ignoredFields;
	}

	/**
	 * @param _ignoredFields the _ignoredFields to set
	 */
	public void setIgnoredFields(List<String> _ignoredFields) {
		this._ignoredFields = _ignoredFields;
	}

	/**
	 * @return the allColumns
	 */
	public String[] getAllColumns() {
		return allColumnNames;
	}

}
