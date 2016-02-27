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

public interface SelectAllProvidersDefault extends GroupByProvider<SelectGroupBy>, 
										OrderByProvider<SelectOrderBy>,
										SelectUnionsProvider, 
										SelectCommonProvider {

	@Override
	default SelectGroupBy groupBy() {
		return getSelect().groupBy();
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
		return this.getSelect().union(select);
	}

	@Override
	default SelectUnionsProvider unionAll(SelectCommon select) {
		return this.getSelect().unionAll(select);
	}

	@Override
	default SelectUnionsProvider except(SelectCommon select) {
		return this.getSelect().except(select);
	}

	@Override
	default SelectUnionsProvider intersect(SelectCommon select) {
		return this.getSelect().intersect(select);
	}

	@Override
	default SelectCommonProvider limit(int limit) {
		return getSelect().limit(limit);
	}

	@Override
	default SelectCommonProvider lockMode(LockMode lockMode) {
		return getSelect().lockMode(lockMode);
	}

	@Override
	default SelectCommonProvider forUpdate() {
		return getSelect().forUpdate();
	}

	@Override
	default SelectCommonProvider forUpdateNoWait() {
		return getSelect().forUpdateNoWait();
	}

	@Override
	default SelectCommonProvider offset(int offset) {
		return getSelect().offset(offset);
	}

	Select<?> getSelect();

}
