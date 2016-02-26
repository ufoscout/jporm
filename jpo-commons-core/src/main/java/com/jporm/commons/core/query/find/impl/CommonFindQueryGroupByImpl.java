/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.commons.core.query.find.impl;

import java.util.List;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.clause.impl.GroupByImpl;
import com.jporm.commons.core.query.find.CommonFindQuery;
import com.jporm.commons.core.query.find.CommonFindQueryGroupBy;
import com.jporm.commons.core.query.find.CommonFindQueryOrderBy;
import com.jporm.commons.core.query.find.CommonFindQueryRoot;
import com.jporm.commons.core.query.find.CommonFindQueryWhere;
import com.jporm.sql.dsl.query.select.groupby.GroupBy;
import com.jporm.sql.dsl.query.where.WhereExpressionElement;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Mar 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class CommonFindQueryGroupByImpl<FIND extends CommonFindQuery<FIND, WHERE, ORDER_BY>, WHERE extends CommonFindQueryWhere<FIND, WHERE, ORDER_BY>, ORDER_BY extends CommonFindQueryOrderBy<FIND, WHERE, ORDER_BY>, GROUP_BY extends CommonFindQueryGroupBy<FIND, WHERE, ORDER_BY, GROUP_BY>>
        extends GroupByImpl<GROUP_BY> implements CommonFindQueryGroupBy<FIND, WHERE, ORDER_BY, GROUP_BY> {

    private final FIND customFindQuery;

    public CommonFindQueryGroupByImpl(final GroupBy sqlGroupBy, final FIND customFindQuery) {
        super(sqlGroupBy);
        this.customFindQuery = customFindQuery;
    }

    @Override
    public final FIND distinct() throws JpoException {
        return customFindQuery.distinct();
    }

    @Override
    public FIND forUpdate() {
        return customFindQuery.forUpdate();
    }

    @Override
    public FIND forUpdateNoWait() {
        return customFindQuery.forUpdateNoWait();
    }

    @Override
    public final FIND limit(final int maxRows) throws JpoException {
        return customFindQuery.limit(maxRows);
    }

    @Override
    public final FIND offset(final int firstRow) throws JpoException {
        return customFindQuery.offset(firstRow);
    }

    @Override
    public final ORDER_BY orderBy() throws JpoException {
        return customFindQuery.orderBy();
    }

    @Override
    public final FIND root() {
        return customFindQuery;
    }

    @Override
    protected GROUP_BY sqlQuery() {
        return (GROUP_BY) this;
    }

    @Override
    public final WHERE where(final List<WhereExpressionElement> expressionElements) {
        return customFindQuery.where(expressionElements);
    }

    @Override
    public final WHERE where(final String customClause, final Object... args) {
        return customFindQuery.where(customClause, args);
    }

    @Override
    public final WHERE where(final WhereExpressionElement... expressionElements) {
        return customFindQuery.where(expressionElements);
    }

    @Override
    public FIND union(CommonFindQueryRoot select) {
        return customFindQuery.union(select);
    }

    @Override
    public FIND unionAll(CommonFindQueryRoot select) {
        return customFindQuery.unionAll(select);
    }

//    @Override
//    public FIND except(CommonFindQueryRoot select) {
//        return customFindQuery.except(select);
//    }

//    @Override
//    public FIND intersect(CommonFindQueryRoot select) {
//        return customFindQuery.intersect(select);
//    }

}
