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
package com.jporm.core.query.clause.impl;

import java.util.ArrayList;
import java.util.List;

import com.jporm.core.query.AQuerySubElement;
import com.jporm.core.query.clause.OrderBy;
import com.jporm.core.query.clause.impl.order.OrderByType;
import com.jporm.core.query.clause.impl.order.OrderElement;
import com.jporm.core.query.clause.impl.order.OrderElementImpl;
import com.jporm.core.query.namesolver.NameSolver;

/**
 * 
 * @author Francesco Cina
 *
 * 24/giu/2011
 */
public abstract class OrderByImpl<T extends OrderBy<?>> extends AQuerySubElement implements OrderBy<T> {

    private final List<OrderElement> elementList = new ArrayList<OrderElement>();

    protected abstract T orderBy();

    @Override
    public final void renderSqlElement(final StringBuilder queryBuilder, final NameSolver nameSolver) {
        if (!this.elementList.isEmpty()) {
            queryBuilder.append("ORDER BY "); //$NON-NLS-1$
            for (final OrderElement expressionElement : this.elementList) {
                expressionElement.renderSqlElement(queryBuilder, nameSolver);
            }
        }
    }

    @Override
    public final int getElementStatusVersion() {
        return this.elementList.size();
    }

    @Override
    public final void appendElementValues(final List<Object> values) {
        // do nothing
    }

    @Override
    public T asc(final String property) {
        return addOrderElement(property, OrderByType.ASC);
    }

    @Override
    public T desc(final String property) {
        return addOrderElement(property, OrderByType.DESC);
    }

    @Override
    public T ascNullsFirst(final String property) {
        return addOrderElement(property, OrderByType.ASC_NULLS_FIRST);
    }

    @Override
    public T ascNullsLast(final String property) {
        return addOrderElement(property, OrderByType.ASC_NULLS_LAST);
    }

    @Override
    public T descNullsFirst(final String property) {
        return addOrderElement(property, OrderByType.DESC_NULLS_FIRST);
    }

    @Override
    public T descNullsLast(final String property) {
        return addOrderElement(property, OrderByType.DESC_NULLS_LAST);
    }

    private T addOrderElement(final String property, final OrderByType orderByType) {
        this.elementList.add(new OrderElementImpl(property, this.elementList.isEmpty(), orderByType));
        return orderBy();
    }

}
