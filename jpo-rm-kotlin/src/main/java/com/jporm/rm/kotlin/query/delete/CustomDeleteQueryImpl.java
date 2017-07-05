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
package com.jporm.rm.kotlin.query.delete;

import com.jporm.rm.kotlin.session.SqlExecutor;
import com.jporm.sql.query.delete.Delete;
import com.jporm.sql.query.where.Where;
import com.jporm.sql.query.where.WhereDefault;

import java.util.List;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class CustomDeleteQueryImpl implements
        CustomDeleteQuery,
        CustomDeleteQueryWhere, WhereDefault<CustomDeleteQueryWhere> {

    private final SqlExecutor sqlExecutor;
    private final Delete sqlDelete;

    public CustomDeleteQueryImpl(Delete sqlDelete, final SqlExecutor sqlExecutor) {
        this.sqlDelete = sqlDelete;
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public int execute() {
        final List<Object> values = sqlDelete.sqlValues();
        return sqlExecutor.update(sqlQuery(), values);
    }

    @Override
    public CustomDeleteQueryWhere where() {
        return this;
    }

    @Override
    public void sqlValues(List<Object> values) {
        sqlDelete.sqlValues(values);
    }

    @Override
    public void sqlQuery(StringBuilder queryBuilder) {
        sqlDelete.sqlQuery(queryBuilder);
    }

    @Override
    public Where<?> whereImplementation() {
        return sqlDelete.where();
    }

}

