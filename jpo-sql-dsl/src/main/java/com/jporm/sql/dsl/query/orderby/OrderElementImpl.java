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
package com.jporm.sql.dsl.query.orderby;

import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.SqlSubElement;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;

/**
 *
 * @author Francesco Cina
 *
 *         24/giu/2011
 */
public class OrderElementImpl implements OrderElement, SqlSubElement {

    private final boolean isFirstElement;
    private final OrderByType type;
    private final String property;

    public OrderElementImpl(final String property, final boolean isFirstElement, final OrderByType type) {
        this.property = property;
        this.isFirstElement = isFirstElement;
        this.type = type;
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        // do nothing
    }

    @Override
    public final void sqlElementQuery(final StringBuilder queryBuilder, final DBProfile dbProfile, final PropertiesProcessor nameSolver) {
        if (!isFirstElement) {
            queryBuilder.append(", ");
        }
        queryBuilder.append(nameSolver.solvePropertyName(property));
        queryBuilder.append(" ");
        queryBuilder.append(type.getType());
        queryBuilder.append(type.getNulls());
    }

}
