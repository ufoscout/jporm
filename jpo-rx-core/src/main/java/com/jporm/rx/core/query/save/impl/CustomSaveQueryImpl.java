/**
 * *****************************************************************************
 * Copyright 2013 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *****************************************************************************
 */
package com.jporm.rx.core.query.save.impl;

import java.util.ArrayList;
import java.util.List;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.save.impl.CommonSaveQueryImpl;
import com.jporm.rx.core.connection.UpdateResult;
import com.jporm.rx.core.query.save.CustomSaveQuery;
import com.jporm.rx.core.query.save.CustomSaveQueryValues;
import com.jporm.rx.core.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class CustomSaveQueryImpl<BEAN> extends CommonSaveQueryImpl<CustomSaveQuery, CustomSaveQueryValues> implements CustomSaveQuery {

    private final SqlExecutor sqlExecutor;

    public CustomSaveQueryImpl(final Class<BEAN> clazz, final ServiceCatalog serviceCatalog, SqlExecutor sqlExecutor, SqlFactory sqlFactory) {
        super(clazz, sqlFactory);
        this.sqlExecutor = sqlExecutor;
        setElemValues(new CustomSaveQueryValuesImpl<>(query().values(), this));
    }

    @Override
    public CompletableFuture<UpdateResult> execute() {

        return sqlExecutor.dbType().thenCompose(dbType -> {
            final List<Object> values = new ArrayList<>();
            sql().appendValues(values);
            return sqlExecutor.update(sql().renderSql(dbType.getDBProfile()), values);
        });

    }
}
