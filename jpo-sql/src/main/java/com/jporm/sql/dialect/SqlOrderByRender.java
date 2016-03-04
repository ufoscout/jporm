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
import com.jporm.sql.query.select.orderby.OrderByImpl;
import com.jporm.sql.query.select.orderby.OrderByType;
import com.jporm.sql.query.select.orderby.OrderElementImpl;

public interface SqlOrderByRender {

    String ORDER_BY = "ORDER BY ";

    default void render(OrderByImpl<?> orderBy, StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        List<OrderElementImpl> elementList = orderBy.getOrderByElements();
        if (!elementList.isEmpty()) {
            queryBuilder.append(ORDER_BY);
            for (final OrderElementImpl orderElement : elementList) {
                renderOrderElement(orderElement, queryBuilder, propertiesProcessor);
            }
        }
    }

    default void renderOrderElement(OrderElementImpl orderElement, final StringBuilder queryBuilder, final PropertiesProcessor propertiesProcessor) {
        if (!orderElement.isFirstElement()) {
            queryBuilder.append(", ");
        }
        queryBuilder.append(propertiesProcessor.solvePropertyName(orderElement.getProperty()));
        queryBuilder.append(" ");
        OrderByType type = orderElement.getType();
        queryBuilder.append(type.getType());
        queryBuilder.append(type.getNulls());
    }

}
