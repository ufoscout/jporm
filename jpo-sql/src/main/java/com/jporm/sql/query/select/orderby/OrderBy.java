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
package com.jporm.sql.query.select.orderby;

import com.jporm.sql.query.select.SelectCommon;

/**
 *
 * @author Francesco Cina
 *
 *         18/giu/2011
 */
public interface OrderBy<ORDER_BY extends OrderBy<ORDER_BY>> extends SelectCommon {

    ORDER_BY asc(String property);

    ORDER_BY ascNullsFirst(String property);

    ORDER_BY ascNullsLast(String property);

    ORDER_BY desc(String property);

    ORDER_BY descNullsFirst(String property);

    ORDER_BY descNullsLast(String property);

}
