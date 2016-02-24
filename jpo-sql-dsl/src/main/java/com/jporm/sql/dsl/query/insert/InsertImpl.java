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
package com.jporm.sql.dsl.query.insert;

import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.ASql;
import com.jporm.sql.dsl.query.insert.values.ValuesImpl;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class InsertImpl extends ASql implements Insert {

    private final ValuesImpl elemValues;
    private final String intoTable;
    private final DBProfile dbProfile;

    public InsertImpl(DBProfile dbProfile, final String[] fields, final String intoTable) {
        this.dbProfile = dbProfile;
        this.intoTable = intoTable;
        elemValues = new ValuesImpl(this, fields);
    }

    @Override
    public final void sqlValues(final List<Object> values) {
        elemValues.sqlElementValues(values);
    }

    @Override
    public final void sqlQuery(final StringBuilder queryBuilder) {
        queryBuilder.append("INSERT INTO ");
        queryBuilder.append(intoTable);
        queryBuilder.append(" ");
        elemValues.sqlElementQuery(queryBuilder, dbProfile);
    }

    @Override
    public Insert values(final Object... values) {
        elemValues.values(values);
        return this;
    }
}
