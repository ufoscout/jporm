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
package com.jporm.rm.kotlin.query.update

import com.jporm.rm.kotlin.session.SqlExecutor
import com.jporm.sql.query.update.Update
import com.jporm.sql.query.update.set.CaseWhen
import com.jporm.sql.query.where.Where
import com.jporm.sql.query.where.WhereDefault

/**

 * @author Francesco Cina
 * *
 * *         10/lug/2011
 */
class CustomUpdateQueryImpl(private val update: Update, private val sqlExecutor: SqlExecutor) : CustomUpdateQuery, CustomUpdateQueryWhere, WhereDefault<CustomUpdateQueryWhere> {

    override fun execute(): Int {
        val values = update.sqlValues()
        return sqlExecutor.update(sqlQuery(), values)
    }

    override fun where(): CustomUpdateQueryWhere {
        return this
    }

    override fun whereImplementation(): Where<*> {
        return update.where()
    }

    override fun sqlValues(values: List<Any>) {
        update.sqlValues(values)
    }

    override fun sqlQuery(queryBuilder: StringBuilder) {
        update.sqlQuery(queryBuilder)
    }

    override fun set(property: String, value: Any): CustomUpdateQuery {
        update.set(property, value)
        return this
    }

    override fun set(property: String, value: CaseWhen): CustomUpdateQuery {
        update.set(property, value)
        return this
    }

}
