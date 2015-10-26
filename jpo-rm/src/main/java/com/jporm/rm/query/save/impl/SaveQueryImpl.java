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
package com.jporm.rm.query.save.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.save.ASaveQuery;
import com.jporm.persistor.Persistor;
import com.jporm.rm.query.save.SaveQuery;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dialect.DBType;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;


/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class SaveQueryImpl<BEAN> extends ASaveQuery<BEAN> implements SaveQuery<BEAN> {

	private final Collection<BEAN> beans;
	private final SqlExecutor sqlExecutor;
	private final DBType dbType;

	public SaveQueryImpl(final Collection<BEAN> beans, Class<BEAN> clazz, final ServiceCatalog serviceCatalog, SqlExecutor sqlExecutor, SqlFactory sqlFactory, DBType dbType) {
		super(serviceCatalog.getClassToolMap().get(clazz), clazz, serviceCatalog.getSqlCache(), sqlFactory);
		this.beans = beans;
		this.sqlExecutor = sqlExecutor;
		this.dbType = dbType;
	}

	@Override
	public List<BEAN> execute() {
		List<BEAN> result = new ArrayList<>();
		for (BEAN bean : beans) {
			result.add(save(getOrmClassTool().getPersistor().clone(bean)));
		}
		return result;
	}

	private BEAN save(final BEAN bean) {

		final Persistor<BEAN> persistor = getOrmClassTool().getPersistor();

		//CHECK IF OBJECT HAS A 'VERSION' FIELD and increase it
		persistor.increaseVersion(bean, true);
		boolean useGenerator = getOrmClassTool().getPersistor().useGenerators(bean);
		String sql = getQuery(dbType.getDBProfile(), useGenerator);
		if (!useGenerator) {
			String[] keys = getOrmClassTool().getDescriptor().getAllColumnJavaNames();
			Object[] values = persistor.getPropertyValues(keys, bean);
			sqlExecutor.update(sql, values);
		} else {
			final GeneratedKeyReader generatedKeyExtractor = new GeneratedKeyReader() {
				@Override
				public void read(final ResultSet generatedKeyResultSet) {
					if (generatedKeyResultSet.next()) {
						persistor.updateGeneratedValues(generatedKeyResultSet, bean);
					}
				}

				@Override
				public String[] generatedColumnNames() {
					return getOrmClassTool().getDescriptor().getAllGeneratedColumnDBNames();
				}
			};
			String[] keys = getOrmClassTool().getDescriptor().getAllNotGeneratedColumnJavaNames();
			Object[] values = persistor.getPropertyValues(keys, bean);
			sqlExecutor.update(sql, values, generatedKeyExtractor);
		}
		return bean;

	}

}
