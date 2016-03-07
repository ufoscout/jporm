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

import com.jporm.sql.dialect.SqlUpdateRender;
import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.processor.TableName;
import com.jporm.sql.query.processor.TablePropertiesProcessor;
import com.jporm.sql.query.update.set.CaseWhen;
import com.jporm.sql.query.update.set.SetImpl;
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
    private final SqlUpdateRender updateRender;

    public <T> UpdateImpl(SqlUpdateRender updateRender, final T tableNameSource, final TablePropertiesProcessor<T> propertiesProcessor) {
        this.updateRender = updateRender;
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
        updateRender.render(this, queryBuilder, propertiesProcessor);
    }

    @Override
    public UpdateWhereImpl where() {
        return where;
    }

    @Override
    public Update set(String property, Object value) {
        set.eq(property, value);
        return this;
    }

    /**
     * @return the set
     */
    public SetImpl getSet() {
        return set;
    }

    /**
     * @return the tableName
     */
    public TableName getTableName() {
        return tableName;
    }

    @Override
    public Update set(String property, CaseWhen caseWhen) {
        set.eq(property, caseWhen);
        return this;
    }

}
