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
package com.jporm.core.query.clause.where;

import java.util.List;

import com.jporm.core.query.AQuerySubElement;
import com.jporm.query.clause.WhereExpressionElement;
import com.jporm.query.find.FindQueryRoot;
import com.jporm.query.namesolver.NameSolver;

/**
 * 
 * @author ufo
 *
 */
public abstract class SubQueryExpressionElement extends AQuerySubElement implements WhereExpressionElement {

    private final FindQueryRoot query;
    private final String property;
    private final String condition;

    public SubQueryExpressionElement(final String property, final FindQueryRoot query, final String condition) {
        this.property = property;
        this.query = query;
        this.condition = condition;
    }

    @Override
    public final void renderSqlElement(final StringBuilder queryBuilder, final NameSolver nameSolver) {
        queryBuilder.append(  nameSolver.solvePropertyName( property ) );
        queryBuilder.append( condition );
        queryBuilder.append( "( "); //$NON-NLS-1$
        query.renderSql(queryBuilder);
        queryBuilder.append(") "); //$NON-NLS-1$
    }

    @Override
    public final void appendElementValues(final List<Object> values) {
        query.appendValues(values);
    }

    @Override
    public final int getElementStatusVersion() {
        return 0;
    }
}
