/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rx.session.impl;

import java.util.concurrent.CompletableFuture;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.commons.core.connection.AsyncConnectionProvider;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.inject.ClassToolMap;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.persistor.Persistor;
import com.jporm.rx.query.delete.CustomDeleteQuery;
import com.jporm.rx.query.delete.DeleteResult;
import com.jporm.rx.query.delete.impl.CustomDeleteQueryImpl;
import com.jporm.rx.query.delete.impl.DeleteQueryImpl;
import com.jporm.rx.query.find.CustomFindQuery;
import com.jporm.rx.query.find.CustomFindQueryWhere;
import com.jporm.rx.query.find.CustomResultFindQueryBuilder;
import com.jporm.rx.query.find.FindQuery;
import com.jporm.rx.query.find.impl.CustomFindQueryImpl;
import com.jporm.rx.query.find.impl.CustomResultFindQueryBuilderImpl;
import com.jporm.rx.query.find.impl.FindQueryImpl;
import com.jporm.rx.query.save.CustomSaveQuery;
import com.jporm.rx.query.save.impl.CustomSaveQueryImpl;
import com.jporm.rx.query.save.impl.SaveQueryImpl;
import com.jporm.rx.query.update.CustomUpdateQuery;
import com.jporm.rx.query.update.impl.CustomUpdateQueryImpl;
import com.jporm.rx.query.update.impl.UpdateQueryImpl;
import com.jporm.rx.session.Session;
import com.jporm.rx.session.SqlExecutor;

public class SessionImpl implements Session {

    private final ServiceCatalog serviceCatalog;
    private final AsyncConnectionProvider connectionProvider;
    private final ClassToolMap classToolMap;
    private final SqlFactory sqlFactory;
    private final boolean autoCommit;

    public SessionImpl(final ServiceCatalog serviceCatalog, final AsyncConnectionProvider connectionProvider, final boolean autoCommit) {
        this.serviceCatalog = serviceCatalog;
        this.connectionProvider = connectionProvider;
        this.autoCommit = autoCommit;
        classToolMap = serviceCatalog.getClassToolMap();
        sqlFactory = new SqlFactory(classToolMap, serviceCatalog.getPropertiesFactory());
    }

    @Override
    public <BEAN> CompletableFuture<DeleteResult> delete(final BEAN bean) {
        return new DeleteQueryImpl<BEAN>(bean, (Class<BEAN>) bean.getClass(), serviceCatalog, sqlExecutor(), sqlFactory).execute();
    }

    @Override
    public <BEAN> CustomDeleteQuery<BEAN> delete(final Class<BEAN> clazz) throws JpoException {
        return new CustomDeleteQueryImpl<>(clazz, serviceCatalog, sqlExecutor(), sqlFactory);
    }

    /**
     * Returns whether a bean has to be saved. Otherwise it has to be updated
     * because it already exists.
     *
     * @return
     */
    private <BEAN> CompletableFuture<Boolean> exist(final BEAN bean, final Persistor<BEAN> persistor) {
        if (persistor.hasGenerator()) {
            return CompletableFuture.completedFuture(!persistor.useGenerators(bean));
        } else {
            return findByModelId(bean).exist();
        }
    }

    @Override
    public final <BEAN> CustomFindQuery<BEAN> find(final Class<BEAN> clazz) throws JpoException {
        return find(clazz, clazz.getSimpleName());
    }

    private final <BEAN> FindQuery<BEAN> find(final Class<BEAN> clazz, final ClassDescriptor<BEAN> descriptor, final String[] pks, final Object[] values)
            throws JpoException {
        FindQueryImpl<BEAN> findQuery = new FindQueryImpl<BEAN>(serviceCatalog, clazz, clazz.getSimpleName(), sqlExecutor(), sqlFactory);
        CustomFindQueryWhere<BEAN> query = findQuery.where();
        for (int i = 0; i < pks.length; i++) {
            query.eq(pks[i], values[i]);
        }
        query.limit(1);
        return findQuery;
    }

    @Override
    public final <BEAN> CustomFindQuery<BEAN> find(final Class<BEAN> clazz, final String alias) throws JpoException {
        final CustomFindQueryImpl<BEAN> query = new CustomFindQueryImpl<BEAN>(serviceCatalog, clazz, alias, sqlExecutor(), sqlFactory);
        return query;
    }

    @Override
    public <BEAN> CustomResultFindQueryBuilder find(final String... selectFields) {
        return new CustomResultFindQueryBuilderImpl(selectFields, serviceCatalog, sqlExecutor(), sqlFactory);
    }

    @Override
    public final <BEAN> FindQuery<BEAN> findById(final Class<BEAN> clazz, final Object value) throws JpoException {
        ClassTool<BEAN> ormClassTool = classToolMap.get(clazz);
        ClassDescriptor<BEAN> descriptor = ormClassTool.getDescriptor();
        String[] pks = descriptor.getPrimaryKeyColumnJavaNames();
        return this.find(clazz, descriptor, pks, new Object[] { value });
    }

    @Override
    public final <BEAN> FindQuery<BEAN> findByModelId(final BEAN model) throws JpoException {
        Class<BEAN> modelClass = (Class<BEAN>) model.getClass();
        ClassTool<BEAN> ormClassTool = classToolMap.get(modelClass);
        ClassDescriptor<BEAN> descriptor = ormClassTool.getDescriptor();
        String[] pks = descriptor.getPrimaryKeyColumnJavaNames();
        Object[] values = ormClassTool.getPersistor().getPropertyValues(pks, model);
        return find(modelClass, descriptor, pks, values);
    }

    @Override
    public <BEAN> CompletableFuture<BEAN> save(final BEAN bean) {
        try {
            serviceCatalog.getValidatorService().validateThrowException(bean);
        } catch (Exception e) {
            CompletableFuture<BEAN> validate = new CompletableFuture<BEAN>();
            validate.completeExceptionally(e);
            return validate;
        }
        return new SaveQueryImpl<BEAN>(bean, (Class<BEAN>) bean.getClass(), serviceCatalog, sqlExecutor(), sqlFactory).execute();
    }

    @Override
    public <BEAN> CustomSaveQuery save(final Class<BEAN> clazz, final String... fields) throws JpoException {
        return new CustomSaveQueryImpl<>(clazz, fields, serviceCatalog, sqlExecutor(), sqlFactory);
    }

    @Override
    public <BEAN> CompletableFuture<BEAN> saveOrUpdate(final BEAN bean) {
        try {
            serviceCatalog.getValidatorService().validateThrowException(bean);
        } catch (Exception e) {
            CompletableFuture<BEAN> validate = new CompletableFuture<BEAN>();
            validate.completeExceptionally(e);
            return validate;
        }
        Persistor<BEAN> persistor = (Persistor<BEAN>) serviceCatalog.getClassToolMap().get(bean.getClass()).getPersistor();
        return exist(bean, persistor).thenCompose(exists -> {
            if (exists) {
                return update(bean);
            }
            return save(bean);
        });
    }

    @Override
    public SqlExecutor sqlExecutor() {
        return new SqlExecutorImpl(serviceCatalog.getTypeFactory(), connectionProvider, autoCommit);
    }

    @Override
    public <BEAN> CompletableFuture<BEAN> update(final BEAN bean) {
        try {
            serviceCatalog.getValidatorService().validateThrowException(bean);
        } catch (Exception e) {
            CompletableFuture<BEAN> validate = new CompletableFuture<BEAN>();
            validate.completeExceptionally(e);
            return validate;
        }
        return new UpdateQueryImpl<BEAN>(bean, (Class<BEAN>) bean.getClass(), serviceCatalog, sqlExecutor(), sqlFactory).execute();
    }

    @Override
    public <BEAN> CustomUpdateQuery update(final Class<BEAN> clazz) throws JpoException {
        return new CustomUpdateQueryImpl(clazz, serviceCatalog, sqlExecutor(), sqlFactory);
    }

}
