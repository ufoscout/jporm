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
package com.jporm.rx.query.update.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.update.impl.CommonUpdateQueryImpl;
import com.jporm.rx.query.update.CustomUpdateQuery;
import com.jporm.rx.query.update.CustomUpdateQueryWhere;
import com.jporm.rx.query.update.UpdateResult;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.query.update.Update;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class CustomUpdateQueryImpl extends CommonUpdateQueryImpl<CustomUpdateQuery, CustomUpdateQueryWhere> implements CustomUpdateQuery {

    private final SqlExecutor sqlExecutor;

    public CustomUpdateQueryImpl(final Class<?> clazz, final ServiceCatalog serviceCatalog, final SqlExecutor sqlExecutor, final SqlFactory sqlFactory) {
        super(clazz, sqlFactory);
        this.sqlExecutor = sqlExecutor;
        Update update = query();
        setWhere(new CustomUpdateQueryWhereImpl(update.where(), this));
    }

    @Override
    public CompletableFuture<UpdateResult> execute() {

        return sqlExecutor.dbType().thenCompose(dbType -> {
            final List<Object> values = new ArrayList<>();
            sql().sqlValues(values);
            return sqlExecutor.update(sql().sqlQuery(dbType.getDBProfile()), values);
        });

    }
}
