/*******************************************************************************
 * Copyright 2013 Francesco Cina'
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.jporm.rx.sync.query.find.impl;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.find.CommonFindQueryRoot;
import com.jporm.rx.query.find.FindQuery;
import com.jporm.rx.sync.query.find.FindQueryOrderBySync;
import com.jporm.rx.sync.query.find.FindQuerySync;
import com.jporm.rx.sync.query.find.FindQueryWhereSync;
import com.jporm.sql.query.clause.WhereExpressionElement;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FindQueryWhereSyncImpl<BEAN> implements FindQueryWhereSync<BEAN> {

    private FindQuerySync<BEAN> findQuerySync;
    private FindQuery<BEAN> findQuery;

    public FindQueryWhereSyncImpl(FindQuerySync<BEAN> findQuerySync, FindQuery<BEAN> findQuery) {
        this.findQuerySync = findQuerySync;
        this.findQuery = findQuery;
    }

    @Override
    public FindQuerySync<BEAN> root() {
        return findQuerySync;
    }

    @Override
    public FindQueryOrderBySync<BEAN> orderBy() throws JpoException {
        return findQuerySync.orderBy();
    }

    @Override
    public FindQuerySync<BEAN> distinct() throws JpoException {
        return findQuerySync.distinct();
    }

    @Override
    public FindQuerySync<BEAN> forUpdate() {
        return findQuerySync.forUpdate();
    }

    @Override
    public FindQuerySync<BEAN> forUpdateNoWait() {
        return findQuerySync.forUpdateNoWait();
    }

    @Override
    public FindQuerySync<BEAN> limit(int limit) throws JpoException {
        return findQuerySync.limit(limit);
    }

    @Override
    public FindQuerySync<BEAN> offset(int offset) throws JpoException {
        return findQuerySync.offset(offset);
    }

    @Override
    public BEAN fetch() {
        return findQuerySync.fetch();
    }

    @Override
    public Optional<BEAN> fetchOptional() {
        return findQuerySync.fetchOptional();
    }

    @Override
    public BEAN fetchUnique() {
        return findQuerySync.fetchUnique();
    }

    @Override
    public Boolean exist() {
        return exist();
    }

    @Override
    public List<BEAN> fetchList() {
        return findQuerySync.fetchList();
    }

    @Override
    public Integer fetchRowCount() {
        return findQuerySync.fetchRowCount();
    }

    @Override
    public FindQueryWhereSync<BEAN> allEq(Map<String, Object> propertyMap) {
        findQuery.where().allEq(propertyMap);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> and(WhereExpressionElement... WhereExpressionElements) {
        findQuery.where().and(WhereExpressionElements);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> and(List<WhereExpressionElement> WhereExpressionElements) {
        findQuery.where().and(WhereExpressionElements);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> and(String customClause, Object... args) {
        findQuery.where().and(customClause, args);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> eq(String property, Object value) {
        findQuery.where().eq(property, value);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> eqProperties(String firstProperty, String secondProperty) {
        findQuery.where().eqProperties(firstProperty, secondProperty);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> ge(String property, Object value) {
        findQuery.where().ge(property, value);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> geProperties(String firstProperty, String secondProperty) {
        findQuery.where().geProperties(firstProperty, secondProperty);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> gt(String property, Object value) {
        findQuery.where().gt(property, value);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> gtProperties(String firstProperty, String secondProperty) {
        findQuery.where().gtProperties(firstProperty, secondProperty);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> ieq(String property, String value) {
        findQuery.where().ieq(property, value);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> ieqProperties(String firstProperty, String secondProperty) {
        findQuery.where().ieqProperties(firstProperty, secondProperty);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> ilike(String property, String value) {
        findQuery.where().ilike(property, value);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> in(String property, CommonFindQueryRoot subQuery) {
        findQuery.where().in(property, subQuery);

        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> in(String property, Collection<?> values) {
        findQuery.where().in(property, values);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> in(String property, Object[] values) {
        findQuery.where().in(property, values);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> isNotNull(String property) {
        findQuery.where().isNotNull(property);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> isNull(String property) {
        findQuery.where().isNull(property);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> le(String property, Object value) {
        findQuery.where().le(property, value);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> leProperties(String firstProperty, String secondProperty) {
        findQuery.where().leProperties(firstProperty, secondProperty);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> like(String property, String value) {
        findQuery.where().like(property, value);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> lt(String property, Object value) {
        findQuery.where().lt(property, value);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> ltProperties(String firstProperty, String secondProperty) {
        findQuery.where().ltProperties(firstProperty, secondProperty);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> ne(String property, Object value) {
        findQuery.where().ne(property, value);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> neProperties(String firstProperty, String secondProperty) {
        findQuery.where().neProperties(firstProperty, secondProperty);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> nin(String property, CommonFindQueryRoot subQuery) {
        findQuery.where().nin(property, subQuery);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> nin(String property, Collection<?> values) {
        findQuery.where().nin(property, values);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> nin(String property, Object[] values) {
        findQuery.where().nin(property, values);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> nlike(String property, String value) {
        findQuery.where().nlike(property, value);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> not(WhereExpressionElement... expression) {
        findQuery.where().not(expression);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> not(List<WhereExpressionElement> whereExpressionElements) {
        findQuery.where().not(whereExpressionElements);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> not(String customClause, Object... args) {
        findQuery.where().not(customClause, args);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> or(WhereExpressionElement... whereExpressionElements) {
        findQuery.where().or(whereExpressionElements);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> or(List<WhereExpressionElement> whereExpressionElements) {
        findQuery.where().or(whereExpressionElements);
        return this;
    }

    @Override
    public FindQueryWhereSync<BEAN> or(String customClause, Object... args) {
        findQuery.where().or(customClause, args);
        return this;
    }
}
