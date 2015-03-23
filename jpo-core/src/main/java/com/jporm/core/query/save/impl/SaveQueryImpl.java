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
package com.jporm.core.query.save.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

import com.jporm.cache.Cache;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.core.query.save.SaveQuery;
import com.jporm.core.session.GeneratedKeyReader;
import com.jporm.core.session.Session;
import com.jporm.core.session.SqlExecutor;
import com.jporm.persistor.Persistor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.query.clause.Insert;
import com.jporm.sql.query.clause.Values;
import com.jporm.types.JdbcResultSet;


/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class SaveQueryImpl<BEAN> implements SaveQuery<BEAN> {

	private final Class<BEAN> clazz;
	private final Stream<BEAN> updatedBeans;
	private final ServiceCatalog<Session> serviceCatalog;
	private final ClassTool<BEAN> ormClassTool;
	private final SqlFactory sqlFactory;
	private boolean executed = false;

	public SaveQueryImpl(final Stream<BEAN> beans, Class<BEAN> clazz, final ServiceCatalog<Session> serviceCatalog, SqlFactory sqlFactory) {
		this.sqlFactory = sqlFactory;
		ormClassTool = serviceCatalog.getClassToolMap().get(clazz);
		Persistor<BEAN> persistor = ormClassTool.getPersistor();
		this.updatedBeans = beans.map(bean -> persistor.clone(bean));
		this.serviceCatalog = serviceCatalog;
		this.clazz = clazz;
	}

	@Override
	public Stream<BEAN> now() {
		executed = true;
		return updatedBeans.map(bean -> save(bean));
	}

	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed ;
	}

	private BEAN save(final BEAN bean) {

		final Persistor<BEAN> persistor = ormClassTool.getPersistor();
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();

		//CHECK IF OBJECT HAS A 'VERSION' FIELD and increase it
		persistor.increaseVersion(bean, true);
		boolean useGenerator = ormClassTool.getPersistor().useGenerators(bean);
		String sql = getQuery(useGenerator);
		if (!useGenerator) {
			String[] keys = ormClassTool.getDescriptor().getAllColumnJavaNames();
			Object[] values = persistor.getPropertyValues(keys, bean);
			sqlExec.update(sql, values);
		} else {
			final GeneratedKeyReader generatedKeyExtractor = new GeneratedKeyReader() {

				@Override
				public void read(final ResultSet generatedKeyResultSet) throws SQLException {
					JdbcResultSet jdbcResultSet = new JdbcResultSet(generatedKeyResultSet);
					if (jdbcResultSet.next()) {
						persistor.updateGeneratedValues(jdbcResultSet, bean);
					}
				}

				@Override
				public String[] generatedColumnNames() {
					return ormClassTool.getDescriptor().getAllGeneratedColumnDBNames();
				}
			};
			String[] keys = ormClassTool.getDescriptor().getAllNotGeneratedColumnJavaNames();
			Object[] values = persistor.getPropertyValues(keys, bean);
			sqlExec.update(sql, generatedKeyExtractor, values);
		}
		return bean;

	}

	private String getQuery(final boolean useGenerator) {

		Cache<Class<?>, String> cache = null;
		if (useGenerator) {
			cache = serviceCatalog.getSqlCache().saveWithGenerators();
		} else {
			cache = serviceCatalog.getSqlCache().saveWithoutGenerators();
		}

		return cache.get(clazz, key -> {
			Insert insert = sqlFactory.insert(clazz);
			insert.useGenerators(useGenerator);
			Values queryValues = insert.values();
			String[] fields = ormClassTool.getDescriptor().getAllColumnJavaNames();
			for (String field : fields) {
				queryValues.eq(field, "");
			}
			return insert.renderSql();
		});

	}

}
