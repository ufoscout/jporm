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
package com.jporm.sql.dsl.query.select.from;

import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.ASqlSubElement;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public abstract class AFromElement extends ASqlSubElement {

    private final String table;
    private final String alias;

    public AFromElement(final String table, final String alias) {
        this.table = table;
        this.alias = alias;
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        // do nothing
    }

    protected abstract String getJoinName();

    protected abstract boolean hasOnClause();

    protected abstract String onLeftProperty();

    protected abstract String onRightProperty();

    @Override
    public final void sqlElementQuery(final StringBuilder queryBuilder, DBProfile dbProfile) {
        queryBuilder.append(getJoinName());
        queryBuilder.append(table);
        if (!alias.isEmpty()) {
            queryBuilder.append(" ");
            queryBuilder.append(alias);
        }

        if (hasOnClause()) {
            queryBuilder.append(" ON ");
            queryBuilder.append(onLeftProperty());
            queryBuilder.append(" = ");
            queryBuilder.append(onRightProperty());
        }

        queryBuilder.append(" ");
    }

}
