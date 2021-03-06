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
package com.jporm.sql.query.select.where;

import com.jporm.sql.query.select.SelectCommon;
import com.jporm.sql.query.select.groupby.GroupByProvider;
import com.jporm.sql.query.select.groupby.SelectGroupBy;
import com.jporm.sql.query.select.orderby.OrderByProvider;
import com.jporm.sql.query.select.orderby.SelectOrderBy;
import com.jporm.sql.query.select.pagination.SelectPaginationProvider;
import com.jporm.sql.query.select.unions.SelectUnionsProvider;
import com.jporm.sql.query.where.Where;

public interface SelectWhere extends Where<SelectWhere>,
                                        GroupByProvider<SelectGroupBy>,
                                        OrderByProvider<SelectOrderBy>,
                                        SelectUnionsProvider,
                                        SelectPaginationProvider,
                                        SelectCommon {

}
