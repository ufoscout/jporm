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
import java.util.List;

import com.jporm.sql.query.SqlSubElement;
import com.jporm.sql.query.insert.Insert;

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

    /**
     * @return the fields
     */
    public String[] getFields() {
        return fields;
    }

    /**
     * @return the values
     */
    public List<Object[]> getValues() {
        return values;
    }

}
