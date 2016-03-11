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
package com.jporm.rm.query.find;

import com.jporm.commons.core.query.SqlFactory;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.query.select.Select;

/**
 *
 * @author ufo
 */
public class CustomResultFindQueryBuilderImpl implements CustomResultFindQueryBuilder {
    private final String[] selectFields;
    private final SqlExecutor sqlExecutor;
    private final SqlFactory sqlFactory;

    /**
     *
     * @param selectFields
     * @param serviceCatalog
     * @param sqlExecutor
     * @param sqlFactory
     * @param dbType
     */
    public CustomResultFindQueryBuilderImpl(final String[] selectFields, final SqlExecutor sqlExecutor,
            final SqlFactory sqlFactory) {
        this.selectFields = selectFields;
        this.sqlExecutor = sqlExecutor;
        this.sqlFactory = sqlFactory;

    }

    @Override
    public <BEAN> CustomResultFindQuery<Class<?>> from(final Class<BEAN> clazz, final String alias) {
        Select<Class<?>> select = sqlFactory.select(()->selectFields).from(clazz, alias);
        return new CustomResultFindQueryImpl<>(select, sqlExecutor);
    }

}
