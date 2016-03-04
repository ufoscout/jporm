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
package com.jporm.sql.dialect.sqlserver2008;

import java.util.function.Consumer;

import com.jporm.sql.dialect.SqlPaginationRender;

public class SQLServer2008_SqlPaginationRender implements SqlPaginationRender {

    private static final String WHERE_ROWNUM_LESS = ") A WHERE rownum <= ";
    private static final String WHERE_ROWNUM_MORE = ") A WHERE rownum > ";
    private static final String AND_ROWNUM_LESS = " AND RowNum <= ";
    private static final String SELECT_A_FROM = "SELECT A.* FROM ( ";
    private static final String ORDER_BY_ROW_NUM = " ORDER BY RowNum ";

    @Override
    public void paginateSQL(final StringBuilder query, final int firstRow, final int maxRows, final Consumer<StringBuilder> queryBuilder) {
        if ((firstRow >= 0) && (maxRows > 0)) {
            query.append(SELECT_A_FROM);
            queryBuilder.accept(query);
            query.append(WHERE_ROWNUM_MORE);
            query.append(firstRow);
            query.append(AND_ROWNUM_LESS);
            query.append(firstRow + maxRows);
            query.append(ORDER_BY_ROW_NUM);
            return;
        }
        if (firstRow >= 0) {
            query.append(SELECT_A_FROM);
            queryBuilder.accept(query);
            query.append(WHERE_ROWNUM_MORE);
            query.append(firstRow);
            query.append(ORDER_BY_ROW_NUM);
            return;
        }
        if (maxRows > 0) {
            query.append(SELECT_A_FROM);
            queryBuilder.accept(query);
            query.append(WHERE_ROWNUM_LESS);
            query.append(maxRows);
            query.append(ORDER_BY_ROW_NUM);
            return;
        }
        queryBuilder.accept(query);
    }
}
