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
package com.jporm.sql.query.groupby;

import java.util.List;

import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.SqlSubElement;
import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.select.Select;
import com.jporm.sql.query.where.WhereExpressionElement;
import com.jporm.sql.query.where.expression.Exp;

/**
 *
 * @author Francesco Cina
 *
 *         24/giu/2011
 */
public abstract class GroupByImpl<GROUP_BY extends GroupBy<GROUP_BY>> implements GroupBy<GROUP_BY>, SqlSubElement {

    private String[] fields = new String[0];
    private WhereExpressionElement _exp;
    private Select<?> select;

    public GroupByImpl(Select<?> select) {
        this.select = select;
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        if (_exp != null) {
            _exp.sqlElementValues(values);
        }
    }

    @Override
    public final void sqlElementQuery(final StringBuilder queryBuilder, final DBProfile dbprofile, final PropertiesProcessor nameSolver) {
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
                _exp.sqlElementQuery(queryBuilder, dbprofile, nameSolver);
            }
        }
    }

    @Override
    public final void sqlValues(List<Object> values) {
        select.sqlValues(values);
    }

    @Override
    public final void sqlQuery(StringBuilder queryBuilder) {
        select.sqlQuery();
    }

    @Override
    public final GROUP_BY fields(String... fields) {
        if (fields.length>0) {
            this.fields = fields;
        }
        return getGroupBy();
    }

    @Override
    public final GROUP_BY having(String havingClause, Object... args) {
        _exp = Exp.and(havingClause, args);
        return getGroupBy();
    }

    private GROUP_BY getGroupBy() {
        return (GROUP_BY) this;
    };

}
