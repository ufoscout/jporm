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
package com.jporm.rm.kotlin.query.delete

import com.jporm.rm.kotlin.session.SqlExecutor
import com.jporm.sql.query.delete.Delete
import com.jporm.sql.query.where.Where
import com.jporm.sql.query.where.WhereDefault

/**

 * @author Francesco Cina
 * *
 * *         10/lug/2011
 */
class CustomDeleteQueryImpl(private val sqlDelete: Delete, private val sqlExecutor: SqlExecutor) : CustomDeleteQuery, CustomDeleteQueryWhere, WhereDefault<CustomDeleteQueryWhere> {

    override fun execute(): Int {
        val values = sqlDelete.sqlValues()
        return sqlExecutor.update(sqlQuery(), values)
    }

    override fun where(): CustomDeleteQueryWhere {
        return this
    }

    override fun sqlValues(values: List<Any>) {
        sqlDelete.sqlValues(values)
    }

    override fun sqlQuery(queryBuilder: StringBuilder) {
        sqlDelete.sqlQuery(queryBuilder)
    }

    override fun whereImplementation(): Where<*> {
        return sqlDelete.where()
    }

}

