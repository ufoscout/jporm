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

import java.util.List;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.insert.Insert;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
@Deprecated
public class InsertImpl implements Insert {

    private final ValuesImpl elemValues;
    private final PropertiesProcessor nameSolver;
    private final String table;
    private final DBProfile dbProfile;

    public InsertImpl(final DBProfile dbProfile, final ClassDescriptor classDescriptor, PropertiesProcessor nameSolver, final String table, final String[] fields) {
        elemValues = new ValuesImpl<>(this, classDescriptor, fields);
        this.table = table;
        this.nameSolver = nameSolver;
        this.dbProfile = dbProfile;
    }

    @Override
    public final void sqlValues(final List<Object> values) {
        elemValues.sqlElementValues(values);
    }

    public boolean isUseGenerators() {
        return elemValues.isUseGenerators();
    }

    @Override
    public final void sqlQuery(final StringBuilder queryBuilder) {
        queryBuilder.append("INSERT INTO "); //$NON-NLS-1$
        queryBuilder.append(table);
        queryBuilder.append(" "); //$NON-NLS-1$
        elemValues.sqlElementQuery(queryBuilder, dbProfile, nameSolver);
    }

    public void useGenerators(final boolean useGenerators) {
        elemValues.setUseGenerators(useGenerators);
    }

    @Override
    public Insert values(final Object... values) {
        return elemValues.values(values);
    }
}
