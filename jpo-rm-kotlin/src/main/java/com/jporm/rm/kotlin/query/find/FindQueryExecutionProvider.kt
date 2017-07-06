/*******************************************************************************
 * Copyright 2016 Francesco Cina'

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
package com.jporm.rm.kotlin.query.find

import com.jporm.commons.core.exception.JpoException
import com.jporm.commons.core.exception.JpoNotUniqueResultException
import com.jporm.commons.core.exception.JpoNotUniqueResultManyResultsException
import com.jporm.commons.core.exception.JpoNotUniqueResultNoResultException
import com.jporm.commons.core.util.GenericWrapper
import com.jporm.sql.query.select.SelectCommon

import javax.swing.tree.RowMapper
import java.util.Optional

interface FindQueryExecutionProvider<BEAN> : SelectCommon {

    /**
     * Return whether at least one entries exists that matches the query. It is
     * equivalent to fetchRowCount()>0

     * @return
     */
    fun exist(): Boolean {
        return fetchRowCount() > 0
    }

    /**
     * Execute the query returning the list of beans.

     * @return
     */
    fun fetchAll(): List<BEAN> {
        return fetchAll<BEAN>({ newObject: BEAN, rowCount: Int -> newObject })
    }

    /**
     * Execute the query and for each bean returned the callback method of
     * [RowMapper] is called. No references to created Beans are hold by
     * the orm; in addition, one bean at time is created just before calling the
     * callback method. This method permits to handle big amount of data with a
     * minimum memory footprint.

     * @param orm
     * *
     * @throws JpoException
     */
    @Throws(JpoException::class)
    fun fetchAll(beanReader: (BEAN, Int) -> Unit) {
        val persistor = executionEnvProvider.ormClassTool.persistor
        val ignoredFields = executionEnvProvider.ignoredFields
        executionEnvProvider.sqlExecutor.query(sqlQuery(), sqlValues()) { entry, rowCount -> beanReader(persistor.beanFromResultSet(entry, ignoredFields), rowCount) }
    }

    /**
     * Execute the query and for each bean returned the callback method of
     * [RowMapper] is called. No references to created Beans are hold by
     * the orm; in addition, one bean at time is created just before calling the
     * callback method. This method permits to handle big amount of data with a
     * minimum memory footprint.

     * @param orm
     * *
     * @throws JpoException
     */
    @Throws(JpoException::class)
    fun <R> fetchAll(beanReader: (BEAN, Int) -> R): List<R> {
        val persistor = executionEnvProvider.ormClassTool.persistor
        val ignoredFields = executionEnvProvider.ignoredFields
        return executionEnvProvider.sqlExecutor.query<R>(sqlQuery(), sqlValues()) { resultEntry, rowCount -> beanReader(persistor.beanFromResultSet(resultEntry, ignoredFields), rowCount) }
    }

    /**
     * Fetch the bean

     * @return
     */
    @Throws(JpoException::class)
    fun fetchOne(): BEAN {
        return executionEnvProvider.sqlExecutor.query<BEAN>(sqlQuery(), sqlValues()) { resultSet ->
            if (resultSet.hasNext()) {
                val entry = resultSet.next()
                val persistor = executionEnvProvider.ormClassTool.persistor
                return@getExecutionEnvProvider ().getSqlExecutor().query persistor . beanFromResultSet entry, executionEnvProvider.getIgnoredFields())
            }
            null
        }
    }

    /**
     * Fetch the bean

     * @return
     */
    @Throws(JpoException::class)
    fun fetchOneOptional(): Optional<BEAN> {
        return Optional.ofNullable(fetchOne())
    }

    /**
     * Fetch the bean. An [JpoNotUniqueResultException] is thrown if the
     * result is not unique.

     * @return
     */
    @Throws(JpoNotUniqueResultException::class)
    fun fetchOneUnique(): BEAN {
        val wrapper = GenericWrapper<BEAN>(null)
        fetchAll({ newObject: BEAN, rowCount: Int ->
            if (rowCount > 0) {
                throw JpoNotUniqueResultManyResultsException(
                        "The query execution returned a number of rows different than one: more than one result found")
            }
            wrapper.setValue(newObject)
        })
        if (wrapper.value == null) {
            throw JpoNotUniqueResultNoResultException("The query execution returned a number of rows different than one: no results found")
        }
        return wrapper.value
    }

    /**
     * Return the count of entities this query should return.

     * @return
     */
    fun fetchRowCount(): Int {
        return executionEnvProvider.sqlExecutor.queryForIntUnique(sqlRowCountQuery(), sqlValues())!!
    }

    val executionEnvProvider: ExecutionEnvProvider<BEAN>

}
