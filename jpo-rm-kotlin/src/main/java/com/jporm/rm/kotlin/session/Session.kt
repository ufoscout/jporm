/*******************************************************************************
 * Copyright 2013 Francesco Cina'

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jporm.rm.kotlin.session

import com.jporm.commons.core.exception.JpoException
import com.jporm.rm.kotlin.query.delete.CustomDeleteQuery
import com.jporm.rm.kotlin.query.find.CustomFindQuery
import com.jporm.rm.kotlin.query.find.CustomResultFindQueryBuilder
import com.jporm.rm.kotlin.query.find.FindQuery
import com.jporm.rm.kotlin.query.save.CustomSaveQuery
import com.jporm.rm.kotlin.query.update.CustomUpdateQuery

/**

 * @author Francesco Cina
 * *
 * *         21/mag/2011
 */
interface Session {

    /**
     * Delete one bean from the database

     * @param bean
     * *
     * @param cascade
     * *
     * @return
     */
    @Throws(JpoException::class)
    fun <BEAN> delete(bean: BEAN): Int

    /**
     * Delete entries from a specific table

     * @param clazz
     * *            the TABLE related Class
     * *
     * @throws JpoException
     */
    @Throws(JpoException::class)
    fun <BEAN> delete(clazz: Class<BEAN>): CustomDeleteQuery

    /**
     * Delete the beans from the database

     * @param <BEAN>
     * *
     * @param beans
     * *            the beans to delete
     * *
     * @throws JpoException
     * *
     * @return
    </BEAN> */
    @Throws(JpoException::class)
    fun <BEAN> delete(beans: Collection<BEAN>): Int

    /**
     * Create a new query to find bean

     * @param <BEAN>
     * *
     * @param clazz
     * *            The class of the bean that will be retrieved by the query
     * *            execution. The simple class name will be used as alias for the
     * *            class
     * *
     * @return
     * *
     * @throws JpoException
    </BEAN> */
    @Throws(JpoException::class)
    fun <BEAN> find(clazz: Class<BEAN>): CustomFindQuery<BEAN>

    /**
     * Create a new query to find bean

     * @param <BEAN>
     * *
     * @param clazz
     * *            The class of the bean that will be retrieved by the query
     * *            execution.
     * *
     * @param alias
     * *            The alias of the class in the query.
     * *
     * @return
     * *
     * @throws JpoException
    </BEAN> */
    @Throws(JpoException::class)
    fun <BEAN> find(clazz: Class<BEAN>, alias: String): CustomFindQuery<BEAN>

    /**
     * Create a new custom query that permits to specify a custom select clause.

     * @param <BEAN>
     * *
     * @param selectFields
     * *
     * @return
    </BEAN> */
    fun <BEAN> find(vararg selectFields: String): CustomResultFindQueryBuilder

    /**
     * Find a bean using its ID.

     * @param <BEAN>
     * *
     * @param clazz
     * *            The Class of the bean to load
     * *
     * @param idValue
     * *            the value of the identifying column of the bean
     * *
     * @return
    </BEAN> */
    fun <BEAN> findById(clazz: Class<BEAN>, idValue: Any): FindQuery<BEAN>

    /**
     * Find a bean using another bean as model. The model class and id(s) will
     * be used to build the find query.

     * @param <BEAN>
     * *
     * @param bean
     * *
     * @return
    </BEAN> */
    fun <BEAN> findByModelId(model: BEAN): FindQuery<BEAN>

    /**
     * Persist the new bean in the database

     * @param <BEAN>
     * *
     * @param bean
     * *
     * @throws JpoException
     * *
     * @return
    </BEAN> */
    fun <BEAN> save(bean: BEAN): BEAN

    /**
     * Permits to define a custom insert query

     * @param clazz
     * *            the TABLE related Class
     * *
     * @throws JpoException
     */
    @Throws(JpoException::class)
    fun <BEAN> save(clazz: Class<BEAN>, vararg fields: String): CustomSaveQuery

    /**
     * Persist the new beans in the database

     * @param beans
     * *            the beans to persist
     * *
     * @param cascade
     * *            whether to persist the children recursively
     * *
     * @return
     * *
     * @throws JpoException
     */
    @Throws(JpoException::class)
    fun <BEAN> save(beans: Collection<BEAN>): List<BEAN>

    /**
     * For each bean in the list, update the bean if it exists, otherwise saves
     * it

     * @param bean
     * *            the bean to persist
     * *
     * @return
     * *
     * @throws JpoException
     */
    @Throws(JpoException::class)
    fun <BEAN> saveOrUpdate(bean: BEAN): BEAN

    /**
     * For each bean in the list, update the bean if it exists, otherwise saves
     * it

     * @param beans
     * *            the beans to persist
     * *
     * @param cascade
     * *            whether to saveOrUpdate the children recursively
     * *
     * @return
     * *
     * @throws JpoException
     */
    @Throws(JpoException::class)
    fun <BEAN> saveOrUpdate(beans: Collection<BEAN>): List<BEAN>

    /**
     * A script executor useful to execute multiple sql statement from files.

     * @return
     * *
     * @throws JpoException
     */
    @Throws(JpoException::class)
    fun scriptExecutor(): ScriptExecutor

    /**
     * An executor to perform any kind of plain SQL statements.

     * @return
     */
    fun sql(): SqlSession

    /**
     * @param aggregatedUser
     * *
     * @return
     */
    @Throws(JpoException::class)
    fun <BEAN> update(bean: BEAN): BEAN

    /**
     * Update the entries of a specific TABLE

     * @param clazz
     * *            the TABLE related Class
     * *
     * @throws JpoException
     */
    @Throws(JpoException::class)
    fun <BEAN> update(clazz: Class<BEAN>): CustomUpdateQuery

    /**
     * Update the values of the existing beans in the database

     * @param <BEAN>
     * *
     * @param beans
     * *            the beans to update
     * *
     * @throws JpoException
     * *
     * @return
    </BEAN> */
    @Throws(JpoException::class)
    fun <BEAN> update(beans: Collection<BEAN>): List<BEAN>

}
