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
package com.jporm.sql.query.delete;

import java.util.List;

import com.jporm.sql.dialect.SqlDeleteRender;
import com.jporm.sql.query.delete.where.DeleteWhereImpl;
import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.processor.TableName;
import com.jporm.sql.query.processor.TablePropertiesProcessor;

public class DeleteImpl implements Delete {

    private final PropertiesProcessor propertiesProcessor;
    private final DeleteWhereImpl where;
    private final TableName tableName;
    private final SqlDeleteRender deleteRender;

    public <T> DeleteImpl(SqlDeleteRender deleteRender, final T table, TablePropertiesProcessor<T> propertiesProcessor) {
        this.deleteRender = deleteRender;
        tableName = propertiesProcessor.getTableName(table);
        this.propertiesProcessor = propertiesProcessor;
        where = new DeleteWhereImpl(this);
    }

    @Override
    public final void sqlValues(final List<Object> values) {
        where.sqlElementValues(values);
    }

    @Override
    public final void sqlQuery(final StringBuilder queryBuilder) {
        deleteRender.render(this, queryBuilder, propertiesProcessor);
    }

    @Override
    public DeleteWhereImpl where() {
        return where;
    }

    public TableName getTableName() {
        return tableName;
    }

}
