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
package com.jporm.core.query.clause;

import java.util.ArrayList;
import java.util.List;

import com.jporm.core.query.SmartRenderableSqlSubElement;
import com.jporm.core.query.clause.where.EqExpressionElement;
import com.jporm.query.clause.Set;
import com.jporm.query.clause.WhereExpressionElement;
import com.jporm.query.namesolver.NameSolver;

/**
 * 
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public abstract class SetImpl<T extends Set<?>> extends SmartRenderableSqlSubElement implements Set<T> {

    List<WhereExpressionElement> elementList = new ArrayList<WhereExpressionElement>();

    protected abstract T set();

    @Override
    public T eq(final String property, final Object value) {
        final WhereExpressionElement expressionElement = new EqExpressionElement(property, value);
        this.elementList.add(expressionElement);
        return set();
    }

    @Override
    public final void renderSqlElement(final StringBuilder queryBuilder, final NameSolver nameSolver) {
        boolean first = true;
        if (!this.elementList.isEmpty()) {
            queryBuilder.append("SET "); //$NON-NLS-1$
            for (final WhereExpressionElement expressionElement : this.elementList) {
                if (!first) {
                    queryBuilder.append(", "); //$NON-NLS-1$
                }
                expressionElement.renderSqlElement(queryBuilder, nameSolver);
                first = false;
            }
        }
    }

    @Override
    public final void appendElementValues(final List<Object> values) {
        if (!this.elementList.isEmpty()) {
            for (final WhereExpressionElement expressionElement : this.elementList) {
                expressionElement.appendElementValues(values);
            }
        }
    }

    @Override
    public final int getElementStatusVersion() {
        return this.elementList.size();
    }
}
