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
/*
 * ---------------------------------------------------------------------------- PROJECT : JPOrm CREATED BY : Francesco
 * Cina' ON : Feb 23, 2013 ----------------------------------------------------------------------------
 */
package com.jporm.rx.query.update.impl;

import java.util.concurrent.CompletableFuture;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.commons.core.exception.JpoOptimisticLockException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.update.AUpdateQuery;
import com.jporm.commons.core.util.ArrayUtil;
import com.jporm.persistor.Persistor;
import com.jporm.rx.query.update.UpdateQuery;
import com.jporm.rx.query.update.UpdateResult;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.dialect.DBType;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class UpdateQueryImpl<BEAN> extends AUpdateQuery<BEAN> implements UpdateQuery<BEAN> {

    // private final BEAN bean;
    private final Class<BEAN> clazz;
    private final String[] pkAndVersionFieldNames;
    private final String[] notPksFieldNames;
    private final SqlExecutor sqlExecutor;
    private final BEAN bean;

    /**
     * @param newBean
     * @param serviceCatalog
     * @param ormSession
     */
    public UpdateQueryImpl(final BEAN bean, final Class<BEAN> clazz, final ServiceCatalog serviceCatalog, final SqlExecutor sqlExecutor,
            final SqlFactory sqlFactory) {
        super(clazz, serviceCatalog.getClassToolMap().get(clazz), serviceCatalog.getSqlCache(), sqlFactory);
        this.bean = bean;
        this.clazz = clazz;
        this.sqlExecutor = sqlExecutor;
        ClassDescriptor<BEAN> descriptor = getOrmClassTool().getDescriptor();
        pkAndVersionFieldNames = descriptor.getPrimaryKeyAndVersionColumnJavaNames();
        notPksFieldNames = descriptor.getNotPrimaryKeyColumnJavaNames();
    }

    @Override
    public CompletableFuture<BEAN> execute() {
        return sqlExecutor.dbType().thenCompose(this::now);
    }

    private CompletableFuture<BEAN> now(final DBType dbType) {

        Persistor<BEAN> persistor = getOrmClassTool().getPersistor();
        BEAN updatedBean = persistor.clone(bean);

        Object[] pkAndOriginalVersionValues = persistor.getPropertyValues(pkAndVersionFieldNames, updatedBean);
        persistor.increaseVersion(updatedBean, false);
        Object[] notPksValues = persistor.getPropertyValues(notPksFieldNames, updatedBean);

        // if (persistor.isVersionableWithLock()) {
        //
        // if (sqlExecutor.queryForIntUnique(lockQuery,
        // pkAndOriginalVersionValues) == 0) {
        // throw new JpoOptimisticLockException(
        // "The bean of class [" + clazz + "] cannot be updated. Version in the
        // DB is not the expected one."); //$NON-NLS-1$
        // }
        // }

        CompletableFuture<UpdateResult> update = sqlExecutor.update(getCacheableQuery(dbType.getDBProfile()),
                ArrayUtil.concat(notPksValues, pkAndOriginalVersionValues));
        return update.thenApply(updateResult -> {
            if (updateResult.updated() == 0) {
                throw new JpoOptimisticLockException("The bean of class [" + clazz //$NON-NLS-1$
                        + "] cannot be updated. Version in the DB is not the expected one or the ID of the bean is associated with and existing bean.");
            } else {
                return updatedBean;
            }
        });

    }

    // @Override
    // public Stream<BEAN> executeWithSimpleUpdate() {
    // executed = true;
    //
    // String updateQuery = getQuery();
    // String lockQuery = getLockQuery();
    //
    // // VERSION WITHOUT BATCH UPDATE
    // return beans.map(bean -> {
    // Persistor<BEAN> persistor = getOrmClassTool().getPersistor();
    // BEAN updatedBean = persistor.clone(bean);
    //
    // Object[] pkAndOriginalVersionValues =
    // persistor.getPropertyValues(pkAndVersionFieldNames, updatedBean);
    // persistor.increaseVersion(updatedBean, false);
    // Object[] notPksValues = persistor.getPropertyValues(notPksFieldNames,
    // updatedBean);
    //
    // if (persistor.isVersionableWithLock()) {
    //
    // if (sqlExecutor.queryForIntUnique(lockQuery, pkAndOriginalVersionValues)
    // == 0) {
    // throw new JpoOptimisticLockException(
    // "The bean of class [" + clazz + "] cannot be updated. Version in the DB
    // is not the expected one."); //$NON-NLS-1$
    // }
    // }
    //
    //
    // if (sqlExecutor.update(updateQuery, ArrayUtil.concat(notPksValues,
    // pkAndOriginalVersionValues)) == 0) {
    // throw new JpoOptimisticLockException(
    // "The bean of class [" + clazz + "] cannot be updated. Version in the DB
    // is not the expected one or the ID of the bean is associated with and
    // existing bean."); //$NON-NLS-1$
    // }
    // return updatedBean;
    // });
    //
    // }

    // @Override
    // public Stream<BEAN> executeWithBatchUpdate() {
    // executed = true;
    //
    // String updateQuery = getQuery();
    // String lockQuery = getLockQuery();
    // List<BEAN> updatedBeans = new ArrayList<>();
    //
    // Stream<Object[]> values = beans.map(bean -> {
    // Persistor<BEAN> persistor = getOrmClassTool().getPersistor();
    // BEAN updatedBean = persistor.clone(bean);
    // updatedBeans.add(updatedBean);
    // Object[] pkAndOriginalVersionValues =
    // persistor.getPropertyValues(pkAndVersionFieldNames, updatedBean);
    // persistor.increaseVersion(updatedBean, false);
    // Object[] notPksValues = persistor.getPropertyValues(notPksFieldNames,
    // updatedBean);
    //
    // //TODO this could be done in a single query
    // if (persistor.isVersionableWithLock()) {
    //
    // if (sqlExecutor.queryForIntUnique(lockQuery, pkAndOriginalVersionValues)
    // == 0) {
    // throw new JpoOptimisticLockException(
    // "The bean of class [" + clazz + "] cannot be updated. Version in the DB
    // is not the expected one."); //$NON-NLS-1$
    // }
    // }
    //
    // return ArrayUtil.concat(notPksValues, pkAndOriginalVersionValues);
    // });
    //
    // int[] result = sqlExecutor.batchUpdate(updateQuery, values);
    //
    // if (IntStream.of(result).sum() < updatedBeans.size()) {
    // throw new JpoOptimisticLockException(
    // "The bean of class [" + clazz + "] cannot be updated. Version in the DB
    // is not the expected one or the ID of the bean is not associated with and
    // existing bean."); //$NON-NLS-1$
    // }
    //
    // return updatedBeans.stream();
    // }

}
