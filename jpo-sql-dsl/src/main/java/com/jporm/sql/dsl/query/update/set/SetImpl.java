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
package com.jporm.sql.dsl.query.update.set;

import java.util.ArrayList;
import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.ASqlSubElement;
import com.jporm.sql.dsl.query.where.WhereExpressionElement;
import com.jporm.sql.dsl.query.where.expression.EqExpressionElement;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class SetImpl extends ASqlSubElement implements Set {

    private List<WhereExpressionElement> elementList = new ArrayList<WhereExpressionElement>();

    @Override
    public final void sqlElementValues(final List<Object> values) {
        if (!elementList.isEmpty()) {
            for (final WhereExpressionElement expressionElement : elementList) {
                expressionElement.sqlElementValues(values);
            }
        }
    }

    @Override
    public final void eq(final String property, final Object value) {
        final WhereExpressionElement expressionElement = new EqExpressionElement(property, value);
        elementList.add(expressionElement);
    }

    @Override
    public final void sqlElementQuery(final StringBuilder queryBuilder, DBProfile dbProfile) {
        boolean first = true;
        if (!elementList.isEmpty()) {
            queryBuilder.append("SET "); //$NON-NLS-1$
            for (final WhereExpressionElement expressionElement : elementList) {
                if (!first) {
                    queryBuilder.append(", "); //$NON-NLS-1$
                }
                expressionElement.sqlElementQuery(queryBuilder, dbProfile);
                first = false;
            }
        }
    }

}
