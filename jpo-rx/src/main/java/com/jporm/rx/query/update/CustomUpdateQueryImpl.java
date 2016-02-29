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
package com.jporm.rx.query.update;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.query.SqlFactory;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.query.update.Update;
import com.jporm.sql.query.where.Where;
import com.jporm.sql.query.where.WhereDefault;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class CustomUpdateQueryImpl implements CustomUpdateQuery, CustomUpdateQueryWhere, WhereDefault<CustomUpdateQueryWhere> {

    private final SqlExecutor sqlExecutor;
    private final Update update;

    public CustomUpdateQueryImpl(final Class<?> clazz, final SqlExecutor sqlExecutor, final SqlFactory sqlFactory) {
        this.sqlExecutor = sqlExecutor;
        update = sqlFactory.update(clazz);
    }

    @Override
    public CompletableFuture<UpdateResult> execute() {
        return sqlExecutor.update(sqlQuery(), sqlValues());
    }

    @Override
    public CustomUpdateQueryWhere where() {
        return this;
    }

    @Override
    public Where<?> whereImplementation() {
        return update.where();
    }

    @Override
    public void sqlValues(List<Object> values) {
        update.sqlValues(values);
    }

    @Override
    public void sqlQuery(StringBuilder queryBuilder) {
        update.sqlQuery(queryBuilder);
    }

    @Override
    public CustomUpdateQuery set(String property, Object value) {
        update.set(property, value);
        return this;
    }

}
