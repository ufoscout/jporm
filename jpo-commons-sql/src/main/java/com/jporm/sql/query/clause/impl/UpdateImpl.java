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
package com.jporm.sql.query.clause.impl;

import java.util.List;

import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.ASqlRoot;
import com.jporm.sql.query.clause.Set;
import com.jporm.sql.query.clause.Update;
import com.jporm.sql.query.clause.Where;
import com.jporm.sql.query.namesolver.PropertiesProcessor;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class UpdateImpl extends ASqlRoot implements Update {

    private final SetImpl set = new SetImpl();
    private final WhereImpl where = new WhereImpl();
    private final PropertiesProcessor nameSolver;
    private final String table;

    public UpdateImpl(final PropertiesProcessor nameSolver, final String table) {
        this.table = table;
        this.nameSolver = nameSolver;
    }

    @Override
    public final void appendValues(final List<Object> values) {
        set.appendElementValues(values);
        where.appendElementValues(values);
    }

    @Override
    public final void renderSql(final DBProfile dbProfile, final StringBuilder queryBuilder) {
        queryBuilder.append("UPDATE "); //$NON-NLS-1$
        queryBuilder.append(table);
        queryBuilder.append(" "); //$NON-NLS-1$
        set.renderSqlElement(dbProfile, queryBuilder, nameSolver);
        where.renderSqlElement(dbProfile, queryBuilder, nameSolver);
    }

    @Override
    public Set set() {
        return set;
    }

    @Override
    public Where where() {
        return where;
    }

}
