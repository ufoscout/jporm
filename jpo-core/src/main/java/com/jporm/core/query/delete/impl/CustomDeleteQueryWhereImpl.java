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
package com.jporm.core.query.delete.impl;

import java.util.List;

import com.jporm.commons.core.query.clause.impl.WhereImpl;
import com.jporm.core.query.delete.CustomDeleteQuery;
import com.jporm.core.query.delete.CustomDeleteQueryWhere;
import com.jporm.sql.query.clause.Where;

/**
 *
 * @author ufo
 *
 */
public class CustomDeleteQueryWhereImpl<BEAN> extends WhereImpl<CustomDeleteQueryWhere<BEAN>> implements CustomDeleteQueryWhere<BEAN> {

	private final CustomDeleteQuery<BEAN> deleteQuery;

	@Override
	public void execute() {
		deleteQuery.execute();
	}

	@Override
	public boolean isExecuted() {
		return deleteQuery.isExecuted();
	}

	public CustomDeleteQueryWhereImpl(Where sqlWhere, final CustomDeleteQuery<BEAN> deleteQuery) {
		super(sqlWhere);
		this.deleteQuery = deleteQuery;
	}

	@Override
	public String renderSql() {
		return this.deleteQuery.renderSql();
	}

	@Override
	public void renderSql(final StringBuilder stringBuilder) {
		this.deleteQuery.renderSql(stringBuilder);
	}

	@Override
	public void appendValues(final List<Object> values) {
		this.deleteQuery.appendValues(values);
	}

	@Override
	public CustomDeleteQuery<BEAN> query() {
		return this.deleteQuery;
	}

	@Override
	protected CustomDeleteQueryWhere<BEAN> where() {
		return this;
	}

	@Override
	public int now() {
		return this.deleteQuery.now();
	}

	@Override
	public int getVersion() {
		return deleteQuery.getVersion();
	}

}
