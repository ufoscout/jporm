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
package com.jporm.sql.query.insert;

import java.util.List;

import com.jporm.sql.dialect.SqlInsertRender;
import com.jporm.sql.query.insert.values.ValuesImpl;
import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.processor.TableName;
import com.jporm.sql.query.processor.TablePropertiesProcessor;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class InsertImpl<T> implements Insert {

    private final PropertiesProcessor propertiesProcessor;
    private final ValuesImpl elemValues;
    private final TableName tableName;
    private final SqlInsertRender insertRender;

    public InsertImpl(SqlInsertRender insertRender, final String[] fields, final T table, TablePropertiesProcessor<T> propertiesProcessor) {
        this.insertRender = insertRender;
        tableName = propertiesProcessor.getTableName(table);
        this.propertiesProcessor = propertiesProcessor;
        elemValues = new ValuesImpl(this, fields);
    }

    @Override
    public final void sqlValues(final List<Object> values) {
        elemValues.sqlElementValues(values);
    }

    @Override
    public final void sqlQuery(final StringBuilder queryBuilder) {
        insertRender.render(this, queryBuilder, propertiesProcessor);
    }

    @Override
    public Insert values(final Object... values) {
        elemValues.values(values);
        return this;
    }

    /**
     * @return the tableName
     */
    public TableName getTableName() {
        return tableName;
    }

    /**
     * @return the elemValues
     */
    public ValuesImpl getElemValues() {
        return elemValues;
    }

}
