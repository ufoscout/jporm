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

import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.delete.where.DeleteWhere;
import com.jporm.sql.query.delete.where.DeleteWhereImpl;
import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.processor.TableName;
import com.jporm.sql.query.processor.TablePropertiesProcessor;

public class DeleteImpl implements Delete {

    private final PropertiesProcessor propertiesProcessor;
    private final DeleteWhereImpl where;
    private final TableName tableName;
    private final DBProfile dbProfile;

    public <T> DeleteImpl(DBProfile dbProfile, final T table, TablePropertiesProcessor<T> propertiesProcessor) {
        this.dbProfile = dbProfile;
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
        queryBuilder.append("DELETE FROM ");
        queryBuilder.append(tableName.getTable());
        queryBuilder.append(" ");
        where.sqlElementQuery(queryBuilder, propertiesProcessor);
    }

    @Override
    public DeleteWhere where() {
        return where;
    }

}
