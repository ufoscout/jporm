/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.sql.dsl.query.select;

import com.jporm.sql.dsl.query.groupby.GroupByProvider;
import com.jporm.sql.dsl.query.orderby.OrderByProvider;
import com.jporm.sql.dsl.query.select.groupby.SelectGroupBy;
import com.jporm.sql.dsl.query.select.orderby.SelectOrderBy;
import com.jporm.sql.dsl.query.select.pagination.SelectPaginationProvider;
import com.jporm.sql.dsl.query.select.unions.SelectUnionsProvider;

public interface SelectAllProvidersDefault extends GroupByProvider<SelectGroupBy>,
										OrderByProvider<SelectOrderBy>,
										SelectUnionsProvider,
										SelectPaginationProvider {

	@Override
	default SelectGroupBy groupBy(String... fields) {
		return getSelect().groupBy(fields);
	}

	@Override
	default SelectOrderBy orderBy() {
		return getSelect().orderBy();
	}

	@Override
	default String sqlRowCountQuery() {
		return getSelect().sqlRowCountQuery();
	}

	@Override
	default SelectUnionsProvider union(SelectCommon select) {
		return getSelect().union(select);
	}

	@Override
	default SelectUnionsProvider unionAll(SelectCommon select) {
		return getSelect().unionAll(select);
	}

	@Override
	default SelectUnionsProvider except(SelectCommon select) {
		return getSelect().except(select);
	}

	@Override
	default SelectUnionsProvider intersect(SelectCommon select) {
		return getSelect().intersect(select);
	}

	@Override
	default SelectPaginationProvider limit(int limit) {
		return getSelect().limit(limit);
	}

	@Override
	default SelectPaginationProvider lockMode(LockMode lockMode) {
		return getSelect().lockMode(lockMode);
	}

	@Override
	default SelectPaginationProvider forUpdate() {
		return getSelect().forUpdate();
	}

	@Override
	default SelectPaginationProvider forUpdateNoWait() {
		return getSelect().forUpdateNoWait();
	}

	@Override
	default SelectPaginationProvider offset(int offset) {
		return getSelect().offset(offset);
	}

	Select<?> getSelect();

}
