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
package com.jporm.rm.query.find.impl;

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
import com.jporm.persistor.BeanFromResultSet;
import com.jporm.persistor.Persistor;
import com.jporm.rm.query.find.CustomFindQuery;
import com.jporm.rm.query.find.CustomFindQueryOrderBy;
import com.jporm.rm.query.find.CustomFindQueryWhere;
import com.jporm.rm.session.SqlExecutor;
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
public class CustomFindQueryImpl<BEAN> extends CommonFindQueryImpl<CustomFindQuery<BEAN>, CustomFindQueryWhere<BEAN>, CustomFindQueryOrderBy<BEAN>> implements CustomFindQuery<BEAN> {

	private final Class<BEAN> clazz;
	private final SqlExecutor sqlExecutor;
	private final ServiceCatalog serviceCatalog;
	private final DBType dbType;

	public CustomFindQueryImpl(final ServiceCatalog serviceCatalog, final Class<BEAN> clazz, final String alias, SqlExecutor sqlExecutor, SqlFactory sqlFactory, DBType dbType) {
		super(clazz, alias, sqlFactory, serviceCatalog.getClassToolMap());
		this.serviceCatalog = serviceCatalog;
		this.clazz = clazz;
		this.sqlExecutor = sqlExecutor;
		this.dbType = dbType;
		Select select = getSelect();
		select.selectFields(getAllColumns());
		setFrom(new CommonFindFromImpl<>(select.from(), this));
		setWhere(new CustomFindQueryWhereImpl<>(select.where(), this));
		setOrderBy(new CustomFindQueryOrderByImpl<>(select.orderBy(), this));
	}

	@Override
	public BEAN fetch() throws JpoException {
		final GenericWrapper<BEAN> wrapper = new GenericWrapper<>(null);
		get((final BEAN newObject, final int rowCount) -> {
                    wrapper.setValue(newObject);
                }, 1);
		return wrapper.getValue();
	}

	@Override
	public void fetch(final RowMapper<BEAN> srr) throws JpoException {
		get(srr, Integer.MAX_VALUE);
	}

	@Override
	public List<BEAN> fetchList() {
		final List<BEAN> results = new ArrayList<>();
		fetch((final BEAN newObject, final int rowCount) -> {
                    results.add(newObject);
                });
		return results;
	}

	@Override
	public Optional<BEAN> fetchOptional() throws JpoException {
		return Optional.ofNullable(fetch());
	}

	@Override
	public int fetchRowCount() {
		final List<Object> values = new ArrayList<>();
		sql().appendValues(values);
		return sqlExecutor.queryForIntUnique(renderRowCountSql(), values);
	}

	@Override
	public BEAN fetchUnique() throws JpoNotUniqueResultException {
		final GenericWrapper<BEAN> wrapper = new GenericWrapper<>(null);
		fetch((final BEAN newObject, final int rowCount) -> {
                    if (rowCount>0) {
                        throw new JpoNotUniqueResultManyResultsException("The query execution returned a number of rows different than one: more than one result found");
                    }
                    wrapper.setValue(newObject);
                });
		if (wrapper.getValue() == null) {
			throw new JpoNotUniqueResultNoResultException("The query execution returned a number of rows different than one: no results found");
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

					sqlExecutor.query(sql, values, resultSetReader);
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

	protected String renderRowCountSql() {
	    return getSelect().renderRowCountSql(dbType.getDBProfile());
    }

	@Override
	public boolean exist() {
		return fetchRowCount()>0;
	}

    protected Class<BEAN> getBeanClass() {
        return clazz;
    }

    protected ServiceCatalog getServiceCatalog() {
        return serviceCatalog;
    }

}
