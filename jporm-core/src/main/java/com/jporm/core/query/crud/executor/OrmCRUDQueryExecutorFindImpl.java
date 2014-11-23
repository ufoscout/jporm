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
package com.jporm.core.query.crud.executor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.find.FindQueryOrm;
import com.jporm.core.query.find.cache.CacheStrategyCallback;
import com.jporm.core.query.find.cache.CacheStrategyEntry;
import com.jporm.exception.OrmException;
import com.jporm.persistor.BeanFromResultSet;
import com.jporm.persistor.Persistor;
import com.jporm.query.OrmRowMapper;
import com.jporm.session.ResultSetReader;
import com.jporm.session.SqlExecutor;


/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 14, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public class OrmCRUDQueryExecutorFindImpl implements OrmCRUDQueryExecutorFind {

	private final ServiceCatalog serviceCatalog;

	public OrmCRUDQueryExecutorFindImpl(final ServiceCatalog serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
	}

	@Override
	public <BEAN> int getRowCount(final FindQueryOrm<BEAN> findQuery) {
		final List<Object> values = new ArrayList<Object>();
		findQuery.appendValues(values);
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
		sqlExec.setQueryTimeout(findQuery.getQueryTimeout());
		return sqlExec.queryForIntUnique(findQuery.renderRowCountSql(), values);
	}

	@Override
	public <BEAN> void get(final FindQueryOrm<BEAN> findQuery, final Class<BEAN> clazz, final OrmRowMapper<BEAN> srr, final int firstRow, final int maxRows, final int ignoreResultsMoreThan) throws OrmException {
		final List<Object> values = new ArrayList<Object>();
		findQuery.appendValues(values);
		final String sql = serviceCatalog.getDbProfile().getQueryTemplate().paginateSQL(findQuery.renderSql(), firstRow, maxRows);
		serviceCatalog.getCacheStrategy().find(findQuery.getCacheName(), sql, values, findQuery.getIgnoredFields(), srr, new CacheStrategyCallback<BEAN>() {

			@Override
			public void doWhenNotInCache(final CacheStrategyEntry<BEAN> cacheStrategyEntry) {
				final ResultSetReader<Object> resultSetReader = new ResultSetReader<Object>() {
					@Override
					public Object read(final ResultSet resultSet) throws SQLException {
						int rowCount = 0;
						final Persistor<BEAN> ormClassTool = serviceCatalog.getClassToolMap().get(clazz).getPersistor();
						while ( resultSet.next() && (rowCount<ignoreResultsMoreThan)) {
							BeanFromResultSet<BEAN> beanFromRS = ormClassTool.beanFromResultSet(resultSet, findQuery.getIgnoredFields());
							srr.read( beanFromRS.getBean() , rowCount );
							cacheStrategyEntry.add(beanFromRS.getBean());
							rowCount++;
						}
						cacheStrategyEntry.end();
						return null;
					}

				};

				final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
				sqlExec.setQueryTimeout(findQuery.getQueryTimeout());
				sqlExec.query(sql, resultSetReader, values);
			}

		});

	}

}
