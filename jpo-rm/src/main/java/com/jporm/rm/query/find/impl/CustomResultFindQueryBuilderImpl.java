/*
 * Copyright 2015 ufo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jporm.rm.query.find.impl;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.rm.query.find.CustomResultFindQuery;
import com.jporm.rm.query.find.CustomResultFindQueryBuilder;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.dialect.DBType;

/**
 *
 * @author ufo
 */
public class CustomResultFindQueryBuilderImpl implements CustomResultFindQueryBuilder {
    private final String[] selectFields;
    private final ServiceCatalog serviceCatalog;
    private final SqlExecutor sqlExecutor;
    private final SqlFactory sqlFactory;
    private final DBType dbType;

    /**
     *
     * @param selectFields
     * @param serviceCatalog
     * @param sqlExecutor
     * @param sqlFactory
     * @param dbType
     */
    public CustomResultFindQueryBuilderImpl(final String[] selectFields, final ServiceCatalog serviceCatalog, final SqlExecutor sqlExecutor,
            final SqlFactory sqlFactory, final DBType dbType) {
        this.selectFields = selectFields;
        this.serviceCatalog = serviceCatalog;
        this.sqlExecutor = sqlExecutor;
        this.sqlFactory = sqlFactory;
        this.dbType = dbType;

    }

    @Override
    public CustomResultFindQuery from(final Class<?> clazz, final String alias) {
        return new CustomResultFindQueryImpl(selectFields, serviceCatalog, sqlExecutor, clazz, alias, sqlFactory, dbType);
    }

}
