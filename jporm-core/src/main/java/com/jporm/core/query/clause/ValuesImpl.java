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
package com.jporm.core.query.clause;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jporm.annotation.mapper.clazz.FieldDescriptor;
import com.jporm.core.inject.ClassTool;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.AQuerySubElement;
import com.jporm.core.query.save.generator.AColumnValueGenerator;
import com.jporm.core.query.save.generator.ColumnValueGeneratorFactory;
import com.jporm.query.clause.Values;
import com.jporm.query.namesolver.NameSolver;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public abstract class ValuesImpl<BEAN, T extends Values<?>> extends AQuerySubElement implements Values<T> {

	private Map<String, Object> properties = new LinkedHashMap<>();
	private List<String> generatedFields = new ArrayList<String>();

	private int version = 0;
	private boolean useGenerators = true;

	private final ClassTool<BEAN> classTool;
	private final ServiceCatalog serviceCatalog;

	public ValuesImpl(Class<BEAN> clazz, final ServiceCatalog serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
		classTool = serviceCatalog.getClassToolMap().get(clazz);
	}

	protected abstract T values();

	@Override
	public T eq(final String property, final Object value) {
		version++;
		properties.put(property, value);
		return values();
	}

	@Override
	public final void renderSqlElement(final StringBuilder queryBuilder, final NameSolver nameSolver) {
		updateGeneratedPropertiesIfNeeded();
		queryBuilder.append("(");
		Set<String> propertyNames = new LinkedHashSet<>();
		propertyNames.addAll(generatedFields);
		propertyNames.addAll(properties.keySet());
		queryBuilder.append( columnToCommaSepareted( propertyNames ) );
		queryBuilder.append(") VALUES (");
		queryBuilder.append( questionCommaSepareted( propertyNames ));
		queryBuilder.append(") ");
	}

	@Override
	public final void appendElementValues(final List<Object> values) {
		values.addAll(properties.values());
	}

	@Override
	public final int getElementStatusVersion() {
		return version;
	}


	private String questionCommaSepareted(final Set<String> fieldNames) {
		List<String> queryParameters = new ArrayList<String>();
		boolean generatedKey = false;

		for (String field : fieldNames) {
			FieldDescriptor<BEAN, ?> classField = classTool.getDescriptor().getFieldDescriptorByJavaName(field);
			final AColumnValueGenerator columnValueGenerator = ColumnValueGeneratorFactory.getColumnValueGenerator( classField, serviceCatalog.getDbProfile(), !useGenerators );
			generatedKey = generatedKey || columnValueGenerator.isAutoGenerated();
			final String queryParameter = columnValueGenerator.insertQueryParameter( "?"); //$NON-NLS-1$
			if (queryParameter.length()>0) {
				queryParameters.add(queryParameter);
			}
		}
		return toQueryString(queryParameters);
	}

	private String columnToCommaSepareted(final Set<String> fieldNames) {
		List<String> queryParameters = new ArrayList<String>();
		for (String field : fieldNames) {
			FieldDescriptor<BEAN, ?> classField = classTool.getDescriptor().getFieldDescriptorByJavaName(field);
			final AColumnValueGenerator columnValueGenerator = ColumnValueGeneratorFactory.getColumnValueGenerator( classField, serviceCatalog.getDbProfile(), !useGenerators );
			final String queryParameter = columnValueGenerator.insertColumn(classField.getColumnInfo().getDBColumnName());
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

	public boolean isUseGenerators() {
		version++;
		return useGenerators;
	}

	public void setUseGenerators(boolean useGenerators) {
		this.useGenerators = useGenerators;
	}

	private void updateGeneratedPropertiesIfNeeded() {
		if(useGenerators) {
			for (String generatedField : classTool.getDescriptor().getAllGeneratedColumnJavaNames() ) {
				generatedFields.add(generatedField);
				properties.remove(generatedField);
			}
		}
	}

}
