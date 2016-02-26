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
package com.jporm.sql.dsl.query.delete;

import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.ASql;
import com.jporm.sql.dsl.query.delete.where.DeleteWhere;
import com.jporm.sql.dsl.query.delete.where.DeleteWhereImpl;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;
import com.jporm.sql.dsl.query.processor.TableName;
import com.jporm.sql.dsl.query.processor.TablePropertiesProcessor;
import com.jporm.sql.dsl.query.where.WhereExpressionElement;

public class DeleteImpl extends ASql implements Delete {

    private final PropertiesProcessor propertiesProcessor;
    private final DeleteWhereImpl where;
    private final TableName tableName;

    public <T> DeleteImpl(DBProfile dbProfile, final T table, TablePropertiesProcessor<T> propertiesProcessor) {
        super(dbProfile);
        tableName = propertiesProcessor.getTableName(table);
        this.propertiesProcessor = propertiesProcessor;
        where = new DeleteWhereImpl(this);
    }

    @Override
    public final void sqlValues(final List<Object> values) {
        where.sqlElementValues(values);
    }

    @Override
    public final void sqlQuery(DBProfile dbProfile, final StringBuilder queryBuilder) {
        queryBuilder.append("DELETE FROM ");
        queryBuilder.append(tableName.getTable());
        queryBuilder.append(" ");
        where.sqlElementQuery(queryBuilder, dbProfile, propertiesProcessor);
    }

    @Override
    public DeleteWhere where() {
        return where;
    }

    @Override
    public DeleteWhere where(final List<WhereExpressionElement> expressionElements) {
        return where.and(expressionElements);
    }

    @Override
    public DeleteWhere where(final String customClause, final Object... args) {
        return where.and(customClause, args);
    }

    @Override
    public DeleteWhere where(final WhereExpressionElement... expressionElements) {
        return where.and(expressionElements);
    }

}
