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
package com.jporm.commons.core.query.save;

import java.util.Map;

import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.insert.Insert;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class ASaveQuery<BEAN> {

    private final Class<BEAN> clazz;
    private final ClassTool<BEAN> ormClassTool;
    private final SqlFactory sqlFactory;
    private SqlCache sqlCache;

    public ASaveQuery(final ClassTool<BEAN> ormClassTool, final Class<BEAN> clazz, final SqlCache sqlCache, final SqlFactory sqlFactory) {
        this.ormClassTool = ormClassTool;
        this.sqlCache = sqlCache;
        this.sqlFactory = sqlFactory;
        this.clazz = clazz;
    }

    /**
     * @return the ormClassTool
     */
    public ClassTool<BEAN> getOrmClassTool() {
        return ormClassTool;
    }

    protected String getCacheableQuery(final DBProfile dbProfile, final boolean useGenerator) {

        Map<Class<?>, String> cache = null;
        if (useGenerator) {
            cache = sqlCache.saveWithGenerators();
        } else {
            cache = sqlCache.saveWithoutGenerators();
        }

        return cache.computeIfAbsent(clazz, key -> {
            String[] fields = getOrmClassTool().getDescriptor().getAllColumnJavaNames();
            Insert insert = sqlFactory.insertInto(clazz, fields);
            insert.useGenerators(useGenerator);

            // this is workaround to avoid the creation of a new array with the
            // same size of 'fields'
            // these values are not read but they are needed to properly render
            // the query
            insert.values(fields);
            return insert.sqlQuery(dbProfile);
        });

    }

}
