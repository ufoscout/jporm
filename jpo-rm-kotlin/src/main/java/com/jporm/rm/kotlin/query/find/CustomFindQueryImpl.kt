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

import com.jporm.sql.query.select.LockMode
import com.jporm.sql.query.select.SelectCommon
import com.jporm.sql.query.select.from.From
import com.jporm.sql.query.select.from.FromDefault
import com.jporm.sql.query.select.orderby.OrderBy
import com.jporm.sql.query.select.orderby.OrderByDefault
import com.jporm.sql.query.where.Where
import com.jporm.sql.query.where.WhereDefault
import java.lang.StringBuilder

/**

 * @author Francesco Cina
 * *
 * *         20/giu/2011
 */
class CustomFindQueryImpl<BEAN>(val rmQuery: com.jporm.rm.query.find.CustomFindQuery<BEAN>) : CustomFindQuery<BEAN>, FromDefault<Class<*>, CustomFindQuery<BEAN>>, CustomFindQueryWhere<BEAN>, WhereDefault<CustomFindQueryWhere<BEAN>>, CustomFindQueryOrderBy<BEAN>, OrderByDefault<CustomFindQueryOrderBy<BEAN>> {

    override fun where(): CustomFindQueryWhere<BEAN> {
        return this
    }

    override fun whereImplementation(): Where<*> {
        return rmQuery.where()
    }

    override fun orderBy(): CustomFindQueryOrderBy<BEAN> {
        return this
    }

    override fun orderByImplementation(): OrderBy<*> {
        return rmQuery.orderBy()
    }

    override fun union(select: SelectCommon): CustomFindQueryUnionsProvider<BEAN> {
        this.rmQuery.union(select)
        return this
    }

    override fun unionAll(select: SelectCommon): CustomFindQueryUnionsProvider<BEAN> {
        this.rmQuery.unionAll(select)
        return this
    }

    override fun limit(limit: Int): CustomFindQueryPaginationProvider<BEAN> {
        rmQuery.limit(limit)
        return this
    }

    override fun lockMode(lockMode: LockMode): CustomFindQueryPaginationProvider<BEAN> {
        rmQuery.lockMode(lockMode)
        return this
    }

    override fun forUpdate(): CustomFindQueryPaginationProvider<BEAN> {
        rmQuery.forUpdate()
        return this
    }

    override fun forUpdateNoWait(): CustomFindQueryPaginationProvider<BEAN> {
        rmQuery.forUpdateNoWait()
        return this
    }

    override fun offset(offset: Int): CustomFindQueryPaginationProvider<BEAN> {
        rmQuery.offset(offset)
        return this
    }

    override fun fromImplementation(): From<Class<*>, *> {
        return rmQuery
    }

    override fun from(): CustomFindQuery<BEAN> {
        return this
    }

    override fun distinct(): CustomFindQuery<BEAN> {
        rmQuery.distinct()
        return this
    }

    override fun distinct(distinct: Boolean): CustomFindQuery<BEAN> {
        rmQuery.distinct(distinct)
        return this
    }

    override fun ignore(vararg ignoreFields: String): CustomFindQuery<BEAN> {
        rmQuery.ignore(*ignoreFields)
        return this
    }

    override fun fetchAll(beanReader: (BEAN, Int) -> Unit) {
        rmQuery.fetchAll(beanReader);
    }

    override fun <R> fetchAll(beanReader: (BEAN, Int) -> R): List<R> {
        return rmQuery.fetchAll(beanReader)
    }

    override fun fetchOne(): BEAN? {
        return rmQuery.fetchOne()
    }

    override fun fetchOneUnique(): BEAN {
        return rmQuery.fetchOneUnique()
    }

    override fun fetchRowCount(): Int {
        return rmQuery.fetchRowCount()
    }

    override fun sqlQuery(queryBuilder: StringBuilder) {
        rmQuery.sqlQuery(queryBuilder)
    }

    override fun sqlValues(values: MutableList<Any>?) {
        rmQuery.sqlValues(values)
    }

    override fun sqlRowCountQuery(): String {
        return rmQuery.sqlRowCountQuery()
    }
}
