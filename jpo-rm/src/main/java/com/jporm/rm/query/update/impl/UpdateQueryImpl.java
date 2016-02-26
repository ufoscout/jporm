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
package com.jporm.rm.query.update.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.commons.core.exception.JpoOptimisticLockException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.strategy.QueryExecutionStrategy;
import com.jporm.commons.core.query.strategy.UpdateExecutionStrategy;
import com.jporm.commons.core.query.update.AUpdateQuery;
import com.jporm.commons.core.util.ArrayUtil;
import com.jporm.persistor.Persistor;
import com.jporm.rm.query.update.UpdateQuery;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.dialect.DBType;

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
public class UpdateQueryImpl<BEAN> extends AUpdateQuery<BEAN> implements UpdateQuery<BEAN>, UpdateExecutionStrategy<BEAN> {

    // private final BEAN bean;
    private final Collection<BEAN> beans;
    private final Class<BEAN> clazz;
    private final String[] pkAndVersionFieldNames;
    private final String[] notPksFieldNames;
    private final SqlExecutor sqlExecutor;
    private final DBType dbType;

    /**
     * @param newBean
     * @param serviceCatalog
     * @param ormSession
     */
    public UpdateQueryImpl(final Collection<BEAN> beans, final Class<BEAN> clazz, final ServiceCatalog serviceCatalog, final SqlExecutor sqlExecutor,
            final SqlFactory sqlFactory, final DBType dbType) {
        super(clazz, serviceCatalog.getClassToolMap().get(clazz), serviceCatalog.getSqlCache(), sqlFactory);
        this.beans = beans;
        this.clazz = clazz;
        this.sqlExecutor = sqlExecutor;
        this.dbType = dbType;
        ClassDescriptor<BEAN> descriptor = getOrmClassTool().getDescriptor();
        pkAndVersionFieldNames = descriptor.getPrimaryKeyAndVersionColumnJavaNames();
        notPksFieldNames = descriptor.getNotPrimaryKeyColumnJavaNames();
    }

    @Override
    public List<BEAN> execute() {
        return QueryExecutionStrategy.build(dbType.getDBProfile()).executeUpdate(this);
    }

    @Override
    public List<BEAN> executeWithBatchUpdate() {

        String updateQuery = getCacheableQuery(dbType.getDBProfile());
        List<BEAN> updatedBeans = new ArrayList<>();
        Collection<Object[]> values = new ArrayList<>();

        beans.forEach(bean -> {
            Persistor<BEAN> persistor = getOrmClassTool().getPersistor();
            BEAN updatedBean = persistor.clone(bean);
            updatedBeans.add(updatedBean);
            Object[] pkAndOriginalVersionValues = persistor.getPropertyValues(pkAndVersionFieldNames, updatedBean);
            persistor.increaseVersion(updatedBean, false);
            Object[] notPksValues = persistor.getPropertyValues(notPksFieldNames, updatedBean);

            values.add(ArrayUtil.concat(notPksValues, pkAndOriginalVersionValues));
        });

        int[] result = sqlExecutor.batchUpdate(updateQuery, values);

        if (IntStream.of(result).sum() < updatedBeans.size()) {
            throw new JpoOptimisticLockException("The bean of class [" + clazz //$NON-NLS-1$
                    + "] cannot be updated. Version in the DB is not the expected one or the ID of the bean is not associated with and existing bean.");
        }

        return updatedBeans;
    }

    @Override
    public List<BEAN> executeWithSimpleUpdate() {

        String updateQuery = getCacheableQuery(dbType.getDBProfile());

        List<BEAN> result = new ArrayList<>();

        // VERSION WITHOUT BATCH UPDATE
        beans.forEach(bean -> {
            Persistor<BEAN> persistor = getOrmClassTool().getPersistor();
            BEAN updatedBean = persistor.clone(bean);

            Object[] pkAndOriginalVersionValues = persistor.getPropertyValues(pkAndVersionFieldNames, updatedBean);
            persistor.increaseVersion(updatedBean, false);
            Object[] notPksValues = persistor.getPropertyValues(notPksFieldNames, updatedBean);

            if (sqlExecutor.update(updateQuery, ArrayUtil.concat(notPksValues, pkAndOriginalVersionValues)) == 0) {
                throw new JpoOptimisticLockException("The bean of class [" + clazz //$NON-NLS-1$
                        + "] cannot be updated. Version in the DB is not the expected one or the ID of the bean is associated with and existing bean.");
            }
            result.add(updatedBean);
        });

        return result;
    }

}
