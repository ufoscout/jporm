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
package com.jporm.rm.kotlin.query.find

import com.jporm.commons.core.inject.ClassTool
import com.jporm.commons.core.query.SqlFactory
import com.jporm.commons.core.query.cache.SqlCache
import com.jporm.commons.core.query.find.FindQueryBase
import com.jporm.rm.kotlin.session.SqlExecutor

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
class FindQueryImpl<BEAN>(clazz: Class<BEAN>, pkFieldValues: Array<Any>, private val ormClassTool: ClassTool<BEAN>, private val sqlExecutor: SqlExecutor, sqlFactory: SqlFactory,
                          sqlCache: SqlCache) : FindQueryBase<BEAN>(clazz, pkFieldValues, sqlCache), FindQuery<BEAN>, ExecutionEnvProvider<BEAN> {

    override fun getSqlExecutor(): SqlExecutor {
        return sqlExecutor
    }

    override fun getOrmClassTool(): ClassTool<BEAN> {
        return ormClassTool
    }

    override val executionEnvProvider: ExecutionEnvProvider<BEAN>
        get() = this

}
