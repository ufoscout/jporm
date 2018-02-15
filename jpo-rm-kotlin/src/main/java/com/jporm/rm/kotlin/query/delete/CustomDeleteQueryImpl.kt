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

import com.jporm.sql.query.where.Where
import com.jporm.sql.query.where.WhereDefault

/**

 * @author Francesco Cina
 * *
 * *         10/lug/2011
 */
class CustomDeleteQueryImpl(private val rmDeleteQuery:  com.jporm.rm.query.delete.CustomDeleteQuery) : CustomDeleteQuery, CustomDeleteQueryWhere, WhereDefault<CustomDeleteQueryWhere> {

    override fun execute(): Int {
        return rmDeleteQuery.execute()
    }

    override fun where(): CustomDeleteQueryWhere {
        return this
    }

    override fun sqlValues(values: List<Any>) {
        rmDeleteQuery.sqlValues(values)
    }

    override fun sqlQuery(queryBuilder: StringBuilder) {
        rmDeleteQuery.sqlQuery(queryBuilder)
    }

    override fun whereImplementation(): Where<*> {
        return rmDeleteQuery.where()
    }

}

