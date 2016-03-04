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
package com.jporm.sql.query.insert.values;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jporm.sql.dialect.SqlFunctionsRender;
import com.jporm.sql.query.SqlSubElement;
import com.jporm.sql.query.insert.Insert;
import com.jporm.sql.query.processor.PropertiesProcessor;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class ValuesImpl implements Values, SqlSubElement {

    private final String[] fields;
    private final List<Object[]> values = new ArrayList<>();
    private final Insert insert;
    private final SqlFunctionsRender functionsRender;

    public ValuesImpl(Insert insert, final String[] fields, SqlFunctionsRender functionsRender) {
        this.insert = insert;
        this.fields = fields;
        this.functionsRender = functionsRender;
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        this.values.forEach(valueSet -> {
            for (Object value : valueSet) {
                if ( (value instanceof Generator) ) {
                    if (((Generator) value).hasValue()) {
                        values.add(((Generator) value).getValue());
                    }
                } else {
                    values.add(value);
                }
            }
        });
    }

    private void columnToCommaSepareted(StringBuilder builder, PropertiesProcessor propertiesProcessor) {
        builder.append("(");
        int i = 0;
        int last = fields.length - 1;
        for (String column : fields) {
            builder.append(propertiesProcessor.solvePropertyName(column));
            if (i != last) {
                builder.append(", "); //$NON-NLS-1$
                i++;
            }
        };
        builder.append(") ");
    }

    private void valuesToCommaSeparated(StringBuilder queryBuilder) {
        Iterator<Object[]> iterator = values.iterator();
        while (iterator.hasNext()) {
            Object[] rowValues = iterator.next();
            queryBuilder.append("(");
            commaSeparetedQuestionMarks(rowValues, queryBuilder);
            if (iterator.hasNext()) {
                queryBuilder.append("), ");
            } else {
                queryBuilder.append(") ");
            }
        }
    }
    private void commaSeparetedQuestionMarks(Object[] rowValues, StringBuilder builder) {
        int last = rowValues.length - 1;
        for (int i=0; i<rowValues.length; i++) {
            Object rowValue = rowValues[i];
            if ( (rowValue instanceof Generator) && ((Generator) rowValue).replaceQuestionMark()) {
                    ((Generator) rowValue).questionMarkReplacement(builder, functionsRender);
            } else {
                builder.append("?");
            }
            if (i != last) {
                builder.append(", "); //$NON-NLS-1$
            }
        };
    }

    @Override
    public final void sqlElementQuery(final StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        columnToCommaSepareted(queryBuilder, propertiesProcessor);
        queryBuilder.append("VALUES ");
        valuesToCommaSeparated(queryBuilder);
    }

    @Override
    public Insert values(Object... values) {
        this.values.add(values);
        return insert;
    }

    @Override
    public final void sqlValues(List<Object> values) {
        insert.sqlValues(values);
    }

    @Override
    public final void sqlQuery(StringBuilder queryBuilder) {
        insert.sqlQuery();
    }

}
