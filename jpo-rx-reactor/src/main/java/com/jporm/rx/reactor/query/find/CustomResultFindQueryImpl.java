/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rx.reactor.query.find;

import com.jporm.commons.core.query.find.CustomResultFindQueryBase;
import com.jporm.rx.reactor.session.SqlExecutor;
import com.jporm.sql.query.select.LockMode;
import com.jporm.sql.query.select.Select;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.sql.query.select.from.From;
import com.jporm.sql.query.select.from.FromDefault;
import com.jporm.sql.query.select.groupby.GroupBy;
import com.jporm.sql.query.select.groupby.GroupByDefault;
import com.jporm.sql.query.select.orderby.OrderBy;
import com.jporm.sql.query.select.orderby.OrderByDefault;
import com.jporm.sql.query.where.Where;
import com.jporm.sql.query.where.WhereDefault;

/**
 * @author Francesco Cina 20/giu/2011
 */
public class CustomResultFindQueryImpl<TYPE> extends CustomResultFindQueryBase
                                            implements
											CustomResultFindQuery<TYPE>, FromDefault<TYPE, CustomResultFindQuery<TYPE>>,
											CustomResultFindQueryWhere, WhereDefault<CustomResultFindQueryWhere>,
											CustomResultFindQueryGroupBy, GroupByDefault<CustomResultFindQueryGroupBy>,
											CustomResultFindQueryOrderBy, OrderByDefault<CustomResultFindQueryOrderBy>
{

    private final SqlExecutor sqlExecutor;
	private final Select<TYPE> select;

    public CustomResultFindQueryImpl(final Select<TYPE> select, final SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
        this.select = select;
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
    public From<TYPE, ?> fromImplementation() {
        return select;
    }

	@Override
	public CustomResultFindQuery<TYPE> from() {
		return this;
	}

	@Override
	public CustomResultFindQuery<TYPE> distinct() {
		select.distinct();
		return this;
	}

	@Override
	protected Select<TYPE> getSelect() {
	    return select;
	}

	@Override
	public CustomResultFindQuery<TYPE> distinct(boolean distinct) {
		select.distinct(distinct);
		return this;
	}

}
