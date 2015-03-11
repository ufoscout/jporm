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
package com.jporm.sql.query.clause;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jporm.sql.query.Sql;
import com.jporm.sql.query.clause.impl.where.Exp;

/**
 *
 * @author Francesco Cina
 *
 *         18/giu/2011
 */
public interface Where extends Sql {

	/**
	 * All Equal - Map containing property names and their values.
	 * @param propertyMap
	 * @return
	 */
	Where allEq(Map<String,Object> propertyMap);

	/**
	 * And - Build a chain of {@link WhereExpressionElement} in and.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @param WhereExpressionElements
	 * @return
	 */
	Where and(final WhereExpressionElement... WhereExpressionElements);

	/**
	 * And - Chain more {@link WhereExpressionElement} with a logical and.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @param WhereExpressionElements
	 * @return
	 */
	Where and(final List<WhereExpressionElement> WhereExpressionElements);

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
	Where and(String customClause, Object... args );

	/**
	 * Express the "Equals to" relation between an object's property
	 * and a fixed value.
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	Where eq(String property, Object value);

	/**
	 * Express the "Equals to" relation between objects properties
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	Where eqProperties(String firstProperty, String secondProperty);

	/**
	 * Express the "Greater or equals to" relation between an object's property
	 * and a fixed value.
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	Where ge(String property, Object value);

	/**
	 * Express the "Greater or equals to" relation between objects properties
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	Where geProperties(String firstProperty, String secondProperty);

	/**
	 * Express the "Greater than" relation between an object's property
	 * and a fixed value.
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	Where gt(String property, Object value);

	/**
	 * Express the "Greater than" relation between objects properties
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	Where gtProperties(String firstProperty, String secondProperty);

	/**
	 * Express the "Insensitive Equal To" between an object's property
	 * and a fixed value (it uses a lower() function to make both case insensitive).
	 *
	 * @param propertyName
	 * @param value
	 * @return
	 */
	Where ieq(String property, String value);

	/**
	 * Express the "Insensitive Equal To" bbetween objects properties
	 * (it uses a lower() function to make both case insensitive).
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	Where ieqProperties(String firstProperty, String secondProperty);

	/**
	 * Case insensitive Like - property like value where the value contains the
	 * SQL wild card characters % (percentage) and _ (underscore).
	 *
	 * @param propertyName
	 * @param value
	 * @return
	 */
	Where ilike(String property, String value);

	/**
	 * In - using a subQuery.
	 *
	 * @param propertyName
	 * @param subQuery
	 * @return
	 */
	Where in(String property, SelectCommon subQuery);

	/**
	 * In - property has a value in the collection of values.
	 *
	 * @param propertyName
	 * @param values
	 * @return
	 */
	Where in(String property, Collection<?> values);

	/**
	 * In - property has a value in the array of values.
	 *
	 * @param propertyName
	 * @param values
	 * @return
	 */
	Where in(String property, Object[] values);

	/**
	 * Is Not Null - property is not null.
	 *
	 * @param propertyName
	 * @return
	 */
	Where isNotNull(String property);

	/**
	 * Is Null - property is null.
	 *
	 * @param propertyName
	 * @return
	 */
	Where isNull(String property);

	/**
	 * Express the "Lesser or equals to" relation between an object's property
	 * and a fixed value.
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	Where le(String property, Object value);

	/**
	 * Express the "Lesser or equals to" relation between objects properties
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	Where leProperties(String firstProperty, String secondProperty);


	/**
	 * Like - property like value where the value contains the SQL wild card
	 * characters % (percentage) and _ (underscore).
	 *
	 * @param propertyName
	 * @param value
	 */
	Where like(String property, String value);

	/**
	 *
	 * Express the "Lesser than" relation between an object's property
	 * and a fixed value.
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	Where lt(String property, Object value);

	/**
	 * Express the "Lesser than" relation between objects properties
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	Where ltProperties(String firstProperty, String secondProperty);

	/**
	 * Express the "Not Equals to" relation between objects properties.
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	Where ne(String property, Object value);

	/**
	 * Express the "Not Equals to" relation between an object's property
	 * and a fixed value.
	 *
	 * @param firstProperty
	 * @param secondProperty
	 * @return
	 */
	Where neProperties(String firstProperty, String secondProperty);

	/**
	 * Not In - using a subQuery.
	 *
	 * @param propertyName
	 * @param subQuery
	 * @return
	 */
	Where nin(String property, SelectCommon subQuery);

	/**
	 * Not In - property has a value in the collection of values.
	 *
	 * @param propertyName
	 * @param values
	 * @return
	 */
	Where nin(String property, Collection<?> values);

	/**
	 * Not In - property has a value in the array of values.
	 *
	 * @param propertyName
	 * @param values
	 * @return
	 */
	Where nin(String property, Object[] values);

	/**
	 * Not Like - property like value where the value contains the SQL wild card
	 * characters % (percentage) and _ (underscore).
	 *
	 * @param propertyName
	 * @param value
	 */
	Where nlike(String property, String value);

	/**
	 * Negate a chain of expressions chained with a logical AND.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @param exp
	 * @return
	 */
	Where not(WhereExpressionElement... expression);

	/**
	 * Negate a chain of expressions chained with a logical AND.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @param whereExpressionElements
	 * @return
	 */
	Where not(final List<WhereExpressionElement> whereExpressionElements);

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
	Where not(String customClause, Object... args );

	/**
	 * Or - Chain more expressions with a logical or.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @param whereExpressionElements
	 * @return
	 */
	Where or(final WhereExpressionElement... whereExpressionElements);

	/**
	 * Or - Chain more expressions with a logical or.
	 * To build the {@link WhereExpressionElement} use the {@link Exp} factory.
	 *
	 * @param whereExpressionElements
	 * @return
	 */
	Where or(final List<WhereExpressionElement> whereExpressionElements);

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
	Where or(String customClause, Object... args );

}
