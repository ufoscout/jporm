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
package com.jporm.sql.dialect.unknown;

import com.jporm.sql.dialect.SqlFromRender;
import com.jporm.sql.dialect.SqlGroupByRender;
import com.jporm.sql.dialect.SqlOrderByRender;
import com.jporm.sql.dialect.SqlPaginationRender;
import com.jporm.sql.dialect.SqlSelectRender;
import com.jporm.sql.dialect.SqlWhereRender;

public class UnknownSqlSelectRender implements SqlSelectRender, SqlFromRender, SqlOrderByRender, SqlGroupByRender, SqlWhereRender {

    private final SqlPaginationRender paginationRender = new UnknownSqlPaginationRender();

    @Override
    public SqlPaginationRender getPaginationRender() {
        return paginationRender;
    }

    @Override
    public SqlFromRender getFromRender() {
        return this;
    }

    @Override
    public SqlWhereRender getWhereRender() {
        return this;
    }

    @Override
    public SqlGroupByRender getGroupByRender() {
        return this;
    }

    @Override
    public SqlOrderByRender getOrderByRender() {
        return this;
    }

}
