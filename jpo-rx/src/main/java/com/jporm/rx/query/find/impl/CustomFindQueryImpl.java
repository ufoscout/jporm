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
package com.jporm.rx.query.find.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultManyResultsException;
import com.jporm.commons.core.exception.JpoNotUniqueResultNoResultException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.find.CommonFindQueryImpl;
import com.jporm.commons.core.query.find.impl.CommonFindFromImpl;
import com.jporm.persistor.Persistor;
import com.jporm.rx.query.find.CustomFindQuery;
import com.jporm.rx.query.find.CustomFindQueryOrderBy;
import com.jporm.rx.query.find.CustomFindQueryWhere;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.dsl.dialect.DBType;
import com.jporm.sql.dsl.query.select.pagination.PaginationProvider;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

/**
 *
 * @author Francesco Cina
 *
 *         20/giu/2011
 */
public class CustomFindQueryImpl<BEAN> extends CommonFindQueryImpl<CustomFindQuery<BEAN>, CustomFindQueryWhere<BEAN>, CustomFindQueryOrderBy<BEAN>> implements CustomFindQuery<BEAN> {

    private final ServiceCatalog serviceCatalog;
    private final Class<BEAN> clazz;
    private final SqlExecutor sqlExecutor;

    public CustomFindQueryImpl(final ServiceCatalog serviceCatalog, final Class<BEAN> clazz, final String alias, final SqlExecutor sqlExecutor,
            final SqlFactory sqlFactory) {
        super(clazz, alias, sqlFactory, serviceCatalog.getClassToolMap());
        this.serviceCatalog = serviceCatalog;
        this.clazz = clazz;
        this.sqlExecutor = sqlExecutor;
        Select select = getSelect();
        select.selectFields(getAllColumns());
        setFrom(new CommonFindFromImpl<>(select.from(), this));
        setWhere(new CustomFindQueryWhereImpl<>(select.where(), this));
        setOrderBy(new CustomFindQueryOrderByImpl<>(select.orderBy(), this));
    }

    @Override
    public CompletableFuture<Boolean> exist() {
        return fetchRowCount().thenApply(count -> count > 0);
    }

    @Override
    public CompletableFuture<BEAN> fetch() {
        return get(resultSet -> {
            if (resultSet.next()) {
                return getPersistor().beanFromResultSet(resultSet, getIgnoredFields()).getBean();
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<List<BEAN>> fetchList() {
        final Persistor<BEAN> persistor = getPersistor();
        return get((rowEntry, count) -> {
            return persistor.beanFromResultSet(rowEntry, getIgnoredFields()).getBean();
        });
    }

    @Override
    public CompletableFuture<Optional<BEAN>> fetchOptional() {
        return fetch().thenApply(Optional::ofNullable);
    }

    @Override
    public CompletableFuture<Integer> fetchRowCount() {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.queryForInt(renderRowCountSql(dbType), getParams());
        });
    }

    @Override
    public CompletableFuture<BEAN> fetchUnique() {
        final Persistor<BEAN> persistor = getPersistor();
        return get((rowEntry, count) -> {
            if (count > 0) {
                throw new JpoNotUniqueResultManyResultsException(
                        "The query execution returned a number of rows different than one: more than one result found"); //$NON-NLS-1$
            }
            return persistor.beanFromResultSet(rowEntry, getIgnoredFields()).getBean();
        }).thenApply(beans -> {
            if (beans.isEmpty()) {
                throw new JpoNotUniqueResultNoResultException("The query execution returned a number of rows different than one: no results found"); //$NON-NLS-1$
            }
            return beans.get(0);
        });
    }

    private <T> CompletableFuture<T> get(final ResultSetReader<T> rsr) throws JpoException {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.query(renderSql(dbType), getParams(), rsr);
        });
    }

    private <T> CompletableFuture<List<T>> get(final ResultSetRowReader<T> rsr) throws JpoException {
        return sqlExecutor.dbType().thenCompose(dbType -> {
            return sqlExecutor.query(renderSql(dbType), getParams(), rsr);
        });
    }

    private List<Object> getParams() {
        final List<Object> params = new ArrayList<>();
        sql().sqlValues(params);
        return params;
    }

    private Persistor<BEAN> getPersistor() {
        return serviceCatalog.getClassToolMap().get(clazz).getPersistor();
    }

    @Override
    public PaginationProvider sql() {
        return getSelect();
    }

    protected String renderSql(DBType dbType) {
        return sql().sqlQuery(dbType.getDBProfile());
    }

    protected String renderRowCountSql(DBType dbType) {
        return getSelect().renderRowCountSql(dbType.getDBProfile());
    }

    protected ServiceCatalog getServiceCatalog() {
        return serviceCatalog;
    }

    protected Class<BEAN> getBeanClass() {
        return clazz;
    }

}
