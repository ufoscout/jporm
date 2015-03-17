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
package com.jporm.rx.core.query.find.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.jporm.annotation.LockMode;
import com.jporm.annotation.exception.JpoWrongPropertyNameException;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.exception.JpoNotUniqueResultManyResultsException;
import com.jporm.commons.core.exception.JpoNotUniqueResultNoResultException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.AQueryRoot;
import com.jporm.commons.core.util.GenericWrapper;
import com.jporm.persistor.BeanFromResultSet;
import com.jporm.persistor.Persistor;
import com.jporm.rx.core.query.find.FindQuery;
import com.jporm.rx.core.query.find.FindQueryOrderBy;
import com.jporm.rx.core.query.find.FindQueryWhere;
import com.jporm.rx.core.session.Session;
import com.jporm.sql.query.clause.Select;
import com.jporm.sql.query.clause.WhereExpressionElement;

/**
 *
 * @author Francesco Cina
 *
 * 20/giu/2011
 */
public class FindQueryImpl<BEAN> extends AQueryRoot implements FindQuery<BEAN> {

	private final ServiceCatalog<Session> serviceCatalog;
	private final Class<BEAN> clazz;
	private final Select select;
	private final FindQueryWhereImpl<BEAN> where;
	private final FindQueryOrderByImpl<BEAN> orderBy;
	private final FindFromImpl<BEAN> from;
	private List<String> _ignoredFields = Collections.EMPTY_LIST;
	private String cacheName;
	private String[] allColumns;

	public FindQueryImpl(final ServiceCatalog<Session> serviceCatalog, final Class<BEAN> clazz, final String alias) {
		super(serviceCatalog.getSqlCache());
		this.serviceCatalog = serviceCatalog;
		this.clazz = clazz;

		select = serviceCatalog.getSqlFactory().select(clazz, alias);
		allColumns = serviceCatalog.getClassToolMap().get(clazz).getDescriptor().getAllColumnJavaNames();
		select.selectFields(allColumns);
		this.from = new FindFromImpl<BEAN>(select.from(), this);
		where = new FindQueryWhereImpl<BEAN>(select.where(), this);
		orderBy = new FindQueryOrderByImpl<BEAN>(select.orderBy(), this);
	}

	@Override
	public final void appendValues(final List<Object> values) {
		select.appendValues(values);
	}

	@Override
	public FindQuery<BEAN> cache(final String cache) {
		this.cacheName = cache;
		return this;
	}

	@Override
	public FindQuery<BEAN> distinct(final boolean distinct) {
		select.distinct(distinct);
		return this;
	}

	@Override
	public FindQuery<BEAN> firstRow(final int firstRow) throws JpoException {
		select.firstRow(firstRow);
		return this;
	}

	@Override
	public FindQuery<BEAN> fullOuterJoin(final Class<?> joinClass) {
		return this.from.fullOuterJoin(joinClass);
	}

	@Override
	public FindQuery<BEAN> fullOuterJoin(final Class<?> joinClass,
			final String joinClassAlias) {
		return this.from.fullOuterJoin(joinClass, joinClassAlias);
	}

	@Override
	public FindQuery<BEAN> fullOuterJoin(final Class<?> joinClass,
			final String onLeftProperty, final String onRigthProperty) {
		return this.from.fullOuterJoin(joinClass, onLeftProperty, onRigthProperty);
	}

	@Override
	public FindQuery<BEAN> fullOuterJoin(final Class<?> joinClass,
			final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		return this.from.fullOuterJoin(joinClass, joinClassAlias, onLeftProperty,
				onRigthProperty);
	}


	/**
	 * @return the cacheName
	 */
	public String getCacheName() {
		return cacheName;
	}

	@Override
	public final int getVersion() {
		return select.getVersion();

	}

	@Override
	public final FindQuery<BEAN> ignore(final boolean ignoreFieldsCondition, final String... fields) {
		if(ignoreFieldsCondition && (fields.length>0)) {
			_ignoredFields = Arrays.asList(fields);
			List<String> selectedColumns = new ArrayList<>();
			for (int i=0; i<allColumns.length; i++) {
				selectedColumns.add(allColumns[i]);
			}
			selectedColumns.removeAll(_ignoredFields);
			if (allColumns.length != (selectedColumns.size() + fields.length)) {
				throw new JpoWrongPropertyNameException("One of the specified fields is not a property of [" + clazz.getName() + "]");
			}
			select.selectFields(selectedColumns.toArray(new String[0]));
		}
		return this;
	}

	@Override
	public final FindQuery<BEAN> ignore(final String... fields) {
		return ignore(true, fields);
	}

	@Override
	public FindQuery<BEAN> innerJoin(final Class<?> joinClass) {
		return this.from.innerJoin(joinClass);
	}

	@Override
	public FindQuery<BEAN> innerJoin(final Class<?> joinClass, final String joinClassAlias) {
		return this.from.innerJoin(joinClass, joinClassAlias);
	}

	@Override
	public FindQuery<BEAN> innerJoin(final Class<?> joinClass, final String onLeftProperty,
			final String onRigthProperty) {
		return this.from.innerJoin(joinClass, onLeftProperty, onRigthProperty);
	}

	@Override
	public FindQuery<BEAN> innerJoin(final Class<?> joinClass, final String joinClassAlias,
			final String onLeftProperty, final String onRigthProperty) {
		return this.from.innerJoin(joinClass, joinClassAlias, onLeftProperty,
				onRigthProperty);
	}

	@Override
	public FindQuery<BEAN> join(final Class<?> joinClass) {
		return this.from.join(joinClass);
	}

	@Override
	public FindQuery<BEAN> join(final Class<?> joinClass, final String joinClassAlias) {
		return this.from.join(joinClass, joinClassAlias);
	}

	@Override
	public FindQuery<BEAN> leftOuterJoin(final Class<?> joinClass) {
		return this.from.leftOuterJoin(joinClass);
	}

	@Override
	public FindQuery<BEAN> leftOuterJoin(final Class<?> joinClass,
			final String joinClassAlias) {
		return this.from.leftOuterJoin(joinClass, joinClassAlias);
	}

	@Override
	public FindQuery<BEAN> leftOuterJoin(final Class<?> joinClass,
			final String onLeftProperty, final String onRigthProperty) {
		return this.from.leftOuterJoin(joinClass, onLeftProperty, onRigthProperty);
	}

	@Override
	public FindQuery<BEAN> leftOuterJoin(final Class<?> joinClass,
			final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		return this.from.leftOuterJoin(joinClass, joinClassAlias, onLeftProperty,
				onRigthProperty);
	}

	@Override
	public FindQuery<BEAN> lockMode(final LockMode lockMode) {
		select.lockMode(lockMode);
		return this;
	}

	@Override
	public final FindQuery<BEAN> maxRows(final int maxRows) throws JpoException {
		select.maxRows(maxRows);
		return this;
	}

	@Override
	public FindQuery<BEAN> naturalJoin(final Class<?> joinClass) {
		return this.from.naturalJoin(joinClass);
	}

	@Override
	public FindQuery<BEAN> naturalJoin(final Class<?> joinClass, final String joinClassAlias) {
		return this.from.naturalJoin(joinClass, joinClassAlias);
	}

	@Override
	public final FindQueryOrderBy<BEAN> orderBy() throws JpoException {
		return this.orderBy;
	}

	@Override
	public String renderRowCountSql() {
		return select.renderRowCountSql();
	}

	@Override
	public final void renderSql(final StringBuilder queryBuilder) {
		this.select.renderSql(queryBuilder);
	}

	@Override
	public FindQuery<BEAN> rightOuterJoin(final Class<?> joinClass) {
		return this.from.rightOuterJoin(joinClass);
	}

	@Override
	public FindQuery<BEAN> rightOuterJoin(final Class<?> joinClass,
			final String joinClassAlias) {
		return this.from.rightOuterJoin(joinClass, joinClassAlias);
	}

	@Override
	public FindQuery<BEAN> rightOuterJoin(final Class<?> joinClass,
			final String onLeftProperty, final String onRigthProperty) {
		return this.from.rightOuterJoin(joinClass, onLeftProperty, onRigthProperty);
	}

	@Override
	public FindQuery<BEAN> rightOuterJoin(final Class<?> joinClass,
			final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
		return this.from.rightOuterJoin(joinClass, joinClassAlias, onLeftProperty,
				onRigthProperty);
	}

	@Override
	public FindQueryWhere<BEAN> where(final List<WhereExpressionElement> expressionElements) {
		where.and(expressionElements);
		return where;
	}

	@Override
	public FindQueryWhere<BEAN> where(final String customClause, final Object... args) {
		where.and(customClause, args);
		return where;
	}

	@Override
	public FindQueryWhere<BEAN> where(final WhereExpressionElement... expressionElements) {
		if (expressionElements.length > 0) {
			where.and(expressionElements);
		}
		return where;
	}

	@Override
	public CompletableFuture<BEAN> get() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public BEAN get() throws JpoException {
//		final GenericWrapper<BEAN> wrapper = new GenericWrapper<BEAN>(null);
//		OrmRowMapper<BEAN> srr = new OrmRowMapper<BEAN>() {
//			@Override
//			public void read(final BEAN newObject, final int rowCount) {
//				wrapper.setValue(newObject);
//			}
//		};
//		get(srr, 1);
//		return wrapper.getValue();
//	}
//
//	private void get(final OrmRowMapper<BEAN> srr, final int ignoreResultsMoreThan) throws JpoException {
//		final List<Object> values = new ArrayList<Object>();
//		appendValues(values);
//		final String sql = renderSql();
//		serviceCatalog.getCacheStrategy().find(getCacheName(), sql, values, _ignoredFields,
//				(List<BEAN> fromCacheBeans) -> {
//		            for (int i = 0; i < fromCacheBeans.size(); i++) {
//		                srr.read(fromCacheBeans.get(i), i);
//		            }
//				},
//				cacheStrategyEntry -> {
//					final ResultSetReader<Object> resultSetReader = resultSet -> {
//						int rowCount = 0;
//						final Persistor<BEAN> ormClassTool = serviceCatalog.getClassToolMap().get(clazz).getPersistor();
//						while ( resultSet.next() && (rowCount<ignoreResultsMoreThan)) {
//							BeanFromResultSet<BEAN> beanFromRS = ormClassTool.beanFromResultSet(new JpoJdbcResultSet(resultSet), _ignoredFields);
//							srr.read( beanFromRS.getBean() , rowCount );
//							cacheStrategyEntry.add(beanFromRS.getBean());
//							rowCount++;
//						}
//						cacheStrategyEntry.end();
//						return null;
//					};
//
//					final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
//					sqlExec.query(sql, resultSetReader, values);
//				});
//
//	}

}