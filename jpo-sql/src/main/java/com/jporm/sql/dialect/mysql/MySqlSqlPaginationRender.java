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
package com.jporm.sql.dialect.mysql;

import java.util.function.Consumer;

import com.jporm.sql.dialect.SqlPaginationRender;

public class MySqlSqlPaginationRender implements SqlPaginationRender {

    private static final String SPACE = " ";
    private static final String OFFSET = " OFFSET ";
    private static final String LIMIT = "LIMIT ";
    private static final String LIMIT_MAX_OFFEST = LIMIT + Long.MAX_VALUE + OFFSET;

    @Override
    public void paginateSQL(final StringBuilder query, final int firstRow, final int maxRows, final Consumer<StringBuilder> queryBuilder) {
        if ((firstRow >= 0) && (maxRows > 0)) {
            queryBuilder.accept(query);
            query.append(LIMIT);
            query.append(maxRows);
            query.append(OFFSET);
            query.append(firstRow);
            query.append(SPACE);
            return;
        }
        if (firstRow >= 0) {
            queryBuilder.accept(query);
            query.append(LIMIT_MAX_OFFEST);
            query.append(firstRow);
            query.append(SPACE);
            return;
        }
        if (maxRows > 0) {
            queryBuilder.accept(query);
            query.append(LIMIT);
            query.append(maxRows);
            query.append(SPACE);
            return;
        }
        queryBuilder.accept(query);
    }

}
