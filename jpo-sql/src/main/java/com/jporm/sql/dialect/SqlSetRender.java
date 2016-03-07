/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.sql.dialect;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.update.set.CaseWhen;
import com.jporm.sql.query.update.set.SetImpl;
import com.jporm.sql.query.where.WhereExpressionElement;

public interface SqlSetRender {

    public static final String ELSE = "\nELSE ";
    public static final String NEW_LINE = "\n";
    public static final String SPACE_EQUALS_SPACE_CASE = " = CASE ";
    public static final String COMMA_WHITE_SPACE = ", ";
    public static final String SET = "SET ";
    public static final String WHEN_THEN = "\nWHEN ? THEN ? ";
    public static final String END = " END \n";

    default void render(SetImpl set, StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        boolean first = true;
        List<WhereExpressionElement> elementList = set.getElementList();
        Map<String, CaseWhen> caseWhenMap = set.getCaseWhenMap();
        if (!elementList.isEmpty() || !caseWhenMap.isEmpty()) {
            queryBuilder.append(SET);

            for (final WhereExpressionElement expressionElement : elementList) {
                if (!first) {
                    queryBuilder.append(COMMA_WHITE_SPACE);
                }
                expressionElement.sqlElementQuery(queryBuilder, propertiesProcessor);
                first = false;
            }

            for (Entry<String, CaseWhen> setWithCase : caseWhenMap.entrySet()) {
                if (!first) {
                    queryBuilder.append(COMMA_WHITE_SPACE);
                }
                queryBuilder.append(NEW_LINE);
                String setWithCaseField = propertiesProcessor.solvePropertyName(setWithCase.getKey());
                queryBuilder.append(setWithCaseField);
                queryBuilder.append(SPACE_EQUALS_SPACE_CASE);
                CaseWhen caseWhen = setWithCase.getValue();
                caseWhen.visit((caseField, whenThen) -> {
                    queryBuilder.append(propertiesProcessor.solvePropertyName(caseField));
                    for (int i=0; i<(whenThen.size()/2); i++) {
                        queryBuilder.append(WHEN_THEN);
                    }
                    queryBuilder.append(ELSE);
                    queryBuilder.append(setWithCaseField);
                    queryBuilder.append(END);
                });
                first = false;
            }

        }

    }

}
