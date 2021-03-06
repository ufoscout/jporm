/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.rm.session;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rm.query.delete.CustomDeleteQuery;
import com.jporm.rm.query.delete.CustomDeleteQueryImpl;
import com.jporm.rm.query.find.SelectQueryBuilder;
import com.jporm.rm.query.find.SelectQueryBuilderImpl;
import com.jporm.rm.query.save.CustomSaveQuery;
import com.jporm.rm.query.save.CustomSaveQueryImpl;
import com.jporm.rm.query.update.CustomUpdateQuery;
import com.jporm.rm.query.update.CustomUpdateQueryImpl;
import com.jporm.sql.SqlDsl;

public class SqlSessionImpl implements SqlSession {

    private static final String ALL = "*";

    private final SqlExecutor sqlExecutor;
    private final SqlDsl<String> sqlDsl;

    public SqlSessionImpl(SqlExecutor sqlExecutor, SqlDsl<String> sqlDsl) {
        this.sqlExecutor = sqlExecutor;
        this.sqlDsl = sqlDsl;
    }

    @Override
    public SqlExecutor executor() {
        return sqlExecutor;
    }

    @Override
    public CustomDeleteQuery deleteFrom(String table) throws JpoException {
        return new CustomDeleteQueryImpl(sqlDsl.deleteFrom(table), sqlExecutor);
    }

    @Override
    public CustomSaveQuery insertInto(String table, String... fields) {
        return new CustomSaveQueryImpl<>(sqlDsl.insertInto(table, fields), sqlExecutor);
    }

    @Override
    public CustomUpdateQuery update(String table) {
        return new CustomUpdateQueryImpl(sqlDsl.update(table), sqlExecutor);
    }

    @Override
    public SelectQueryBuilder selectAll() {
        return select(ALL);
    }

    @Override
    public SelectQueryBuilder select(String... fields) {
        return new SelectQueryBuilderImpl(fields, sqlExecutor, sqlDsl);
    }

}
