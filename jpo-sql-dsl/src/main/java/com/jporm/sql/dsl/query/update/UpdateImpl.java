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
package com.jporm.sql.dsl.query.update;

import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.ASql;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;
import com.jporm.sql.dsl.query.processor.TableName;
import com.jporm.sql.dsl.query.processor.TablePropertiesProcessor;
import com.jporm.sql.dsl.query.update.set.SetImpl;
import com.jporm.sql.dsl.query.update.where.UpdateWhere;
import com.jporm.sql.dsl.query.update.where.UpdateWhereImpl;
import com.jporm.sql.dsl.query.where.WhereExpressionElement;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class UpdateImpl extends ASql implements Update {

    private final PropertiesProcessor propertiesProcessor;
    private final SetImpl set;
    private final UpdateWhereImpl where;
    private final TableName tableName;

    public <T> UpdateImpl(DBProfile dbProfile, final T tableNameSource, final TablePropertiesProcessor<T> propertiesProcessor) {
        super(dbProfile);
        tableName = propertiesProcessor.getTableName(tableNameSource);
        this.propertiesProcessor = propertiesProcessor;
        where = new UpdateWhereImpl(this);
        set = new SetImpl();
    }

    @Override
    public final void sqlValues(final List<Object> values) {
        set.sqlElementValues(values);
        where.sqlElementValues(values);
    }

    @Override
    public final void sqlQuery(final DBProfile dbProfile, final StringBuilder queryBuilder) {
        queryBuilder.append("UPDATE "); //$NON-NLS-1$
        queryBuilder.append(tableName.getTable());
        queryBuilder.append(" "); //$NON-NLS-1$
        set.sqlElementQuery(queryBuilder, dbProfile, propertiesProcessor);
        where.sqlElementQuery(queryBuilder, dbProfile, propertiesProcessor);
    }

    @Override
    public UpdateWhere where() {
        return where;
    }

    @Override
    public UpdateWhere where(final List<WhereExpressionElement> expressionElements) {
        return where.and(expressionElements);
    }

    @Override
    public UpdateWhere where(final String customClause, final Object... args) {
        return where.and(customClause, args);
    }

    @Override
    public UpdateWhere where(final WhereExpressionElement... expressionElements) {
        return where.and(expressionElements);
    }

    @Override
    public Update set(String property, Object value) {
        set.eq(property, value);
        return this;
    }

}
