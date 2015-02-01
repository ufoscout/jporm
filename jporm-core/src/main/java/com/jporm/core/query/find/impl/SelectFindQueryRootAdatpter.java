/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.core.query.find.impl;

import java.util.List;

import com.jporm.core.query.find.FindQueryRoot;
import com.jporm.sql.query.clause.SelectCommon;

public class SelectFindQueryRootAdatpter implements SelectCommon {

	private FindQueryRoot query;

	public SelectFindQueryRootAdatpter(FindQueryRoot query) {
		this.query = query;
	}

	@Override
	public String renderSql() {
		return query.renderSql();
	}

	@Override
	public void renderSql(StringBuilder queryBuilder) {
		query.renderSql(queryBuilder);
	}

	@Override
	public void appendValues(List<Object> values) {
		query.appendValues(values);
	}

	@Override
	public int getVersion() {
		return query.getVersion();
	}

}
