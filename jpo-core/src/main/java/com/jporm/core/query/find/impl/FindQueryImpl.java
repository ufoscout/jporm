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
import com.jporm.commons.core.query.find.impl.CommonFindFromImpl;
import com.jporm.commons.core.query.find.impl.CommonFindQueryImpl;
import com.jporm.commons.core.util.GenericWrapper;
import com.jporm.core.query.OrmRowMapper;
import com.jporm.core.query.ResultSetReader;
import com.jporm.core.query.find.FindQuery;
import com.jporm.core.query.find.FindQueryOrderBy;
import com.jporm.core.query.find.FindQueryWhere;
import com.jporm.core.session.Session;
import com.jporm.core.session.SqlExecutor;
import com.jporm.persistor.BeanFromResultSet;
import com.jporm.persistor.Persistor;
import com.jporm.sql.query.clause.Select;
import com.jporm.types.JdbcResultSet;
import com.jporm.types.ResultSet;

/**
 *
 * @author Francesco Cina
 *
 * 20/giu/2011
 */
public class FindQueryImpl<BEAN> extends CommonFindQueryImpl<FindQuery<BEAN>, FindQueryWhere<BEAN>, FindQueryOrderBy<BEAN>> implements FindQuery<BEAN> {

	private ServiceCatalog<Session> serviceCatalog;
	private Class<BEAN> clazz;

	public FindQueryImpl(final ServiceCatalog<Session> serviceCatalog, final Class<BEAN> clazz, final String alias) {
		super(serviceCatalog, clazz, alias);
		this.serviceCatalog = serviceCatalog;
		this.clazz = clazz;
		Select select = getSelect();
		select.selectFields(getAllColumns());
		setFrom(new CommonFindFromImpl<>(select.from(), this));
		setWhere(new FindQueryWhereImpl<>(select.where(), this));
		setOrderBy(new FindQueryOrderByImpl<>(select.orderBy(), this));
	}

	@Override
	public BEAN get() throws JpoException {
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
	public void get(final OrmRowMapper<BEAN> srr) throws JpoException {
		get(srr, Integer.MAX_VALUE);
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

	@Override
	public Optional<BEAN> getOptional() throws JpoException {
		return Optional.ofNullable(get());
	}

	@Override
	public int getRowCount() {
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
		return sqlExec.queryForIntUnique(renderRowCountSql(), values);
	}

	@Override
	public BEAN getUnique() throws JpoNotUniqueResultException {
		final GenericWrapper<BEAN> wrapper = new GenericWrapper<BEAN>(null);
		OrmRowMapper<BEAN> srr = new OrmRowMapper<BEAN>() {
			@Override
			public void read(final BEAN newObject, final int rowCount) {
				if (rowCount>0) {
					throw new JpoNotUniqueResultManyResultsException("The query execution returned a number of rows different than one: more than one result found"); //$NON-NLS-1$
				}
				wrapper.setValue(newObject);
			}
		};
		get(srr);
		if (wrapper.getValue() == null) {
			throw new JpoNotUniqueResultNoResultException("The query execution returned a number of rows different than one: no results found"); //$NON-NLS-1$
		}
		return wrapper.getValue();
	}

	@Override
	public boolean exist() {
		return getRowCount()>0;
	}

	private void get(final OrmRowMapper<BEAN> srr, final int ignoreResultsMoreThan) throws JpoException {
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
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
						ResultSet jdbcResultSet = new JdbcResultSet(resultSet);
						while ( jdbcResultSet.next() && (rowCount<ignoreResultsMoreThan)) {
							BeanFromResultSet<BEAN> beanFromRS = ormClassTool.beanFromResultSet(jdbcResultSet, getIgnoredFields());
							srr.read( beanFromRS.getBean() , rowCount );
							cacheStrategyEntry.add(beanFromRS.getBean());
							rowCount++;
						}
						cacheStrategyEntry.end();
						return null;
					};

					final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
					sqlExec.query(sql, resultSetReader, values);
				});

	}

}
