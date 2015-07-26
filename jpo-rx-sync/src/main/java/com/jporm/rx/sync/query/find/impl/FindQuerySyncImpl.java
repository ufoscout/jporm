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

import co.paralleluniverse.fibers.Suspendable;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.rx.query.find.FindQuery;
import com.jporm.rx.sync.quasar.JpoCompletableWrapper;
import com.jporm.rx.sync.query.find.FindQueryOrderBySync;
import com.jporm.rx.sync.query.find.FindQuerySync;
import com.jporm.rx.sync.query.find.FindQueryWhereSync;
import com.jporm.sql.query.clause.SelectCommon;
import com.jporm.sql.query.clause.WhereExpressionElement;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Francesco Cina
 *
 *         20/giu/2011
 */
@Suspendable
public class FindQuerySyncImpl<BEAN> implements FindQuerySync<BEAN> {

    private final FindQuery<BEAN> findQuery;
    private final FindQueryWhereSync<BEAN> findQueryWhereSync;
    private final FindQueryOrderBySync<BEAN> findQueryOrderBySync;

    public FindQuerySyncImpl(FindQuery<BEAN> findQuery) {
        this.findQuery = findQuery;
        findQueryWhereSync = new FindQueryWhereSyncImpl<>(this, findQuery);
        findQueryOrderBySync = new FindQueryOrderBySyncImpl<>(this, findQuery);
    }

    @Override
    public SelectCommon sql() {
        return findQuery.sql();
    }

    @Override
    public BEAN fetch() {
        return JpoCompletableWrapper.get(findQuery.fetch());
    }

    @Override
    public Optional<BEAN> fetchOptional() {
        return JpoCompletableWrapper.get(findQuery.fetchOptional());
    }

    @Override
    public BEAN fetchUnique() {
        return JpoCompletableWrapper.get(findQuery.fetchUnique());
    }

    @Override
    public Boolean exist() {
        return JpoCompletableWrapper.get(findQuery.exist());
    }

    @Override
    public List<BEAN> fetchList() {
        return JpoCompletableWrapper.get(findQuery.fetchList());
    }

    @Override
    public Integer fetchRowCount() {
        return JpoCompletableWrapper.get(findQuery.fetchRowCount());
    }

    @Override
    public FindQueryWhereSync<BEAN> where(WhereExpressionElement... expressionElements) {
        findQuery.where(expressionElements);
        return findQueryWhereSync;
    }

    public FindQueryWhereSync<BEAN> where(List list) {
        findQuery.where(list);
        return findQueryWhereSync;
    }

    @Override
    public FindQueryWhereSync<BEAN> where(String customClause, Object... args) {
        findQuery.where(customClause, args);
        return findQueryWhereSync;
    }

    @Override
    public FindQueryOrderBySync<BEAN> orderBy() throws JpoException {
        findQuery.orderBy();
        return findQueryOrderBySync;
    }

    @Override
    public FindQuerySync<BEAN> cache(String cache) {
        findQuery.cache(cache);
        return this;
    }

    @Override
    public FindQuerySync<BEAN> ignore(String... fields) {
        findQuery.ignore(fields);
        return this;
    }

    @Override
    public FindQuerySync<BEAN> ignore(boolean ignoreFieldsCondition, String... fields) {
        findQuery.ignore(ignoreFieldsCondition, fields);
        return this;
    }

    public FindQuerySync<BEAN> join(Class joinClass) {
        findQuery.join(joinClass);
        return this;
    }

    public FindQuerySync<BEAN> join(Class joinClass, String joinClassAlias) {
        findQuery.join(joinClass, joinClassAlias);
        return this;
    }

    public FindQuerySync<BEAN> naturalJoin(Class joinClass) {
        findQuery.naturalJoin(joinClass);
        return this;
    }

    public FindQuerySync<BEAN> naturalJoin(Class joinClass, String joinClassAlias) {
        findQuery.naturalJoin(joinClass, joinClassAlias);
        return this;
    }

    public FindQuerySync<BEAN> innerJoin(Class joinClass) {
        findQuery.innerJoin(joinClass);
        return this;
    }

    public FindQuerySync<BEAN> innerJoin(Class joinClass, String joinClassAlias) {
        findQuery.innerJoin(joinClass, joinClassAlias);
        return this;
    }

    public FindQuerySync<BEAN> innerJoin(Class joinClass, String onLeftProperty, String onRigthProperty) {
        findQuery.innerJoin(joinClass, onLeftProperty, onRigthProperty);
        return this;
    }

    public FindQuerySync<BEAN> innerJoin(Class joinClass, String joinClassAlias, String onLeftProperty, String onRigthProperty) {
        findQuery.innerJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
        return this;
    }

    public FindQuerySync<BEAN> leftOuterJoin(Class joinClass) {
        findQuery.leftOuterJoin(joinClass);
        return this;
    }

    public FindQuerySync<BEAN> leftOuterJoin(Class joinClass, String joinClassAlias) {
        findQuery.leftOuterJoin(joinClass, joinClassAlias);
        return this;
    }

    public FindQuerySync<BEAN> leftOuterJoin(Class joinClass, String onLeftProperty, String onRigthProperty) {
        findQuery.leftOuterJoin(joinClass, onLeftProperty, onRigthProperty);
        return this;
    }

    public FindQuerySync<BEAN> leftOuterJoin(Class joinClass, String joinClassAlias, String onLeftProperty, String onRigthProperty) {
        findQuery.leftOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
        return this;
    }

    public FindQuerySync<BEAN> rightOuterJoin(Class joinClass) {
        findQuery.rightOuterJoin(joinClass);
        return this;
    }

    public FindQuerySync<BEAN> rightOuterJoin(Class joinClass, String joinClassAlias) {
        findQuery.rightOuterJoin(joinClass, joinClassAlias);
        return this;
    }

    public FindQuerySync<BEAN> rightOuterJoin(Class joinClass, String onLeftProperty, String onRigthProperty) {
        findQuery.rightOuterJoin(joinClass, onLeftProperty, onRigthProperty);
        return this;
    }

    public FindQuerySync<BEAN> rightOuterJoin(Class joinClass, String joinClassAlias, String onLeftProperty, String onRigthProperty) {
        findQuery.rightOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
        return this;
    }

    public FindQuerySync<BEAN> fullOuterJoin(Class joinClass) {
        findQuery.fullOuterJoin(joinClass);
        return this;
    }

    public FindQuerySync<BEAN> fullOuterJoin(Class joinClass, String joinClassAlias) {
        findQuery.fullOuterJoin(joinClass, joinClassAlias);
        return this;
    }

    public FindQuerySync<BEAN> fullOuterJoin(Class joinClass, String onLeftProperty, String onRigthProperty) {
        findQuery.fullOuterJoin(joinClass, onLeftProperty, onRigthProperty);
        return this;
    }

    public FindQuerySync<BEAN> fullOuterJoin(Class joinClass, String joinClassAlias, String onLeftProperty, String onRigthProperty) {
        findQuery.fullOuterJoin(joinClass, joinClassAlias, onLeftProperty, onRigthProperty);
        return this;
    }

    @Override
    public FindQuerySync<BEAN> distinct() throws JpoException {
        findQuery.distinct();
        return this;
    }

    @Override
    public FindQuerySync<BEAN> forUpdate() {
        findQuery.forUpdate();
        return this;
    }

    @Override
    public FindQuerySync<BEAN> forUpdateNoWait() {
        findQuery.forUpdateNoWait();
        return this;
    }

    @Override
    public FindQuerySync<BEAN> limit(int limit) throws JpoException {
        findQuery.limit(limit);
        return this;
    }

    @Override
    public FindQuerySync<BEAN> offset(int offset) throws JpoException {
        findQuery.offset(offset);
        return this;
    }
}
