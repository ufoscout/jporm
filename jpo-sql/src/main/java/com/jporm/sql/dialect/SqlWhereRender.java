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

import java.util.List;

import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.where.WhereExpressionElement;
import com.jporm.sql.query.where.WhereImpl;

public interface SqlWhereRender {

    String WHERE = "WHERE ";
    String AND = "AND ";

    default void render(WhereImpl<?> where, StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        boolean first = true;
        List<WhereExpressionElement> elementList = where.getElementList();
        if (!elementList.isEmpty()) {
            queryBuilder.append(WHERE);
            for (final WhereExpressionElement expressionElement : elementList) {
                if (!first) {
                    queryBuilder.append(AND);
                }
                expressionElement.sqlElementQuery(queryBuilder, propertiesProcessor);
                first = false;
            }
        }
    }

}
