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
package com.jporm.sql.dsl.query.select.groupby;

import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.ASqlSubElement;
import com.jporm.sql.dsl.query.select.Select;
import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.sql.dsl.query.select.SelectUnionsProvider;
import com.jporm.sql.dsl.query.select.orderby.OrderBy;
import com.jporm.sql.dsl.query.where.WhereExpressionElement;
import com.jporm.sql.dsl.query.where.expression.Exp;

/**
 *
 * @author Francesco Cina
 *
 *         24/giu/2011
 */
public class GroupByImpl extends ASqlSubElement implements GroupBy {

    private String[] fields = new String[0];
    private WhereExpressionElement _exp;
    private Select select;

    public GroupByImpl(Select select) {
        this.select = select;
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        if (_exp != null) {
            _exp.sqlElementValues(values);
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
    public final void sqlElementQuery(final StringBuilder queryBuilder, DBProfile dbProfile) {
        if (fields.length > 0) {
            queryBuilder.append("GROUP BY "); //$NON-NLS-1$
            for (int i = 0; i < fields.length; i++) {
                queryBuilder.append(fields[i]);
                if (i < (fields.length - 1)) {
                    queryBuilder.append(", ");
                }
            }
            queryBuilder.append(" ");
            if (_exp != null) {
                queryBuilder.append("HAVING ");
                _exp.sqlElementQuery(queryBuilder, dbProfile);
            }
        }
    }

    @Override
    public String sqlRowCountQuery() {
        return select.sqlRowCountQuery();
    }

    @Override
    public OrderBy orderBy() {
        return select.orderBy();
    }

    @Override
    public SelectUnionsProvider union(SelectCommon select) {
       return this.select.union(select);
    }

    @Override
    public SelectUnionsProvider unionAll(SelectCommon select) {
        return this.select.unionAll(select);
    }

    @Override
    public SelectUnionsProvider except(SelectCommon select) {
        return this.select.except(select);
    }

    @Override
    public SelectUnionsProvider intersect(SelectCommon select) {
        return this.select.intersect(select);
    }

    @Override
    public final List<Object> sqlValues() {
        return select.sqlValues();
    }

    @Override
    public final void sqlValues(List<Object> values) {
        select.sqlValues(values);
    }

    @Override
    public final String sqlQuery() {
        return select.sqlQuery();
    }

    @Override
    public final void sqlQuery(StringBuilder queryBuilder) {
        select.sqlQuery();
    }

}
