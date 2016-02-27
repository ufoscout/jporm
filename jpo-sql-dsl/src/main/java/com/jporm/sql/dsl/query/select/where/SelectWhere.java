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
package com.jporm.sql.dsl.query.select.where;

import com.jporm.sql.dsl.query.groupby.GroupByProvider;
import com.jporm.sql.dsl.query.orderby.OrderByProvider;
import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.sql.dsl.query.select.SelectCommonProvider;
import com.jporm.sql.dsl.query.select.SelectUnionsProvider;
import com.jporm.sql.dsl.query.select.groupby.SelectGroupBy;
import com.jporm.sql.dsl.query.select.orderby.SelectOrderBy;
import com.jporm.sql.dsl.query.where.Where;

public interface SelectWhere extends Where<SelectWhere>,
                                        GroupByProvider<SelectGroupBy>,
                                        OrderByProvider<SelectOrderBy>,
                                        SelectUnionsProvider,
                                        SelectCommonProvider,
                                        SelectCommon {

}