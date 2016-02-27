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
package com.jporm.sql.dsl.query.where;

import java.util.List;

public interface WhereProvider<WHERE extends Where<?>> {

    WHERE where();

    public default WHERE where(List<WhereExpressionElement> expressionElements) {
        WHERE where = where();
        where.and(expressionElements);
        return where;
    }

    public default WHERE where(String customClause, Object... args) {
        WHERE where = where();
        where.and(customClause, args);
        return where;
    }

    public default WHERE where(WhereExpressionElement... expressionElements) {
        WHERE where = where();
        where.and(expressionElements);
        return where;
    }

}
