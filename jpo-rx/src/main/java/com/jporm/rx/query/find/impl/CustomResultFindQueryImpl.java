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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.find.CommonFindQueryImpl;
import com.jporm.commons.core.query.find.impl.CommonFindFromImpl;
import com.jporm.rx.query.find.CustomResultFindQuery;
import com.jporm.rx.query.find.CustomResultFindQueryGroupBy;
import com.jporm.rx.query.find.CustomResultFindQueryOrderBy;
import com.jporm.rx.query.find.CustomResultFindQueryWhere;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.query.select.pagination.PaginationProvider;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

/**
 * @author Francesco Cina 20/giu/2011
 */
public class CustomResultFindQueryImpl extends CommonFindQueryImpl<CustomResultFindQuery, CustomResultFindQueryWhere, CustomResultFindQueryOrderBy> implements CustomResultFindQuery {

    private final CustomResultFindQueryGroupByImpl groupBy;
    private final SqlExecutor sqlExecutor;

    public CustomResultFindQueryImpl(final String[] selectFields, final ServiceCatalog serviceCatalog, final Class<?> clazz, final String alias,
            final SqlExecutor sqlExecutor, final SqlFactory sqlFactory) {
        super(clazz, alias, sqlFactory, serviceCatalog.getClassToolMap());
        this.sqlExecutor = sqlExecutor;
        Select select = getSelect();
        select.selectFields(selectFields);
        groupBy = new CustomResultFindQueryGroupByImpl(select.groupBy(), this);
        setFrom(new CommonFindFromImpl<>(select.from(), this));
        setWhere(new CustomResultFindQueryWhereImpl(select.where(), this));
        setOrderBy(new CustomResultFindQueryOrderByImpl(select.orderBy(), this));
    }

    @Override
    public <T> CompletableFuture<T> fetch(final ResultSetReader<T> rsr) {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.query(sql().sqlQuery(dbType.getDBProfile()), getParams(), rsr);
        });
    }

    @Override
    public <T> CompletableFuture<List<T>> fetch(final ResultSetRowReader<T> rsrr) {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.query(sql().sqlQuery(dbType.getDBProfile()), getParams(), rsrr);
        });
    }

    @Override
    public CompletableFuture<BigDecimal> fetchBigDecimal() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForBigDecimal(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public CompletableFuture<Optional<BigDecimal>> fetchBigDecimalOptional() {
        return toOptional(fetchBigDecimal());
    }

    @Override
    public CompletableFuture<BigDecimal> fetchBigDecimalUnique() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForBigDecimalUnique(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public CompletableFuture<Boolean> fetchBoolean() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForBoolean(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public CompletableFuture<Optional<Boolean>> fetchBooleanOptional() {
        return toOptional(fetchBoolean());
    }

    @Override
    public CompletableFuture<Boolean> fetchBooleanUnique() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForBooleanUnique(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public CompletableFuture<Double> fetchDouble() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForDouble(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public CompletableFuture<Optional<Double>> fetchDoubleOptional() {
        return toOptional(fetchDouble());
    }

    @Override
    public CompletableFuture<Double> fetchDoubleUnique() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForDoubleUnique(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public CompletableFuture<Float> fetchFloat() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForFloat(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public CompletableFuture<Optional<Float>> fetchFloatOptional() {
        return toOptional(fetchFloat());
    }

    @Override
    public CompletableFuture<Float> fetchFloatUnique() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForFloatUnique(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public CompletableFuture<Integer> fetchInt() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForInt(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public CompletableFuture<Optional<Integer>> fetchIntOptional() {
        return toOptional(fetchInt());
    }

    @Override
    public CompletableFuture<Integer> fetchIntUnique() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForIntUnique(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public CompletableFuture<Long> fetchLong() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForLong(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public CompletableFuture<Optional<Long>> fetchLongOptional() {
        return toOptional(fetchLong());
    }

    @Override
    public CompletableFuture<Long> fetchLongUnique() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForLongUnique(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public CompletableFuture<String> fetchString() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForString(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public CompletableFuture<Optional<String>> fetchStringOptional() {
        return toOptional(fetchString());
    }

    @Override
    public CompletableFuture<String> fetchStringUnique() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForStringUnique(sql().sqlQuery(dbType.getDBProfile()), getParams());
        });
    }

    @Override
    public <T> CompletableFuture<T> fetchUnique(final ResultSetRowReader<T> rsrr) {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForUnique(sql().sqlQuery(dbType.getDBProfile()), getParams(), rsrr);
        });
    }

    private List<Object> getParams() {
        final List<Object> params = new ArrayList<>();
        sql().sqlValues(params);
        return params;
    }

    @Override
    public CustomResultFindQueryGroupBy groupBy(final String... fields) throws JpoException {
        groupBy.fields(fields);
        return groupBy;
    }

    @Override
    public PaginationProvider sql() {
        return getSelect();
    }

    private <T> CompletableFuture<Optional<T>> toOptional(final CompletableFuture<T> future) {
        return future.thenApply(value -> Optional.ofNullable(value));
    }

}
