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
package com.jporm.rm.query.find.impl;

import com.jporm.cache.Cache;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.rm.query.find.FindQuery;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
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
public class FindQueryImpl<BEAN> extends CustomFindQueryImpl<BEAN> implements FindQuery<BEAN> {

    public FindQueryImpl(ServiceCatalog serviceCatalog, Class<BEAN> clazz, String alias, SqlExecutor sqlExecutor, SqlFactory sqlFactory, DBType dbType) {
        super(serviceCatalog, clazz, alias, sqlExecutor, sqlFactory, dbType);
    }

    @Override
    public String renderSql() {
        Cache<Class<?>, String> cache = getServiceCatalog().getSqlCache().find();
        return cache.get(getBeanClass(), key -> {
            return super.renderSql();
        });
    }

    @Override
    protected String renderRowCountSql() {
        Cache<Class<?>, String> cache = getServiceCatalog().getSqlCache().findRowCount();
        return cache.get(getBeanClass(), key -> {
            return super.renderRowCountSql();
        });
    }

}
