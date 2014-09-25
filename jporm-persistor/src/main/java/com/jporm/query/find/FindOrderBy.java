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
package com.jporm.query.find;

import java.util.List;

import com.jporm.query.clause.OrderBy;
import com.jporm.query.clause.where.Exp;
import com.jporm.query.clause.where.ExpressionElement;

/**
 * 
 * @author ufo
 *
 */
public interface FindOrderBy<BEAN> extends OrderBy<FindOrderBy<BEAN>>, FindQueryCommon<BEAN> {

    FindQuery<BEAN> query();

    /**
     * Chain more {@link ExpressionElement} with a logical and.
     * To build the {@link ExpressionElement} use the {@link Exp} factory.
     * 
     * @return
     */
    FindWhere<BEAN> where(final ExpressionElement... expressionElements);

    /**
     * Chain more {@link ExpressionElement} with a logical and.
     * To build the {@link ExpressionElement} use the {@link Exp} factory.
     * 
     * @return
     */
    FindWhere<BEAN> where(final List<ExpressionElement> expressionElements);

    /**
     * It permits to define a custom where clause.
     * E.g.: clause("mod(Bean.id, 10) = 1 AND Bean.property is not null")
     * 
     * For a better readability and usability placeholders can be used:
     * E.g.: clause("mod(Bean.id, ?) = ? AND Bean.property is not null", new Object[]{10,1})
     * 
     * @param customClause the custom where clause
     * @param args the values of the placeholders if present
     * To build the {@link ExpressionElement} use the {@link Exp} factory.
     * 
     * @return
     */
    FindWhere<BEAN> where(String customClause, Object... args);

}
