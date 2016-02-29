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
package com.jporm.rx.query.save;

import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.query.save.ASaveQuery;
import com.jporm.persistor.Persistor;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class SaveQueryImpl<BEAN> extends ASaveQuery<BEAN> implements SaveQuery<BEAN> {

    private final BEAN bean;
    private final SqlExecutor sqlExecutor;

    public SaveQueryImpl(final BEAN bean, final Class<BEAN> clazz, final ClassTool<BEAN> ormClassTool, final SqlCache sqlCache, final SqlExecutor sqlExecutor,
            final SqlFactory sqlFactory) {
        super(ormClassTool, clazz, sqlCache, sqlFactory);
        this.bean = bean;
        this.sqlExecutor = sqlExecutor;
    }


    @Override
    public CompletableFuture<BEAN> execute() {
        final Persistor<BEAN> persistor = getOrmClassTool().getPersistor();

        BEAN clonedBean = persistor.clone(bean);

        // CHECK IF OBJECT HAS A 'VERSION' FIELD and increase it
        persistor.increaseVersion(clonedBean, true);
        boolean useGenerator = persistor.useGenerators(clonedBean);
        String sql = getCacheableQuery(useGenerator);

        if (!useGenerator) {
            String[] keys = getOrmClassTool().getDescriptor().getAllColumnJavaNames();
            Object[] values = persistor.getPropertyValues(keys, clonedBean);
            return sqlExecutor.update(sql, values).thenApply(result -> clonedBean);
        } else {
            final GeneratedKeyReader generatedKeyExtractor = new GeneratedKeyReader() {
                @Override
                public String[] generatedColumnNames() {
                    return getOrmClassTool().getDescriptor().getAllGeneratedColumnDBNames();
                }

                @Override
                public void read(final ResultSet generatedKeyResultSet) {
                    if (generatedKeyResultSet.next()) {
                        persistor.updateGeneratedValues(generatedKeyResultSet, clonedBean);
                    }
                }
            };
            String[] keys = getOrmClassTool().getDescriptor().getAllNotGeneratedColumnJavaNames();
            Object[] values = persistor.getPropertyValues(keys, clonedBean);
            return sqlExecutor.update(sql, values, generatedKeyExtractor).thenApply(result -> clonedBean);
        }
    }

}
