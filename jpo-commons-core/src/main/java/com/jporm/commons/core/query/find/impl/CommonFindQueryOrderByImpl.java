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
package com.jporm.commons.core.query.find.impl;

import java.util.List;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.clause.impl.OrderByImpl;
import com.jporm.commons.core.query.find.CommonFindQuery;
import com.jporm.commons.core.query.find.CommonFindQueryOrderBy;
import com.jporm.commons.core.query.find.CommonFindQueryWhere;
import com.jporm.sql.query.clause.WhereExpressionElement;

/**
 *
 * @author ufo
 *
 * @param <BEAN>
 */
public class CommonFindQueryOrderByImpl<FIND extends CommonFindQuery<FIND, WHERE, ORDER_BY>, WHERE extends CommonFindQueryWhere<FIND, WHERE, ORDER_BY>, ORDER_BY extends CommonFindQueryOrderBy<FIND, WHERE, ORDER_BY>>
        extends OrderByImpl<ORDER_BY> implements CommonFindQueryOrderBy<FIND, WHERE, ORDER_BY> {

    private final FIND findQuery;

    public CommonFindQueryOrderByImpl(final com.jporm.sql.query.clause.OrderBy sqlOrderBy, final FIND findQuery) {
        super(sqlOrderBy);
        this.findQuery = findQuery;
    }

    @Override
    public final FIND distinct() throws JpoException {
        return this.findQuery.distinct();
    }

    @Override
    public FIND forUpdate() {
        return this.findQuery.forUpdate();
    }

    @Override
    public FIND forUpdateNoWait() {
        return this.findQuery.forUpdateNoWait();
    }

    @Override
    public final FIND limit(final int maxRows) throws JpoException {
        return this.findQuery.limit(maxRows);
    }

    @Override
    public final FIND offset(final int firstRow) throws JpoException {
        return this.findQuery.offset(firstRow);
    }

    @Override
    protected final ORDER_BY orderBy() {
        return findQuery.orderBy();
    }

    @Override
    public final FIND root() {
        return this.findQuery;
    }

    @Override
    public final WHERE where(final List<WhereExpressionElement> expressionElements) {
        return findQuery.where(expressionElements);
    }

    @Override
    public final WHERE where(final String customClause, final Object... args) {
        return findQuery.where(customClause, args);
    }

    @Override
    public final WHERE where(final WhereExpressionElement... expressionElements) {
        return findQuery.where(expressionElements);
    }

}
