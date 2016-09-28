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
package com.jporm.rx.query.update;

import com.jporm.commons.core.exception.JpoOptimisticLockException;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.persistor.Persistor;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.util.ArrayUtil;

import io.reactivex.Single;

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
public class UpdateQueryImpl<BEAN> implements UpdateQuery<BEAN> {

    // private final BEAN bean;
    private final BEAN bean;
    private final Class<BEAN> clazz;
    private final String[] pkAndVersionFieldNames;
    private final String[] notPksFieldNames;
    private final SqlExecutor sqlExecutor;
    private final ClassTool<BEAN> ormClassTool;
    private final SqlCache sqlCache;

    /**
     * @param newBean
     * @param serviceCatalog
     * @param ormSession
     */
    public UpdateQueryImpl(BEAN bean, final Class<BEAN> clazz, final ClassTool<BEAN> ormClassTool, final SqlCache sqlCache, final SqlExecutor sqlExecutor) {
        this.bean = bean;
        this.clazz = clazz;
        this.ormClassTool = ormClassTool;
        this.sqlCache = sqlCache;
        this.sqlExecutor = sqlExecutor;
        pkAndVersionFieldNames = ormClassTool.getDescriptor().getPrimaryKeyAndVersionColumnJavaNames();
        notPksFieldNames = ormClassTool.getDescriptor().getNotPrimaryKeyColumnJavaNames();
    }

    @Override
    public Single<BEAN> execute() {
        String updateQuery = sqlCache.update(clazz);
        Persistor<BEAN> persistor = ormClassTool.getPersistor();
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

        return sqlExecutor.update(updateQuery, ArrayUtil.concat(notPksValues, pkAndOriginalVersionValues))
                .map(updateResult -> {
                        if (updateResult.updated() == 0) {
                            throw new JpoOptimisticLockException("The bean of class [" + clazz //$NON-NLS-1$
                                    + "] cannot be updated. Version in the DB is not the expected one or the ID of the bean is associated with and existing bean.");
                        } else {
                            return updatedBean;
                        }
                    });

    }

}
