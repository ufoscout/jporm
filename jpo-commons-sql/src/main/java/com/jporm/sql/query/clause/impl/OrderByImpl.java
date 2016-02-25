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
package com.jporm.sql.query.clause.impl;

import java.util.ArrayList;
import java.util.List;

import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.ASqlSubElement;
import com.jporm.sql.query.clause.OrderBy;
import com.jporm.sql.query.clause.impl.order.OrderByType;
import com.jporm.sql.query.clause.impl.order.OrderElement;
import com.jporm.sql.query.clause.impl.order.OrderElementImpl;
import com.jporm.sql.query.namesolver.PropertiesProcessor;

/**
 *
 * @author Francesco Cina
 *
 *         24/giu/2011
 */
public class OrderByImpl extends ASqlSubElement implements OrderBy {

    private final List<OrderElement> elementList = new ArrayList<>();

    private void addOrderElement(final String property, final OrderByType orderByType) {
        elementList.add(new OrderElementImpl(property, elementList.isEmpty(), orderByType));
    }

    @Override
    public final void appendElementValues(final List<Object> values) {
        // do nothing
    }

    @Override
    public void asc(final String property) {
        addOrderElement(property, OrderByType.ASC);
    }

    @Override
    public void ascNullsFirst(final String property) {
        addOrderElement(property, OrderByType.ASC_NULLS_FIRST);
    }

    @Override
    public void ascNullsLast(final String property) {
        addOrderElement(property, OrderByType.ASC_NULLS_LAST);
    }

    @Override
    public void desc(final String property) {
        addOrderElement(property, OrderByType.DESC);
    }

    @Override
    public void descNullsFirst(final String property) {
        addOrderElement(property, OrderByType.DESC_NULLS_FIRST);
    }

    @Override
    public void descNullsLast(final String property) {
        addOrderElement(property, OrderByType.DESC_NULLS_LAST);
    }

    @Override
    public final void renderSqlElement(final DBProfile dbProfile, final StringBuilder queryBuilder, final PropertiesProcessor nameSolver) {
        if (!elementList.isEmpty()) {
            queryBuilder.append("ORDER BY "); //$NON-NLS-1$
            for (final OrderElement expressionElement : elementList) {
                expressionElement.renderSqlElement(dbProfile, queryBuilder, nameSolver);
            }
        }
    }

}
