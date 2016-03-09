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
package com.jporm.rm.query.save;

import java.util.List;

import com.jporm.commons.core.query.SqlFactory;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.query.insert.Insert;
import com.jporm.types.io.GeneratedKeyReader;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class CustomSaveQueryImpl<BEAN> implements CustomSaveQuery {

    private final SqlExecutor sqlExecutor;
    private final Insert insert;

    public CustomSaveQueryImpl(final Class<BEAN> clazz, final String[] fields, final SqlExecutor sqlExecutor, final SqlFactory sqlFactory) {
        insert = sqlFactory.insertInto(clazz, fields);
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public int execute() {
        final List<Object> values = insert.sqlValues();
        return sqlExecutor.update(sqlQuery(), values);
    }

    @Override
    public <R> R execute(GeneratedKeyReader<R> generatedKeyReader) {
        return sqlExecutor.update(sqlQuery(), insert.sqlValues(), generatedKeyReader);
    }

    @Override
    public CustomSaveQuery values(Object... values) {
        insert.values(values);
        return this;
    }

    @Override
    public void sqlValues(List<Object> values) {
        insert.sqlValues(values);
    }

    @Override
    public void sqlQuery(StringBuilder queryBuilder) {
        insert.sqlQuery(queryBuilder);
    }

}
