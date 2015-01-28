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
package com.jporm.core.query.clause.impl.where;

import java.util.List;

import com.jporm.core.query.AQuerySubElement;
import com.jporm.core.query.clause.WhereExpressionElement;
import com.jporm.core.query.namesolver.NameSolver;

/**
 * 
 * @author Francesco Cina
 *
 * 26/giu/2011
 */
public abstract class MultipleExpressionElement extends AQuerySubElement implements WhereExpressionElement {

    private final String relationType;
    private final List<WhereExpressionElement> expressionElements;

    public MultipleExpressionElement(final String relationType, final List<WhereExpressionElement> expressionElements) {
        this.expressionElements = expressionElements;
        this.relationType = relationType;
    }

    @Override
    public final void renderSqlElement(final StringBuilder stringBuilder, final NameSolver nameSolver) {
        stringBuilder.append("( "); //$NON-NLS-1$
        int last = expressionElements.size() - 1;
        if (last<0) {
            stringBuilder.append( "1=1 " ); //$NON-NLS-1$
        } else {
            for (int i=0; i<expressionElements.size(); i++) {
                expressionElements.get(i).renderSqlElement(stringBuilder, nameSolver);
                if (i!=last) {
                    stringBuilder.append(relationType);
                }
            }
        }
        stringBuilder.append(") "); //$NON-NLS-1$
    }

    @Override
    public final void appendElementValues(final List<Object> values) {
        for (WhereExpressionElement expressionElement : expressionElements) {
            expressionElement.appendElementValues(values);
        }
    }

    @Override
    public final int getElementStatusVersion() {
        return 0;
    }

}
