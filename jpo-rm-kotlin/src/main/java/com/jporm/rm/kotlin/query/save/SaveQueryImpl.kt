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
package com.jporm.rm.kotlin.query.save

import com.jporm.commons.core.inject.ClassTool
import com.jporm.commons.core.query.SqlFactory
import com.jporm.commons.core.query.cache.SqlCache
import com.jporm.commons.core.query.save.SaveQueryBase
import com.jporm.persistor.generator.Persistor
import com.jporm.rm.kotlin.session.SqlExecutor
import com.jporm.sql.dialect.DBProfile
import com.jporm.types.io.GeneratedKeyReader
import com.jporm.types.io.ResultSet

import java.util.ArrayList

/**

 * @author Francesco Cina
 * *
 * *         10/lug/2011
 */
class SaveQueryImpl<BEAN>(private val beans: Collection<BEAN>, clazz: Class<BEAN>, private val ormClassTool: ClassTool<BEAN>, sqlCache: SqlCache, private val sqlExecutor: SqlExecutor,
                          sqlFactory: SqlFactory, dbProfile: DBProfile) : SaveQueryBase<BEAN>(clazz, sqlCache), SaveQuery<BEAN> {

    override fun execute(): List<BEAN> {
        val result = ArrayList<BEAN>()
        for (bean in beans) {
            result.add(save(ormClassTool.persistor.clone(bean)))
        }
        return result
    }

    private fun save(bean: BEAN): BEAN {

        val persistor = ormClassTool.persistor

        // CHECK IF OBJECT HAS A 'VERSION' FIELD and increase it
        val updatedBean = persistor.increaseVersion(bean, true)
        val useGenerator = ormClassTool.persistor.useGenerators(updatedBean)
        val sql = getCacheableQuery(useGenerator)
        if (!useGenerator) {
            val keys = ormClassTool.descriptor.allColumnJavaNames
            sqlExecutor.update(sql) { statement -> persistor.setBeanValuesToStatement(keys, updatedBean, statement, 0) }
            return updatedBean
        } else {
            val generatedKeyExtractor = GeneratedKeyReader.get(ormClassTool.descriptor.allGeneratedColumnDBNames
            ) { generatedKeyResultSet: ResultSet, affectedRows: Int ->
                var result = updatedBean
                if (generatedKeyResultSet.hasNext()) {
                    result = persistor.updateGeneratedValues(generatedKeyResultSet.next(), result)
                }
                result
            }
            val keys = ormClassTool.descriptor.allNotGeneratedColumnJavaNames
            return sqlExecutor.update(sql, generatedKeyExtractor) { statement -> persistor.setBeanValuesToStatement(keys, updatedBean, statement, 0) }
        }

    }

}
