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

import com.jporm.sql.dsl.query.orderby.OrderByProvider;
import com.jporm.sql.dsl.query.select.LockMode;
import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.sql.dsl.query.select.SelectCommonProvider;
import com.jporm.sql.dsl.query.select.SelectUnionsProvider;
import com.jporm.sql.dsl.query.where.WhereProvider;

public interface CustomFindQueryAllProvidersDefault<BEAN> extends WhereProvider<CustomFindQueryWhere<BEAN>>,
																OrderByProvider<CustomFindQueryOrderBy<BEAN>>,
																FindQueryExecutorProvider<BEAN>,
																SelectUnionsProvider,
																SelectCommonProvider {

    @Override
    default SelectUnionsProvider union(SelectCommon select) {
        return getFindQuery().union(select);
    }

    @Override
    default CustomFindQueryOrderBy<BEAN> orderBy() {
        return getFindQuery().orderBy();
    }

    @Override
    default SelectUnionsProvider unionAll(SelectCommon select) {
        return getFindQuery().unionAll(select);
    }

    @Override
    default String sqlRowCountQuery() {
        return getFindQuery().sqlRowCountQuery();
    }

    @Override
    default SelectCommonProvider limit(int limit) {
        return getFindQuery().limit(limit);
    }

    @Override
    default SelectUnionsProvider except(SelectCommon select) {
        return getFindQuery().except(select);
    }

    @Override
    default SelectCommonProvider lockMode(LockMode lockMode) {
        return getFindQuery().lockMode(lockMode);
    }

    @Override
    default SelectUnionsProvider intersect(SelectCommon select) {
        return getFindQuery().intersect(select);
    }

    @Override
    default SelectCommonProvider forUpdate() {
        return getFindQuery().forUpdate();
    }

    @Override
    default SelectCommonProvider forUpdateNoWait() {
        return getFindQuery().forUpdateNoWait();
    }

    @Override
    default SelectCommonProvider offset(int offset) {
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
