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
package com.jporm.sql.dialect.h2;

import java.util.function.Consumer;

import com.jporm.sql.dialect.SqlPaginationRender;

public class H2SqlPaginationRender implements SqlPaginationRender {

    private static final String A_B_WHERE_B_A_ROWNUM = ") A ) B WHERE B.a_rownum > ";
    private static final String B_WHERE_B_A_ROWNUM = ") B WHERE B.a_rownum > ";
    private static final String A_WHERE_ROWNUM = ") A WHERE rownum <= ";
    private static final String SELECT_FROM_SELECT_A_ROWNUM_A_ROWNUM_FROM = "SELECT * FROM (SELECT A.*, rownum a_rownum FROM ( ";
    private static final String SPACE = " ";
    private static final String WHERE_ROWNUM_MIN = ") A WHERE rownum <= ";
    private static final String SELECT_A_FROM = "SELECT A.* FROM ( ";

    @Override
    public void paginateSQL(final StringBuilder query, final int firstRow, final int maxRows, final Consumer<StringBuilder> queryBuilder) {
        if ((firstRow >= 0) && (maxRows > 0)) {
            query.append(SELECT_FROM_SELECT_A_ROWNUM_A_ROWNUM_FROM);
            queryBuilder.accept(query);
            query.append(A_WHERE_ROWNUM);
            query.append((firstRow + maxRows));
            query.append(B_WHERE_B_A_ROWNUM);
            query.append(firstRow);
            query.append(SPACE);
            return;
        }
        if (firstRow >= 0) {
            query.append(SELECT_FROM_SELECT_A_ROWNUM_A_ROWNUM_FROM);
            queryBuilder.accept(query);
            query.append(A_B_WHERE_B_A_ROWNUM);
            query.append(firstRow);
            query.append(SPACE);
            return;
        }
        if (maxRows > 0) {
            query.append(SELECT_A_FROM);
            queryBuilder.accept(query);
            query.append(WHERE_ROWNUM_MIN);
            query.append(maxRows);
            query.append(SPACE);
            return;
        }
        queryBuilder.accept(query);
    }

}
