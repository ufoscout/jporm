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
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.ASqlRoot;
import com.jporm.sql.query.clause.Insert;
import com.jporm.sql.query.namesolver.PropertiesProcessor;
import com.jporm.sql.query.namesolver.impl.NameSolverImpl;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;
import com.jporm.sql.query.tool.DescriptorToolMap;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class InsertImpl extends ASqlRoot implements Insert {

    private final ValuesImpl elemValues;
    private final PropertiesProcessor nameSolver;
    private final String table;

    public InsertImpl(final ClassDescriptor classDescriptor, PropertiesProcessor nameSolver, final String table, final String[] fields) {
        elemValues = new ValuesImpl<>(classDescriptor, fields);
        this.table = table;
        this.nameSolver = nameSolver;
    }

    @Override
    public final void appendValues(final List<Object> values) {
        elemValues.appendElementValues(values);
    }

    public boolean isUseGenerators() {
        return elemValues.isUseGenerators();
    }

    @Override
    public final void renderSql(final DBProfile dbprofile, final StringBuilder queryBuilder) {
        queryBuilder.append("INSERT INTO "); //$NON-NLS-1$
        queryBuilder.append(table);
        queryBuilder.append(" "); //$NON-NLS-1$
        elemValues.renderSqlElement(dbprofile, queryBuilder, nameSolver);
    }

    @Override
    public void useGenerators(final boolean useGenerators) {
        elemValues.setUseGenerators(useGenerators);
    }

    @Override
    public void values(final Object[] values) {
        elemValues.values(values);
    }
}
