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
package com.jporm.sql.query.clause.impl.order;

import java.util.List;

import com.jporm.sql.query.ASqlSubElement;
import com.jporm.sql.query.namesolver.NameSolver;

/**
 * 
 * @author Francesco Cina
 *
 * 24/giu/2011
 */
public class OrderElementImpl extends ASqlSubElement implements OrderElement {

    private final boolean isFirstElement;
    private final OrderByType type;
    private final String property;

    public OrderElementImpl(final String property, final boolean isFirstElement, final OrderByType type) {
        this.property = property;
        this.isFirstElement = isFirstElement;
        this.type = type;
    }

    @Override
    public final int getElementStatusVersion() {
        return 0;
    }

    @Override
    public final void renderSqlElement(final StringBuilder queryBuilder, final NameSolver nameSolver) {
        if (!isFirstElement) {
            queryBuilder.append( ", " ); //$NON-NLS-1$
        }
        queryBuilder.append( nameSolver.solvePropertyName(property) );
        queryBuilder.append( " " ); //$NON-NLS-1$
        queryBuilder.append( type.getType() );
        queryBuilder.append( type.getNulls() );
    }

    @Override
    public final void appendElementValues(final List<Object> values) {
        // do nothing
    }

}
