/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rm.query.find;

import java.util.Collections;
import java.util.List;

import com.jporm.commons.core.inject.ClassTool;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.query.from.From;
import com.jporm.sql.dsl.query.from.FromDefault;
import com.jporm.sql.dsl.query.groupby.GroupBy;
import com.jporm.sql.dsl.query.groupby.GroupByDefault;
import com.jporm.sql.dsl.query.orderby.OrderBy;
import com.jporm.sql.dsl.query.orderby.OrderByDefault;
import com.jporm.sql.dsl.query.select.LockMode;
import com.jporm.sql.dsl.query.select.Select;
import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.sql.dsl.query.where.Where;
import com.jporm.sql.dsl.query.where.WhereDefault;

/**
 * @author Francesco Cina 20/giu/2011
 */
public class CustomResultFindQueryImpl<BEAN> implements
											CustomResultFindQuery,
											FromDefault<Class<?>, CustomResultFindQuery>,
											CustomResultFindQueryWhere, WhereDefault<CustomResultFindQueryWhere>,
											CustomResultFindQueryGroupBy, GroupByDefault<CustomResultFindQueryGroupBy>,
											CustomResultFindQueryOrderBy, OrderByDefault<CustomResultFindQueryOrderBy>,
											ExecutionEnvProvider<BEAN>
{

    private final SqlExecutor sqlExecutor;
	private final Select<Class<?>> select;
	private final ClassTool<BEAN> ormClassTool;

    public CustomResultFindQueryImpl(final String[] selectFields, final SqlExecutor sqlExecutor, final Class<BEAN> clazz,
    		ClassTool<BEAN> ormClassTool, final String alias, final SqlFactory sqlFactory) {
        this.sqlExecutor = sqlExecutor;
		this.ormClassTool = ormClassTool;
        select = sqlFactory.select(selectFields).from(clazz, alias);
    }

	@Override
	public CustomResultFindQueryWhere where() {
		return this;
	}

    @Override
    public Where<?> whereImplementation() {
        return select.where();
    }

	@Override
	public CustomResultFindQueryGroupBy groupBy(String... fields) {
	    select.groupBy(fields);
		return this;
	}

    @Override
    public GroupBy<?> groupByImplementation() {
        return select.groupBy();
    };

	@Override
	public CustomResultFindQueryOrderBy orderBy() {
		return this;
	}

    @Override
    public OrderBy<?> orderByImplementation() {
        return select.orderBy();
    };

	@Override
	public ExecutionEnvProvider<?> getExecutionEnvProvider() {
		return this;
	}

	@Override
	public void sqlValues(List<Object> values) {
		select.sqlValues(values);
	}

	@Override
	public void sqlQuery(StringBuilder queryBuilder) {
		select.sqlQuery(queryBuilder);
	}

    @Override
    public final CustomResultFindQueryUnionsProvider union(SelectCommon select) {
        this.select.union(select);
        return this;
    }

    @Override
    public final CustomResultFindQueryUnionsProvider unionAll(SelectCommon select) {
        this.select.unionAll(select);
        return this;
    }

    @Override
    public final  CustomResultFindQueryUnionsProvider except(SelectCommon select) {
        this.select.except(select);
        return this;
    }

    @Override
    public final CustomResultFindQueryUnionsProvider intersect(SelectCommon select) {
        this.select.intersect(select);
        return this;
    }

	@Override
	public CustomResultFindQueryPaginationProvider limit(int limit) {
		this.select.limit(limit);
        return this;
	}

	@Override
	public CustomResultFindQueryPaginationProvider lockMode(LockMode lockMode) {
		this.select.lockMode(lockMode);
        return this;
	}

	@Override
	public CustomResultFindQueryPaginationProvider forUpdate() {
		this.select.forUpdate();
        return this;
	}

	@Override
	public CustomResultFindQueryPaginationProvider forUpdateNoWait() {
		this.select.forUpdateNoWait();
        return this;
	}

	@Override
	public CustomResultFindQueryPaginationProvider offset(int offset) {
		this.select.offset(offset);
        return this;
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
    public From<Class<?>, ?> fromImplementation() {
        return select;
    }

	@Override
	public CustomResultFindQuery from() {
		return this;
	}

	@Override
	public String sqlRowCountQuery() {
		return select.sqlRowCountQuery();
	}

	@Override
	public CustomResultFindQuery distinct() {
		select.distinct();
		return this;
	}

	@Override
	public CustomResultFindQuery distinct(boolean distinct) {
		select.distinct(distinct);
		return this;
	}

    @Override
    public List<String> getIgnoredFields() {
        return Collections.EMPTY_LIST;
    }

}
