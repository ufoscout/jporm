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
package com.jporm.sql.dsl.query.where.expression;

import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.ASqlSubElement;
import com.jporm.sql.dsl.query.where.WhereExpressionElement;

/**
 *
 * @author Francesco Cina
 *
 *         26/giu/2011
 */
public class NotExpressionElement extends ASqlSubElement implements WhereExpressionElement {

    protected final WhereExpressionElement expression;

    public NotExpressionElement(final WhereExpressionElement expression) {
        this.expression = expression;
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        expression.sqlElementValues(values);
    }

    @Override
    public final void sqlElementQuery(final StringBuilder query, DBProfile dbProfile) {
        query.append("NOT ( "); //$NON-NLS-1$
        expression.sqlElementQuery(query, dbProfile);
        query.append(") "); //$NON-NLS-1$
    }
}
