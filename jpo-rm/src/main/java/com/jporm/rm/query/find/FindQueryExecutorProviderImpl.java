/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.rm.query.find;

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
import com.jporm.commons.core.util.GenericWrapper;
import com.jporm.persistor.BeanFromResultSet;
import com.jporm.persistor.Persistor;
import com.jporm.rm.session.SqlExecutor;

public abstract class FindQueryExecutorProviderImpl<BEAN> implements FindQueryExecutorProvider<BEAN> {

    private final SqlExecutor sqlExecutor;
    private final ClassTool<BEAN> ormClassTool;

    public FindQueryExecutorProviderImpl(SqlExecutor sqlExecutor, ClassTool<BEAN> ormClassTool) {
        this.sqlExecutor = sqlExecutor;
        this.ormClassTool = ormClassTool;
    }

    @Override
    public final BEAN fetch() throws JpoException {
        return sqlExecutor.query(getSqlQuery(), getSqlValues(), resultSet -> {
            if (resultSet.next()) {
                final Persistor<BEAN> persistor = ormClassTool.getPersistor();
                BeanFromResultSet<BEAN> beanFromRS = persistor.beanFromResultSet(resultSet, getIgnoredFields());
                return beanFromRS.getBean();
            }
            return null;
        });
    }

    protected abstract List<Object> getSqlValues();

    protected abstract String getSqlQuery();

    protected abstract String getSqlRowCountQuery();

    @Override
     public final void fetch(final RowMapper<BEAN> srr) throws JpoException {
         sqlExecutor.query(getSqlQuery(), getSqlValues(), resultSet -> {
             int rowCount = 0;
             final Persistor<BEAN> persistor = ormClassTool.getPersistor();
             while (resultSet.next()) {
                 BeanFromResultSet<BEAN> beanFromRS = persistor.beanFromResultSet(resultSet, getIgnoredFields());
                 srr.read(beanFromRS.getBean(), rowCount);
                 rowCount++;
             }
             return null;
         });
     }

    private List<String> getIgnoredFields() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public final List<BEAN> fetchList() {
        final List<BEAN> results = new ArrayList<>();
        fetch((final BEAN newObject, final int rowCount) -> {
            results.add(newObject);
        });
        return results;
    }

    @Override
    public final Optional<BEAN> fetchOptional() throws JpoException {
        return Optional.ofNullable(fetch());
    }

    @Override
    public final int fetchRowCount() {
        return sqlExecutor.queryForIntUnique(getSqlRowCountQuery(), getSqlValues());
    }


    @Override
    public final BEAN fetchUnique() throws JpoNotUniqueResultException {
        final GenericWrapper<BEAN> wrapper = new GenericWrapper<>(null);
        fetch((final BEAN newObject, final int rowCount) -> {
            if (rowCount > 0) {
                throw new JpoNotUniqueResultManyResultsException(
                        "The query execution returned a number of rows different than one: more than one result found");
            }
            wrapper.setValue(newObject);
        });
        if (wrapper.getValue() == null) {
            throw new JpoNotUniqueResultNoResultException("The query execution returned a number of rows different than one: no results found");
        }
        return wrapper.getValue();
    }

    @Override
    public final boolean exist() {
        return fetchRowCount() > 0;
    }

}
