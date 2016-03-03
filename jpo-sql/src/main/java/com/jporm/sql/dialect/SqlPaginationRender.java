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
package com.jporm.sql.dialect;

import java.util.function.Consumer;

public interface SqlPaginationRender {

    /**
     * Decore the sql select query adding the proper pagination instructions if
     * needed.
     *
     * @param sql
     * @param firstRow
     *            set the first row number to retrieve. It is ignored if
     *            negative.
     * @param maxRows
     *            set the max number of rows to retrieve. It is ignored if
     *            negative or equals to 0.
     * @return
     */
    default String paginateSQL(String sql, int firstRow, int maxRows) {
        StringBuilder query = new StringBuilder();
        paginateSQL(query, firstRow, maxRows, queryBuilder -> queryBuilder.append(sql));
        return query.toString();
    }

    /**
     * Decore the sql select query adding the proper pagination instructions if
     * needed.
     *
     * @param sql
     * @param firstRow
     *            set the first row number to retrieve. It is ignored if
     *            negative.
     * @param maxRows
     *            set the max number of rows to retrieve. It is ignored if
     *            negative or equals to 0.
     * @return
     */
    void paginateSQL(StringBuilder sql, int firstRow, int maxRows, Consumer<StringBuilder> queryBuilder);

}
