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
package com.jporm.commons.core.query.clause;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jporm.commons.core.query.find.CommonFindQueryRoot;
import com.jporm.sql.query.clause.WhereExpressionElement;
import com.jporm.sql.query.clause.impl.where.Exp;

/**
 *
 * @author Francesco Cina
 *
 *         18/giu/2011
 */
public interface Where<T extends Where<?>> extends QueryClause<T> {

	/**
	 * All Equal - Map containing property names and their values.
	 * @param propertyMap
	 * @return
	 */
	T allEq(Map<String,Object> propertyMap);

	/**
	 * And - Build a chain of {@link WhereExpressionElement} in and.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @param WhereExpressionElements
	 * @return
	 */
	T and(final WhereExpressionElement... WhereExpressionElements);

	/**
	 * And - Chain more {@link WhereExpressionElement} with a logical and.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @param WhereExpressionElements
	 * @return
	 */
	T and(final List<WhereExpressionElement> WhereExpressionElements);

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
	T and(String customClause, Object... args );

	/**
	 * Express the "Equals to" relation between an object's property
	 * and a fixed value.
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	T eq(String property, Object value);

	/**
	 * Express the "Equals to" relation between objects properties
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	T eqProperties(String firstProperty, String secondProperty);

	/**
	 * Express the "Greater or equals to" relation between an object's property
	 * and a fixed value.
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	T ge(String property, Object value);

	/**
	 * Express the "Greater or equals to" relation between objects properties
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	T geProperties(String firstProperty, String secondProperty);

	/**
	 * Express the "Greater than" relation between an object's property
	 * and a fixed value.
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	T gt(String property, Object value);

	/**
	 * Express the "Greater than" relation between objects properties
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	T gtProperties(String firstProperty, String secondProperty);

	/**
	 * Express the "Insensitive Equal To" between an object's property
	 * and a fixed value (it uses a lower() function to make both case insensitive).
	 *
	 * @param propertyName
	 * @param value
	 * @return
	 */
	T ieq(String property, String value);

	/**
	 * Express the "Insensitive Equal To" bbetween objects properties
	 * (it uses a lower() function to make both case insensitive).
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	T ieqProperties(String firstProperty, String secondProperty);

	/**
	 * Case insensitive Like - property like value where the value contains the
	 * SQL wild card characters % (percentage) and _ (underscore).
	 *
	 * @param propertyName
	 * @param value
	 * @return
	 */
	T ilike(String property, String value);

	/**
	 * In - using a subQuery.
	 *
	 * @param propertyName
	 * @param subQuery
	 * @return
	 */
	T in(String property, CommonFindQueryRoot subQuery);

	/**
	 * In - property has a value in the collection of values.
	 *
	 * @param propertyName
	 * @param values
	 * @return
	 */
	T in(String property, Collection<?> values);

	/**
	 * In - property has a value in the array of values.
	 *
	 * @param propertyName
	 * @param values
	 * @return
	 */
	T in(String property, Object[] values);

	/**
	 * Is Not Null - property is not null.
	 *
	 * @param propertyName
	 * @return
	 */
	T isNotNull(String property);

	/**
	 * Is Null - property is null.
	 *
	 * @param propertyName
	 * @return
	 */
	T isNull(String property);

	/**
	 * Express the "Lesser or equals to" relation between an object's property
	 * and a fixed value.
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	T le(String property, Object value);

	/**
	 * Express the "Lesser or equals to" relation between objects properties
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	T leProperties(String firstProperty, String secondProperty);


	/**
	 * Like - property like value where the value contains the SQL wild card
	 * characters % (percentage) and _ (underscore).
	 *
	 * @param propertyName
	 * @param value
	 */
	T like(String property, String value);

	/**
	 *
	 * Express the "Lesser than" relation between an object's property
	 * and a fixed value.
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	T lt(String property, Object value);

	/**
	 * Express the "Lesser than" relation between objects properties
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	T ltProperties(String firstProperty, String secondProperty);

	/**
	 * Express the "Not Equals to" relation between objects properties.
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	T ne(String property, Object value);

	/**
	 * Express the "Not Equals to" relation between an object's property
	 * and a fixed value.
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	T neProperties(String firstProperty, String secondProperty);

	/**
	 * Not In - using a subQuery.
	 *
	 * @param propertyName
	 * @param subQuery
	 * @return
	 */
	T nin(String property, CommonFindQueryRoot subQuery);

	/**
	 * Not In - property has a value in the collection of values.
	 *
	 * @param propertyName
	 * @param values
	 * @return
	 */
	T nin(String property, Collection<?> values);

	/**
	 * Not In - property has a value in the array of values.
	 *
	 * @param propertyName
	 * @param values
	 * @return
	 */
	T nin(String property, Object[] values);

	/**
	 * Not Like - property like value where the value contains the SQL wild card
	 * characters % (percentage) and _ (underscore).
	 *
	 * @param propertyName
	 * @param value
	 */
	T nlike(String property, String value);

	/**
	 * Negate a chain of expressions chained with a logical AND.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @param exp
	 * @return
	 */
	T not(WhereExpressionElement... expression);

	/**
	 * Negate a chain of expressions chained with a logical AND.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @param whereExpressionElements
	 * @return
	 */
	T not(final List<WhereExpressionElement> whereExpressionElements);

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
	T not(String customClause, Object... args );

	/**
	 * Or - Chain more expressions with a logical or.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @param whereExpressionElements
	 * @return
	 */
	T or(final WhereExpressionElement... whereExpressionElements);

	/**
	 * Or - Chain more expressions with a logical or.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @param whereExpressionElements
	 * @return
	 */
	T or(final List<WhereExpressionElement> whereExpressionElements);

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
	T or(String customClause, Object... args );

}
