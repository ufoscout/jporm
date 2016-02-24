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
package com.jporm.sql.dsl.query.select.groupby;

import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.sql.dsl.query.select.SelectUnionsProvider;
import com.jporm.sql.dsl.query.select.orderby.OrderByProvider;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Mar 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public interface GroupBy extends OrderByProvider, SelectUnionsProvider, SelectCommon {

    /**
     * The fields to group by
     *
     * @param fields
     * @return
     */
    GroupBy fields(String... fields);

    /**
     * It permits to define a custom having clause. E.g.: having(
     * "count(*) > 100")
     *
     * For a better readability and usability placeholders can be used: E.g.:
     * having("count(*) > ?"), 100)
     *
     * @param havingClause
     *            the custom where clause
     * @param args
     *            the values of the placeholders if present
     * @return
     */
    GroupBy having(String havingClause, Object... args);

}
