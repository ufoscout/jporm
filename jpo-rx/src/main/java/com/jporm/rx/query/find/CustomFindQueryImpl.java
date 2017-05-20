/*******************************************************************************
 * Copyright 2013 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.jporm.rx.query.find;

import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.find.CustomFindQueryBase;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.query.select.LockMode;
import com.jporm.sql.query.select.Select;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.sql.query.select.from.From;
import com.jporm.sql.query.select.from.FromDefault;
import com.jporm.sql.query.select.orderby.OrderBy;
import com.jporm.sql.query.select.orderby.OrderByDefault;
import com.jporm.sql.query.where.Where;
import com.jporm.sql.query.where.WhereDefault;

/**
 *
 * @author Francesco Cina
 *
 *         20/giu/2011
 */
public class CustomFindQueryImpl<BEAN> extends CustomFindQueryBase<BEAN>
                                        implements
                                        CustomFindQuery<BEAN>, FromDefault<Class<?>, CustomFindQuery<BEAN>>,
                                        CustomFindQueryWhere<BEAN>, WhereDefault<CustomFindQueryWhere<BEAN>>,
                                        CustomFindQueryOrderBy<BEAN>, OrderByDefault<CustomFindQueryOrderBy<BEAN>>,
                                        ExecutionEnvProvider<BEAN>
{

    private final SqlExecutor sqlExecutor;
    private final Select<Class<?>> select;
    private final ClassTool<BEAN> ormClassTool;

    public CustomFindQueryImpl(final Class<BEAN> clazz, final String alias, final ClassTool<BEAN> ormClassTool, final SqlExecutor sqlExecutor,
            final SqlFactory sqlFactory) {
        super(clazz, alias, ormClassTool, sqlFactory);
        this.ormClassTool = ormClassTool;
        this.sqlExecutor = sqlExecutor;
        select = getSelect();
    }

    @Override
    public SqlExecutor getSqlExecutor() {
        return sqlExecutor;
    }

    @Override
    public ClassTool<BEAN> getOrmClassTool() {
        return ormClassTool;
    }

    @Override
    public ExecutionEnvProvider<BEAN> getExecutionEnvProvider() {
        return this;
    }

    @Override
    public CustomFindQueryWhere<BEAN> where() {
        return this;
    }

    @Override
    public Where<?> whereImplementation() {
        return select.where();
    }

    @Override
    public CustomFindQueryOrderBy<BEAN> orderBy() {
        return this;
    }

    @Override
    public OrderBy<?> orderByImplementation() {
        return select.orderBy();
    };

    @Override
    public final CustomFindQueryUnionsProvider<BEAN> union(SelectCommon select) {
        this.select.union(select);
        return this;
    }

    @Override
    public final CustomFindQueryUnionsProvider<BEAN> unionAll(SelectCommon select) {
        this.select.unionAll(select);
        return this;
    }

    @Override
    public CustomFindQueryPaginationProvider<BEAN> limit(int limit) {
        select.limit(limit);
        return this;
    }

    @Override
    public CustomFindQueryPaginationProvider<BEAN> lockMode(LockMode lockMode) {
        select.lockMode(lockMode);
        return this;
    }

    @Override
    public CustomFindQueryPaginationProvider<BEAN> forUpdate() {
        select.forUpdate();
        return this;
    }

    @Override
    public CustomFindQueryPaginationProvider<BEAN> forUpdateNoWait() {
        select.forUpdateNoWait();
        return this;
    }

    @Override
    public CustomFindQueryPaginationProvider<BEAN> offset(int offset) {
        select.offset(offset);
        return this;
    }

    @Override
    public From<Class<?>, ?> fromImplementation() {
        return select;
    }

    @Override
    public CustomFindQuery<BEAN> from() {
        return this;
    }

    @Override
    public CustomFindQuery<BEAN> distinct() {
        select.distinct();
        return this;
    }

    @Override
    public CustomFindQuery<BEAN> distinct(boolean distinct) {
        select.distinct(distinct);
        return this;
    }

    @Override
    public final CustomFindQuery<BEAN> ignore(final String... ignoreFields) {
        setIgnoredFields(ignoreFields);
        return this;
    }

}
