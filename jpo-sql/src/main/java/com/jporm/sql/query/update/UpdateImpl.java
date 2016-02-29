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
package com.jporm.sql.query.update;

import java.util.List;

import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.processor.TableName;
import com.jporm.sql.query.processor.TablePropertiesProcessor;
import com.jporm.sql.query.update.set.SetImpl;
import com.jporm.sql.query.update.where.UpdateWhere;
import com.jporm.sql.query.update.where.UpdateWhereImpl;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class UpdateImpl implements Update {

    private final PropertiesProcessor propertiesProcessor;
    private final SetImpl set;
    private final UpdateWhereImpl where;
    private final TableName tableName;
    private DBProfile dbProfile;

    public <T> UpdateImpl(DBProfile dbProfile, final T tableNameSource, final TablePropertiesProcessor<T> propertiesProcessor) {
        this.dbProfile = dbProfile;
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
    public final void sqlQuery(final StringBuilder queryBuilder) {
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
    public Update set(String property, Object value) {
        set.eq(property, value);
        return this;
    }

}
