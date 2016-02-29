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
package com.jporm.sql.query.where.expression;

import java.util.List;

import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.SqlSubElement;
import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.sql.query.where.WhereExpressionElement;

/**
 *
 * @author ufo
 *
 */
public abstract class SubQueryExpressionElement implements WhereExpressionElement, SqlSubElement {

    private final SelectCommon query;
    private final String property;
    private final String condition;

    public SubQueryExpressionElement(final String property, final SelectCommon query, final String condition) {
        this.property = property;
        this.query = query;
        this.condition = condition;
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        query.sqlValues(values);
    }

    @Override
    public final void sqlElementQuery(final StringBuilder queryBuilder, final DBProfile dbProfile, final PropertiesProcessor nameSolver) {
        queryBuilder.append(nameSolver.solvePropertyName(property));
        queryBuilder.append(condition);
        queryBuilder.append("( "); //$NON-NLS-1$
        query.sqlQuery(queryBuilder);
        queryBuilder.append(") "); //$NON-NLS-1$
    }
}
