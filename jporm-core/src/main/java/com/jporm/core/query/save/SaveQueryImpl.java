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
package com.jporm.core.query.save;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.jporm.core.inject.ClassTool;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.save.generator.AColumnValueGenerator;
import com.jporm.core.query.save.generator.ColumnValueGeneratorFactory;
import com.jporm.introspector.mapper.clazz.ClassDescriptor;
import com.jporm.introspector.mapper.clazz.FieldDescriptor;
import com.jporm.persistor.Persistor;
import com.jporm.session.GeneratedKeyReader;
import com.jporm.session.SqlExecutor;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class SaveQueryImpl<BEAN> implements SaveQuery<BEAN> {

	private int _queryTimeout = 0;
	private final Class<BEAN> clazz;
	private final Stream<BEAN> updatedBeans;
	private final ServiceCatalog serviceCatalog;
	private boolean executed = false;

	public SaveQueryImpl(final Stream<BEAN> beans, Class<BEAN> clazz, final ServiceCatalog serviceCatalog) {
		final ClassTool<BEAN> ormClassTool = serviceCatalog.getClassToolMap().get(clazz);
		Persistor<BEAN> persistor = ormClassTool.getPersistor();
		this.updatedBeans = beans.map(bean -> persistor.clone(bean));
		this.serviceCatalog = serviceCatalog;
		this.clazz = clazz;
	}

	@Override
	public Stream<BEAN> now() {
		executed = true;
		int deprecatedCode;
		return updatedBeans.map(bean -> save(bean, clazz, _queryTimeout));
	}

	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed ;
	}

	//TODO to refactor everything from here
	@Deprecated
	private BEAN save(final BEAN bean, final Class<BEAN> clazz, final int queryTimeout) {
		final ClassTool<BEAN> ormClassTool = serviceCatalog.getClassToolMap().get(clazz);

		final Persistor<BEAN> persistor = ormClassTool.getPersistor();
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
		sqlExec.setTimeout(queryTimeout);

		//CHECK IF OBJECT HAS A 'VERSION' FIELD and increase it
		persistor.increaseVersion(bean, true);
		boolean useGenerator = ormClassTool.getPersistor().useGenerators(bean);
		String sql = generateSaveQuery(useGenerator, ormClassTool.getDescriptor());
		if (!useGenerator) {
			String[] keys = ormClassTool.getDescriptor().getAllColumnJavaNames();
			Object[] values = persistor.getPropertyValues(keys, bean);
			sqlExec.update(sql, values);
		} else {
			final GeneratedKeyReader generatedKeyExtractor = new GeneratedKeyReader() {

				@Override
				public void read(final ResultSet generatedKeyResultSet) throws SQLException {
					if (generatedKeyResultSet.next()) {
						persistor.updateGeneratedValues(generatedKeyResultSet, bean);
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

	@Deprecated
	private String generateSaveQuery(final boolean useGenerator, final ClassDescriptor<BEAN> classMap) {
		final StringBuilder builder = new StringBuilder("INSERT INTO "); //$NON-NLS-1$
		builder.append(classMap.getTableInfo().getTableNameWithSchema());
		builder.append(" ("); //$NON-NLS-1$
		builder.append( columnToCommaSepareted( "" , classMap.getAllColumnJavaNames(), !useGenerator, classMap )); //$NON-NLS-1$
		builder.append(") VALUES ("); //$NON-NLS-1$
		builder.append( questionCommaSepareted( classMap.getAllColumnJavaNames(), !useGenerator, classMap ));
		builder.append(") "); //$NON-NLS-1$
		return builder.toString();
	}

	@Deprecated
	private String questionCommaSepareted(final String[] fieldNames, final boolean ignoreGenerators, final ClassDescriptor<BEAN> classMap) {
		List<String> queryParameters = new ArrayList<String>();
		boolean generatedKey = false;
		for (int i=0; i<fieldNames.length ; i++) {
			FieldDescriptor<BEAN, ?> classField = classMap.getFieldDescriptorByJavaName(fieldNames[i]);
			final AColumnValueGenerator columnValueGenerator = ColumnValueGeneratorFactory.getColumnValueGenerator( classField, serviceCatalog.getDbProfile(), ignoreGenerators );
			generatedKey = generatedKey || columnValueGenerator.isAutoGenerated();
			final String queryParameter = columnValueGenerator.insertQueryParameter( "?"); //$NON-NLS-1$
			if (queryParameter.length()>0) {
				queryParameters.add(queryParameter);
			}
		}
		return toQueryString(queryParameters);
	}

	@Deprecated
	private String columnToCommaSepareted(final String prefix, final String[] fieldNames, final boolean ignoreGenerators, final ClassDescriptor<BEAN> classMap) {
		List<String> queryParameters = new ArrayList<String>();
		for (int i=0; i<(fieldNames.length) ; i++) {
			FieldDescriptor<BEAN, ?> classField = classMap.getFieldDescriptorByJavaName(fieldNames[i]);
			final AColumnValueGenerator columnValueGenerator = ColumnValueGeneratorFactory.getColumnValueGenerator( classField, serviceCatalog.getDbProfile(), ignoreGenerators );
			final String queryParameter = columnValueGenerator.insertColumn(prefix + classField.getColumnInfo().getDBColumnName());
			if (queryParameter.length()>0) {
				queryParameters.add(queryParameter);
			}
		}
		return toQueryString(queryParameters);
	}

	@Deprecated
	private String toQueryString(final List<String> queryParameters) {
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<queryParameters.size(); i++) {
			builder.append( queryParameters.get(i) );
			if (i != (queryParameters.size() - 1)) {
				builder.append(", "); //$NON-NLS-1$
			}
		}
		return builder.toString();
	}

}
