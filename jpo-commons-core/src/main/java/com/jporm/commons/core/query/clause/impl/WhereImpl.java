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
package com.jporm.commons.core.query.clause.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jporm.commons.core.query.clause.Where;
import com.jporm.commons.core.query.find.CommonFindQueryRoot;

/**
 *
 * @author Francesco Cina
 *
 *         19/giu/2011
 */
public abstract class WhereImpl<T extends Where<T>> implements Where<T> {

    private final com.jporm.sql.dsl.query.where.Where sqlWhere;

    public WhereImpl(final com.jporm.sql.dsl.query.where.Where sqlWhere) {
        this.sqlWhere = sqlWhere;
    }

    @Override
    public T allEq(final Map<String, Object> propertyMap) {
        sqlWhere.allEq(propertyMap);
        return where();
    }

    @Override
    public T and(final com.jporm.sql.dsl.query.where.WhereExpressionElement... WhereExpressionElements) {
        sqlWhere.and(WhereExpressionElements);
        return where();
    }

    @Override
    public T and(final List<com.jporm.sql.dsl.query.where.WhereExpressionElement> WhereExpressionElements) {
        sqlWhere.and(WhereExpressionElements);
        return where();
    }

    @Override
    public T and(final String customClause, final Object... args) {
        sqlWhere.and(customClause, args);
        return where();
    }

    @Override
    public T eq(final String property, final Object value) {
        sqlWhere.eq(property, value);
        return where();
    }

    @Override
    public T eqProperties(final String firstProperty, final String secondProperty) {
        sqlWhere.eqProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T ge(final String property, final Object value) {
        sqlWhere.ge(property, value);
        return where();
    }

    @Override
    public T geProperties(final String firstProperty, final String secondProperty) {
        sqlWhere.geProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T gt(final String property, final Object value) {
        sqlWhere.gt(property, value);
        return where();
    }

    @Override
    public T gtProperties(final String firstProperty, final String secondProperty) {
        sqlWhere.gtProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T ieq(final String property, final String value) {
        sqlWhere.ieq(property, value);
        return where();
    }

    @Override
    public T ieqProperties(final String firstProperty, final String secondProperty) {
        sqlWhere.ieqProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T ilike(final String property, final String value) {
        sqlWhere.ilike(property, value);
        return where();
    }

    @Override
    public T in(final String property, final Collection<?> values) {
        sqlWhere.in(property, values);
        return where();
    }

    @Override
    public T in(final String property, final CommonFindQueryRoot subQuery) {
        sqlWhere.in(property, subQuery.sql());
        return where();
    }

    @Override
    public T in(final String property, final Object[] values) {
        sqlWhere.in(property, values);
        return where();
    }

    @Override
    public T isNotNull(final String property) {
        sqlWhere.isNotNull(property);
        return where();
    }

    @Override
    public T isNull(final String property) {
        sqlWhere.isNull(property);
        return where();
    }

    @Override
    public T le(final String property, final Object value) {
        sqlWhere.le(property, value);
        return where();
    }

    @Override
    public T leProperties(final String firstProperty, final String secondProperty) {
        sqlWhere.leProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T like(final String property, final String value) {
        sqlWhere.like(property, value);
        return where();
    }

    @Override
    public T lt(final String property, final Object value) {
        sqlWhere.lt(property, value);
        return where();
    }

    @Override
    public T ltProperties(final String firstProperty, final String secondProperty) {
        sqlWhere.ltProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T ne(final String property, final Object value) {
        sqlWhere.ne(property, value);
        return where();
    }

    @Override
    public T neProperties(final String firstProperty, final String secondProperty) {
        sqlWhere.neProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T nin(final String property, final Collection<?> values) {
        sqlWhere.nin(property, values);
        return where();
    }

    @Override
    public T nin(final String property, final CommonFindQueryRoot subQuery) {
        sqlWhere.nin(property, subQuery.sql());
        return where();
    }

    @Override
    public T nin(final String property, final Object[] values) {
        sqlWhere.nin(property, values);
        return where();
    }

    @Override
    public T nlike(final String property, final String value) {
        sqlWhere.nlike(property, value);
        return where();
    }

    @Override
    public T not(final com.jporm.sql.dsl.query.where.WhereExpressionElement... expression) {
        sqlWhere.not(expression);
        return where();
    }

    @Override
    public T not(final List<com.jporm.sql.dsl.query.where.WhereExpressionElement> whereExpressionElements) {
        sqlWhere.not(whereExpressionElements);
        return where();
    }

    @Override
    public T not(final String customClause, final Object... args) {
        sqlWhere.not(customClause, args);
        return where();
    }

    @Override
    public T or(final com.jporm.sql.dsl.query.where.WhereExpressionElement... whereExpressionElements) {
        sqlWhere.or(whereExpressionElements);
        return where();
    }

    @Override
    public T or(final List<com.jporm.sql.dsl.query.where.WhereExpressionElement> whereExpressionElements) {
        sqlWhere.or(whereExpressionElements);
        return where();
    }

    @Override
    public T or(final String customClause, final Object... args) {
        sqlWhere.or(customClause, args);
        return where();
    }

    protected abstract T where();

}
