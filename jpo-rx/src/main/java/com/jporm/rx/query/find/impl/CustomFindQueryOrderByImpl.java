/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rx.query.find.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.find.impl.CommonFindQueryOrderByImpl;
import com.jporm.rx.query.find.CustomFindQuery;
import com.jporm.rx.query.find.CustomFindQueryGroupBy;
import com.jporm.rx.query.find.CustomFindQueryOrderBy;
import com.jporm.rx.query.find.CustomFindQueryWhere;
import com.jporm.sql.query.clause.SelectCommon;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

/**
 * @author ufo
 */
public class CustomFindQueryOrderByImpl extends CommonFindQueryOrderByImpl<CustomFindQuery, CustomFindQueryWhere, CustomFindQueryOrderBy>
        implements CustomFindQueryOrderBy {

    public CustomFindQueryOrderByImpl(final com.jporm.sql.query.clause.OrderBy sqlOrderBy, final CustomFindQuery customFindQuery) {
        super(sqlOrderBy, customFindQuery);
    }

    @Override
    public <T> CompletableFuture<T> fetch(final ResultSetReader<T> rsr) {
        return root().fetch(rsr);
    }

    @Override
    public <T> CompletableFuture<List<T>> fetch(final ResultSetRowReader<T> rsrr) {
        return root().fetch(rsrr);
    }

    @Override
    public CompletableFuture<BigDecimal> fetchBigDecimal() {
        return root().fetchBigDecimal();
    }

    @Override
    public CompletableFuture<Optional<BigDecimal>> fetchBigDecimalOptional() {
        return root().fetchBigDecimalOptional();
    }

    @Override
    public CompletableFuture<BigDecimal> fetchBigDecimalUnique() {
        return root().fetchBigDecimalUnique();
    }

    @Override
    public CompletableFuture<Boolean> fetchBoolean() {
        return root().fetchBoolean();
    }

    @Override
    public CompletableFuture<Optional<Boolean>> fetchBooleanOptional() {
        return root().fetchBooleanOptional();
    }

    @Override
    public CompletableFuture<Boolean> fetchBooleanUnique() {
        return root().fetchBooleanUnique();
    }

    @Override
    public CompletableFuture<Double> fetchDouble() {
        return root().fetchDouble();
    }

    @Override
    public CompletableFuture<Optional<Double>> fetchDoubleOptional() {
        return root().fetchDoubleOptional();
    }

    @Override
    public CompletableFuture<Double> fetchDoubleUnique() {
        return root().fetchDoubleUnique();
    }

    @Override
    public CompletableFuture<Float> fetchFloat() {
        return root().fetchFloat();
    }

    @Override
    public CompletableFuture<Optional<Float>> fetchFloatOptional() {
        return root().fetchFloatOptional();
    }

    @Override
    public CompletableFuture<Float> fetchFloatUnique() {
        return root().fetchFloatUnique();
    }

    @Override
    public CompletableFuture<Integer> fetchInt() {
        return root().fetchInt();
    }

    @Override
    public CompletableFuture<Optional<Integer>> fetchIntOptional() {
        return root().fetchIntOptional();
    }

    @Override
    public CompletableFuture<Integer> fetchIntUnique() {
        return root().fetchIntUnique();
    }

    @Override
    public CompletableFuture<Long> fetchLong() {
        return root().fetchLong();
    }

    @Override
    public CompletableFuture<Optional<Long>> fetchLongOptional() {
        return root().fetchLongOptional();
    }

    @Override
    public CompletableFuture<Long> fetchLongUnique() {
        return root().fetchLongUnique();
    }

    @Override
    public CompletableFuture<String> fetchString() {
        return root().fetchString();
    }

    @Override
    public CompletableFuture<Optional<String>> fetchStringOptional() {
        return root().fetchStringOptional();
    }

    @Override
    public CompletableFuture<String> fetchStringUnique() {
        return root().fetchStringUnique();
    }

    @Override
    public <T> CompletableFuture<T> fetchUnique(final ResultSetRowReader<T> rsrr) {
        return root().fetchUnique(rsrr);
    }

    @Override
    public CustomFindQueryGroupBy groupBy(final String... fields) throws JpoException {
        return root().groupBy(fields);
    }

    @Override
    public SelectCommon sql() {
        return root().sql();
    }

}
