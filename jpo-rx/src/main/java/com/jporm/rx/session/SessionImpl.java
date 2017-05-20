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
package com.jporm.rx.session;

import java.util.concurrent.CompletableFuture;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.commons.core.connection.AsyncConnectionProvider;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.inject.ClassToolMap;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.persistor.Persistor;
import com.jporm.rx.query.delete.CustomDeleteQuery;
import com.jporm.rx.query.delete.CustomDeleteQueryImpl;
import com.jporm.rx.query.delete.DeleteQueryImpl;
import com.jporm.rx.query.delete.DeleteResult;
import com.jporm.rx.query.find.CustomFindQuery;
import com.jporm.rx.query.find.CustomFindQueryImpl;
import com.jporm.rx.query.find.CustomResultFindQueryBuilder;
import com.jporm.rx.query.find.CustomResultFindQueryBuilderImpl;
import com.jporm.rx.query.find.FindQuery;
import com.jporm.rx.query.find.FindQueryImpl;
import com.jporm.rx.query.save.CustomSaveQuery;
import com.jporm.rx.query.save.CustomSaveQueryImpl;
import com.jporm.rx.query.save.SaveQueryImpl;
import com.jporm.rx.query.update.CustomUpdateQuery;
import com.jporm.rx.query.update.CustomUpdateQueryImpl;
import com.jporm.rx.query.update.UpdateQueryImpl;
import com.jporm.sql.dialect.DBProfile;

public class SessionImpl implements Session {

    private final ServiceCatalog serviceCatalog;
    private final ClassToolMap classToolMap;
    private final SqlFactory sqlFactory;
    private final DBProfile dbType;
    private final SqlCache sqlCache;
    private final SqlSession sqlSession;

    public SessionImpl(final ServiceCatalog serviceCatalog, final AsyncConnectionProvider connectionProvider, final boolean autoCommit, SqlCache sqlCache,
            SqlFactory sqlFactory) {
        this.serviceCatalog = serviceCatalog;
        this.sqlCache = sqlCache;
        this.sqlFactory = sqlFactory;
        classToolMap = serviceCatalog.getClassToolMap();
        dbType = connectionProvider.getDBProfile();
        sqlSession = new SqlSessionImpl(new SqlExecutorImpl(serviceCatalog.getTypeFactory(), connectionProvider, autoCommit), sqlFactory.getSqlDsl());
    }

    @Override
    public <BEAN> CompletableFuture<DeleteResult> delete(final BEAN bean) {
        Class<BEAN> typedClass = (Class<BEAN>) bean.getClass();
        return new DeleteQueryImpl<>(bean, typedClass, serviceCatalog.getClassToolMap().get(typedClass), sqlCache, sql().executor()).execute();
    }

    @Override
    public <BEAN> CustomDeleteQuery delete(final Class<BEAN> clazz) throws JpoException {
        return new CustomDeleteQueryImpl(sqlFactory.deleteFrom(clazz), sql().executor());
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

    private final <BEAN> FindQuery<BEAN> find(final Class<BEAN> clazz, final Object... pkFieldValues) throws JpoException {
        return new FindQueryImpl<>(clazz, pkFieldValues, serviceCatalog.getClassToolMap().get(clazz), sql().executor(), sqlFactory, sqlCache);
    }

    @Override
    public final <BEAN> CustomFindQuery<BEAN> find(final Class<BEAN> clazz, final String alias) throws JpoException {
        return new CustomFindQueryImpl<>(clazz, alias, serviceCatalog.getClassToolMap().get(clazz), sql().executor(), sqlFactory);
    }

    @Override
    public <BEAN> CustomResultFindQueryBuilder find(final String... selectFields) {
        return new CustomResultFindQueryBuilderImpl(selectFields, sql().executor(), sqlFactory);
    }

    @Override
    public final <BEAN> FindQuery<BEAN> findById(final Class<BEAN> clazz, final Object value) throws JpoException {
        return this.find(clazz, value);
    }

    @Override
    public final <BEAN> FindQuery<BEAN> findByModelId(final BEAN model) throws JpoException {
        Class<BEAN> modelClass = (Class<BEAN>) model.getClass();
        ClassTool<BEAN> ormClassTool = classToolMap.get(modelClass);
        ClassDescriptor<BEAN> descriptor = ormClassTool.getDescriptor();
        String[] pks = descriptor.getPrimaryKeyColumnJavaNames();
        Object[] values = ormClassTool.getPersistor().getPropertyValues(pks, model);
        return find(modelClass, values);
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
        Class<BEAN> typedClass = (Class<BEAN>) bean.getClass();
        return new SaveQueryImpl<>(bean, typedClass, serviceCatalog.getClassToolMap().get(typedClass), sqlCache, sql().executor(), sqlFactory, dbType).execute();
    }

    @Override
    public <BEAN> CustomSaveQuery save(final Class<BEAN> clazz, final String... fields) throws JpoException {
        return new CustomSaveQueryImpl<>(sqlFactory.insertInto(clazz, fields), sql().executor());
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
    public SqlSession sql() {
        return sqlSession;
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
        Class<BEAN> typedClass = (Class<BEAN>) bean.getClass();
        return new UpdateQueryImpl<>(bean, typedClass, serviceCatalog.getClassToolMap().get(typedClass), sqlCache, sql().executor()).execute();
    }

    @Override
    public <BEAN> CustomUpdateQuery update(final Class<BEAN> clazz) throws JpoException {
        return new CustomUpdateQueryImpl(sqlFactory.update(clazz), sql().executor());
    }

}
