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
package com.jporm.rm.query.find;

import java.util.List;

import com.jporm.commons.core.inject.ClassTool;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.query.from.FromDefault;
import com.jporm.sql.dsl.query.select.LockMode;
import com.jporm.sql.dsl.query.select.Select;
import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.sql.dsl.query.select.SelectCommonProvider;
import com.jporm.sql.dsl.query.select.SelectUnionsProvider;

/**
 *
 * @author Francesco Cina
 *
 *         20/giu/2011
 */
public class CustomFindQueryImpl<BEAN> implements FromDefault<BEAN, CustomFindQueryFrom<BEAN>>,
													FindQueryExecutorProviderDefault<BEAN>, 
													CustomFindQuery<BEAN>,
													ExecutionEnvProvider<BEAN> {

    private final SqlExecutor sqlExecutor;
    private final Select<Class<?>> select;
    private final ClassTool<BEAN> ormClassTool;
    private final CustomFindQueryWhereImpl<BEAN> where;
    private final CustomFindQueryOrderByImpl<BEAN> orderBy;

    public CustomFindQueryImpl(final Class<BEAN> clazz, final String alias, final boolean distinct, final ClassTool<BEAN> ormClassTool, final SqlExecutor sqlExecutor, final SqlFactory sqlFactory) {
        this.ormClassTool = ormClassTool;
        this.sqlExecutor = sqlExecutor;
        String[] fields = ormClassTool.getDescriptor().getAllColumnJavaNames();;
        select = sqlFactory.select(fields).distinct(distinct).from(clazz, alias);
        where = new CustomFindQueryWhereImpl<>(this, select);
        orderBy = new CustomFindQueryOrderByImpl<>(this, select);
    }

    @Override
    public List<Object> getSqlValues() {
        return select.sqlValues();
    }

    @Override
    public String getSqlQuery() {
        return select.sqlQuery();
    }

    @Override
    public String getSqlRowCountQuery() {
        return select.sqlRowCountQuery();
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
		return where;
	}

	@Override
	public CustomFindQueryOrderBy<BEAN> orderBy() {
		return orderBy;
	}

    @Override
    public final SelectUnionsProvider union(SelectCommon select) {
        this.select.union(select);
        return this;
    }

    @Override
    public final SelectUnionsProvider unionAll(SelectCommon select) {
        this.select.unionAll(select);
        return this;
    }

    @Override
    public final  SelectUnionsProvider except(SelectCommon select) {
        this.select.except(select);
        return this;
    }

    @Override
    public final SelectUnionsProvider intersect(SelectCommon select) {
        this.select.intersect(select);
        return this;
    }

	@Override
	public String sqlRowCountQuery() {
		return select.sqlRowCountQuery();
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
	public SelectCommonProvider limit(int limit) {
		select.limit(limit);
		return this;
	}

	@Override
	public SelectCommonProvider lockMode(LockMode lockMode) {
		select.lockMode(lockMode);
		return this;
	}

	@Override
	public SelectCommonProvider forUpdate() {
		select.forUpdate();
		return this;
	}

	@Override
	public SelectCommonProvider forUpdateNoWait() {
		select.forUpdateNoWait();
		return this;
	}

	@Override
	public SelectCommonProvider offset(int offset) {
		select.offset(offset);
		return this;
	}

	@Override
	public CustomFindQueryFrom<BEAN> getFrom() {
		return this;
	}

}
