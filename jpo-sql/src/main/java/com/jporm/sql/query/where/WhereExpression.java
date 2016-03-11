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
import java.util.Map;

import com.jporm.sql.query.select.SelectCommon;
import com.jporm.sql.query.where.expression.Exp;

/**
 *
 * @author Francesco Cina
 *
 *         18/giu/2011
 */
public interface WhereExpression<WHERE extends WhereExpression<WHERE>> {

    /**
     * All Equal - Map containing property names and their values.
     *
     * @param propertyMap
     * @return
     */
    WHERE allEq(Map<String, Object> propertyMap);

    /**
     * And - Chain more {@link WhereExpressionElement} with a logical and.
     *
     * @param WhereExpressionElements
     * @return
     */
    WHERE and();

    /**
     * It permits to define a custom where clause. E.g.: and(
     * "mod(Bean.id, 10) = 1 AND Bean.property is not null")
     *
     * For a better readability and usability placeholders can be used: E.g.:
     * and("mod(Bean.id, ?) = ? AND Bean.property is not null", new
     * Object[]{10,1})
     *
     * @param customClause
     *            the custom where clause
     * @param args
     *            the values of the placeholders if present
     * @return
     */
    WHERE and(String customClause, Object... args);

    /**
     * And - Build a chain of {@link WhereExpressionElement} in and. To build
     * the {@link WhereExpressionBuilder} use the {@link Exp} factory.
     *
     * @param WhereExpression
     * @return
     */
    WHERE and(final WhereExpressionBuilder WhereExpression);

    /**
     * Express the "Equals to" relation between an object's property and a fixed
     * value.
     *
     * @param property
     * @param value
     * @return
     */
    WHERE eq(String property, Object value);

    /**
     * Express the "Equals to" relation between objects properties
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    WHERE eqProperties(String firstProperty, String secondProperty);

    /**
     * Express the "Greater or equals to" relation between an object's property
     * and a fixed value.
     *
     * @param property
     * @param value
     * @return
     */
    WHERE ge(String property, Object value);

    /**
     * Express the "Greater or equals to" relation between objects properties
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    WHERE geProperties(String firstProperty, String secondProperty);

    /**
     * Express the "Greater than" relation between an object's property and a
     * fixed value.
     *
     * @param property
     * @param value
     * @return
     */
    WHERE gt(String property, Object value);

    /**
     * Express the "Greater than" relation between objects properties
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    WHERE gtProperties(String firstProperty, String secondProperty);

    /**
     * Express the "Insensitive Equal To" between an object's property and a
     * fixed value (it uses a lower() function to make both case insensitive).
     *
     * @param propertyName
     * @param value
     * @return
     */
    WHERE ieq(String property, String value);

    /**
     * Express the "Insensitive Equal To" bbetween objects properties (it uses a
     * lower() function to make both case insensitive).
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    WHERE ieqProperties(String firstProperty, String secondProperty);

    /**
     * Case insensitive Like - property like value where the value contains the
     * SQL wild card characters % (percentage) and _ (underscore).
     *
     * @param propertyName
     * @param value
     * @return
     */
    WHERE ilike(String property, String value);

    /**
     * In - property has a value in the collection of values.
     *
     * @param propertyName
     * @param values
     * @return
     */
    WHERE in(String property, Collection<?> values);

    /**
     * In - property has a value in the array of values.
     *
     * @param propertyName
     * @param values
     * @return
     */
    WHERE in(String property, Object... values);

    /**
     * In - using a subQuery.
     *
     * @param propertyName
     * @param subQuery
     * @return
     */
    WHERE in(String property, SelectCommon subQuery);

    /**
     * Is Not Null - property is not null.
     *
     * @param propertyName
     * @return?
     */
    WHERE isNotNull(String property);

    /**
     * Is Null - property is null.
     *
     * @param propertyName
     * @return
     */
    WHERE isNull(String property);

    /**
     * Express the "Lesser or equals to" relation between an object's property
     * and a fixed value.
     *
     * @param property
     * @param value
     * @return
     */
    WHERE le(String property, Object value);

    /**
     * Express the "Lesser or equals to" relation between objects properties
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    WHERE leProperties(String firstProperty, String secondProperty);

    /**
     * Like - property like value where the value contains the SQL wild card
     * characters % (percentage) and _ (underscore).
     *
     * @param propertyName
     * @param value
     */
    WHERE like(String property, String value);

    /**
     *
     * Express the "Lesser than" relation between an object's property and a
     * fixed value.
     *
     * @param property
     * @param value
     * @return
     */
    WHERE lt(String property, Object value);

    /**
     * Express the "Lesser than" relation between objects properties
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    WHERE ltProperties(String firstProperty, String secondProperty);

    /**
     * Express the "Not Equals to" relation between objects properties.
     *
     * @param property
     * @param value
     * @return
     */
    WHERE ne(String property, Object value);

    /**
     * Express the "Not Equals to" relation between an object's property and a
     * fixed value.
     *
     * @param firstProperty
     * @param secondProperty
     * @return
     */
    WHERE neProperties(String firstProperty, String secondProperty);

    /**
     * Not In - property has a value in the collection of values.
     *
     * @param propertyName
     * @param values
     * @return
     */
    WHERE nin(String property, Collection<?> values);

    /**
     * Not In - property has a value in the array of values.
     *
     * @param propertyName
     * @param values
     * @return
     */
    WHERE nin(String property, Object... values);

    /**
     * Not In - using a subQuery.
     *
     * @param propertyName
     * @param subQuery
     * @return
     */
    WHERE nin(String property, SelectCommon subQuery);

    /**
     * Not Like - property like value where the value contains the SQL wild card
     * characters % (percentage) and _ (underscore).
     *
     * @param propertyName
     * @param value
     */
    WHERE nlike(String property, String value);

    /**
     * Negate a chain of expressions chained with a logical AND.
     *
     * @param whereExpressionElements
     * @return
     */
    WHERE not();

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
    WHERE not(String customClause, Object... args);

    /**
     * Negate a chain of expressions chained with a logical AND. To build the
     * {@link WhereExpressionBuilder} use the {@link Exp} factory.
     *
     * @param exp
     * @return
     */
    WHERE not(WhereExpressionBuilder expression);

    /**
     * Or - Chain more expressions with a logical or.
     *
     * @param whereExpressionElements
     * @return
     */
    WHERE or();

    /**
     * Creates an OR custom where clause . E.g.: or(
     * "mod(Bean.id, 10) = 1 AND Bean.property is not null")
     *
     * For a better readability and usability placeholders can be used: E.g.:
     * or("mod(Bean.id, ?) = ? AND Bean.property is not null", new
     * Object[]{10,1})
     *
     * @param customClause
     *            the custom where clause
     * @param args
     *            the values of the placeholders if present
     * @return
     */
    WHERE or(String customClause, Object... args);

    /**
     * Or - Chain more expressions with a logical or. To build the
     * {@link WhereExpressionBuilder} use the {@link Exp} factory.
     *
     * @param whereExpression
     * @return
     */
    WHERE or(final WhereExpressionBuilder whereExpression);

}
