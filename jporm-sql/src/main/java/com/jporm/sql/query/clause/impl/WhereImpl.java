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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jporm.sql.query.ASqlSubElement;
import com.jporm.sql.query.clause.Select;
import com.jporm.sql.query.clause.Where;
import com.jporm.sql.query.clause.WhereExpressionElement;
import com.jporm.sql.query.clause.impl.where.Exp;
import com.jporm.sql.query.namesolver.NameSolver;

/**
 *
 * @author Francesco Cina
 *
 * 19/giu/2011
 */
public class WhereImpl extends ASqlSubElement implements Where {

    private List<WhereExpressionElement> elementList = new ArrayList<WhereExpressionElement>();

    private Where addExpression(final WhereExpressionElement expressionElement) {
        getElementList().add(expressionElement);
        return this;
    }

    @Override
    public final Where allEq(final Map<String, Object> propertyMap) {
        for (final Entry<String, Object> entry : propertyMap.entrySet()) {
            eq(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public final Where and(final WhereExpressionElement... expressionElements) {
    	if (expressionElements.length > 0) {
    		and(Arrays.asList(expressionElements));
		}
    	return this;
    }

    @Override
    public final Where and(final List<WhereExpressionElement> expressionElements) {
    	return addExpression( Exp.and(expressionElements) );
    }

    @Override
    public final Where and(final String customClause, final Object... args) {
    	return addExpression(  Exp.and(customClause, args) );
    }

    @Override
    public final void appendElementValues(final List<Object> values) {
        for (final WhereExpressionElement expressionElement : getElementList()) {
            expressionElement.appendElementValues(values);
        }
    }

    @Override
    public final Where eq(final String property, final Object value) {
    	return addExpression( Exp.eq(property, value) );
    }

    @Override
    public final Where eqProperties(final String firstProperty, final String secondProperty) {
    	return addExpression( Exp.eqProperties(firstProperty, secondProperty) );
    }

    @Override
    public final Where ge(final String property, final Object value) {
    	return addExpression( Exp.ge(property, value) );
    }

    @Override
    public final Where geProperties(final String firstProperty, final String secondProperty) {
    	return addExpression( Exp.geProperties(firstProperty, secondProperty) );
    }

    public final List<WhereExpressionElement> getElementList() {
        return elementList;
    }

    @Override
    public final int getElementStatusVersion() {
        return getElementList().size();
    }

    @Override
    public final Where gt(final String property, final Object value) {
    	return addExpression( Exp.gt(property, value) );
    }

    @Override
    public final Where gtProperties(final String firstProperty, final String secondProperty) {
    	return addExpression( Exp.gtProperties(firstProperty, secondProperty) );
    }

    @Override
    public final Where ieq(final String property, final String value) {
    	return addExpression(  Exp.ieq(property, value) );
    }

    @Override
    public final Where ieqProperties(final String firstProperty, final String secondProperty) {
    	return addExpression(  Exp.ieqProperties(firstProperty, secondProperty) );
    }

    @Override
    public final Where ilike(final String property, final String value) {
    	return addExpression(  Exp.ilike(property, value) );
    }

    @Override
    public final Where in(final String property, final Select subQuery) {
    	return addExpression(  Exp.in(property, subQuery) );
    }

    @Override
    public final Where in(final String property, final Collection<?> values) {
    	return addExpression(  Exp.in(property, values) );
    }

    @Override
    public final Where in(final String property, final Object[] values) {
    	return in(property, Arrays.asList( values ));
    }

    @Override
    public final Where isNotNull(final String property) {
    	return addExpression(  Exp.isNotNull(property) );
    }

    @Override
    public final Where isNull(final String property) {
    	return addExpression(  Exp.isNull(property) );
    }

    @Override
    public final Where le(final String property, final Object value) {
    	return addExpression( Exp.le(property, value) );
    }

    @Override
    public final Where leProperties(final String firstProperty, final String secondProperty) {
    	return addExpression( Exp.leProperties(firstProperty, secondProperty) );
    }

    @Override
    public final Where like(final String property, final String value) {
    	return addExpression(  Exp.like(property, value) );
    }

    @Override
    public final Where lt(final String property, final Object value) {
    	return addExpression( Exp.lt(property, value) );
    }

    @Override
    public final Where ltProperties(final String firstProperty, final String secondProperty) {
    	return addExpression( Exp.ltProperties(firstProperty, secondProperty) );
    }

    @Override
    public final Where ne(final String property, final Object value) {
    	return addExpression(  Exp.ne(property, value) );
    }

    @Override
    public final Where neProperties(final String firstProperty, final String secondProperty) {
    	return addExpression(  Exp.neProperties(firstProperty, secondProperty) );
    }

    @Override
    public final Where nin(final String property, final Select subQuery) {
    	return addExpression(  Exp.nin(property, subQuery) );
    }

    @Override
    public final Where nin(final String property, final Collection<?> values) {
    	return addExpression(  Exp.nin(property, values) );
    }

    @Override
    public final Where nin(final String property, final Object[] values) {
    	return nin(property, Arrays.asList( values ));
    }

    @Override
    public final Where nlike(final String property, final String value) {
    	return addExpression(  Exp.nlike(property, value) );
    }

    @Override
    public final Where not(final WhereExpressionElement... expressions) {
    	return addExpression(  Exp.not(expressions) );
    }

    @Override
    public final Where not(final List<WhereExpressionElement> expressions) {
    	return addExpression(  Exp.not(expressions) );
    }

    @Override
    public final Where not(final String customClause, final Object... args) {
    	return addExpression(  Exp.not(customClause, args) );
    }

    @Override
    public final Where or(final WhereExpressionElement... expressionElements) {
    	return or(Arrays.asList(expressionElements));
    }

    @Override
    public final Where or(final List<WhereExpressionElement> expressionElements) {
    	return addExpression( Exp.or(expressionElements) );
    }

    @Override
    public final Where or(final String customClause, final Object... args) {
        return addExpression(  Exp.or(customClause, args) );
    }

    @Override
    public final void renderSqlElement(final StringBuilder queryBuilder, final NameSolver nameSolver) {
        boolean first = true;
        if (!getElementList().isEmpty()) {
            queryBuilder.append("WHERE "); //$NON-NLS-1$
            for (final WhereExpressionElement expressionElement : getElementList()) {
                if (!first) {
                    queryBuilder.append("AND "); //$NON-NLS-1$
                }
                expressionElement.renderSqlElement(queryBuilder, nameSolver);
                first = false;
            }
        }
    }

}
