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

import com.jporm.core.inject.ClassTool;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.save.AColumnValueGenerator;
import com.jporm.core.query.save.ColumnValueGeneratorFactory;
import com.jporm.exception.OrmOptimisticLockException;
import com.jporm.introspector.mapper.clazz.ClassDescriptor;
import com.jporm.introspector.mapper.clazz.FieldDescriptor;
import com.jporm.persistor.Persistor;
import com.jporm.query.find.FindWhere;
import com.jporm.query.update.CustomUpdateQuery;
import com.jporm.query.update.CustomUpdateSet;
import com.jporm.query.update.CustomUpdateWhere;
import com.jporm.session.GeneratedKeyReader;
import com.jporm.session.SqlExecutor;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 13, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public class OrmCRUDQueryExecutorSaveOrUpdateImpl implements OrmCRUDQueryExecutorSaveOrUpdate {

	private final ServiceCatalog serviceCatalog;

	public OrmCRUDQueryExecutorSaveOrUpdateImpl(final ServiceCatalog serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <BEAN> BEAN update(final BEAN bean, final Class<BEAN> clazz, final int queryTimeout) {
		ClassTool<BEAN> ormClassTool = serviceCatalog.getClassToolMap().get(clazz);

		final Persistor<BEAN> persistor = ormClassTool.getPersistor();

		String[] pkAndVersionFieldNames = ormClassTool.getDescriptor().getPrimaryKeyAndVersionColumnJavaNames();

		// CHECK IF OBJECT HAS A 'VERSION' FIELD AND THE DATA MUST BE LOCKED BEFORE UPDATE
		if (persistor.isVersionableWithLock()) {
			FindWhere<BEAN> query = (FindWhere<BEAN>) serviceCatalog.getSession().findQuery(bean.getClass())
					.lockMode(persistor.getVersionableLockMode()).where();
			Object[] values = persistor.getPropertyValues(pkAndVersionFieldNames, bean);
			for (int i = 0; i < pkAndVersionFieldNames.length; i++) {
				query.eq(pkAndVersionFieldNames[i], values[i]);
			}
			if (query.getRowCount() == 0) {
				throw new OrmOptimisticLockException(
						"The bean of class [" + bean.getClass() + "] cannot be updated. Version in the DB is not the expected one."); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		CustomUpdateQuery updateQuery = serviceCatalog.getSession().updateQuery(bean.getClass()).timeout(queryTimeout);

		CustomUpdateWhere updateQueryWhere = updateQuery.where();
		Object[] pkAndVersionValues = persistor.getPropertyValues(pkAndVersionFieldNames, bean);
		for (int i = 0; i < pkAndVersionFieldNames.length; i++) {
			updateQueryWhere.eq(pkAndVersionFieldNames[i], pkAndVersionValues[i]);
		}

		persistor.increaseVersion(bean, false);

		CustomUpdateSet updateQuerySet = updateQuery.set();
		String[] notPks = ormClassTool.getDescriptor().getNotPrimaryKeyColumnJavaNames();
		Object[] notPkValues = persistor.getPropertyValues(notPks, bean);
		for (int i = 0; i < notPks.length; i++) {
			updateQuerySet.eq(notPks[i], notPkValues[i]);
		}

		if (updateQuery.now() == 0) {
			throw new OrmOptimisticLockException(
					"The bean of class [" + bean.getClass() + "] cannot be updated. Version in the DB is not the expected one or the ID of the bean is associated with and existing bean."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return bean;
	}

	@Override
	public <BEAN> BEAN save(final BEAN bean, final Class<BEAN> clazz, final int queryTimeout) {
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

	private <BEAN> String generateSaveQuery(final boolean useGenerator, final ClassDescriptor<BEAN> classMap) {
		final StringBuilder builder = new StringBuilder("INSERT INTO "); //$NON-NLS-1$
		builder.append(classMap.getTableInfo().getTableNameWithSchema());
		builder.append(" ("); //$NON-NLS-1$
		builder.append( columnToCommaSepareted( "" , classMap.getAllColumnJavaNames(), !useGenerator, classMap )); //$NON-NLS-1$
		builder.append(") VALUES ("); //$NON-NLS-1$
		builder.append( questionCommaSepareted( classMap.getAllColumnJavaNames(), !useGenerator, classMap ));
		builder.append(") "); //$NON-NLS-1$
		return builder.toString();
	}

	private <BEAN> String questionCommaSepareted(final String[] fieldNames, final boolean ignoreGenerators, final ClassDescriptor<BEAN> classMap) {
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

	private <BEAN> String columnToCommaSepareted(final String prefix, final String[] fieldNames, final boolean ignoreGenerators, final ClassDescriptor<BEAN> classMap) {
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
