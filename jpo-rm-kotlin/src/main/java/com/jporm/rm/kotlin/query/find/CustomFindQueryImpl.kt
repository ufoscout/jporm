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
package com.jporm.rm.kotlin.query.find

import com.jporm.commons.core.inject.ClassTool
import com.jporm.commons.core.query.SqlFactory
import com.jporm.commons.core.query.find.CustomFindQueryBase
import com.jporm.rm.kotlin.session.SqlExecutor
import com.jporm.sql.query.select.LockMode
import com.jporm.sql.query.select.Select
import com.jporm.sql.query.select.SelectCommon
import com.jporm.sql.query.select.from.From
import com.jporm.sql.query.select.from.FromDefault
import com.jporm.sql.query.select.orderby.OrderBy
import com.jporm.sql.query.select.orderby.OrderByDefault
import com.jporm.sql.query.where.Where
import com.jporm.sql.query.where.WhereDefault

/**

 * @author Francesco Cina
 * *
 * *         20/giu/2011
 */
class CustomFindQueryImpl<BEAN>(clazz: Class<BEAN>, alias: String, private val ormClassTool: ClassTool<BEAN>, private val sqlExecutor: SqlExecutor,
                                sqlFactory: SqlFactory) : CustomFindQueryBase<BEAN>(clazz, alias, ormClassTool, sqlFactory), CustomFindQuery<BEAN>, FromDefault<Class<*>, CustomFindQuery<BEAN>>, CustomFindQueryWhere<BEAN>, WhereDefault<CustomFindQueryWhere<BEAN>>, CustomFindQueryOrderBy<BEAN>, OrderByDefault<CustomFindQueryOrderBy<BEAN>>, ExecutionEnvProvider<BEAN> {
    private val select: Select<Class<*>>

    init {
        select = getSelect()
    }

    override fun getSqlExecutor(): SqlExecutor {
        return sqlExecutor
    }

    override fun getOrmClassTool(): ClassTool<BEAN> {
        return ormClassTool
    }

    override val executionEnvProvider: ExecutionEnvProvider<BEAN>
        get() = this

    override fun where(): CustomFindQueryWhere<BEAN> {
        return this
    }

    override fun whereImplementation(): Where<*> {
        return select.where()
    }

    override fun orderBy(): CustomFindQueryOrderBy<BEAN> {
        return this
    }

    override fun orderByImplementation(): OrderBy<*> {
        return select.orderBy()
    }

    override fun union(select: SelectCommon): CustomFindQueryUnionsProvider<BEAN> {
        this.select.union(select)
        return this
    }

    override fun unionAll(select: SelectCommon): CustomFindQueryUnionsProvider<BEAN> {
        this.select.unionAll(select)
        return this
    }

    override fun limit(limit: Int): CustomFindQueryPaginationProvider<BEAN> {
        select.limit(limit)
        return this
    }

    override fun lockMode(lockMode: LockMode): CustomFindQueryPaginationProvider<BEAN> {
        select.lockMode(lockMode)
        return this
    }

    override fun forUpdate(): CustomFindQueryPaginationProvider<BEAN> {
        select.forUpdate()
        return this
    }

    override fun forUpdateNoWait(): CustomFindQueryPaginationProvider<BEAN> {
        select.forUpdateNoWait()
        return this
    }

    override fun offset(offset: Int): CustomFindQueryPaginationProvider<BEAN> {
        select.offset(offset)
        return this
    }

    override fun fromImplementation(): From<Class<*>, *> {
        return select
    }

    override fun from(): CustomFindQuery<BEAN> {
        return this
    }

    override fun distinct(): CustomFindQuery<BEAN> {
        select.distinct()
        return this
    }

    override fun distinct(distinct: Boolean): CustomFindQuery<BEAN> {
        select.distinct(distinct)
        return this
    }

    override fun ignore(vararg ignoreFields: String): CustomFindQuery<BEAN> {
        setIgnoredFields(*ignoreFields)
        return this
    }

}
