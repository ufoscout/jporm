/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.sql.query;

import java.util.ArrayList;
import java.util.List;

import com.jporm.sql.dialect.DBProfile;

public interface Sql {

    /**
     * Return the list all the values of the expression's elements
     *
     * @return
     */
    default List<Object> sqlValues() {
        List<Object> values = new ArrayList<>();
        sqlValues(values);
        return values;
    }

    /**
     * Append to the list all the values of the expression's elements
     *
     * @return
     */
    void sqlValues(List<Object> values);

    /**
     * Return the sql query generated by this IQuery Object using the global {@link DBProfile}
     *
     * @return
     */
    default String sqlQuery() {
        final StringBuilder queryBuilder = new StringBuilder();
        sqlQuery(queryBuilder);
        return queryBuilder.toString();
    }

    /**
     * Append to the string buffer the sql query generated by this IQuery Object using the global {@link DBProfile}
     *
     * @param queryBuilder
     */
    void sqlQuery(StringBuilder queryBuilder);

}