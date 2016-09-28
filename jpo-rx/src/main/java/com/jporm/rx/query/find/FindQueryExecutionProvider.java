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
package com.jporm.rx.query.find;

import java.util.List;
import java.util.Optional;

import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.persistor.Persistor;
import com.jporm.sql.query.select.SelectCommon;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface FindQueryExecutionProvider<BEAN> extends SelectCommon {

    /**
     * Return whether a bean exists with the specified id(s)
     *
     * @return
     */
    default Single<Boolean> exist() {
        return fetchRowCount().map(count -> count > 0);
    }

    /**
     * Execute the query returning the list of beans.
     *
     * @return
     */
    default Observable<BEAN> fetchAll() {
        ExecutionEnvProvider<BEAN> env = getExecutionEnvProvider();
        final Persistor<BEAN> persistor = env.getOrmClassTool().getPersistor();
        List<String> ignoredFields = env.getIgnoredFields();
        return env.getSqlExecutor().query(sqlQuery(), sqlValues(), (rowEntry, count) -> {
            return persistor.beanFromResultSet(rowEntry, ignoredFields).getBean();
        });
    }

    /**
     * Fetch the bean
     *
     * @return
     */
    default Single<Optional<BEAN>> fetchOneOptional() {
        ExecutionEnvProvider<BEAN> env = getExecutionEnvProvider();
        return env.getSqlExecutor().queryForOptional(sqlQuery(), sqlValues(), (rowEntry, count) -> {
            List<String> ignoredFields = env.getIgnoredFields();
            final Persistor<BEAN> persistor = env.getOrmClassTool().getPersistor();
            return persistor.beanFromResultSet(rowEntry, ignoredFields).getBean();
        });
    }

    /**
     * Return the count of entities this query should return.
     *
     * @return
     */
    default Single<Integer> fetchRowCount() {
        return getExecutionEnvProvider().getSqlExecutor().queryForIntUnique(sqlRowCountQuery(), sqlValues());
    }

    /**
     * Fetch the bean. An {@link JpoNotUniqueResultException} is thrown if the
     * result is not unique.
     *
     * @return
     */
    default Single<BEAN> fetchOneUnique() {
        ExecutionEnvProvider<BEAN> env = getExecutionEnvProvider();
        return env.getSqlExecutor().queryForUnique(sqlQuery(), sqlValues(), (rowEntry, count) -> {
            List<String> ignoredFields = env.getIgnoredFields();
            final Persistor<BEAN> persistor = env.getOrmClassTool().getPersistor();
            return persistor.beanFromResultSet(rowEntry, ignoredFields).getBean();
        });
    }

    ExecutionEnvProvider<BEAN> getExecutionEnvProvider();

}
