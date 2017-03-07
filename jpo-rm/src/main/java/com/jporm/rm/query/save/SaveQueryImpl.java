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
package com.jporm.rm.query.save;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.query.save.SaveQueryBase;
import com.jporm.persistor.generator.Persistor;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class SaveQueryImpl<BEAN> extends SaveQueryBase<BEAN> implements SaveQuery<BEAN> {

	private final Collection<BEAN> beans;
	private final SqlExecutor sqlExecutor;
	private final ClassTool<BEAN> ormClassTool;

	public SaveQueryImpl(final Collection<BEAN> beans, final Class<BEAN> clazz, final ClassTool<BEAN> ormClassTool, final SqlCache sqlCache, final SqlExecutor sqlExecutor,
			final SqlFactory sqlFactory, DBProfile dbProfile) {
		super(clazz, sqlCache);
		this.beans = beans;
		this.ormClassTool = ormClassTool;
		this.sqlExecutor = sqlExecutor;
	}

	@Override
	public List<BEAN> execute() {
		final List<BEAN> result = new ArrayList<>();
		for (final BEAN bean : beans) {
			result.add(save(ormClassTool.getPersistor().clone(bean)));
		}
		return result;
	}

	private BEAN save(final BEAN bean) {

		final Persistor<BEAN> persistor = ormClassTool.getPersistor();

		// CHECK IF OBJECT HAS A 'VERSION' FIELD and increase it
		final BEAN updatedBean = persistor.increaseVersion(bean, true);
		final boolean useGenerator = ormClassTool.getPersistor().useGenerators(updatedBean);
		final String sql = getCacheableQuery(useGenerator);
		if (!useGenerator) {
			final String[] keys = ormClassTool.getDescriptor().getAllColumnJavaNames();
			sqlExecutor.update(sql, statement -> persistor.setBeanValuesToStatement(keys, updatedBean, statement, 0));
			return updatedBean;
		} else {
			final GeneratedKeyReader<BEAN> generatedKeyExtractor = GeneratedKeyReader.get(ormClassTool.getDescriptor().getAllGeneratedColumnDBNames(),
					(final ResultSet generatedKeyResultSet, Integer affectedRows) -> {
						BEAN result = updatedBean;
						if (generatedKeyResultSet.hasNext()) {
							result = persistor.updateGeneratedValues(generatedKeyResultSet.next(), result);
						}
						return result;
					});
			final String[] keys = ormClassTool.getDescriptor().getAllNotGeneratedColumnJavaNames();
			return sqlExecutor.update(sql, statement -> persistor.setBeanValuesToStatement(keys, updatedBean, statement, 0), generatedKeyExtractor);
		}

	}

}
