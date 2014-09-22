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
package com.jporm.query.update;

import com.jporm.mapper.ServiceCatalog;
import com.jporm.query.crud.executor.SaveOrUpdateType;

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
public class UpdateQueryOrm<BEAN> implements UpdateQuery<BEAN> {

    private final BEAN bean;
    private int _queryTimeout;
    private final Class<BEAN> clazz;
    private SaveOrUpdateType _saveOrUpdateType = SaveOrUpdateType.UPDATE;
    private final ServiceCatalog serviceCatalog;

    /**
     * @param newBean
     * @param serviceCatalog
     * @param ormSession
     */
    public UpdateQueryOrm(final BEAN bean, final ServiceCatalog serviceCatalog) {
        this.bean = bean;
        this.serviceCatalog = serviceCatalog;
        this.clazz = (Class<BEAN>) bean.getClass();
    }

    @Override
    public BEAN now() {
        return serviceCatalog.getOrmQueryExecutor().saveOrUpdate().update(bean, clazz, _saveOrUpdateType, _queryTimeout);
    }

    @Override
    public UpdateQuery<BEAN> queryTimeout(final int queryTimeout) {
        this._queryTimeout = queryTimeout;
        return this;
    }

    @Override
    public int getQueryTimeout() {
        return _queryTimeout;
    }

    @Override
    public UpdateQuery<BEAN> saveOrUpdate(final SaveOrUpdateType saveOrUpdateType) {
        _saveOrUpdateType = saveOrUpdateType;
        return this;
    }

}
