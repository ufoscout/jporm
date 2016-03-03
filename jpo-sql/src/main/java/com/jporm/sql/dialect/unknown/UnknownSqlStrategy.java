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
package com.jporm.sql.dialect.unknown;

import java.util.function.Consumer;

import com.jporm.sql.dialect.SqlStrategy;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Mar 16, 2013
 *
 * @author - Francesco Cina
 * @version $Revision
 */
public class UnknownSqlStrategy implements SqlStrategy {

    @Override
    public String insertQuerySequence(final String name) {
        return name + ".nextval"; //$NON-NLS-1$
    }

    @Override
    public String paginateSQL(final String sql, final int firstRow, final int maxRows) {
        StringBuilder query = new StringBuilder();
        paginateSQL(query, firstRow, maxRows, queryBuilder -> queryBuilder.append(sql));
        return query.toString();
    }

    @Override
    public void paginateSQL(final StringBuilder sql, final int firstRow, final int maxRows, final Consumer<StringBuilder> queryBuilder) {
        if ((firstRow >= 0) || (maxRows > 1)) {
            throw new RuntimeException("Pagination is not available for the unknown database type");
        }
        queryBuilder.accept(sql);
    }

}
