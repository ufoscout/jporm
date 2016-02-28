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
package com.jporm.rm.query.find;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.query.select.Select;
import com.jporm.sql.dsl.query.select.where.SelectWhere;

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
public class FindQueryImpl<BEAN> implements FindQuery<BEAN>, ExecutionEnvProvider<BEAN> {

    private Select<Class<?>> select;
    private final SqlCache sqlCache;
    private final Class<BEAN> clazz;
    private final SqlFactory sqlFactory;
    private final ClassTool<BEAN> ormClassTool;
    private final Object[] idValues;
	private final SqlExecutor sqlExecutor;

    public FindQueryImpl(Class<BEAN> clazz, Object[] pkFieldValues, final ClassTool<BEAN> ormClassTool, SqlExecutor sqlExecutor, SqlFactory sqlFactory,
            SqlCache sqlCache) {
        this.clazz = clazz;
        this.ormClassTool = ormClassTool;
		this.sqlExecutor = sqlExecutor;
        this.sqlFactory = sqlFactory;
        this.sqlCache = sqlCache;
        this.idValues = pkFieldValues;
    }

    private String sqlQueryFromCache() {
        Map<Class<?>, String> cache = sqlCache.find();
        return cache.computeIfAbsent(clazz, key -> {
            return getSelect().sqlQuery();
        });
    }

    @Override
    public String sqlRowCountQuery() {
        Map<Class<?>, String> cache = sqlCache.findRowCount();
        return cache.computeIfAbsent(clazz, key -> {
            return getSelect().sqlRowCountQuery();
        });
    }

    /**
     * @return the select
     */
    public Select<Class<?>> getSelect() {
        if (select == null) {

            ClassDescriptor<BEAN> descriptor = ormClassTool.getDescriptor();
            String[] fields = ormClassTool.getDescriptor().getAllColumnJavaNames();

            select = sqlFactory.select(fields).from(clazz);

            SelectWhere where = getSelect().where();
            String[] pks = descriptor.getPrimaryKeyColumnJavaNames();
            for (int i = 0; i < pks.length; i++) {
                where.eq(pks[i], idValues[i]);
            }
            getSelect().limit(1);
        }
        return select;
    }

	@Override
	public SqlExecutor getSqlExecutor() {
		return sqlExecutor;
	}

	@Override
	public ClassTool<BEAN> getOrmClassTool() {
		return ormClassTool;
	}

	@Override
	public ExecutionEnvProvider<BEAN> getExecutionEnvProvider() {
		return this;
	}

	@Override
	public void sqlValues(List<Object> values) {
		for(Object value : idValues) {
			values.add(value);
		}
	}

	@Override
	public void sqlQuery(StringBuilder queryBuilder) {
		queryBuilder.append(sqlQueryFromCache());
	}

    @Override
    public List<String> getIgnoredFields() {
        return Collections.EMPTY_LIST;
    }
}
