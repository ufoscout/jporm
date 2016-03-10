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
import com.jporm.commons.core.query.save.SaveQueryBase;
import com.jporm.persistor.Persistor;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class SaveQueryImpl<BEAN> extends SaveQueryBase<BEAN> implements SaveQuery<BEAN> {

    private final BEAN bean;
    private final SqlExecutor sqlExecutor;
    private final ClassTool<BEAN> ormClassTool;

    public SaveQueryImpl(final BEAN bean, final Class<BEAN> clazz, final ClassTool<BEAN> ormClassTool, final SqlCache sqlCache, final SqlExecutor sqlExecutor,
            final SqlFactory sqlFactory, DBProfile dbProfile) {
        super(clazz, sqlCache);
        this.bean = bean;
        this.ormClassTool = ormClassTool;
        this.sqlExecutor = sqlExecutor;
    }


    @Override
    public CompletableFuture<BEAN> execute() {
        final Persistor<BEAN> persistor = ormClassTool.getPersistor();

        BEAN clonedBean = persistor.clone(bean);

        // CHECK IF OBJECT HAS A 'VERSION' FIELD and increase it
        persistor.increaseVersion(clonedBean, true);
        boolean useGenerator = persistor.useGenerators(clonedBean);
        String sql = getCacheableQuery(useGenerator);

        if (!useGenerator) {
            String[] keys = ormClassTool.getDescriptor().getAllColumnJavaNames();
            Object[] values = persistor.getPropertyValues(keys, clonedBean);
            return sqlExecutor.update(sql, values).thenApply(result -> clonedBean);
        } else {
            final GeneratedKeyReader<Void> generatedKeyExtractor = GeneratedKeyReader.get(ormClassTool.getDescriptor().getAllGeneratedColumnDBNames(),
                    (final ResultSet generatedKeyResultSet, Integer affectedRows) -> {
                        if (generatedKeyResultSet.next()) {
                            persistor.updateGeneratedValues(generatedKeyResultSet, clonedBean);
                        }
                        return null;
                    });
            String[] keys = ormClassTool.getDescriptor().getAllNotGeneratedColumnJavaNames();
            Object[] values = persistor.getPropertyValues(keys, clonedBean);
            return sqlExecutor.update(sql, values, generatedKeyExtractor).thenApply(result -> clonedBean);
        }
    }

}
