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
package com.jporm.sql.dsl.query.where;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jporm.sql.dsl.query.select.SelectCommon;

public interface WhereDefault<WHERE extends Where<WHERE>> extends Where<WHERE>, WhereProvider<WHERE> {

    Where<?> whereImplementation();

    @Override
    default WHERE allEq(Map<String, Object> propertyMap) {
        whereImplementation().allEq(propertyMap);
        return where();
    }

    @Override
    default WHERE and(List<WhereExpressionElement> WhereExpressionElements) {
        whereImplementation().and(WhereExpressionElements);
        return where();
    }

    @Override
    default WHERE and(String customClause, Object... args) {
        whereImplementation().and(customClause, args);
        return where();
    }

    @Override
    default WHERE and(WhereExpressionElement... WhereExpressionElements) {
        whereImplementation().and(WhereExpressionElements);
        return where();
    }

    @Override
    default WHERE eq(String property, Object value) {
        whereImplementation().eq(property, value);
        return where();
    }

    @Override
    default WHERE eqProperties(String firstProperty, String secondProperty) {
        whereImplementation().eqProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    default WHERE ge(String property, Object value) {
        whereImplementation().ge(property, value);
        return where();
    }

    @Override
    default WHERE geProperties(String firstProperty, String secondProperty) {
        whereImplementation().geProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    default WHERE gt(String property, Object value) {
        whereImplementation().gt(property, value);
        return where();
    }

    @Override
    default WHERE gtProperties(String firstProperty, String secondProperty) {
        whereImplementation().gtProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    default WHERE ieq(String property, String value) {
        whereImplementation().ieq(property, value);
        return where();
    }

    @Override
    default WHERE ieqProperties(String firstProperty, String secondProperty) {
        whereImplementation().ieqProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    default WHERE ilike(String property, String value) {
        whereImplementation().ilike(property, value);
        return where();
    }

    @Override
    default WHERE in(String property, Collection<?> values) {
        whereImplementation().in(property, values);
        return where();
    }

    @Override
    default WHERE in(String property, Object[] values) {
        whereImplementation().in(property, values);
        return where();
    }

    @Override
    default WHERE in(String property, SelectCommon subQuery) {
        whereImplementation().in(property, subQuery);
        return where();
    }

    @Override
    default WHERE isNotNull(String property) {
        whereImplementation().isNotNull(property);
        return where();
    }

    @Override
    default WHERE isNull(String property) {
        whereImplementation().isNull(property);
        return where();
    }

    @Override
    default WHERE le(String property, Object value) {
        whereImplementation().le(property, value);
        return where();
    }

    @Override
    default WHERE leProperties(String firstProperty, String secondProperty) {
        whereImplementation().leProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    default WHERE like(String property, String value) {
        whereImplementation().like(property, value);
        return where();
    }

    @Override
    default WHERE lt(String property, Object value) {
        whereImplementation().lt(property, value);
        return where();
    }

    @Override
    default WHERE ltProperties(String firstProperty, String secondProperty) {
        whereImplementation().ltProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    default WHERE ne(String property, Object value) {
        whereImplementation().ne(property, value);
        return where();
    }

    @Override
    default WHERE neProperties(String firstProperty, String secondProperty) {
        whereImplementation().neProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    default WHERE nin(String property, Collection<?> values) {
        whereImplementation().nin(property, values);
        return where();
    }

    @Override
    default WHERE nin(String property, Object[] values) {
        whereImplementation().nin(property, values);
        return where();
    }

    @Override
    default WHERE nin(String property, SelectCommon subQuery) {
        whereImplementation().nin(property, subQuery);
        return where();
    }

    @Override
    default WHERE nlike(String property, String value) {
        whereImplementation().nlike(property, value);
        return where();
    }

    @Override
    default WHERE not(List<WhereExpressionElement> whereExpressionElements) {
        whereImplementation().not(whereExpressionElements);
        return where();
    }

    @Override
    default WHERE not(String customClause, Object... args) {
        whereImplementation().not(customClause, args);
        return where();
    }

    @Override
    default WHERE not(WhereExpressionElement... expression) {
        whereImplementation().not(expression);
        return where();
    }

    @Override
    default WHERE or(List<WhereExpressionElement> whereExpressionElements) {
        whereImplementation().or(whereExpressionElements);
        return where();
    }

    @Override
    default WHERE or(String customClause, Object... args) {
        whereImplementation().or(customClause, args);
        return where();
    }

    @Override
    default WHERE or(WhereExpressionElement... whereExpressionElements) {
        whereImplementation().or(whereExpressionElements);
        return where();
    }

}
