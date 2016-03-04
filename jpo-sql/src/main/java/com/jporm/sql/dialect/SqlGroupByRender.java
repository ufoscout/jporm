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
package com.jporm.sql.dialect;

import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.select.groupby.GroupByImpl;
import com.jporm.sql.query.where.WhereExpressionElement;

public interface SqlGroupByRender {

    String GROUP_BY = "GROUP BY ";
    String HAVING = "HAVING ";
    String WHITE_SPACE = " ";
    String COMMA = ", ";

    default void render(GroupByImpl<?> groupBy, StringBuilder queryBuilder, PropertiesProcessor processor) {
        String[] fields = groupBy.getFields();
        if (fields.length > 0) {
            queryBuilder.append(GROUP_BY); 
            for (int i = 0; i < fields.length; i++) {
                queryBuilder.append(processor.solvePropertyName(fields[i]));
                if (i < (fields.length - 1)) {
                    queryBuilder.append(COMMA);
                }
            }
            queryBuilder.append(WHITE_SPACE);
            WhereExpressionElement havingExpression = groupBy.getHavingExpression();
            if (havingExpression != null) {
                queryBuilder.append(HAVING);
                havingExpression.sqlElementQuery(queryBuilder, processor);
            }
        }
    }

}
