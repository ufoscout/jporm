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
package com.jporm.commons.core.query.find;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.jporm.annotation.exception.JpoWrongPropertyNameException;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.sql.query.select.Select;
import com.jporm.sql.query.select.SelectCommon;

/**
 *
 * @author Francesco Cina
 *
 *         20/giu/2011
 */
public class CustomFindQueryBase<BEAN> implements SelectCommon {

    private final static String[] EMPTY_STRING_ARRAY = new String[0];
    private String[] fields;
    private List<String> ignoredFields = Collections.EMPTY_LIST;
    private final Select<Class<?>> select;
    private final Class<BEAN> clazz;

    public CustomFindQueryBase(final Class<BEAN> clazz, final String alias, final ClassTool<BEAN> ormClassTool, final SqlFactory sqlFactory) {
        this.clazz = clazz;
        fields = ormClassTool.getDescriptor().getAllColumnJavaNames();
        select = sqlFactory.select(this::fields).from(clazz, alias);
    }

    public final void setIgnoredFields(final String... ignoreFields) {
        if (ignoreFields.length > 0) {

            ignoredFields = Arrays.asList(ignoreFields);
            List<String> selectedColumns = new ArrayList<>();
            for (int i = 0; i < fields.length; i++) {
                selectedColumns.add(fields[i]);
            }

            selectedColumns.removeAll(ignoredFields);
            if (fields.length != (selectedColumns.size() + ignoreFields.length)) {
                throw new JpoWrongPropertyNameException("One of the specified fields is not a property of [" + clazz.getName() + "]");
            }
            fields = selectedColumns.toArray(EMPTY_STRING_ARRAY);
        }
    }

    public final List<String> getIgnoredFields() {
        return ignoredFields;
    }

    /**
     * @return the select
     */
    protected final Select<Class<?>> getSelect() {
        return select;
    }

    @Override
    public final void sqlValues(List<Object> values) {
        select.sqlValues(values);
    }

    @Override
    public final void sqlQuery(StringBuilder queryBuilder) {
        select.sqlQuery(queryBuilder);
    }

    @Override
    public final String sqlRowCountQuery() {
        return select.sqlRowCountQuery();
    }

    private String[] fields() {
        return fields;
    }

}
