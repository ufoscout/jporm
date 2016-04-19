/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rx.reactor.session;

import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rx.reactor.query.delete.CustomDeleteQuery;
import com.jporm.rx.reactor.query.find.CustomFindQuery;
import com.jporm.rx.reactor.query.find.CustomResultFindQueryBuilder;
import com.jporm.rx.reactor.query.find.FindQuery;
import com.jporm.rx.reactor.query.save.CustomSaveQuery;
import com.jporm.rx.reactor.query.update.CustomUpdateQuery;
import com.jporm.rx.reactor.query.delete.DeleteResult;
import com.jporm.rx.reactor.session.SqlSession;

public interface Session {

    /**
     * Delete one bean from the database
     *
     * @param bean
     * @param cascade
     * @return
     */
    <BEAN> CompletableFuture<DeleteResult> delete(BEAN bean) throws JpoException;

    /**
     * Delete entries from a specific table
     *
     * @param clazz
     *            the TABLE related Class
     * @return
     */
    <BEAN> CustomDeleteQuery delete(Class<BEAN> clazz) throws JpoException;

    // /**
    // * Delete the beans from the database
    // * @param <BEAN>
    // * @param beans the beans to delete
    // * @throws JpoException
    // * @return
    // */
    // <BEAN> CompletableFuture<DeleteResult> delete(Collection<BEAN> beans)
    // throws JpoException;

    /**
     * Create a new query to find bean
     *
     * @param <BEAN>
     * @param clazz
     *            The class of the bean that will be retrieved by the query
     *            execution. The simple class name will be used as alias for the
     *            class
     * @return
     * @throws JpoException
     */
    <BEAN> CustomFindQuery<BEAN> find(Class<BEAN> clazz) throws JpoException;

    /**
     * Create a new query to find bean
     *
     * @param <BEAN>
     * @param clazz
     *            The class of the bean that will be retrieved by the query
     *            execution.
     * @param alias
     *            The alias of the class in the query.
     * @return
     * @throws JpoException
     */
    <BEAN> CustomFindQuery<BEAN> find(Class<BEAN> clazz, String alias) throws JpoException;

    /**
     * Create a new custom query that permits to specify a custom select clause.
     *
     * @param <BEAN>
     * @param selectFields
     * @return
     */
    <BEAN> CustomResultFindQueryBuilder find(String... selectFields);

    /**
     * Find a bean using its ID.
     *
     * @param <BEAN>
     * @param clazz
     *            The Class of the bean to load
     * @param idValue
     *            the value of the identifying column of the bean
     * @return
     */
    <BEAN> FindQuery<BEAN> findById(Class<BEAN> clazz, Object idValue);

    /**
     * Find a bean using another bean as model. The model class and id(s) will
     * be used to build the find query.
     *
     * @param <BEAN>
     * @param bean
     * @return
     *
     */
    <BEAN> FindQuery<BEAN> findByModelId(BEAN model);

    /**
     * Persist the new bean in the database
     *
     * @param <BEAN>
     * @param bean
     * @throws JpoException
     * @return
     */
    <BEAN> CompletableFuture<BEAN> save(BEAN bean);

    /**
     * Permits to define a custom insert query
     *
     * @param clazz
     *            the TABLE related Class
     * @throws JpoException
     */
    <BEAN> CustomSaveQuery save(Class<BEAN> clazz, String... fields) throws JpoException;

    // /**
    // * Persist the new beans in the database
    // * @param beans the beans to persist
    // * @param cascade whether to persist the children recursively
    // * @return
    // * @throws JpoException
    // */
    // <BEAN> List<BEAN> save(Collection<BEAN> beans) throws JpoException;

    /**
     * Updates the bean if it exists, otherwise it saves it
     *
     * @param bean
     *            the bean to be persisted
     * @return
     * @throws JpoException
     */
    <BEAN> CompletableFuture<BEAN> saveOrUpdate(BEAN bean);

    /**
     * An executor to perform any kind of plain SQL statements.
     *
     * @return
     */
    SqlSession sql();

    // /**
    // * Update the values of the existing beans in the database
    // * @param <BEAN>
    // * @param beans the beans to update
    // * @throws JpoException
    // * @return
    // */
    // <BEAN> List<BEAN> update(Collection<BEAN> beans) throws JpoException;

    /**
     * @param aggregatedUser
     * @return
     */
    <BEAN> CompletableFuture<BEAN> update(BEAN bean) throws JpoException;

    /**
     * Update the entries of a specific TABLE
     *
     * @param clazz
     *            the TABLE related Class
     * @throws JpoException
     */
    <BEAN> CustomUpdateQuery update(Class<BEAN> clazz) throws JpoException;

}
