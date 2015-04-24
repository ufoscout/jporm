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
package com.jporm.core.query.find.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.exception.JpoNotUniqueResultManyResultsException;
import com.jporm.commons.core.exception.JpoNotUniqueResultNoResultException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.io.RowMapper;
import com.jporm.commons.core.query.find.impl.CommonFindFromImpl;
import com.jporm.commons.core.query.find.impl.CommonFindQueryImpl;
import com.jporm.commons.core.util.GenericWrapper;
import com.jporm.core.query.find.FindQuery;
import com.jporm.core.query.find.FindQueryOrderBy;
import com.jporm.core.query.find.FindQueryWhere;
import com.jporm.core.session.SqlExecutor;
import com.jporm.persistor.BeanFromResultSet;
import com.jporm.persistor.Persistor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dialect.DBType;
import com.jporm.sql.query.clause.Select;
import com.jporm.sql.query.clause.SelectCommon;
import com.jporm.types.io.ResultSetReader;

/**
 *
 * @author Francesco Cina
 *
 * 20/giu/2011
 */
public class FindQueryImpl<BEAN> extends CommonFindQueryImpl<FindQuery<BEAN>, FindQueryWhere<BEAN>, FindQueryOrderBy<BEAN>> implements FindQuery<BEAN> {

	private final Class<BEAN> clazz;
	private final SqlExecutor sqlExecutor;
	private final ServiceCatalog serviceCatalog;
	private final DBType dbType;

	public FindQueryImpl(final ServiceCatalog serviceCatalog, final Class<BEAN> clazz, final String alias, SqlExecutor sqlExecutor, SqlFactory sqlFactory, DBType dbType) {
		super(clazz, alias, sqlFactory, serviceCatalog.getClassToolMap());
		this.serviceCatalog = serviceCatalog;
		this.clazz = clazz;
		this.sqlExecutor = sqlExecutor;
		this.dbType = dbType;
		Select select = getSelect();
		select.selectFields(getAllColumns());
		setFrom(new CommonFindFromImpl<>(select.from(), this));
		setWhere(new FindQueryWhereImpl<>(select.where(), this));
		setOrderBy(new FindQueryOrderByImpl<>(select.orderBy(), this));
	}

	@Override
	public BEAN get() throws JpoException {
		final GenericWrapper<BEAN> wrapper = new GenericWrapper<>(null);
		get((final BEAN newObject, final int rowCount) -> {
                    wrapper.setValue(newObject);
                }, 1);
		return wrapper.getValue();
	}

	@Override
	public void get(final RowMapper<BEAN> srr) throws JpoException {
		get(srr, Integer.MAX_VALUE);
	}

	@Override
	public List<BEAN> getList() {
		final List<BEAN> results = new ArrayList<>();
		get((final BEAN newObject, final int rowCount) -> {
                    results.add(newObject);
                });
		return results;
	}

	@Override
	public Optional<BEAN> getOptional() throws JpoException {
		return Optional.ofNullable(get());
	}

	@Override
	public int getRowCount() {
		final List<Object> values = new ArrayList<>();
		sql().appendValues(values);
		return sqlExecutor.queryForIntUnique(getSelect().renderRowCountSql(dbType.getDBProfile()), values);
	}

	@Override
	public BEAN getUnique() throws JpoNotUniqueResultException {
		final GenericWrapper<BEAN> wrapper = new GenericWrapper<>(null);
		get((final BEAN newObject, final int rowCount) -> {
                    if (rowCount>0) {
                        throw new JpoNotUniqueResultManyResultsException("The query execution returned a number of rows different than one: more than one result found"); //$NON-NLS-1$
                    }
                    wrapper.setValue(newObject);
                });
		if (wrapper.getValue() == null) {
			throw new JpoNotUniqueResultNoResultException("The query execution returned a number of rows different than one: no results found"); //$NON-NLS-1$
		}
		return wrapper.getValue();
	}

	private void get(final RowMapper<BEAN> srr, final int ignoreResultsMoreThan) throws JpoException {
		final List<Object> values = new ArrayList<>();
		sql().appendValues(values);
		final String sql = renderSql();
		serviceCatalog.getCacheStrategy().find(getCacheName(), sql, values, getIgnoredFields(),
				(List<BEAN> fromCacheBeans) -> {
		            for (int i = 0; i < fromCacheBeans.size(); i++) {
		                srr.read(fromCacheBeans.get(i), i);
		            }
				},
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

					sqlExecutor.query(sql, resultSetReader, values);
				});

	}

	@Override
	public SelectCommon sql() {
		return getSelect();
	}

	@Override
	public String renderSql() {
		return sql().renderSql(dbType.getDBProfile());
	}

	@Override
	public boolean exist() {
		return getRowCount()>0;
	}

}
