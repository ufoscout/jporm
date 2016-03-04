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

import com.jporm.sql.query.SqlSubElement;
import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.where.WhereExpressionElement;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Mar 21, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class CustomExpressionElement implements WhereExpressionElement, SqlSubElement {

    private String customClause;
    private Object[] args;

    /**
     * @param customClause
     * @param args
     */
    public CustomExpressionElement(final String customClause, final Object[] args) {
        this.customClause = customClause;
        this.args = args;
    }

    @Override
    public void sqlElementValues(final List<Object> values) {
        for (Object arg : args) {
            values.add(arg);
        }
    }

    @Override
    public void sqlElementQuery(final StringBuilder queryBuilder, final PropertiesProcessor nameSolver) {
        nameSolver.solveAllPropertyNames(customClause, queryBuilder);
        queryBuilder.append(" ");
    }

}
