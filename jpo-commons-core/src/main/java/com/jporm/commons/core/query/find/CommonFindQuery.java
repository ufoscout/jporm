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
package com.jporm.commons.core.query.find;

import java.util.List;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.clause.From;
import com.jporm.sql.dsl.query.where.SelectCommonSupplier;
import com.jporm.sql.dsl.query.where.WhereExpressionElement;
import com.jporm.sql.dsl.query.where.expression.Exp;

/**
 *
 * @author Francesco Cina
 *
 *         18/giu/2011
 */
public interface CommonFindQuery<FIND extends CommonFindQuery<FIND, WHERE, ORDER_BY>, WHERE extends CommonFindQueryWhere<FIND, WHERE, ORDER_BY>, ORDER_BY extends CommonFindQueryOrderBy<FIND, WHERE, ORDER_BY>>
        extends From<FIND>, CommonFindQueryBase<FIND, WHERE, ORDER_BY>, SelectCommonSupplier {

    /**
     * The value of the Bean fields listed will not be fetched from the DB. This
     * is useful to load only a partial Bean to reduce the amount of work of the
     * DB. Normally this is used to avoid loading LOB values when not needed. If
     * 'ignoreFieldsCondition' is false the fields will not be ignored fetched.
     *
     * @param fields
     * @param ignoreFieldsCondition
     * @return
     */
    FIND ignore(boolean ignoreFieldsCondition, String... fields);

    /**
     * The value of the Bean fields listed will not be fetched from the DB. This
     * is useful to load only a partial Bean to reduce the amount of work of the
     * DB. Normally this is used to avoid loading LOB values when not needed.
     *
     * @param fields
     * @return
     */
    FIND ignore(String... fields);

    /**
     * Set the order by clause.
     *
     * @return
     */
    ORDER_BY orderBy() throws JpoException;

    /**
     * Chain more {@link WhereExpressionElement} with a logical and. To build
     * the {@link WhereExpressionElement} use the {@link Exp} factory.
     *
     * @return
     */
    WHERE where(final List<WhereExpressionElement> expressionElements);

    /**
     * It permits to define a custom where clause. E.g.: clause(
     * "mod(Bean.id, 10) = 1 AND Bean.property is not null")
     *
     * For a better readability and usability placeholders can be used: E.g.:
     * clause("mod(Bean.id, ?) = ? AND Bean.property is not null", new
     * Object[]{10,1})
     *
     * @param customClause
     *            the custom where clause
     * @param args
     *            the values of the placeholders if present To build the
     *            {@link WhereExpressionElement} use the {@link Exp} factory.
     *
     * @return
     */
    WHERE where(String customClause, Object... args);

    /**
     * Chain more {@link WhereExpressionElement} with a logical and. To build
     * the {@link WhereExpressionElement} use the {@link Exp} factory.
     *
     * @return
     */
    WHERE where(final WhereExpressionElement... expressionElements);

}
