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
package com.jporm.sql.query.clause.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.FieldDescriptor;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.ASqlSubElement;
import com.jporm.sql.query.clause.Values;
import com.jporm.sql.query.clause.impl.value.AColumnValueGenerator;
import com.jporm.sql.query.clause.impl.value.ColumnValueGeneratorFactory;
import com.jporm.sql.query.namesolver.PropertiesProcessor;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class ValuesImpl<BEAN> extends ASqlSubElement implements Values {

    private final String[] fields;
    private final List<Object[]> values = new ArrayList<>();
    private List<String> generatedFields = new ArrayList<>();

    private boolean useGenerators = true;

    private final ClassDescriptor<BEAN> classDescriptor;

    public ValuesImpl(final ClassDescriptor<BEAN> classDescriptor, final String[] fields) {
        this.classDescriptor = classDescriptor;
        this.fields = fields;
    }

    @Override
    public final void appendElementValues(final List<Object> values) {
        this.values.forEach(valueSet -> {
            for (Object value : valueSet) {
                values.add(value);
            }
        });
    }

    private String columnToCommaSepareted(final DBProfile dbProfile, final Set<String> fieldNames) {
        List<String> queryParameters = new ArrayList<String>();
        for (String field : fieldNames) {
            FieldDescriptor<BEAN, ?> classField = classDescriptor.getFieldDescriptorByJavaName(field);
            final AColumnValueGenerator columnValueGenerator = ColumnValueGeneratorFactory.getColumnValueGenerator(classField, dbProfile, !useGenerators);
            final String queryParameter = columnValueGenerator.insertColumn(classField.getColumnInfo().getDBColumnName());
            if (queryParameter.length() > 0) {
                queryParameters.add(queryParameter);
            }
        }
        return toQueryString(queryParameters);
    }

    public boolean isUseGenerators() {
        return useGenerators;
    }

    private String questionCommaSepareted(final DBProfile dbProfile, final Set<String> fieldNames) {
        List<String> queryParameters = new ArrayList<String>();

        for (String field : fieldNames) {
            FieldDescriptor<BEAN, ?> classField = classDescriptor.getFieldDescriptorByJavaName(field);
            final AColumnValueGenerator columnValueGenerator = ColumnValueGeneratorFactory.getColumnValueGenerator(classField, dbProfile, !useGenerators);
            final String queryParameter = columnValueGenerator.insertQueryParameter("?"); //$NON-NLS-1$
            if (queryParameter.length() > 0) {
                queryParameters.add(queryParameter);
            }
        }
        return toQueryString(queryParameters);
    }

    @Override
    public final void renderSqlElement(final DBProfile dbprofile, final StringBuilder queryBuilder, final PropertiesProcessor nameSolver) {
        updateGeneratedPropertiesIfNeeded();
        queryBuilder.append("(");
        Set<String> propertyNames = new LinkedHashSet<>();
        propertyNames.addAll(generatedFields);
        for (String field : fields) {
            propertyNames.add(field);
        }
        queryBuilder.append(columnToCommaSepareted(dbprofile, propertyNames));
        queryBuilder.append(") VALUES ");
        Iterator<Object[]> iterator = values.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            queryBuilder.append("(");
            queryBuilder.append(questionCommaSepareted(dbprofile, propertyNames));
            if (iterator.hasNext()) {
                queryBuilder.append("), ");
            } else {
                queryBuilder.append(") ");
            }
        }
    }

    public void setUseGenerators(final boolean useGenerators) {
        this.useGenerators = useGenerators;
    }

    private String toQueryString(final List<String> queryParameters) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < queryParameters.size(); i++) {
            builder.append(queryParameters.get(i));
            if (i != (queryParameters.size() - 1)) {
                builder.append(", "); //$NON-NLS-1$
            }
        }
        return builder.toString();
    }

    private void updateGeneratedPropertiesIfNeeded() {
        if (useGenerators) {
            for (String generatedField : classDescriptor.getAllGeneratedColumnJavaNames()) {
                generatedFields.add(generatedField);
            }
        }
    }

    @Override
    public Values values(final Object[] values) {
        this.values.add(values);
        return this;
    }

}
