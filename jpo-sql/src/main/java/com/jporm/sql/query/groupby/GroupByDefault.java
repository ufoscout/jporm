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
package com.jporm.sql.query.groupby;

public interface GroupByDefault<GROUP_BY extends GroupBy<GROUP_BY>> extends GroupBy<GROUP_BY>, GroupByProvider<GROUP_BY> {

    GroupBy<?> groupByImplementation();

    @Override
    default GROUP_BY fields(String... fields) {
        groupByImplementation().fields(fields);
        return groupBy();
    }

    @Override
    default GROUP_BY having(String havingClause, Object... args) {
        groupByImplementation().having(havingClause, args);
        return groupBy();
    }

}
