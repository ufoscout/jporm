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

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;
import com.jporm.sql.query.ASqlSubElement;
import com.jporm.sql.query.clause.GroupBy;
import com.jporm.sql.query.clause.WhereExpressionElement;
import com.jporm.sql.query.clause.impl.where.Exp;

/**
 *
 * @author Francesco Cina
 *
 *         24/giu/2011
 */
public class GroupByImpl extends ASqlSubElement implements GroupBy {

    private String[] fields = new String[0];
    private WhereExpressionElement _exp;

    @Override
    public final void appendElementValues(final List<Object> values) {
        if (_exp != null) {
            _exp.appendElementValues(values);
        }
    }

    @Override
    public final GroupBy fields(final String... fields) {
        this.fields = fields;
        return this;
    }

    @Override
    public final GroupBy having(final String havingClause, final Object... args) {
        _exp = Exp.and(havingClause, args);
        return this;
    }

    @Override
    public final void renderSqlElement(final DBProfile dbprofile, final StringBuilder queryBuilder, final PropertiesProcessor nameSolver) {

        if (fields.length > 0) {
            queryBuilder.append("GROUP BY "); //$NON-NLS-1$
            for (int i = 0; i < fields.length; i++) {
                queryBuilder.append(nameSolver.solvePropertyName(fields[i]));
                if (i < (fields.length - 1)) {
                    queryBuilder.append(", ");
                }
            }
            queryBuilder.append(" ");
            if (_exp != null) {
                queryBuilder.append("HAVING ");
                _exp.renderSqlElement(dbprofile, queryBuilder, nameSolver);
            }
        }
    }

}
