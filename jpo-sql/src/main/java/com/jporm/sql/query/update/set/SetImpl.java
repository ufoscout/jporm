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
package com.jporm.sql.query.update.set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jporm.sql.query.SqlSubElement;
import com.jporm.sql.query.where.WhereExpressionElement;
import com.jporm.sql.query.where.expression.EqExpressionElement;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class SetImpl implements Set, SqlSubElement {

    private final List<WhereExpressionElement> elementList = new ArrayList<WhereExpressionElement>();
    private final Map<String, CaseWhen> caseWhenMap = new HashMap<>();

    @Override
    public final void sqlElementValues(final List<Object> values) {
        if (!elementList.isEmpty()) {
            for (final WhereExpressionElement expressionElement : elementList) {
                expressionElement.sqlElementValues(values);
            }
        }
        if (!caseWhenMap.isEmpty()) {
            for (Entry<String, CaseWhen> caseWhen : caseWhenMap.entrySet()) {
                caseWhen.getValue().sqlElementValues(values);
            }
        }
    }

    @Override
    public final void eq(final String property, final Object value) {
        final WhereExpressionElement expressionElement = new EqExpressionElement(property, value);
        elementList.add(expressionElement);
    }

    /**
     * @return the elementList
     */
    public List<WhereExpressionElement> getElementList() {
        return elementList;
    }

    @Override
    public void eq(String property, CaseWhen caseWhen) {
        if (caseWhen!=null) {
            caseWhenMap.put(property, caseWhen);
        } else {
            eq(property, (Object) caseWhen);
        }
    }

    /**
     * @return the caseWhenMap
     */
    public Map<String, CaseWhen> getCaseWhenMap() {
        return caseWhenMap;
    }

}
