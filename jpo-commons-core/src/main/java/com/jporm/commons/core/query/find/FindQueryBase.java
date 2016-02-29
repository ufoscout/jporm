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
package com.jporm.commons.core.query.find;

import java.util.Collections;
import java.util.List;

import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.sql.query.select.SelectCommon;

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
public class FindQueryBase<BEAN> implements SelectCommon {

    private final SqlCache sqlCache;
    private final Class<BEAN> clazz;
    private final Object[] idValues;

    public FindQueryBase(Class<BEAN> clazz, Object[] pkFieldValues, SqlCache sqlCache) {
        this.clazz = clazz;
        this.sqlCache = sqlCache;
        this.idValues = pkFieldValues;
    }

    private String sqlQueryFromCache() {
        return sqlCache.find(clazz);
    }

    @Override
    public final String sqlRowCountQuery() {
        return sqlCache.findRowCount(clazz);
    }

    @Override
    public final void sqlValues(List<Object> values) {
        for (Object value : idValues) {
            values.add(value);
        }
    }

    @Override
    public final void sqlQuery(StringBuilder queryBuilder) {
        queryBuilder.append(sqlQueryFromCache());
    }

    public final List<String> getIgnoredFields() {
        return Collections.EMPTY_LIST;
    }

}
