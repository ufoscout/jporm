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
package com.jporm.rm.kotlin.query.delete

import com.jporm.commons.core.inject.ClassTool
import com.jporm.commons.core.query.cache.SqlCache
import com.jporm.commons.core.query.strategy.DeleteExecutionStrategy
import com.jporm.commons.core.query.strategy.QueryExecutionStrategy
import com.jporm.persistor.generator.Persistor
import com.jporm.rm.kotlin.session.SqlExecutor
import com.jporm.sql.dialect.DBProfile
import com.jporm.types.io.BatchPreparedStatementSetter
import com.jporm.types.io.Statement
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
class DeleteQueryImpl<BEAN>
/**
 * @param newBean
 * *
 * @param serviceCatalog
 * *
 * @param ormSession
 */
(// private final BEAN bean;
        private val beans: List<BEAN>, private val clazz: Class<BEAN>, private val ormClassTool: ClassTool<BEAN>, private val sqlCache: SqlCache, private val sqlExecutor: SqlExecutor,
        private val dbType: DBProfile) : DeleteQuery, DeleteExecutionStrategy {

    override fun execute(): Int {
        return QueryExecutionStrategy.build(dbType).executeDelete(this)
    }

    override fun executeWithBatchUpdate(): Int {
        val query = sqlCache.delete(clazz)
        val pks = ormClassTool.descriptor.primaryKeyColumnJavaNames

        // WITH BATCH UPDATE VERSION:
        val persistor = ormClassTool.persistor
        val result = sqlExecutor.batchUpdate(query, object : BatchPreparedStatementSetter {

            override fun set(ps: Statement, i: Int) {
                persistor.setBeanValuesToStatement(pks, beans[i], ps, 0)
            }

            override fun getBatchSize(): Int {
                return beans.size
            }
        })
        return IntStream.of(*result).sum()
    }

    override fun executeWithSimpleUpdate(): Int {
        val query = sqlCache.delete(clazz)
        val pks = ormClassTool.descriptor.primaryKeyColumnJavaNames
        val persistor = ormClassTool.persistor

        // WITHOUT BATCH UPDATE VERSION:
        var result = 0
        for (bean in beans) {
            result += sqlExecutor.update(query) { statement: Statement -> persistor.setBeanValuesToStatement(pks, bean, statement, 0) }
        }
        return result
    }

}
