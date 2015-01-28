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
package com.jporm.core.query.clause.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jporm.core.query.AQuerySubElement;
import com.jporm.core.query.clause.Where;
import com.jporm.core.query.clause.WhereExpressionElement;
import com.jporm.core.query.clause.impl.where.Exp;
import com.jporm.core.query.find.FindQueryRoot;
import com.jporm.core.query.namesolver.NameSolver;

/**
 * 
 * @author Francesco Cina
 *
 * 19/giu/2011
 */
public abstract class WhereImpl<T extends Where<?>> extends AQuerySubElement implements Where<T> {

    private List<WhereExpressionElement> elementList = new ArrayList<WhereExpressionElement>();

    private T addExpression(final WhereExpressionElement expressionElement) {
        this.getElementList().add(expressionElement);
        return where();
    }

    @Override
    public final T allEq(final Map<String, Object> propertyMap) {
        for (final Entry<String, Object> entry : propertyMap.entrySet()) {
            eq(entry.getKey(), entry.getValue());
        }
        return where();
    }

    @Override
    public final T and(final WhereExpressionElement... expressionElements) {
        return and(Arrays.asList(expressionElements));
    }

    @Override
    public final T and(final List<WhereExpressionElement> expressionElements) {
        return addExpression( Exp.and(expressionElements) );
    }

    @Override
    public final T and(final String customClause, final Object... args) {
        return addExpression(  Exp.and(customClause, args) );
    }

    @Override
    public final void appendElementValues(final List<Object> values) {
        for (final WhereExpressionElement expressionElement : this.getElementList()) {
            expressionElement.appendElementValues(values);
        }
    }

    @Override
    public final T eq(final String property, final Object value) {
        return addExpression( Exp.eq(property, value) );
    }

    @Override
    public final T eqProperties(final String firstProperty, final String secondProperty) {
        return addExpression( Exp.eqProperties(firstProperty, secondProperty) );
    }

    @Override
    public final T ge(final String property, final Object value) {
        return addExpression( Exp.ge(property, value) );
    }

    @Override
    public final T geProperties(final String firstProperty, final String secondProperty) {
        return addExpression( Exp.geProperties(firstProperty, secondProperty) );
    }

    public List<WhereExpressionElement> getElementList() {
        return elementList;
    }

    @Override
    public final int getElementStatusVersion() {
        return this.getElementList().size();
    }

    @Override
    public final T gt(final String property, final Object value) {
        return addExpression( Exp.gt(property, value) );
    }

    @Override
    public final T gtProperties(final String firstProperty, final String secondProperty) {
        return addExpression( Exp.gtProperties(firstProperty, secondProperty) );
    }

    @Override
    public final T ieq(final String property, final String value) {
        return addExpression(  Exp.ieq(property, value) );
    }

    @Override
    public final T ieqProperties(final String firstProperty, final String secondProperty) {
        return addExpression(  Exp.ieqProperties(firstProperty, secondProperty) );
    }

    @Override
    public final T ilike(final String property, final String value) {
        return addExpression(  Exp.ilike(property, value) );
    }

    @Override
    public final T in(final String property, final FindQueryRoot subQuery) {
        return addExpression(  Exp.in(property, subQuery) );
    }

    @Override
    public final T in(final String property, final Collection<?> values) {
        return addExpression(  Exp.in(property, values) );
    }

    @Override
    public final T in(final String property, final Object[] values) {
        return in(property, Arrays.asList( values ));
    }

    @Override
    public final T isNotNull(final String property) {
        return addExpression(  Exp.isNotNull(property) );
    }

    @Override
    public final T isNull(final String property) {
        return addExpression(  Exp.isNull(property) );
    }

    @Override
    public final T le(final String property, final Object value) {
        return addExpression( Exp.le(property, value) );
    }

    @Override
    public final T leProperties(final String firstProperty, final String secondProperty) {
        return addExpression( Exp.leProperties(firstProperty, secondProperty) );
    }

    @Override
    public final T like(final String property, final String value) {
        return addExpression(  Exp.like(property, value) );
    }

    @Override
    public final T lt(final String property, final Object value) {
        return addExpression( Exp.lt(property, value) );
    }

    @Override
    public final T ltProperties(final String firstProperty, final String secondProperty) {
        return addExpression( Exp.ltProperties(firstProperty, secondProperty) );
    }

    @Override
    public final T ne(final String property, final Object value) {
        return addExpression(  Exp.ne(property, value) );
    }

    @Override
    public final T neProperties(final String firstProperty, final String secondProperty) {
        return addExpression(  Exp.neProperties(firstProperty, secondProperty) );
    }

    @Override
    public final T nin(final String property, final FindQueryRoot subQuery) {
        return addExpression(  Exp.nin(property, subQuery) );
    }

    @Override
    public final T nin(final String property, final Collection<?> values) {
        return addExpression(  Exp.nin(property, values) );
    }

    @Override
    public final T nin(final String property, final Object[] values) {
        return nin(property, Arrays.asList( values ));
    }

    @Override
    public final T nlike(final String property, final String value) {
        return addExpression(  Exp.nlike(property, value) );
    }

    @Override
    public final T not(final WhereExpressionElement... expressions) {
        return addExpression(  Exp.not(expressions) );
    }

    @Override
    public final T not(final List<WhereExpressionElement> expressions) {
        return addExpression(  Exp.not(expressions) );
    }

    @Override
    public final T not(final String customClause, final Object... args) {
        return addExpression(  Exp.not(customClause, args) );
    }

    @Override
    public final T or(final WhereExpressionElement... expressionElements) {
        return or(Arrays.asList(expressionElements));
    }

    @Override
    public final T or(final List<WhereExpressionElement> expressionElements) {
        return addExpression( Exp.or(expressionElements) );
    }

    @Override
    public final T or(final String customClause, final Object... args) {
        return addExpression(  Exp.or(customClause, args) );
    }

    @Override
    public final void renderSqlElement(final StringBuilder queryBuilder, final NameSolver nameSolver) {
        boolean first = true;
        if (!this.getElementList().isEmpty()) {
            queryBuilder.append("WHERE "); //$NON-NLS-1$
            for (final WhereExpressionElement expressionElement : this.getElementList()) {
                if (!first) {
                    queryBuilder.append("AND "); //$NON-NLS-1$
                }
                expressionElement.renderSqlElement(queryBuilder, nameSolver);
                first = false;
            }
        }
    }

    protected abstract T where();

}
