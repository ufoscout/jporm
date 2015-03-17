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
package com.jporm.commons.core.query.update.impl;

import java.util.List;

import com.jporm.commons.core.query.clause.impl.SetImpl;
import com.jporm.commons.core.query.update.CommonUpdateQuery;
import com.jporm.commons.core.query.update.CommonUpdateQuerySet;
import com.jporm.commons.core.query.update.CommonUpdateQueryWhere;

/**
 *
 * @author ufo
 *
 */
public class CommonUpdateQuerySetImpl<UPDATE extends CommonUpdateQuery<UPDATE, WHERE, SET>,
										WHERE extends CommonUpdateQueryWhere<UPDATE, WHERE, SET>,
										SET extends CommonUpdateQuerySet<UPDATE, WHERE, SET>>
							extends SetImpl<SET> implements CommonUpdateQuerySet<UPDATE, WHERE, SET> {

	private final UPDATE query;

	public CommonUpdateQuerySetImpl(com.jporm.sql.query.clause.Set sqlSet, final UPDATE query) {
		super(sqlSet);
		this.query = query;
	}

	@Override
	public final String renderSql() {
		return query.renderSql();
	}

	@Override
	public final void renderSql(final StringBuilder stringBuilder) {
		query.renderSql(stringBuilder);
	}

	@Override
	public final void appendValues(final List<Object> values) {
		query.appendValues(values);
	}

	@Override
	public final UPDATE query() {
		return query;
	}

	@Override
	protected final SET set() {
		return query.set();
	}

	@Override
	public final WHERE where() {
		return query.where();
	}

	@Override
	public final int getVersion() {
		return query.getVersion();
	}

}
