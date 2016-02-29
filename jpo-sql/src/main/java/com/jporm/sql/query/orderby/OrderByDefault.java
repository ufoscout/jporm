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
package com.jporm.sql.query.orderby;

public interface OrderByDefault<ORDER_BY extends OrderBy<ORDER_BY>> extends OrderBy<ORDER_BY>, OrderByProvider<ORDER_BY> {

    OrderBy<?> orderByImplementation();

    @Override
    default ORDER_BY asc(String property) {
        orderByImplementation().asc(property);
        return orderBy();
    }

    @Override
    default ORDER_BY ascNullsFirst(String property) {
        orderByImplementation().ascNullsFirst(property);
        return orderBy();
    }

    @Override
    default ORDER_BY ascNullsLast(String property) {
        orderByImplementation().ascNullsLast(property);
        return orderBy();
    }

    @Override
    default ORDER_BY desc(String property) {
        orderByImplementation().desc(property);
        return orderBy();
    }

    @Override
    default ORDER_BY descNullsFirst(String property) {
        orderByImplementation().descNullsFirst(property);
        return orderBy();
    }

    @Override
    default ORDER_BY descNullsLast(String property) {
        orderByImplementation().descNullsLast(property);
        return orderBy();
    }

}
