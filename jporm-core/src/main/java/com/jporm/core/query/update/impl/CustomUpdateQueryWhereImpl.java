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
package com.jporm.core.query.update.impl;

import java.util.List;

import com.jporm.core.query.clause.impl.WhereImpl;
import com.jporm.core.query.update.CustomUpdateQuery;
import com.jporm.core.query.update.CustomUpdateQuerySet;
import com.jporm.core.query.update.CustomUpdateQueryWhere;

/**
 *
 * @author ufo
 *
 */
public class CustomUpdateQueryWhereImpl extends WhereImpl<CustomUpdateQueryWhere> implements CustomUpdateQueryWhere {

	private final CustomUpdateQuery updateQuery;

	public CustomUpdateQueryWhereImpl(final CustomUpdateQuery updateQuery) {
		this.updateQuery = updateQuery;

	}

	@Override
	public CustomUpdateQuery query() {
		return updateQuery;
	}

	@Override
	public CustomUpdateQuerySet set() {
		return updateQuery.set();
	}

	@Override
	protected CustomUpdateQueryWhere where() {
		return this;
	}

	@Override
	public int now() {
		return updateQuery.now();
	}

	@Override
	public final void appendValues(final List<Object> values) {
		updateQuery.appendValues(values);
	}

	@Override
	public String renderSql() {
		return updateQuery.renderSql();
	}

	@Override
	public void renderSql(final StringBuilder stringBuilder) {
		updateQuery.renderSql(stringBuilder);
	}

	@Override
	public CustomUpdateQuery timeout(final int queryTimeout) {
		return updateQuery.timeout(queryTimeout);
	}

	@Override
	public int getTimeout() {
		return updateQuery.getTimeout();
	}

	@Override
	public void execute() {
		updateQuery.execute();
	}

	@Override
	public boolean isExecuted() {
		return updateQuery.isExecuted();
	}

}
