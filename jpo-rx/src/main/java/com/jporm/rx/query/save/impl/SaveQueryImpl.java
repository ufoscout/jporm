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
package com.jporm.rx.query.save.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.save.ASaveQuery;
import com.jporm.persistor.Persistor;
import com.jporm.rx.query.save.SaveQuery;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.dialect.DBType;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class SaveQueryImpl<BEAN> extends ASaveQuery<BEAN> implements SaveQuery<BEAN> {

    private final BEAN bean;
    private final SqlExecutor sqlExecutor;

    public SaveQueryImpl(final BEAN bean, final Class<BEAN> clazz, final ServiceCatalog serviceCatalog, final SqlExecutor sqlExecutor,
            final SqlFactory sqlFactory) {
        super(serviceCatalog.getClassToolMap().get(clazz), clazz, serviceCatalog.getSqlCache(), sqlFactory);
        this.sqlExecutor = sqlExecutor;
        this.bean = bean;
    }

    @Override
    public CompletableFuture<BEAN> execute() {
        final Persistor<BEAN> persistor = getOrmClassTool().getPersistor();
        return sqlExecutor.dbType().thenCompose(dbType -> now(dbType, bean, persistor));

    }

    private CompletableFuture<List<BEAN>> executeNEW() {
        final Persistor<BEAN> persistor = getOrmClassTool().getPersistor();
        final CompletableFuture<List<BEAN>> futureResult = new CompletableFuture<>();
        final List<BEAN> beans = Arrays.asList(bean);
        final List<BEAN> results = new ArrayList<>();
        Iterator<BEAN> iterator = beans.iterator();

        sqlExecutor.dbType().thenCompose(dbType -> {
            doOne(iterator, persistor, beans, dbType, results, futureResult);
            return null;
        });

        return futureResult;
    }

    private void doOne(Iterator<BEAN> iterator, Persistor<BEAN> persistor, List<BEAN> beans, DBType dbType, List<BEAN> results, CompletableFuture<List<BEAN>> futureResult) {
        if (iterator.hasNext()) {
            CompletableFuture<BEAN> comp = now(dbType, iterator.next(), persistor);
            comp.handle(new BiFunction<BEAN, Throwable, Void>() {
                @Override
                public Void apply(BEAN t, Throwable u) {
                    if (u!=null) {
                        futureResult.completeExceptionally(u);
                    } else {
                        results.add(t);
                        doOne(iterator, persistor, beans, dbType, results, futureResult);
                    }
                    return null;
                }
            });
        } else {
            futureResult.complete(results);
        }
    }

    private CompletableFuture<BEAN> now(final DBType dbType, BEAN bean, Persistor<BEAN> persistor) {
        BEAN clonedBean = persistor.clone(bean);

        // CHECK IF OBJECT HAS A 'VERSION' FIELD and increase it
        persistor.increaseVersion(clonedBean, true);
        boolean useGenerator = persistor.useGenerators(clonedBean);
        String sql = getCacheableQuery(dbType.getDBProfile(), useGenerator);

        if (!useGenerator) {
            String[] keys = getOrmClassTool().getDescriptor().getAllColumnJavaNames();
            Object[] values = persistor.getPropertyValues(keys, clonedBean);
            return sqlExecutor.update(sql, values).thenApply(result -> clonedBean);
        } else {
            final GeneratedKeyReader generatedKeyExtractor = new GeneratedKeyReader() {
                @Override
                public String[] generatedColumnNames() {
                    return getOrmClassTool().getDescriptor().getAllGeneratedColumnDBNames();
                }

                @Override
                public void read(final ResultSet generatedKeyResultSet) {
                    if (generatedKeyResultSet.next()) {
                        persistor.updateGeneratedValues(generatedKeyResultSet, clonedBean);
                    }
                }
            };
            String[] keys = getOrmClassTool().getDescriptor().getAllNotGeneratedColumnJavaNames();
            Object[] values = persistor.getPropertyValues(keys, clonedBean);
            return sqlExecutor.update(sql, values, generatedKeyExtractor).thenApply(result -> clonedBean);
        }
    }

}
