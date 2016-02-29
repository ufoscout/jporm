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

import com.jporm.sql.query.select.LockMode;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.sql.query.select.orderby.OrderByProvider;
import com.jporm.sql.query.where.WhereProvider;

public interface CustomFindQueryAllProvidersDefault<BEAN> extends WhereProvider<CustomFindQueryWhere<BEAN>>,
																OrderByProvider<CustomFindQueryOrderBy<BEAN>>,
																FindQueryExecutorProvider<BEAN>,
																CustomFindQueryUnionsProvider<BEAN>,
																CustomFindQueryPaginationProvider<BEAN> {

    @Override
    default CustomFindQueryUnionsProvider<BEAN> union(SelectCommon select) {
        return getFindQuery().union(select);
    }

    @Override
    default CustomFindQueryOrderBy<BEAN> orderBy() {
        return getFindQuery().orderBy();
    }

    @Override
    default CustomFindQueryUnionsProvider<BEAN> unionAll(SelectCommon select) {
        return getFindQuery().unionAll(select);
    }

    @Override
    default String sqlRowCountQuery() {
        return getFindQuery().sqlRowCountQuery();
    }

    @Override
    default CustomFindQueryPaginationProvider<BEAN> limit(int limit) {
        return getFindQuery().limit(limit);
    }

    @Override
    default CustomFindQueryUnionsProvider<BEAN> except(SelectCommon select) {
        return getFindQuery().except(select);
    }

    @Override
    default CustomFindQueryPaginationProvider<BEAN> lockMode(LockMode lockMode) {
        return getFindQuery().lockMode(lockMode);
    }

    @Override
    default CustomFindQueryUnionsProvider<BEAN> intersect(SelectCommon select) {
        return getFindQuery().intersect(select);
    }

    @Override
    default CustomFindQueryPaginationProvider<BEAN> forUpdate() {
        return getFindQuery().forUpdate();
    }

    @Override
    default CustomFindQueryPaginationProvider<BEAN> forUpdateNoWait() {
        return getFindQuery().forUpdateNoWait();
    }

    @Override
    default CustomFindQueryPaginationProvider<BEAN> offset(int offset) {
        return getFindQuery().offset(offset);
    }

	@Override
	default ExecutionEnvProvider<BEAN> getExecutionEnvProvider() {
		return getFindQuery().getExecutionEnvProvider();
	}

	CustomFindQuery<BEAN> getFindQuery();

	@Override
	default void sqlValues(List<Object> values) {
		getFindQuery().sqlValues(values);
	}

	@Override
	default void sqlQuery(StringBuilder queryBuilder) {
		getFindQuery().sqlQuery(queryBuilder);
	}

	@Override
	default CustomFindQueryWhere<BEAN> where() {
		return getFindQuery().where();
	}

}
