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
package com.jporm.core.query.update;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.jporm.query.update.UpdateQuery;

public class UpdateQueryListDecorator<BEAN> implements UpdateQuery<List<BEAN>> {

	private final List<UpdateQuery<BEAN>> updateQueries = new ArrayList<>();
	private boolean executed;

	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed;
	}

	@Override
	public String renderSql() {
		final StringBuilder queryBuilder = new StringBuilder();
		renderSql(queryBuilder);
		return queryBuilder.toString();
	}

	@Override
	public void renderSql(final StringBuilder queryBuilder) {
		updateQueries.forEach(deleteQuery -> {
			deleteQuery.renderSql(queryBuilder);
			queryBuilder.append("\n");
		});
	}

	@Override
	public void appendValues(List<Object> values) {
		updateQueries.forEach(deleteQuery -> {
			List<Object> innerValues = new ArrayList<>();
			deleteQuery.appendValues(innerValues);
			values.add(innerValues);
		});
	}

	public void add(UpdateQuery<BEAN> query) {
		updateQueries.add(query);
	}

	public List<UpdateQuery<BEAN>> getQueries() {
		return updateQueries;
	}

	@Override
	public List<BEAN> now() {
		executed = true;
		return updateQueries.stream().map(query -> query.now()).collect(Collectors.toList());
	}

}
