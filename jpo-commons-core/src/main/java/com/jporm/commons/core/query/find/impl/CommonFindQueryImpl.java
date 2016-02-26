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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.jporm.annotation.exception.JpoWrongPropertyNameException;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ClassToolMap;
import com.jporm.commons.core.query.clause.From;
import com.jporm.commons.core.query.find.CommonFindQuery;
import com.jporm.commons.core.query.find.CommonFindQueryOrderBy;
import com.jporm.commons.core.query.find.CommonFindQueryRoot;
import com.jporm.commons.core.query.find.CommonFindQueryWhere;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.query.select.LockMode;
import com.jporm.sql.dsl.query.select.Select;
import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.sql.dsl.query.where.WhereExpressionElement;

/**
 *
 * @author Francesco Cina
 *
 *         20/giu/2011
 */
public class CommonFindQueryImpl<FIND extends CommonFindQuery<FIND, WHERE, ORDER_BY>, WHERE extends CommonFindQueryWhere<FIND, WHERE, ORDER_BY>, ORDER_BY extends CommonFindQueryOrderBy<FIND, WHERE, ORDER_BY>>
        implements CommonFindQuery<FIND, WHERE, ORDER_BY> {

    private final Class<?> clazz;
    private final Select select;
    private final String[] allColumnNames;
    private WHERE where;
    private ORDER_BY orderBy;
    private From<FIND> from;
    private List<String> _ignoredFields = Collections.EMPTY_LIST;

    public CommonFindQueryImpl(final Class<?> clazz, final String alias, final SqlFactory sqlFactory, final ClassToolMap classToolMap) {
        this.clazz = clazz;
        select = sqlFactory.select(clazz, alias);
        allColumnNames = classToolMap.get(clazz).getDescriptor().getAllColumnJavaNames();
    }

    @Override
    public final FIND distinct() {
        getSelect().distinct(true);
        return query();
    }

    @Override
    public FIND forUpdate() {
        getSelect().lockMode(LockMode.FOR_UPDATE);
        return query();
    }

    @Override
    public FIND forUpdateNoWait() {
        getSelect().lockMode(LockMode.FOR_UPDATE_NOWAIT);
        return query();
    }

    @Override
    public final FIND fullOuterJoin(final Class<?> joinClass) {
        return this.from.fullOuterJoin(joinClass);
    }

    @Override
    public final FIND fullOuterJoin(final Class<?> joinClass, final String joinClassAlias) {
        return this.from.fullOuterJoin(joinClass, joinClassAlias);
    }

    @Override
    public final FIND fullOuterJoin(final Class<?> joinClass, final String onLeftProperty, final String onRigthProperty) {
        return this.from.fullOuterJoin(joinClass, onLeftProperty, onRigthProperty);
    }

    @Override
    public final FIND fullOuterJoin(final Class<?> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
        return this.from.fullOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
    }

    /**
     * @return the allColumns
     */
    public String[] getAllColumns() {
        return allColumnNames;
    }

    /**
     * @return the from
     */
    public From<FIND> getFrom() {
        return from;
    }

    /**
     * @return the _ignoredFields
     */
    public List<String> getIgnoredFields() {
        return _ignoredFields;
    }

    /**
     * @return the orderBy
     */
    public ORDER_BY getOrderBy() {
        return orderBy;
    }

    /**
     * @return the select
     */
    public Select getSelect() {
        return select;
    }

    /**
     * @return the where
     */
    public WHERE getWhere() {
        return where;
    }

    @Override
    public final FIND ignore(final boolean ignoreFieldsCondition, final String... fields) {
        if (ignoreFieldsCondition && (fields.length > 0)) {
            _ignoredFields = Arrays.asList(fields);
            List<String> selectedColumns = new ArrayList<>();
            for (int i = 0; i < getAllColumns().length; i++) {
                selectedColumns.add(getAllColumns()[i]);
            }
            selectedColumns.removeAll(_ignoredFields);
            if (getAllColumns().length != (selectedColumns.size() + fields.length)) {
                throw new JpoWrongPropertyNameException("One of the specified fields is not a property of [" + clazz.getName() + "]");
            }
            getSelect().selectFields(selectedColumns.toArray(new String[0]));
        }
        return query();
    }

    @Override
    public final FIND ignore(final String... fields) {
        return ignore(true, fields);
    }

    @Override
    public final FIND innerJoin(final Class<?> joinClass) {
        return this.from.innerJoin(joinClass);
    }

    @Override
    public final FIND innerJoin(final Class<?> joinClass, final String joinClassAlias) {
        return this.from.innerJoin(joinClass, joinClassAlias);
    }

    @Override
    public final FIND innerJoin(final Class<?> joinClass, final String onLeftProperty, final String onRigthProperty) {
        return this.from.innerJoin(joinClass, onLeftProperty, onRigthProperty);
    }

    @Override
    public final FIND innerJoin(final Class<?> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
        return this.from.innerJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
    }

    @Override
    public final FIND join(final Class<?> joinClass) {
        return this.from.join(joinClass);
    }

    @Override
    public final FIND join(final Class<?> joinClass, final String joinClassAlias) {
        return this.from.join(joinClass, joinClassAlias);
    }

    @Override
    public final FIND leftOuterJoin(final Class<?> joinClass) {
        return this.from.leftOuterJoin(joinClass);
    }

    @Override
    public final FIND leftOuterJoin(final Class<?> joinClass, final String joinClassAlias) {
        return this.from.leftOuterJoin(joinClass, joinClassAlias);
    }

    @Override
    public final FIND leftOuterJoin(final Class<?> joinClass, final String onLeftProperty, final String onRigthProperty) {
        return this.from.leftOuterJoin(joinClass, onLeftProperty, onRigthProperty);
    }

    @Override
    public final FIND leftOuterJoin(final Class<?> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
        return this.from.leftOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
    }

    @Override
    public final FIND limit(final int maxRows) throws JpoException {
        getSelect().limit(maxRows);
        return query();
    }

    @Override
    public final FIND naturalJoin(final Class<?> joinClass) {
        return this.from.naturalJoin(joinClass);
    }

    @Override
    public final FIND naturalJoin(final Class<?> joinClass, final String joinClassAlias) {
        return this.from.naturalJoin(joinClass, joinClassAlias);
    }

    @Override
    public final FIND offset(final int firstRow) throws JpoException {
        getSelect().offset(firstRow);
        return query();
    }

    @Override
    public final ORDER_BY orderBy() throws JpoException {
        return this.orderBy;
    }

    protected final FIND query() {
        return (FIND) this;
    }

    @Override
    public final FIND rightOuterJoin(final Class<?> joinClass) {
        return this.from.rightOuterJoin(joinClass);
    }

    @Override
    public final FIND rightOuterJoin(final Class<?> joinClass, final String joinClassAlias) {
        return this.from.rightOuterJoin(joinClass, joinClassAlias);
    }

    @Override
    public final FIND rightOuterJoin(final Class<?> joinClass, final String onLeftProperty, final String onRigthProperty) {
        return this.from.rightOuterJoin(joinClass, onLeftProperty, onRigthProperty);
    }

    @Override
    public final FIND rightOuterJoin(final Class<?> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
        return this.from.rightOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
    }

    /**
     * @param from
     *            the from to set
     */
    public void setFrom(final From<FIND> from) {
        this.from = from;
    }

    /**
     * @param _ignoredFields
     *            the _ignoredFields to set
     */
    public void setIgnoredFields(final List<String> _ignoredFields) {
        this._ignoredFields = _ignoredFields;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(final ORDER_BY orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * @param where
     *            the where to set
     */
    public void setWhere(final WHERE where) {
        this.where = where;
    }

    @Override
    public SelectCommon sql() {
        return select;
    }

    @Override
    public final WHERE where(final List<WhereExpressionElement> expressionElements) {
        where.and(expressionElements);
        return where;
    }

    @Override
    public final WHERE where(final String customClause, final Object... args) {
        where.and(customClause, args);
        return where;
    }

    @Override
    public final WHERE where(final WhereExpressionElement... expressionElements) {
        if (expressionElements.length > 0) {
            where.and(expressionElements);
        }
        return where;
    }

    @Override
    public FIND union(CommonFindQueryRoot select) {
        getSelect().union(select.sql());
        return query();
    }

    @Override
    public FIND unionAll(CommonFindQueryRoot select) {
        getSelect().unionAll(select.sql());
        return query();
    }

//    @Override
//    public FIND except(CommonFindQueryRoot select) {
//        getSelect().except(select.sql());
//        return query();
//    }

//    @Override
//    public FIND intersect(CommonFindQueryRoot select) {
//        getSelect().intersect(select.sql());
//        return query();
//    }

}
