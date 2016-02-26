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
package com.jporm.sql.dsl.query.select;

import com.jporm.sql.dsl.query.select.from.FromProvider;
import com.jporm.sql.dsl.query.select.groupby.GroupByProvider;
import com.jporm.sql.dsl.query.select.orderby.OrderByProvider;
import com.jporm.sql.dsl.query.select.where.SelectWhere;
import com.jporm.sql.dsl.query.where.WhereProvider;

public interface Select<JOIN> extends FromProvider<JOIN>, WhereProvider<SelectWhere>, GroupByProvider, OrderByProvider, SelectUnionsProvider, SelectCommon {

    Select<JOIN> limit(int limit);

    Select<JOIN> lockMode(LockMode lockMode);

    Select<JOIN> offset(int offset);

}
