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

import java.util.ArrayList;
import java.util.List;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.AQueryRoot;
import com.jporm.core.query.SqlFactory;
import com.jporm.core.query.delete.CustomDeleteQuery;
import com.jporm.core.query.delete.CustomDeleteQueryWhere;
import com.jporm.core.session.SqlExecutor;
import com.jporm.sql.query.clause.Delete;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class CustomDeleteQueryImpl<BEAN> extends AQueryRoot implements CustomDeleteQuery<BEAN> {

	private final CustomDeleteQueryWhere<BEAN> where;
	private final ServiceCatalog serviceCatalog;
	private final Delete delete;

	private int _queryTimeout = 0;
	private boolean executed = false;

	public CustomDeleteQueryImpl(final Class<BEAN> clazz, final ServiceCatalog serviceCatalog) {
		super(serviceCatalog.getSqlCache());
		this.serviceCatalog = serviceCatalog;
		delete = SqlFactory.delete(serviceCatalog, clazz);
		where = new CustomDeleteQueryWhereImpl<>(delete.where(), this);
	}

	@Override
	public CustomDeleteQueryWhere<BEAN> where() {
		return where;
	}

	@Override
	public int now() {
		executed = true;

		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
		sqlExec.setTimeout(_queryTimeout);
		return sqlExec.update(renderSql(), values);
	}

	@Override
	public final void appendValues(final List<Object> values) {
		delete.appendValues(values);
	}

	@Override
	public CustomDeleteQuery<BEAN> timeout(final int queryTimeout) {
		this._queryTimeout = queryTimeout;
		return this;
	}

	@Override
	public int getTimeout() {
		return _queryTimeout;
	}

	@Override
	public int getVersion() {
		return delete.getVersion();
	}

	@Override
	public final void renderSql(final StringBuilder queryBuilder) {
		delete.renderSql(queryBuilder);
	}

	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed;
	}

}
