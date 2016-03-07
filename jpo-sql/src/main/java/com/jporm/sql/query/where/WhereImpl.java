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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jporm.sql.query.Sql;
import com.jporm.sql.query.SqlSubElement;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.sql.query.where.expression.Exp;

/**
 *
 * @author Francesco Cina
 *
 *         19/giu/2011
 */
public abstract class WhereImpl<WHERE extends Where<WHERE>> implements Where<WHERE>, SqlSubElement {

    private final List<WhereExpressionElement> elementList = new ArrayList<WhereExpressionElement>();
    private final Sql parentSql;

    public WhereImpl(Sql parentSql) {
        this.parentSql = parentSql;
    }

    private WHERE addExpression(final WhereExpressionElement expressionElement) {
        getElementList().add(expressionElement);
        return getWhere();
    }

    @Override
    public final WHERE allEq(final Map<String, Object> propertyMap) {
        for (final Entry<String, Object> entry : propertyMap.entrySet()) {
            eq(entry.getKey(), entry.getValue());
        }
        return getWhere();
    }

    @Override
    public final WHERE and(final List<WhereExpressionElement> expressionElements) {
        return addExpression(Exp.and(expressionElements));
    }

    @Override
    public final WHERE and(final String customClause, final Object... args) {
        return addExpression(Exp.and(customClause, args));
    }

    @Override
    public final WHERE and(final WhereExpressionElement... expressionElements) {
        if (expressionElements.length > 0) {
            and(Arrays.asList(expressionElements));
        }
        return getWhere();
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        for (final WhereExpressionElement expressionElement : getElementList()) {
            expressionElement.sqlElementValues(values);
        }
    }

    @Override
    public final WHERE eq(final String property, final Object value) {
        return addExpression(Exp.eq(property, value));
    }

    @Override
    public final WHERE eqProperties(final String firstProperty, final String secondProperty) {
        return addExpression(Exp.eqProperties(firstProperty, secondProperty));
    }

    @Override
    public final WHERE ge(final String property, final Object value) {
        return addExpression(Exp.ge(property, value));
    }

    @Override
    public final WHERE geProperties(final String firstProperty, final String secondProperty) {
        return addExpression(Exp.geProperties(firstProperty, secondProperty));
    }

    public final List<WhereExpressionElement> getElementList() {
        return elementList;
    }

    @Override
    public final WHERE gt(final String property, final Object value) {
        return addExpression(Exp.gt(property, value));
    }

    @Override
    public final WHERE gtProperties(final String firstProperty, final String secondProperty) {
        return addExpression(Exp.gtProperties(firstProperty, secondProperty));
    }

    @Override
    public final WHERE ieq(final String property, final String value) {
        return addExpression(Exp.ieq(property, value));
    }

    @Override
    public final WHERE ieqProperties(final String firstProperty, final String secondProperty) {
        return addExpression(Exp.ieqProperties(firstProperty, secondProperty));
    }

    @Override
    public final WHERE ilike(final String property, final String value) {
        return addExpression(Exp.ilike(property, value));
    }

    @Override
    public final WHERE in(final String property, final Collection<?> values) {
        return addExpression(Exp.in(property, values));
    }

    @Override
    public final WHERE in(final String property, final Object... values) {
        return in(property, Arrays.asList(values));
    }

    @Override
    public final WHERE in(final String property, final SelectCommon subQuery) {
        return addExpression(Exp.in(property, subQuery));
    }

    @Override
    public final WHERE isNotNull(final String property) {
        return addExpression(Exp.isNotNull(property));
    }

    @Override
    public final WHERE isNull(final String property) {
        return addExpression(Exp.isNull(property));
    }

    @Override
    public final WHERE le(final String property, final Object value) {
        return addExpression(Exp.le(property, value));
    }

    @Override
    public final WHERE leProperties(final String firstProperty, final String secondProperty) {
        return addExpression(Exp.leProperties(firstProperty, secondProperty));
    }

    @Override
    public final WHERE like(final String property, final String value) {
        return addExpression(Exp.like(property, value));
    }

    @Override
    public final WHERE lt(final String property, final Object value) {
        return addExpression(Exp.lt(property, value));
    }

    @Override
    public final WHERE ltProperties(final String firstProperty, final String secondProperty) {
        return addExpression(Exp.ltProperties(firstProperty, secondProperty));
    }

    @Override
    public final WHERE ne(final String property, final Object value) {
        return addExpression(Exp.ne(property, value));
    }

    @Override
    public final WHERE neProperties(final String firstProperty, final String secondProperty) {
        return addExpression(Exp.neProperties(firstProperty, secondProperty));
    }

    @Override
    public final WHERE nin(final String property, final Collection<?> values) {
        return addExpression(Exp.nin(property, values));
    }

    @Override
    public final WHERE nin(final String property, final Object... values) {
        return nin(property, Arrays.asList(values));
    }

    @Override
    public final WHERE nin(final String property, final SelectCommon subQuery) {
        return addExpression(Exp.nin(property, subQuery));
    }

    @Override
    public final WHERE nlike(final String property, final String value) {
        return addExpression(Exp.nlike(property, value));
    }

    @Override
    public final WHERE not(final List<WhereExpressionElement> expressions) {
        return addExpression(Exp.not(expressions));
    }

    @Override
    public final WHERE not(final String customClause, final Object... args) {
        return addExpression(Exp.not(customClause, args));
    }

    @Override
    public final WHERE not(final WhereExpressionElement... expressions) {
        return addExpression(Exp.not(expressions));
    }

    @Override
    public final WHERE or(final List<WhereExpressionElement> expressionElements) {
        return addExpression(Exp.or(expressionElements));
    }

    @Override
    public final WHERE or(final String customClause, final Object... args) {
        return addExpression(Exp.or(customClause, args));
    }

    @Override
    public final WHERE or(final WhereExpressionElement... expressionElements) {
        return or(Arrays.asList(expressionElements));
    }

    private final WHERE getWhere() {
        return (WHERE) this;
    };

    @Override
    public final void sqlValues(List<Object> values) {
        parentSql.sqlValues(values);
    }

    @Override
    public final void sqlQuery(StringBuilder queryBuilder) {
        parentSql.sqlQuery();
    }

}
