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

import java.util.Collection;
import java.util.function.Consumer;

import com.jporm.sql.query.select.SelectCommon;

/**
 * A factory helper to build {@link WhereExpressionElement}s
 *
 * @author Francesco Cina'
 *
 */
public interface Exp {

    /**
     * Express the "Equals to" relation between an object's property and a fixed
     * value.
     *
     * @param property
     * @param value
     * @return
     */
    public static WhereExpressionBuilder eq(final String property, final Object value) {
        return new WhereExpressionBuilderImpl(true).eq(property, value);
    }

    /**
     * Express the "Equals to" relation between objects properties
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static WhereExpressionBuilder eqProperties(final String firstProperty, final String secondProperty) {
        return new WhereExpressionBuilderImpl(true).eqProperties(firstProperty, secondProperty);
    }

    /**
     * Express the "Greater or equals to" relation between an object's property
     * and a fixed value.
     *
     * @param property
     * @param value
     * @return
     */
    public static WhereExpressionBuilder ge(final String property, final Object value) {
        return new WhereExpressionBuilderImpl(true).ge(property, value);
    }

    /**
     * Express the "Greater or equals to" relation between objects properties
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static WhereExpressionBuilder geProperties(final String firstProperty, final String secondProperty) {
        return new WhereExpressionBuilderImpl(true).geProperties(firstProperty, secondProperty);
    }

    /**
     * Express the "Greater than" relation between an object's property and a
     * fixed value.
     *
     * @param property
     * @param value
     * @return
     */
    public static WhereExpressionBuilder gt(final String property, final Object value) {
        return new WhereExpressionBuilderImpl(true).gt(property, value);
    }

    /**
     * Express the "Greater than" relation between objects properties
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static WhereExpressionBuilder gtProperties(final String firstProperty, final String secondProperty) {
        return new WhereExpressionBuilderImpl(true).gtProperties(firstProperty, secondProperty);
    }

    /**
     * Express the "Insensitive Equal To" between an object's property and a
     * fixed value (it uses a lower() function to make both case insensitive).
     *
     * @param propertyName
     * @param value
     * @return
     */
    public static WhereExpressionBuilder ieq(final String property, final String value) {
        return new WhereExpressionBuilderImpl(true).ieq(property, value);
    }

    /**
     * Express the "Insensitive Equal To" bbetween objects properties (it uses a
     * lower() function to make both case insensitive).
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static WhereExpressionBuilder ieqProperties(final String firstProperty, final String secondProperty) {
        return new WhereExpressionBuilderImpl(true).ieqProperties(firstProperty, secondProperty);
    }

    /**
     * Case insensitive Like - property like value where the value contains the
     * SQL wild card characters % (percentage) and _ (underscore).
     *
     * @param propertyName
     * @param value
     * @return
     */
    public static WhereExpressionBuilder ilike(final String property, final String value) {
        return new WhereExpressionBuilderImpl(true).ilike(property, value);
    }

    /**
     * In - property has a value in the collection of values.
     *
     * @param propertyName
     * @param values
     * @return
     */
    public static WhereExpressionBuilder in(final String property, final Collection<?> values) {
        return new WhereExpressionBuilderImpl(true).in(property, values);
    }

    /**
     * In - property has a value in the array of values.
     *
     * @param propertyName
     * @param values
     * @return
     */
    public static WhereExpressionBuilder in(final String property, final Object[] values) {
        return new WhereExpressionBuilderImpl(true).in(property, values);
    }

    /**
     * In - using a subQuery.
     *
     * @param propertyName
     * @param subQuery
     * @return
     */
    public static WhereExpressionBuilder in(final String property, final SelectCommon subQuery) {
        return new WhereExpressionBuilderImpl(true).in(property, subQuery);
    }

    /**
     * Is Not Null - property is not null.
     *
     * @param propertyName
     * @return
     */
    public static WhereExpressionBuilder isNotNull(final String property) {
        return new WhereExpressionBuilderImpl(true).isNotNull(property);
    }

    /**
     * Is Null - property is null.
     *
     * @param propertyName
     * @return
     */
    public static WhereExpressionBuilder isNull(final String property) {
        return new WhereExpressionBuilderImpl(true).isNull(property);
    }

    /**
     * Express the "Lesser or equals to" relation between an object's property
     * and a fixed value.
     *
     * @param property
     * @param value
     * @return
     */
    public static WhereExpressionBuilder le(final String property, final Object value) {
        return new WhereExpressionBuilderImpl(true).le(property, value);
    }

    /**
     * Express the "Lesser or equals to" relation between objects properties
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static WhereExpressionBuilder leProperties(final String firstProperty, final String secondProperty) {
        return new WhereExpressionBuilderImpl(true).leProperties(firstProperty, secondProperty);
    }

    /**
     * Like - property like value where the value contains the SQL wild card
     * characters % (percentage) and _ (underscore).
     *
     * @param propertyName
     * @param value
     */
    public static WhereExpressionBuilder like(final String property, final String value) {
        return new WhereExpressionBuilderImpl(true).like(property, value);
    }

    /**
     *
     * Express the "Lesser than" relation between an object's property and a
     * fixed value.
     *
     * @param property
     * @param value
     * @return
     */
    public static WhereExpressionBuilder lt(final String property, final Object value) {
        return new WhereExpressionBuilderImpl(true).lt(property, value);
    }

    /**
     * Express the "Lesser than" relation between objects properties
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static WhereExpressionBuilder ltProperties(final String firstProperty, final String secondProperty) {
        return new WhereExpressionBuilderImpl(true).ltProperties(firstProperty, secondProperty);
    }

    /**
     * Express the "Not Equals to" relation between objects properties.
     *
     * @param property
     * @param value
     * @return
     */
    public static WhereExpressionBuilder ne(final String property, final Object value) {
        return new WhereExpressionBuilderImpl(true).ne(property, value);
    }

    /**
     * Express the "Not Equals to" relation between an object's property and a
     * fixed value.
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    public static WhereExpressionBuilder neProperties(final String firstProperty, final String secondProperty) {
        return new WhereExpressionBuilderImpl(true).neProperties(firstProperty, secondProperty);
    }

    /**
     * Not In - property has a value in the collection of values.
     *
     * @param propertyName
     * @param values
     * @return
     */
    public static WhereExpressionBuilder nin(final String property, final Collection<?> values) {
        return new WhereExpressionBuilderImpl(true).nin(property, values);
    }

    /**
     * Not In - property has a value in the array of values.
     *
     * @param propertyName
     * @param values
     * @return
     */
    public static WhereExpressionBuilder nin(final String property, final Object[] values) {
        return new WhereExpressionBuilderImpl(true).nin(property, values);
    }

    /**
     * Not In - using a subQuery.
     *
     * @param propertyName
     * @param subQuery
     * @return
     */
    public static WhereExpressionBuilder nin(final String property, final SelectCommon subQuery) {
        return new WhereExpressionBuilderImpl(true).nin(property, subQuery);
    }

    /**
     * Not Like - property like value where the value contains the SQL wild card
     * characters % (percentage) and _ (underscore).
     *
     * @param propertyName
     * @param value
     */
    public static WhereExpressionBuilder nlike(final String property, final String value) {
        return new WhereExpressionBuilderImpl(true).nlike(property, value);
    }

    /**
     * Negate a chain of expressions chained with a logical AND.
     *
     * @param exp
     * @return
     */
    public static WhereExpressionBuilder not() {
        return new WhereExpressionBuilderImpl(true).not();
    }

    /**
     * It negates a custom where clause. E.g.: not(
     * "mod(Bean.id, 10) = 1 AND Bean.property is not null")
     *
     * For a better readability and usability placeholders can be used: E.g.:
     * not("mod(Bean.id, ?) = ? AND Bean.property is not null", new
     * Object[]{10,1})
     *
     * @param customClause
     *            the custom where clause
     * @param args
     *            the values of the placeholders if present
     * @return
     */
    public static WhereExpressionBuilder not(final String customClause, final Object... args) {
        return new WhereExpressionBuilderImpl(true).not(customClause, args);
    }

    /**
     * Negate a chain of expressions chained with a logical AND.
     *
     * @param exp
     * @return
     */
    public static WhereExpressionBuilder not(final WhereExpressionBuilder expression) {
        return new WhereExpressionBuilderImpl(true).not(expression);
    }

    /**
     * Negate a chain of expressions chained with a logical AND.
     *
     * @param exp
     * @return
     */
    public static WhereExpressionBuilder not(final Consumer<WhereExpressionBuilder> exp) {
        WhereExpressionBuilder whereExpressionBuilder = new WhereExpressionBuilderImpl(true);
        exp.accept(whereExpressionBuilder);
        return not(whereExpressionBuilder);
    }
}
