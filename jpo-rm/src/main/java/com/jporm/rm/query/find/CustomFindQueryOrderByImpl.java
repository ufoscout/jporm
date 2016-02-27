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

import com.jporm.sql.dsl.query.orderby.OrderByImpl;
import com.jporm.sql.dsl.query.select.LockMode;
import com.jporm.sql.dsl.query.select.Select;
import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.sql.dsl.query.select.SelectCommonProvider;
import com.jporm.sql.dsl.query.select.SelectUnionsProvider;

/**
 *
 * @author ufo
 *
 * @param <BEAN>
 */
public class CustomFindQueryOrderByImpl<BEAN> extends OrderByImpl<CustomFindQueryOrderBy<BEAN>> 
												implements CustomFindQueryOrderBy<BEAN>,
															FindQueryExecutorProviderDefault<BEAN> {

    private final CustomFindQueryImpl<BEAN> findQuery;

    public CustomFindQueryOrderByImpl(final CustomFindQueryImpl<BEAN> findQuery, final Select<?> select) {
        super(select);
        this.findQuery = findQuery;
    }

    @Override
    protected CustomFindQueryOrderBy<BEAN> getOrderBy() {
        return this;
    }

    @Override
    public SelectUnionsProvider union(SelectCommon select) {
        return findQuery.union(select);
    }

    @Override
    public SelectUnionsProvider unionAll(SelectCommon select) {
        return findQuery.unionAll(select);
    }

    @Override
    public SelectCommonProvider limit(int limit) {
        return findQuery.limit(limit);
    }

    @Override
    public SelectUnionsProvider except(SelectCommon select) {
        return findQuery.except(select);
    }

    @Override
    public SelectCommonProvider lockMode(LockMode lockMode) {
        return findQuery.lockMode(lockMode);
    }

    @Override
    public SelectUnionsProvider intersect(SelectCommon select) {
        return findQuery.intersect(select);
    }

    @Override
    public SelectCommonProvider forUpdate() {
        return findQuery.forUpdate();
    }

    @Override
    public SelectCommonProvider forUpdateNoWait() {
        return findQuery.forUpdateNoWait();
    }

    @Override
    public SelectCommonProvider offset(int offset) {
        return findQuery.offset(offset);
    }

	@Override
	public List<Object> getSqlValues() {
		return findQuery.getSqlValues();
	}

	@Override
	public String getSqlQuery() {
		return findQuery.getSqlQuery();
	}

	@Override
	public String getSqlRowCountQuery() {
		return findQuery.getSqlRowCountQuery();
	}

	@Override
	public ExecutionEnvProvider<BEAN> getExecutionEnvProvider() {
		return findQuery.getExecutionEnvProvider();
	}

}
