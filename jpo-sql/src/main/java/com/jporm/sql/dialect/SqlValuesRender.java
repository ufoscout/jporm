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

import java.util.Iterator;
import java.util.List;

import com.jporm.sql.query.insert.values.Generator;
import com.jporm.sql.query.insert.values.ValuesImpl;
import com.jporm.sql.query.processor.PropertiesProcessor;

public interface SqlValuesRender {

    public static final String QUESTION_MARK = "?";
    public static final String PARENTESIS_CLOSE_PLUS_COMMA = "), ";
    public static final String PARENTESIS_CLOSE = ") ";
    public static final String COMMA = ", ";
    public static final String PARENTESIS_OPEN = "(";
    public static final String VALUES = "VALUES ";

    default void render(ValuesImpl elemValues, StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor, SqlFunctionsRender functionsRender) {
        columnToCommaSepareted(elemValues.getFields(), queryBuilder, propertiesProcessor);
        queryBuilder.append(VALUES);
        valuesToCommaSeparated(elemValues.getValues(), queryBuilder, functionsRender);
    }

    default void columnToCommaSepareted(String[] fields, StringBuilder builder, PropertiesProcessor propertiesProcessor) {
        builder.append(PARENTESIS_OPEN);
        int i = 0;
        int last = fields.length - 1;
        for (String column : fields) {
            builder.append(propertiesProcessor.solvePropertyName(column));
            if (i != last) {
                builder.append(COMMA);
                i++;
            }
        };
        builder.append(PARENTESIS_CLOSE);
    }

    default void valuesToCommaSeparated(List<Object[]> values, StringBuilder queryBuilder, SqlFunctionsRender functionsRender) {
        Iterator<Object[]> iterator = values.iterator();
        while (iterator.hasNext()) {
            Object[] rowValues = iterator.next();
            queryBuilder.append(PARENTESIS_OPEN);
            commaSeparetedQuestionMarks(rowValues, queryBuilder, functionsRender);
            if (iterator.hasNext()) {
                queryBuilder.append(PARENTESIS_CLOSE_PLUS_COMMA);
            } else {
                queryBuilder.append(PARENTESIS_CLOSE);
            }
        }
    }

    default void commaSeparetedQuestionMarks(Object[] rowValues, StringBuilder builder, SqlFunctionsRender functionsRender) {
        int last = rowValues.length - 1;
        for (int i=0; i<rowValues.length; i++) {
            Object rowValue = rowValues[i];
            if ( (rowValue instanceof Generator) && ((Generator) rowValue).replaceQuestionMark()) {
                    ((Generator) rowValue).questionMarkReplacement(builder, functionsRender);
            } else {
                builder.append(QUESTION_MARK);
            }
            if (i != last) {
                builder.append(COMMA);
            }
        };
    }

}
