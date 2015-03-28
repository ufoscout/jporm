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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultManyResultsException;
import com.jporm.commons.core.exception.JpoNotUniqueResultNoResultException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.io.RowMapper;
import com.jporm.commons.core.query.find.impl.CommonFindFromImpl;
import com.jporm.commons.core.query.find.impl.CommonFindQueryImpl;
import com.jporm.persistor.Persistor;
import com.jporm.rx.core.query.find.FindQuery;
import com.jporm.rx.core.query.find.FindQueryOrderBy;
import com.jporm.rx.core.query.find.FindQueryWhere;
import com.jporm.rx.core.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.query.clause.Select;
import com.jporm.sql.query.clause.SelectCommon;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

/**
 *
 * @author Francesco Cina
 *
 *         20/giu/2011
 */
public class FindQueryImpl<BEAN> extends CommonFindQueryImpl<FindQuery<BEAN>, FindQueryWhere<BEAN>, FindQueryOrderBy<BEAN>> implements FindQuery<BEAN> {

	private final ServiceCatalog serviceCatalog;
	private final Class<BEAN> clazz;
	private final SqlExecutor sqlExecutor;

	public FindQueryImpl(final ServiceCatalog serviceCatalog, final Class<BEAN> clazz, final String alias, SqlExecutor sqlExecutor, SqlFactory sqlFactory) {
		super(clazz, alias, serviceCatalog.getSqlCache(), sqlFactory, serviceCatalog.getClassToolMap());
		this.serviceCatalog = serviceCatalog;
		this.clazz = clazz;
		this.sqlExecutor = sqlExecutor;
		Select select = getSelect();
		select.selectFields(getAllColumns());
		setFrom(new CommonFindFromImpl<>(select.from(), this));
		setWhere(new FindQueryWhereImpl<>(select.where(), this));
		setOrderBy(new FindQueryOrderByImpl<>(select.orderBy(), this));
	}

	@Override
	public CompletableFuture<BEAN> get() {
		return get(resultSet -> {
			if (resultSet.next() ) {
				return getPersistor().beanFromResultSet(resultSet, getIgnoredFields()).getBean();
			}
			return null;
		});
	}

	private <T> CompletableFuture<T> get(ResultSetReader<T> rsr) throws JpoException {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.query(sql().renderSql(dbType.getDBProfile()), rsr, getParams());
		});
	}

	private <T> CompletableFuture<List<T>> get(ResultSetRowReader<T> rsr) throws JpoException {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.query(sql().renderSql(dbType.getDBProfile()), rsr, getParams());
		});
	}

	private Persistor<BEAN> getPersistor() {
		return serviceCatalog.getClassToolMap().get(clazz).getPersistor();
	}

	@Override
	public SelectCommon sql() {
		return getSelect();
	}

	@Override
	public CompletableFuture<Optional<BEAN>> getOptional() {
		return get().thenApply(Optional::ofNullable);
	}

	@Override
	public CompletableFuture<BEAN> getUnique() {
		final Persistor<BEAN> persistor = getPersistor();
		return get((rowEntry, count) -> {
			if (count>0) {
				throw new JpoNotUniqueResultManyResultsException("The query execution returned a number of rows different than one: more than one result found"); //$NON-NLS-1$
			}
			return persistor.beanFromResultSet(rowEntry, getIgnoredFields()).getBean();
		}).thenApply(beans -> {
			if (beans.isEmpty()) {
				throw new JpoNotUniqueResultNoResultException("The query execution returned a number of rows different than one: no results found"); //$NON-NLS-1$
			}
			return beans.get(0);
		});
	}

	@Override
	public CompletableFuture<Boolean> exist() {
		return getRowCount().thenApply(count -> count > 0);
	}

	@Override
	public CompletableFuture<List<BEAN>> getList() {
		final Persistor<BEAN> persistor = getPersistor();
		return get((rowEntry, count) -> {
			return persistor.beanFromResultSet(rowEntry, getIgnoredFields()).getBean();
		});
	}

	@Override
	public CompletableFuture<Integer> getRowCount() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForInt(getSelect().renderRowCountSql(dbType.getDBProfile()), getParams());
		});
	}

	private List<Object> getParams() {
		final List<Object> params = new ArrayList<Object>();
		sql().appendValues(params);
		return params;
	}

	@Override
	public CompletableFuture<Void> get(final RowMapper<BEAN> orm) {
		final Persistor<BEAN> persistor = getPersistor();
		return get((rowEntry, count) -> {
			orm.read(persistor.beanFromResultSet(rowEntry, getIgnoredFields()).getBean(), count);
			return null;
		}).thenAccept(action -> {});
	}
}
