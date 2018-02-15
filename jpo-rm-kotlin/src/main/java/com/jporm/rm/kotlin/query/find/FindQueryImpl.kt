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
import java.lang.StringBuilder
import java.util.function.BiConsumer
import java.util.function.BiFunction

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
class FindQueryImpl<BEAN>(private val rmfindQuery: com.jporm.rm.query.find.FindQuery<BEAN>) : FindQuery<BEAN> {

    override fun sqlValues(values: MutableList<Any>?) {
        rmfindQuery.sqlValues(values)
    }

    override fun sqlQuery(queryBuilder: StringBuilder?) {
        rmfindQuery.sqlQuery(queryBuilder)
    }

    override fun fetchAll(beanReader: (BEAN, Int) -> Unit) {
        rmfindQuery.fetchAll(BiConsumer<BEAN, Int>{ bean, count -> beanReader(bean, count) })
    }

    override fun fetchOneUnique(): BEAN {
        return rmfindQuery.fetchOneUnique()
    }

    override fun fetchRowCount(): Int {
        return rmfindQuery.fetchRowCount()
    }

    override fun <R> fetchAll(beanReader: (BEAN, Int) -> R): List<R> {
        return rmfindQuery.fetchAll(BiFunction<BEAN, Int, R>{ bean, count -> beanReader(bean, count) })
    }

    override fun fetchOne(): BEAN {
        return rmfindQuery.fetchOne()
    }

    override fun sqlRowCountQuery(): String {
        return rmfindQuery.sqlRowCountQuery()
    }

}
