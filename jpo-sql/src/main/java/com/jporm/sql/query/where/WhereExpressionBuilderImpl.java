/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.sql.query.where.expression.ConnectorElement;
import com.jporm.sql.query.where.expression.CustomExpressionElement;
import com.jporm.sql.query.where.expression.EqExpressionElement;
import com.jporm.sql.query.where.expression.EqPropertiesExpressionElement;
import com.jporm.sql.query.where.expression.GeExpressionElement;
import com.jporm.sql.query.where.expression.GePropertiesExpressionElement;
import com.jporm.sql.query.where.expression.GtExpressionElement;
import com.jporm.sql.query.where.expression.GtPropertiesExpressionElement;
import com.jporm.sql.query.where.expression.IEqExpressionElement;
import com.jporm.sql.query.where.expression.IEqPropertiesExpressionElement;
import com.jporm.sql.query.where.expression.ILikeExpressionElement;
import com.jporm.sql.query.where.expression.InExpressionElement;
import com.jporm.sql.query.where.expression.InSubQueryExpressionElement;
import com.jporm.sql.query.where.expression.IsNotNullExpressionElement;
import com.jporm.sql.query.where.expression.IsNullExpressionElement;
import com.jporm.sql.query.where.expression.LeExpressionElement;
import com.jporm.sql.query.where.expression.LePropertiesExpressionElement;
import com.jporm.sql.query.where.expression.LikeExpressionElement;
import com.jporm.sql.query.where.expression.LtExpressionElement;
import com.jporm.sql.query.where.expression.LtPropertiesExpressionElement;
import com.jporm.sql.query.where.expression.NInExpressionElement;
import com.jporm.sql.query.where.expression.NInSubQueryExpressionElement;
import com.jporm.sql.query.where.expression.NLikeExpressionElement;
import com.jporm.sql.query.where.expression.NeExpressionElement;
import com.jporm.sql.query.where.expression.NePropertiesExpressionElement;
import com.jporm.sql.query.where.expression.NoOpsElement;

public class WhereExpressionBuilderImpl implements WhereExpressionBuilder {

    private static final String CLOSE_PARENTESIS = ") ";
    private static final String OPEN_PARENTESIS = "( ";
    private final static WhereExpressionElement CONNECTOR_AND = new ConnectorElement("AND ");
    private final static WhereExpressionElement CONNECTOR_OR = new ConnectorElement("OR ");
    private final static WhereExpressionElement CONNECTOR_NOT = new ConnectorElement("NOT ");
    private final static WhereExpressionElement CONNECTOR_OR_NOT = new ConnectorElement("OR NOT ");
    private final static WhereExpressionElement CONNECTOR_AND_NOT = new ConnectorElement("AND NOT ");
    private final static WhereExpressionElement CONNECTOR_EMPTY = new NoOpsElement();

    private final List<WhereExpressionElement> elementList = new ArrayList<WhereExpressionElement>();
    private final boolean wrappedIntoParentesisIfMultipleElements;
    private WhereExpressionElement connector = CONNECTOR_EMPTY;

    public WhereExpressionBuilderImpl(boolean wrappedIntoParentesisIfMultipleElements) {
        this.wrappedIntoParentesisIfMultipleElements = wrappedIntoParentesisIfMultipleElements;
    }

    /**
     * TODO Use strategy or state pattern instead
     * @param nextConnector
     */
    private void updateNextConnector(WhereExpressionElement nextConnector) {
        if (CONNECTOR_AND.equals(connector) && CONNECTOR_NOT.equals(nextConnector) ) {
            connector = CONNECTOR_AND_NOT;
        } else if (CONNECTOR_OR.equals(connector) && CONNECTOR_NOT.equals(nextConnector) ) {
            connector = CONNECTOR_OR_NOT;
        } else if (CONNECTOR_AND_NOT.equals(connector) && CONNECTOR_NOT.equals(nextConnector)) {
            connector = CONNECTOR_AND;
        } else if (CONNECTOR_OR_NOT.equals(connector) && CONNECTOR_NOT.equals(nextConnector)) {
            connector = CONNECTOR_OR;
        } else if ((CONNECTOR_AND.equals(nextConnector) || CONNECTOR_OR.equals(nextConnector)) && elementList.isEmpty()) {
            // do nothing
        } else {
            connector = nextConnector;
        }
    }

    private WhereExpressionBuilder addExpression(final WhereExpressionElement expressionElement) {
        elementList.add(connector);
        elementList.add(expressionElement);
        updateNextConnector(CONNECTOR_AND);
        return this;
    }

    @Override
    public final WhereExpressionBuilder allEq(final Map<String, Object> propertyMap) {
        for (final Entry<String, Object> entry : propertyMap.entrySet()) {
            eq(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public final WhereExpressionBuilder and() {
        updateNextConnector(CONNECTOR_AND);
        return this;
    }

    @Override
    public final WhereExpressionBuilder and(final String customClause, final Object... args) {
        updateNextConnector(CONNECTOR_AND);
        return addExpression(new CustomExpressionElement(customClause, args));
    }

    @Override
    public final WhereExpressionBuilder and(final WhereExpressionBuilder whereExpression) {
        updateNextConnector(CONNECTOR_AND);
        return addExpression(whereExpression);
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        for (final WhereExpressionElement expressionElement : elementList) {
            expressionElement.sqlElementValues(values);
        }
    }

    @Override
    public final WhereExpressionBuilder eq(final String property, final Object value) {
        return addExpression(new EqExpressionElement(property, value));
    }

    @Override
    public final WhereExpressionBuilder eqProperties(final String firstProperty, final String secondProperty) {
        return addExpression(new EqPropertiesExpressionElement(firstProperty, secondProperty));
    }

    @Override
    public final WhereExpressionBuilder ge(final String property, final Object value) {
        return addExpression(new GeExpressionElement(property, value));
    }

    @Override
    public final WhereExpressionBuilder geProperties(final String firstProperty, final String secondProperty) {
        return addExpression(new GePropertiesExpressionElement(firstProperty, secondProperty));
    }

    @Override
    public final WhereExpressionBuilder gt(final String property, final Object value) {
        return addExpression(new GtExpressionElement(property, value));
    }

    @Override
    public final WhereExpressionBuilder gtProperties(final String firstProperty, final String secondProperty) {
        return addExpression(new GtPropertiesExpressionElement(firstProperty, secondProperty));
    }

    @Override
    public final WhereExpressionBuilder ieq(final String property, final String value) {
        return addExpression(new IEqExpressionElement(property, value));
    }

    @Override
    public final WhereExpressionBuilder ieqProperties(final String firstProperty, final String secondProperty) {
        return addExpression(new IEqPropertiesExpressionElement(firstProperty, secondProperty));
    }

    @Override
    public final WhereExpressionBuilder ilike(final String property, final String value) {
        return addExpression(new ILikeExpressionElement(property, value));
    }

    @Override
    public final WhereExpressionBuilder in(final String property, final Collection<?> values) {
        return addExpression(new InExpressionElement(property, values));
    }

    @Override
    public final WhereExpressionBuilder in(final String property, final Object... values) {
        return addExpression(new InExpressionElement(property, values));
    }

    @Override
    public final WhereExpressionBuilder in(final String property, final SelectCommon subQuery) {
        return addExpression(new InSubQueryExpressionElement(property, subQuery));
    }

    @Override
    public final WhereExpressionBuilder isNotNull(final String property) {
        return addExpression(new IsNotNullExpressionElement(property));
    }

    @Override
    public final WhereExpressionBuilder isNull(final String property) {
        return addExpression(new IsNullExpressionElement(property));
    }

    @Override
    public final WhereExpressionBuilder le(final String property, final Object value) {
        return addExpression(new LeExpressionElement(property, value));
    }

    @Override
    public final WhereExpressionBuilder leProperties(final String firstProperty, final String secondProperty) {
        return addExpression(new LePropertiesExpressionElement(firstProperty, secondProperty));
    }

    @Override
    public final WhereExpressionBuilder like(final String property, final String value) {
        return addExpression(new LikeExpressionElement(property, value));
    }

    @Override
    public final WhereExpressionBuilder lt(final String property, final Object value) {
        return addExpression(new LtExpressionElement(property, value));
    }

    @Override
    public final WhereExpressionBuilder ltProperties(final String firstProperty, final String secondProperty) {
        return addExpression(new LtPropertiesExpressionElement(firstProperty, secondProperty));
    }

    @Override
    public final WhereExpressionBuilder ne(final String property, final Object value) {
        return addExpression(new NeExpressionElement(property, value));
    }

    @Override
    public final WhereExpressionBuilder neProperties(final String firstProperty, final String secondProperty) {
        return addExpression(new NePropertiesExpressionElement(firstProperty, secondProperty));
    }

    @Override
    public final WhereExpressionBuilder nin(final String property, final Collection<?> values) {
        return addExpression(new NInExpressionElement(property, values));
    }

    @Override
    public final WhereExpressionBuilder nin(final String property, final Object... values) {
        return addExpression(new NInExpressionElement(property, values));
    }

    @Override
    public final WhereExpressionBuilder nin(final String property, final SelectCommon subQuery) {
        return addExpression(new NInSubQueryExpressionElement(property, subQuery));
    }

    @Override
    public final WhereExpressionBuilder nlike(final String property, final String value) {
        return addExpression(new NLikeExpressionElement(property, value));
    }

    @Override
    public final WhereExpressionBuilder not() {
        updateNextConnector(CONNECTOR_NOT);
        return this;
    }

    @Override
    public final WhereExpressionBuilder not(final String customClause, final Object... args) {
        updateNextConnector(CONNECTOR_NOT);
        return addExpression(new CustomExpressionElement(customClause, args));
    }

    @Override
    public final WhereExpressionBuilder not(final WhereExpressionBuilder whereExpression) {
        updateNextConnector(CONNECTOR_NOT);
        return addExpression(whereExpression);
    }

    @Override
    public final WhereExpressionBuilder or() {
        updateNextConnector(CONNECTOR_OR);
        return this;
    }

    @Override
    public final WhereExpressionBuilder or(final String customClause, final Object... args) {
        updateNextConnector(CONNECTOR_OR);
        return addExpression(new CustomExpressionElement(customClause, args));
    }

    @Override
    public final WhereExpressionBuilder or(final WhereExpressionBuilder whereExpression) {
        updateNextConnector(CONNECTOR_OR);
        return addExpression(whereExpression);
    }

    @Override
    public void sqlElementQuery(StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        if (wrappedIntoParentesisIfMultipleElements && (elementList.size()>0)) {
            queryBuilder.append(OPEN_PARENTESIS);
            render(queryBuilder, propertiesProcessor);
            queryBuilder.append(CLOSE_PARENTESIS);
        } else {
            render(queryBuilder, propertiesProcessor);
        }
    }

    private void render(StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        for (WhereExpressionElement element : elementList) {
            element.sqlElementQuery(queryBuilder, propertiesProcessor);
        }
    }

    /**
     * @return the elementList
     */
    public boolean isEmpty() {
        return elementList.isEmpty();
    }

}
