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
package com.jporm.rm.query.find.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.exception.JpoNotUniqueResultManyResultsException;
import com.jporm.commons.core.exception.JpoNotUniqueResultNoResultException;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.io.RowMapper;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.util.GenericWrapper;
import com.jporm.persistor.BeanFromResultSet;
import com.jporm.persistor.Persistor;
import com.jporm.rm.query.find.FindQueryExecutorProvider;
import com.jporm.rm.query.find.FindQueryExecutorProviderImpl;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.dialect.DBType;
import com.jporm.sql.dsl.query.select.Select;

/**
 *
 * @author Francesco Cina
 *
 *         20/giu/2011
 */
public class CustomFindQueryImpl<BEAN> extends FindQueryExecutorProviderImpl<BEAN> implements FindQueryExecutorProvider<BEAN> {

    private final Class<BEAN> clazz;
    private final SqlExecutor sqlExecutor;
    private final DBType dbType;
    private final Select<Class<?>> select;
    private final ClassTool<BEAN> ormClassTool;

    public CustomFindQueryImpl(final Class<BEAN> clazz, final String alias, final boolean distinct, final ClassTool<BEAN> ormClassTool, final SqlCache sqlCache, final SqlExecutor sqlExecutor, final SqlFactory sqlFactory,
            final DBType dbType) {
        super(sqlExecutor, ormClassTool);
        this.clazz = clazz;
        this.ormClassTool = ormClassTool;
        this.sqlExecutor = sqlExecutor;
        this.dbType = dbType;
        String[] fields = ormClassTool.getDescriptor().getAllColumnJavaNames();;
        select = sqlFactory.select(fields).distinct(distinct).from(clazz, alias);
        setFrom(new CommonFindFromImpl<>(select.from(), this));
        setWhere(new CustomFindQueryWhereImpl<>(select.where(), this));
        setOrderBy(new CustomFindQueryOrderByImpl<>(select.orderBy(), this));
    }

    @Override
    protected List<Object> getSqlValues() {
        return select.sqlValues();
    }

    @Override
    protected String getSqlQuery() {
        return select.sqlQuery();
    }

    @Override
    protected String getSqlRowCountQuery() {
        return select.sqlRowCountQuery();
    }

}
