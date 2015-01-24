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
package com.jporm.core.query.find;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.AQueryRoot;
import com.jporm.core.query.namesolver.NameSolverImpl;
import com.jporm.core.util.GenericWrapper;
import com.jporm.exception.OrmException;
import com.jporm.exception.OrmNotUniqueResultException;
import com.jporm.exception.OrmNotUniqueResultManyResultsException;
import com.jporm.exception.OrmNotUniqueResultNoResultException;
import com.jporm.persistor.BeanFromResultSet;
import com.jporm.persistor.Persistor;
import com.jporm.query.LockMode;
import com.jporm.query.OrmRowMapper;
import com.jporm.query.clause.WhereExpressionElement;
import com.jporm.query.find.FindQuery;
import com.jporm.query.find.FindQueryOrderBy;
import com.jporm.query.find.FindQueryWhere;
import com.jporm.query.namesolver.NameSolver;
import com.jporm.session.ResultSetReader;
import com.jporm.session.SqlExecutor;

/**
 *
 * @author Francesco Cina
 *
 * 20/giu/2011
 */
public class FindQueryImpl<BEAN> extends AQueryRoot implements FindQuery<BEAN> {

	private final ServiceCatalog serviceCatalog;
	private final Class<BEAN> clazz;
	private int _queryTimeout = 0;
	private int _maxRows = -1;
	private LockMode _lockMode = LockMode.NO_LOCK;
	private final CustomFindSelectImpl select;
	private final FindQueryWhereImpl<BEAN> where = new FindQueryWhereImpl<BEAN>(this);
	private final FindQueryOrderByImpl<BEAN> orderBy = new FindQueryOrderByImpl<BEAN>(this);
	private final FindFromImpl<BEAN> from;
	private int versionStatus = 0;
	private final NameSolver nameSolver;
	private List<String> _ignoredFields = Collections.EMPTY_LIST;
	private String cacheName;
	private int _firstRow = -1;

	public FindQueryImpl(final ServiceCatalog serviceCatalog, final Class<BEAN> clazz, final String alias) {
		super(serviceCatalog);
		this.serviceCatalog = serviceCatalog;
		this.clazz = clazz;

		this.nameSolver = new NameSolverImpl(serviceCatalog, false);
		this.from = new FindFromImpl<BEAN>(this, serviceCatalog, clazz, nameSolver.register(clazz, alias), nameSolver);
		this.select = new CustomFindSelectImpl(serviceCatalog.getClassToolMap().get(clazz).getDescriptor().getAllColumnJavaNames());

	}

	@Override
	public final void appendValues(final List<Object> values) {
		this.where.appendElementValues(values);
	}

	@Override
	public FindQuery<BEAN> cache(final String cache) {
		this.cacheName = cache;
		return this;
	}

	@Override
	public FindQuery<BEAN> distinct(final boolean distinct) {
		select.setDistinct(distinct);
		return this;
	}

	@Override
	public FindQuery<BEAN> firstRow(final int firstRow) throws OrmException {
		this._firstRow = firstRow;
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

	@Override
	public BEAN get() throws OrmException {
		final GenericWrapper<BEAN> wrapper = new GenericWrapper<BEAN>(null);
		OrmRowMapper<BEAN> srr = new OrmRowMapper<BEAN>() {
			@Override
			public void read(final BEAN newObject, final int rowCount) {
				wrapper.setValue(newObject);
			}
		};
		get(srr, 1);
		return wrapper.getValue();
	}

	@Override
	public void get(final OrmRowMapper<BEAN> srr) throws OrmException {
		get(srr, Integer.MAX_VALUE);
	}

	/**
	 * @return the cacheName
	 */
	public String getCacheName() {
		return cacheName;
	}

	/**
	 * @return the _ignoredFields
	 */
	public List<String> getIgnoredFields() {
		return _ignoredFields;
	}

	@Override
	public List<BEAN> getList() {
		final List<BEAN> results = new ArrayList<BEAN>();
		OrmRowMapper<BEAN> srr = new OrmRowMapper<BEAN>() {
			@Override
			public void read(final BEAN newObject, final int rowCount) {
				results.add(newObject);
			}
		};
		get(srr);
		return results;
	}

	public LockMode getLockMode() {
		return this._lockMode;
	}

	@Override
	public Optional<BEAN> getOptional() throws OrmException {
		return Optional.ofNullable(get());
	}

	@Override
	public int getRowCount() {
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
		sqlExec.setTimeout(getTimeout());
		return sqlExec.queryForIntUnique(renderRowCountSql(), values);
	}

	@Override
	public final int getStatusVersion() {
		return this.versionStatus + select.getElementStatusVersion() + this.from.getElementStatusVersion() + this.where.getElementStatusVersion() + this.orderBy.getElementStatusVersion();

	}

	@Override
	public int getTimeout() {
		return _queryTimeout;
	}

	@Override
	public BEAN getUnique() throws OrmNotUniqueResultException {
		final GenericWrapper<BEAN> wrapper = new GenericWrapper<BEAN>(null);
		OrmRowMapper<BEAN> srr = new OrmRowMapper<BEAN>() {
			@Override
			public void read(final BEAN newObject, final int rowCount) {
				if (rowCount>0) {
					throw new OrmNotUniqueResultManyResultsException("The query execution returned a number of rows different than one: more than one result found"); //$NON-NLS-1$
				}
				wrapper.setValue(newObject);
			}
		};
		get(srr);
		if (wrapper.getValue() == null) {
			throw new OrmNotUniqueResultNoResultException("The query execution returned a number of rows different than one: no results found"); //$NON-NLS-1$
		}
		return wrapper.getValue();
	}

	@Override
	public final FindQuery<BEAN> ignore(final boolean ignoreFieldsCondition, final String... fields) {
		if(ignoreFieldsCondition) {
			_ignoredFields = Arrays.asList(fields);
			versionStatus++;
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

	public boolean isDistinct() throws OrmException {
		return select.isDistinct();
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
		this._lockMode = lockMode;
		this.versionStatus++;
		return this;
	}

	@Override
	public final FindQuery<BEAN> maxRows(final int maxRows) throws OrmException {
		this._maxRows = maxRows;
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
	public final FindQueryOrderBy<BEAN> orderBy() throws OrmException {
		return this.orderBy;
	}

	@Override
	public String renderRowCountSql() {
		final StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(*) FROM ( "); //$NON-NLS-1$
		queryBuilder.append( renderSql() );
		queryBuilder.append( ") a " ); //$NON-NLS-1$
		//        this.from.renderSqlElement(queryBuilder, nameSolver);
		//        this.where.renderSqlElement(queryBuilder, nameSolver);
		return queryBuilder.toString();
	}

	@Override
	public final void renderSql(final StringBuilder queryBuilder) {
		this.select.ignore(_ignoredFields);
		this.select.renderSqlElement(queryBuilder, nameSolver);
		this.from.renderSqlElement(queryBuilder, nameSolver);
		this.where.renderSqlElement(queryBuilder, nameSolver);
		this.orderBy.renderSqlElement(queryBuilder,nameSolver);
		queryBuilder.append(this._lockMode.getMode());
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
	public final FindQuery<BEAN> timeout(final int queryTimeout) {
		this._queryTimeout = queryTimeout;
		return this;
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
	public boolean exist() {
		return getRowCount()>0;
	}

	private void get(final OrmRowMapper<BEAN> srr, final int ignoreResultsMoreThan) throws OrmException {
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		final String sql = serviceCatalog.getDbProfile().getQueryTemplate().paginateSQL(renderSql(), _firstRow, _maxRows);
		serviceCatalog.getCacheStrategy().find(getCacheName(), sql, values, getIgnoredFields(), srr,
				cacheStrategyEntry -> {
					final ResultSetReader<Object> resultSetReader = resultSet -> {
						int rowCount = 0;
						final Persistor<BEAN> ormClassTool = serviceCatalog.getClassToolMap().get(clazz).getPersistor();
						while ( resultSet.next() && (rowCount<ignoreResultsMoreThan)) {
							BeanFromResultSet<BEAN> beanFromRS = ormClassTool.beanFromResultSet(resultSet, getIgnoredFields());
							srr.read( beanFromRS.getBean() , rowCount );
							cacheStrategyEntry.add(beanFromRS.getBean());
							rowCount++;
						}
						cacheStrategyEntry.end();
						return null;
					};

					final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
					sqlExec.setTimeout(getTimeout());
					sqlExec.query(sql, resultSetReader, values);
				});

	}

}
