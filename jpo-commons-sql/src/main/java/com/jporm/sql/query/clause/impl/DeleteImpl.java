/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
import java.util.function.Function;

import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.ASqlRoot;
import com.jporm.sql.query.clause.Delete;
import com.jporm.sql.query.clause.Where;
import com.jporm.sql.query.namesolver.PropertiesProcessor;

public class DeleteImpl extends ASqlRoot implements Delete {

    private final WhereImpl where = new WhereImpl();
    private final PropertiesProcessor nameSolver;
    private final String table;

    public DeleteImpl(final String table, PropertiesProcessor propertiesProcessor) {
        this.table = table;
        nameSolver = propertiesProcessor;
    }

    public <T> DeleteImpl(final T table, PropertiesProcessor propertiesProcessor, Function<T, String> tableProcessor) {
        this.table = tableProcessor.apply(table);
        nameSolver = propertiesProcessor;
    }

    @Override
    public final void appendValues(final List<Object> values) {
        where.appendElementValues(values);
    }

    @Override
    public final void renderSql(final DBProfile dbProfile, final StringBuilder queryBuilder) {
        queryBuilder.append("DELETE FROM ");
        queryBuilder.append(table);
        queryBuilder.append(" ");
        where.renderSqlElement(dbProfile, queryBuilder, nameSolver);
    }

    @Override
    public Where where() {
        return where;
    }

}
