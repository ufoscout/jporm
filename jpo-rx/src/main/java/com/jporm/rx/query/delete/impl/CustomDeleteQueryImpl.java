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
package com.jporm.rx.query.delete.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.delete.CommonDeleteQueryImpl;
import com.jporm.rx.query.delete.CustomDeleteQuery;
import com.jporm.rx.query.delete.CustomDeleteQueryWhere;
import com.jporm.rx.query.delete.DeleteResult;
import com.jporm.rx.session.SqlExecutor;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class CustomDeleteQueryImpl<BEAN> extends CommonDeleteQueryImpl<CustomDeleteQuery<BEAN>, CustomDeleteQueryWhere<BEAN>>
        implements CustomDeleteQuery<BEAN> {

    private final SqlExecutor sqlExecutor;

    public CustomDeleteQueryImpl(final Class<BEAN> clazz, final ServiceCatalog serviceCatalog, final SqlExecutor sqlExecutor, final SqlFactory sqlFactory) {
        super(clazz, sqlFactory);
        this.sqlExecutor = sqlExecutor;
        setWhere(new CustomDeleteQueryWhereImpl<>(getDelete().where(), this));
    }

    @Override
    public CompletableFuture<DeleteResult> execute() {

        return sqlExecutor.dbType().thenCompose(dbType -> {
            final List<Object> values = new ArrayList<>();
            sql().sqlValues(values);
            return sqlExecutor.update(sql().sqlQuery(dbType.getDBProfile()), values);
        }).thenApply(updatedResult -> new DeleteResultImpl(updatedResult.updated()));

    }

}
