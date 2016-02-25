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
package com.jporm.sql.dsl.query.insert.values;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.ASqlSubElement;
import com.jporm.sql.dsl.query.insert.Insert;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;
import com.jporm.sql.dsl.query.update.set.Generator;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class ValuesImpl extends ASqlSubElement implements Values {

    private final String[] fields;
    private final List<Object[]> values = new ArrayList<>();
    private final Insert insert;

    public ValuesImpl(Insert insert, final String[] fields) {
        this.insert = insert;
        this.fields = fields;
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

    private void columnToCommaSepareted(StringBuilder builder) {
        builder.append("(");
        int i = 0;
        int last = fields.length - 1;
        for (String column : fields) {
            builder.append(column);
            if (i != last) {
                builder.append(", "); //$NON-NLS-1$
                i++;
            }
        };
        builder.append(") ");
    }

    private void valuesToCommaSeparated(StringBuilder queryBuilder, DBProfile dbProfile) {
        Iterator<Object[]> iterator = values.iterator();
        while (iterator.hasNext()) {
            Object[] rowValues = iterator.next();
            queryBuilder.append("(");
            commaSeparetedQuestionMarks(rowValues, queryBuilder, dbProfile);
            if (iterator.hasNext()) {
                queryBuilder.append("), ");
            } else {
                queryBuilder.append(") ");
            }
        }
    }
    private void commaSeparetedQuestionMarks(Object[] rowValues, StringBuilder builder, DBProfile dbProfile) {
        int last = rowValues.length - 1;
        for (int i=0; i<rowValues.length; i++) {
            Object rowValue = rowValues[i];
            if ( (rowValue instanceof Generator) && ((Generator) rowValue).replaceQuestionMark()) {
                    ((Generator) rowValue).questionMarkReplacement(builder, dbProfile);
            } else {
                builder.append("?");
            }
            if (i != last) {
                builder.append(", "); //$NON-NLS-1$
            }
        };
    }

    @Override
    public final void sqlElementQuery(final StringBuilder queryBuilder, DBProfile dbProfile, PropertiesProcessor propertiesProcessor) {
        columnToCommaSepareted(queryBuilder);
        queryBuilder.append("VALUES ");
        valuesToCommaSeparated(queryBuilder, dbProfile);
    }

    @Override
    public Insert values(Object... values) {
        this.values.add(values);
        return insert;
    }

    @Override
    public final List<Object> sqlValues() {
        return insert.sqlValues();
    }

    @Override
    public final void sqlValues(List<Object> values) {
        insert.sqlValues(values);
    }

    @Override
    public final String sqlQuery() {
        return insert.sqlQuery();
    }

    @Override
    public final void sqlQuery(StringBuilder queryBuilder) {
        insert.sqlQuery();
    }

}
