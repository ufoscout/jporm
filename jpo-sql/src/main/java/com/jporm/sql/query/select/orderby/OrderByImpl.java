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
package com.jporm.sql.query.select.orderby;

import java.util.ArrayList;
import java.util.List;

import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.SqlSubElement;
import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.select.Select;

/**
 *
 * @author Francesco Cina
 *
 *         24/giu/2011
 */
public abstract class OrderByImpl<ORDER_BY extends OrderBy<ORDER_BY>> implements OrderBy<ORDER_BY>, SqlSubElement {

    private final List<OrderElement> elementList = new ArrayList<>();
    private final Select<?> select;

    public OrderByImpl(Select<?> select) {
        this.select = select;
    }

    private void addOrderElement(final String property, final OrderByType orderByType) {
        elementList.add(new OrderElementImpl(property, elementList.isEmpty(), orderByType));
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
    }

    @Override
    public final ORDER_BY asc(final String property) {
        addOrderElement(property, OrderByType.ASC);
        return getOrderBy();
    }

    @Override
    public final ORDER_BY ascNullsFirst(final String property) {
        addOrderElement(property, OrderByType.ASC_NULLS_FIRST);
        return getOrderBy();
    }

    @Override
    public final ORDER_BY ascNullsLast(final String property) {
        addOrderElement(property, OrderByType.ASC_NULLS_LAST);
        return getOrderBy();
    }

    @Override
    public final ORDER_BY desc(final String property) {
        addOrderElement(property, OrderByType.DESC);
        return getOrderBy();
    }

    @Override
    public final ORDER_BY descNullsFirst(final String property) {
        addOrderElement(property, OrderByType.DESC_NULLS_FIRST);
        return getOrderBy();
    }

    @Override
    public final ORDER_BY descNullsLast(final String property) {
        addOrderElement(property, OrderByType.DESC_NULLS_LAST);
        return getOrderBy();
    }

    @Override
    public final void sqlElementQuery(final StringBuilder queryBuilder, DBProfile dbProfile, PropertiesProcessor propertiesProcessor) {
        if (!elementList.isEmpty()) {
            queryBuilder.append("ORDER BY "); //$NON-NLS-1$
            for (final OrderElement expressionElement : elementList) {
                expressionElement.sqlElementQuery(queryBuilder, dbProfile, propertiesProcessor);
            }
        }
    }

    @Override
    public final String sqlRowCountQuery() {
        return select.sqlRowCountQuery();
    }

    @Override
    public final void sqlValues(List<Object> values) {
        select.sqlValues(values);
    }

    @Override
    public final void sqlQuery(StringBuilder queryBuilder) {
        select.sqlQuery();
    }

    private ORDER_BY getOrderBy() {
        return (ORDER_BY) this;
    };

}
