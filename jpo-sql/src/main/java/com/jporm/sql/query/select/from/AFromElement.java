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
package com.jporm.sql.query.select.from;

import java.util.List;

import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.SqlSubElement;
import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.processor.TableName;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public abstract class AFromElement implements SqlSubElement {

    private final TableName tableName;

    public AFromElement(final TableName tableName) {
        this.tableName = tableName;
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
    public final void sqlElementQuery(final StringBuilder queryBuilder, final DBProfile dbProfile, final PropertiesProcessor nameSolver) {
        queryBuilder.append(getJoinName());
        queryBuilder.append(tableName.getTable());
        if (tableName.hasAlias()) {
            queryBuilder.append(" "); //$NON-NLS-1$
            queryBuilder.append(tableName.getAlias());
        }
        if (hasOnClause()) {
            queryBuilder.append(" ON "); //$NON-NLS-1$
            queryBuilder.append(nameSolver.solvePropertyName(onLeftProperty()));
            queryBuilder.append(" = "); //$NON-NLS-1$
            queryBuilder.append(nameSolver.solvePropertyName(onRightProperty()));
        }

        queryBuilder.append(" "); //$NON-NLS-1$
    }

}
