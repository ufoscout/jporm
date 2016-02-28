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
package com.jporm.rm.query.find;

import com.jporm.sql.dsl.query.orderby.OrderByProvider;
import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.sql.dsl.query.where.WhereProvider;

/**
 *
 * @author Francesco Cina
 *
 *         18/giu/2011
 */
public interface CustomFindQuery<BEAN> extends CustomFindQueryFrom<BEAN>,
                                                WhereProvider<CustomFindQueryWhere<BEAN>>,
                                                OrderByProvider<CustomFindQueryOrderBy<BEAN>>,
                                                FindQueryExecutorProvider<BEAN>,
                                                CustomFindQueryUnionsProvider<BEAN>,
                                                CustomFindQueryPaginationProvider<BEAN>,
                                                SelectCommon {

	CustomFindQuery<BEAN> distinct();

	CustomFindQuery<BEAN> distinct(boolean distinct);


    /**
     * The value of the Bean fields listed will not be fetched from the DB. This
     * is useful to load only a partial Bean to reduce the amount of work of the
     * DB. Normally this is used to avoid loading LOB values when not needed.
     *
     * @param fields
     * @return
     */
	CustomFindQuery<BEAN> ignore(String... fields);

}
