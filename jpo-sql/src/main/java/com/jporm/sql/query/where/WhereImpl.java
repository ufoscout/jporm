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
package com.jporm.sql.query.where;

import java.util.List;

import com.jporm.sql.query.Sql;
import com.jporm.sql.query.SqlSubElement;

/**
 *
 * @author Francesco Cina
 *
 *         19/giu/2011
 */
public abstract class WhereImpl<WHERE extends Where<WHERE>> implements WhereDefault<WHERE>, SqlSubElement {

    private final Sql parentSql;
    private final WhereExpressionBuilderImpl expressionBuilder = new WhereExpressionBuilderImpl(false);

    public WhereImpl(Sql parentSql) {
        this.parentSql = parentSql;
    }

    @Override
    public final WHERE where() {
        return (WHERE) this;
    }

    @Override
    public void sqlElementValues(List<Object> values) {
        expressionBuilder.sqlElementValues(values);
    }

    @Override
    public final void sqlValues(List<Object> values) {
        parentSql.sqlValues(values);
    }

    @Override
    public final void sqlQuery(StringBuilder queryBuilder) {
        parentSql.sqlQuery(queryBuilder);
    }

    @Override
    public final WhereExpressionBuilderImpl whereImplementation() {
        return expressionBuilder;
    }
}
