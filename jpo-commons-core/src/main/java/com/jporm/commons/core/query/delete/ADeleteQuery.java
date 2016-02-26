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
package com.jporm.commons.core.query.delete;

import java.util.Map;

import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.query.clause.Delete;
import com.jporm.sql.query.clause.Where;

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
public class ADeleteQuery<BEAN> {

    // private final BEAN bean;
    private final Class<BEAN> clazz;
    private final ClassTool<BEAN> ormClassTool;
    private final SqlCache sqlCache;
    private SqlFactory sqlFactory;

    /**
     * @param newBean
     * @param serviceCatalog
     * @param ormSession
     */
    public ADeleteQuery(final Class<BEAN> clazz, final ClassTool<BEAN> ormClassTool, final SqlCache sqlCache, final SqlFactory sqlFactory) {
        this.clazz = clazz;
        this.sqlCache = sqlCache;
        this.sqlFactory = sqlFactory;
        this.ormClassTool = ormClassTool;
    }

    /**
     * @return the ormClassTool
     */
    public ClassTool<BEAN> getOrmClassTool() {
        return ormClassTool;
    }

    protected String getCacheableQuery(final DBProfile dbProfile) {
        Map<Class<?>, String> cache = sqlCache.delete();

        return cache.computeIfAbsent(clazz, key -> {
            Delete delete = sqlFactory.delete(clazz);
            Where where = delete.where();
            String[] pks = getOrmClassTool().getDescriptor().getPrimaryKeyColumnJavaNames();
            for (String pk : pks) {
                where.eq(pk, "");
            };
            return delete.renderSql(dbProfile);
        });

    }

}
