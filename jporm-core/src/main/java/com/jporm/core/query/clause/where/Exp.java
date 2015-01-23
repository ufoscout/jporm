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
package com.jporm.core.query.clause.where;

import java.util.Collection;
import java.util.List;

import com.jporm.query.clause.WhereExpressionElement;
import com.jporm.query.find.BaseFindQuery;

/**
 * A factory helper to build {@link WhereExpressionElement}s
 * @author Francesco Cina'
 *
 */
public class Exp {

    private Exp() {
    }

    /**
     * And - Chain more expressions with a logical and.
     * 
     * @param expressionElements
     * @return
     */
    public static AndExpressionElement and(final WhereExpressionElement... expressionElements) {
        return new AndExpressionElement(expressionElements);
    }

    /**
     * And - Chain more expressions with a logical and.
     * 
     * @param expressionElements
     * @return
     */
    public static AndExpressionElement and(final List<WhereExpressionElement> expressionElements) {
        return new AndExpressionElement(expressionElements);
    }

    /**
     * It permits to define a custom where clause.
     * E.g.: and("mod(Bean.id, 10) = 1 AND Bean.property is not null")
     * 
     * For a better readability and usability placeholders can be used:
     * E.g.: and("mod(Bean.id, ?) = ? AND Bean.property is not null", new Object[]{10,1})
     * 
     * @param customClause the custom where clause
     * @param args the values of the placeholders if present
     * @return
     */
    public static WhereExpressionElement and(final String customClause, final Object... args) {
        return new CustomExpressionElement(customClause, args);
    }

    /**
     * Express the "Equals to" relation between an object's property
     * and a fixed value.
     * 
     * @param property
     * @param value
     * @return
     */
    public static  EqExpressionElement eq(final String property, final Object value) {
        return new EqExpressionElement(property, value);
    }

    /**
     * Express the "Equals to" relation between objects properties
     * 
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static  EqPropertiesExpressionElement eqProperties(final String firstProperty, final String secondProperty) {
        return new EqPropertiesExpressionElement(firstProperty, secondProperty);
    }

    /**
     * Express the "Greater or equals to" relation between an object's property
     * and a fixed value.
     * 
     * @param property
     * @param value
     * @return
     */
    public static  GeExpressionElement ge(final String property, final Object value) {
        return new GeExpressionElement(property, value);
    }

    /**
     * Express the "Greater or equals to" relation between objects properties
     * 
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static  GePropertiesExpressionElement geProperties(final String firstProperty, final String secondProperty) {
        return new GePropertiesExpressionElement(firstProperty, secondProperty);
    }

    /**
     * Express the "Greater than" relation between an object's property
     * and a fixed value.
     * 
     * @param property
     * @param value
     * @return
     */
    public static  GtExpressionElement gt(final String property, final Object value) {
        return new GtExpressionElement(property, value);
    }

    /**
     * Express the "Greater than" relation between objects properties
     * 
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static  GtPropertiesExpressionElement gtProperties(final String firstProperty, final String secondProperty) {
        return new GtPropertiesExpressionElement(firstProperty, secondProperty);
    }

    /**
     * Express the "Insensitive Equal To" between an object's property
     * and a fixed value (it uses a lower() function to make both case insensitive).
     * 
     * @param propertyName
     * @param value
     * @return
     */
    public static  IEqExpressionElement ieq(final String property, final Object value) {
        return new IEqExpressionElement(property, value);
    }

    /**
     * Express the "Insensitive Equal To" bbetween objects properties
     * (it uses a lower() function to make both case insensitive).
     * 
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static  IEqPropertiesExpressionElement ieqProperties(final String firstProperty, final String secondProperty) {
        return new IEqPropertiesExpressionElement(firstProperty, secondProperty);
    }

    /**
     * Case insensitive Like - property like value where the value contains the
     * SQL wild card characters % (percentage) and _ (underscore).
     * 
     * @param propertyName
     * @param value
     * @return
     */
    public static  ILikeExpressionElement ilike(final String property, final Object value) {
        return new ILikeExpressionElement(property, value);
    }

    /**
     * In - using a subQuery.
     * 
     * @param propertyName
     * @param subQuery
     * @return
     */
    public static  InSubQueryExpressionElement in(final String property, final BaseFindQuery subQuery) {
        return new InSubQueryExpressionElement(property, subQuery);
    }

    /**
     * In - property has a value in the collection of values.
     * 
     * @param propertyName
     * @param values
     * @return
     */
    public static  InExpressionElement in(final String property, final Collection<?> values) {
        return new InExpressionElement(property, values);
    }

    /**
     * In - property has a value in the array of values.
     * 
     * @param propertyName
     * @param values
     * @return
     */
    public static  InExpressionElement in(final String property, final Object[] values) {
        return new InExpressionElement(property, values);
    }

    /**
     * Is Not Null - property is not null.
     * 
     * @param propertyName
     * @return
     */
    public static  IsNotNullExpressionElement isNotNull(final String property) {
        return new IsNotNullExpressionElement(property);
    }

    /**
     * Is Null - property is null.
     * 
     * @param propertyName
     * @return
     */
    public static  IsNullExpressionElement isNull(final String property) {
        return new IsNullExpressionElement(property);
    }

    /**
     * Express the "Lesser or equals to" relation between an object's property
     * and a fixed value.
     * 
     * @param property
     * @param value
     * @return
     */
    public static  LeExpressionElement le(final String property, final Object value) {
        return new LeExpressionElement(property, value);
    }

    /**
     * Express the "Lesser or equals to" relation between objects properties
     * 
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static  LePropertiesExpressionElement leProperties(final String firstProperty, final String secondProperty) {
        return new LePropertiesExpressionElement(firstProperty, secondProperty);
    }

    /**
     * Like - property like value where the value contains the SQL wild card
     * characters % (percentage) and _ (underscore).
     * 
     * @param propertyName
     * @param value
     */
    public static  LikeExpressionElement like(final String property, final Object value) {
        return new LikeExpressionElement(property, value);
    }

    /**
     * 
     * Express the "Lesser than" relation between an object's property
     * and a fixed value.
     * 
     * @param property
     * @param value
     * @return
     */
    public static  LtExpressionElement lt(final String property, final Object value) {
        return new LtExpressionElement(property, value);
    }

    /**
     * Express the "Lesser than" relation between objects properties
     * 
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static  LtPropertiesExpressionElement ltProperties(final String firstProperty, final String secondProperty) {
        return new LtPropertiesExpressionElement(firstProperty, secondProperty);
    }

    /**
     * Express the "Not Equals to" relation between objects properties.
     * 
     * @param property
     * @param value
     * @return
     */
    public static  NeExpressionElement ne(final String property, final Object value) {
        return new NeExpressionElement(property, value);
    }

    /**
     * Express the "Not Equals to" relation between an object's property
     * and a fixed value.
     * 
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static  NePropertiesExpressionElement neProperties(final String firstProperty, final String secondProperty) {
        return new NePropertiesExpressionElement(firstProperty, secondProperty);
    }

    /**
     * Not In - using a subQuery.
     * 
     * @param propertyName
     * @param subQuery
     * @return
     */
    public static  NInSubQueryExpressionElement nin(final String property, final BaseFindQuery subQuery) {
        return new NInSubQueryExpressionElement(property, subQuery);
    }

    /**
     * Not In - property has a value in the collection of values.
     * 
     * @param propertyName
     * @param values
     * @return
     */
    public static  NInExpressionElement nin(final String property, final Collection<?> values) {
        return new NInExpressionElement(property, values);
    }

    /**
     * Not In - property has a value in the array of values.
     * 
     * @param propertyName
     * @param values
     * @return
     */
    public static  NInExpressionElement nin(final String property, final Object[] values) {
        return new NInExpressionElement(property, values);
    }

    /**
     * Not Like - property like value where the value contains the SQL wild card
     * characters % (percentage) and _ (underscore).
     * 
     * @param propertyName
     * @param value
     */
    public static  NLikeExpressionElement nlike(final String property, final Object value) {
        return new NLikeExpressionElement(property, value);
    }

    /**
     * Negate a chain of expressions chained with a logical AND.
     * 
     * @param exp
     * @return
     */
    public static  NotExpressionElement not(final WhereExpressionElement... expressions) {
        return new NotExpressionElement(and(expressions));
    }

    /**
     * Negate a chain of expressions chained with a logical AND.
     * 
     * @param exp
     * @return
     */
    public static  NotExpressionElement not(final List<WhereExpressionElement> expressions) {
        return new NotExpressionElement(and(expressions));
    }

    /**
     * It negates a custom where clause.
     * E.g.: not("mod(Bean.id, 10) = 1 AND Bean.property is not null")
     * 
     * For a better readability and usability placeholders can be used:
     * E.g.: not("mod(Bean.id, ?) = ? AND Bean.property is not null", new Object[]{10,1})
     *
     * @param customClause the custom where clause
     * @param args the values of the placeholders if present
     * @return
     */
    public static WhereExpressionElement not(final String customClause, final Object... args) {
        return not(new CustomExpressionElement(customClause, args));
    }

    /**
     * Or - Chain more expressions with a logical or.
     * 
     * @param expressionElements
     * @return
     */
    public static OrExpressionElement or(final WhereExpressionElement... expressionElements) {
        return new OrExpressionElement(expressionElements);
    }

    /**
     * Or - Chain more expressions with a logical or.
     * 
     * @param expressionElements
     * @return
     */
    public static OrExpressionElement or(final List<WhereExpressionElement> expressionElements) {
        return new OrExpressionElement(expressionElements);
    }

    /**
     * Creates an OR custom where clause .
     * E.g.: or("mod(Bean.id, 10) = 1 AND Bean.property is not null")
     * 
     * For a better readability and usability placeholders can be used:
     * E.g.: or("mod(Bean.id, ?) = ? AND Bean.property is not null", new Object[]{10,1})
     * 
     * @param customClause the custom where clause
     * @param args the values of the placeholders if present
     * @return
     */
    public static WhereExpressionElement or(final String customClause, final Object... args) {
        return or(new CustomExpressionElement(customClause, args));
    }

}
