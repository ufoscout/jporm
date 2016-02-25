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
package com.jporm.sql.dsl.query.select.orderby;

import java.util.ArrayList;
import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.ASqlSubElement;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;
import com.jporm.sql.dsl.query.select.Select;
import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.sql.dsl.query.select.SelectUnionsProvider;

/**
 *
 * @author Francesco Cina
 *
 *         24/giu/2011
 */
public class OrderByImpl extends ASqlSubElement implements OrderBy {

    private final List<OrderElement> elementList = new ArrayList<>();
    private final Select select;

    public OrderByImpl(Select select) {
        this.select = select;
    }

    private void addOrderElement(final String property, final OrderByType orderByType) {
        elementList.add(new OrderElementImpl(property, elementList.isEmpty(), orderByType));
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
    }

    @Override
    public OrderBy asc(final String property) {
        addOrderElement(property, OrderByType.ASC);
        return this;
    }

    @Override
    public OrderBy ascNullsFirst(final String property) {
        addOrderElement(property, OrderByType.ASC_NULLS_FIRST);
        return this;
    }

    @Override
    public OrderBy ascNullsLast(final String property) {
        addOrderElement(property, OrderByType.ASC_NULLS_LAST);
        return this;
    }

    @Override
    public OrderBy desc(final String property) {
        addOrderElement(property, OrderByType.DESC);
        return this;
    }

    @Override
    public OrderBy descNullsFirst(final String property) {
        addOrderElement(property, OrderByType.DESC_NULLS_FIRST);
        return this;
    }

    @Override
    public OrderBy descNullsLast(final String property) {
        addOrderElement(property, OrderByType.DESC_NULLS_LAST);
        return this;
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
    public String sqlRowCountQuery() {
        return select.sqlRowCountQuery();
    }

    @Override
    public SelectUnionsProvider union(SelectCommon select) {
       return this.select.union(select);
    }

    @Override
    public SelectUnionsProvider unionAll(SelectCommon select) {
        return this.select.unionAll(select);
    }

    @Override
    public SelectUnionsProvider except(SelectCommon select) {
        return this.select.except(select);
    }

    @Override
    public SelectUnionsProvider intersect(SelectCommon select) {
        return this.select.intersect(select);
    }

    @Override
    public final List<Object> sqlValues() {
        return select.sqlValues();
    }

    @Override
    public final void sqlValues(List<Object> values) {
        select.sqlValues(values);
    }

    @Override
    public final String sqlQuery() {
        return select.sqlQuery();
    }

    @Override
    public final void sqlQuery(StringBuilder queryBuilder) {
        select.sqlQuery();
    }
}
