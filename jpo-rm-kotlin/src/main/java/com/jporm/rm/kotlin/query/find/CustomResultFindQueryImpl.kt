/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.jporm.rm.kotlin.query.find

import com.jporm.commons.core.query.find.CustomResultFindQueryBase
import com.jporm.rm.kotlin.session.SqlExecutor
import com.jporm.sql.query.select.LockMode
import com.jporm.sql.query.select.Select
import com.jporm.sql.query.select.SelectCommon
import com.jporm.sql.query.select.from.From
import com.jporm.sql.query.select.from.FromDefault
import com.jporm.sql.query.select.groupby.GroupBy
import com.jporm.sql.query.select.groupby.GroupByDefault
import com.jporm.sql.query.select.orderby.OrderBy
import com.jporm.sql.query.select.orderby.OrderByDefault
import com.jporm.sql.query.where.Where
import com.jporm.sql.query.where.WhereDefault

/**
 * @author Francesco Cina 20/giu/2011
 */
class CustomResultFindQueryImpl<TYPE>(private val select: Select<TYPE>, override val sqlExecutor: SqlExecutor) : CustomResultFindQueryBase(), CustomResultFindQuery<TYPE>, FromDefault<TYPE, CustomResultFindQuery<TYPE>>, CustomResultFindQueryWhere, WhereDefault<CustomResultFindQueryWhere>, CustomResultFindQueryGroupBy, GroupByDefault<CustomResultFindQueryGroupBy>, CustomResultFindQueryOrderBy, OrderByDefault<CustomResultFindQueryOrderBy> {

    override fun where(): CustomResultFindQueryWhere {
        return this
    }

    override fun whereImplementation(): Where<*> {
        return select.where()
    }

    override fun groupBy(vararg fields: String): CustomResultFindQueryGroupBy {
        select.groupBy(*fields)
        return this
    }

    override fun groupByImplementation(): GroupBy<*> {
        return select.groupBy()
    }

    override fun orderBy(): CustomResultFindQueryOrderBy {
        return this
    }

    override fun orderByImplementation(): OrderBy<*> {
        return select.orderBy()
    }

    override fun union(select: SelectCommon): CustomResultFindQueryUnionsProvider {
        this.select.union(select)
        return this
    }

    override fun unionAll(select: SelectCommon): CustomResultFindQueryUnionsProvider {
        this.select.unionAll(select)
        return this
    }

    override fun limit(limit: Int): CustomResultFindQueryPaginationProvider {
        this.select.limit(limit)
        return this
    }

    override fun lockMode(lockMode: LockMode): CustomResultFindQueryPaginationProvider {
        this.select.lockMode(lockMode)
        return this
    }

    override fun forUpdate(): CustomResultFindQueryPaginationProvider {
        this.select.forUpdate()
        return this
    }

    override fun forUpdateNoWait(): CustomResultFindQueryPaginationProvider {
        this.select.forUpdateNoWait()
        return this
    }

    override fun offset(offset: Int): CustomResultFindQueryPaginationProvider {
        this.select.offset(offset)
        return this
    }

    override fun fromImplementation(): From<TYPE, *> {
        return select
    }

    override fun from(): CustomResultFindQuery<TYPE> {
        return this
    }

    override fun distinct(): CustomResultFindQuery<TYPE> {
        select.distinct()
        return this
    }

    override fun distinct(distinct: Boolean): CustomResultFindQuery<TYPE> {
        select.distinct(distinct)
        return this
    }

    /**
     * @return the select
     */
    override fun getSelect(): Select<TYPE> {
        return select
    }

}
