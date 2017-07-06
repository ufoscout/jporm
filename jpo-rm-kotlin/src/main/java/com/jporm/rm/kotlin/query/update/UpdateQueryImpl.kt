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
/*
 * ---------------------------------------------------------------------------- PROJECT : JPOrm CREATED BY : Francesco
 * Cina' ON : Feb 23, 2013 ----------------------------------------------------------------------------
 */
package com.jporm.rm.kotlin.query.update

import com.jporm.commons.core.exception.JpoOptimisticLockException
import com.jporm.commons.core.inject.ClassTool
import com.jporm.commons.core.query.cache.SqlCache
import com.jporm.commons.core.query.strategy.QueryExecutionStrategy
import com.jporm.commons.core.query.strategy.UpdateExecutionStrategy
import com.jporm.persistor.generator.Persistor
import com.jporm.rm.kotlin.session.SqlExecutor
import com.jporm.sql.dialect.DBProfile
import com.jporm.types.io.BatchPreparedStatementSetter
import com.jporm.types.io.Statement

import java.util.ArrayList
import java.util.stream.IntStream

/**
 * <class_description>
 *
 *
 * **notes**:
 *
 *
 * ON : Feb 23, 2013

 * @author Francesco Cina'
 * *
 * @version $Revision
</class_description> */
class UpdateQueryImpl<BEAN>
/**
 * @param newBean
 * *
 * @param serviceCatalog
 * *
 * @param ormSession
 */
(// private final BEAN bean;
        private val beans: List<BEAN>, private val clazz: Class<BEAN>, private val ormClassTool: ClassTool<BEAN>, private val sqlCache: SqlCache, private val sqlExecutor: SqlExecutor,
        private val dbType: DBProfile) : UpdateQuery<BEAN>, UpdateExecutionStrategy<BEAN> {
    private val pkAndVersionFieldNames: Array<String>
    private val notPksFieldNames: Array<String>

    init {
        pkAndVersionFieldNames = ormClassTool.descriptor.primaryKeyAndVersionColumnJavaNames
        notPksFieldNames = ormClassTool.descriptor.notPrimaryKeyColumnJavaNames
    }

    override fun execute(): List<BEAN> {
        return QueryExecutionStrategy.build(dbType).executeUpdate(this)
    }

    override fun executeWithBatchUpdate(): List<BEAN> {

        val updateQuery = sqlCache.update(clazz)
        val updatedBeans = ArrayList<BEAN>()
        val persistor = ormClassTool.persistor

        val result = sqlExecutor.batchUpdate(updateQuery, object : BatchPreparedStatementSetter {

            override fun set(ps: Statement, i: Int) {
                val bean = beans[i]
                val updatedBean = persistor.increaseVersion(persistor.clone(bean), false)
                updatedBeans.add(updatedBean)
                persistor.setBeanValuesToStatement(notPksFieldNames, updatedBean, ps, 0)
                persistor.setBeanValuesToStatement(pkAndVersionFieldNames, bean, ps, notPksFieldNames.size)
            }

            override fun getBatchSize(): Int {
                return beans.size
            }
        })

        if (IntStream.of(*result).sum() < updatedBeans.size) {
            throw JpoOptimisticLockException("The bean of class [" + clazz //$NON-NLS-1$

                    + "] cannot be updated. Version in the DB is not the expected one or the ID of the bean is not associated with and existing bean.")
        }

        return updatedBeans
    }

    override fun executeWithSimpleUpdate(): List<BEAN> {

        val updateQuery = sqlCache.update(clazz)
        val result = ArrayList<BEAN>()
        val persistor = ormClassTool.persistor

        // VERSION WITHOUT BATCH UPDATE
        beans.forEach { bean ->

            val updatedBean = persistor.increaseVersion(persistor.clone(bean), false)

            if (sqlExecutor.update(updateQuery) { statement ->
                persistor.setBeanValuesToStatement(notPksFieldNames, updatedBean, statement, 0)
                persistor.setBeanValuesToStatement(pkAndVersionFieldNames, bean, statement, notPksFieldNames.size)
            } == 0) {
                throw JpoOptimisticLockException("The bean of class [" + clazz
                        + "] cannot be updated. Version in the DB is not the expected one or the ID of the bean is associated with and existing bean.")
            }
            result.add(updatedBean)
        }

        return result
    }

}
