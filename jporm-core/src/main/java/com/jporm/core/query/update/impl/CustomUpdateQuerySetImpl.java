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

import com.jporm.core.query.clause.impl.SetImpl;
import com.jporm.core.query.update.CustomUpdateQuery;
import com.jporm.core.query.update.CustomUpdateQuerySet;
import com.jporm.core.query.update.CustomUpdateQueryWhere;

/**
 *
 * @author ufo
 *
 */
public class CustomUpdateQuerySetImpl extends SetImpl<CustomUpdateQuerySet> implements CustomUpdateQuerySet {

	private final CustomUpdateQuery query;

	public CustomUpdateQuerySetImpl(final CustomUpdateQuery query) {
		this.query = query;
	}

	@Override
	public String renderSql() {
		return query.renderSql();
	}

	@Override
	public void renderSql(final StringBuilder stringBuilder) {
		query.renderSql(stringBuilder);
	}

	@Override
	public void appendValues(final List<Object> values) {
		query.appendValues(values);
	}

	@Override
	public final int now() {
		return query.now();
	}

	@Override
	public CustomUpdateQuery query() {
		return query;
	}

	@Override
	protected CustomUpdateQuerySet set() {
		return this;
	}

	@Override
	public CustomUpdateQueryWhere where() {
		return query.where();
	}

	@Override
	public CustomUpdateQuery timeout(final int queryTimeout) {
		return query.timeout(queryTimeout);
	}

	@Override
	public int getTimeout() {
		return query.getTimeout();
	}

	@Override
	public void execute() {
		query.execute();
	}

	@Override
	public boolean isExecuted() {
		return query.isExecuted();
	}

}
