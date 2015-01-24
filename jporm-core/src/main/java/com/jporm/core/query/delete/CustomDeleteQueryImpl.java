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
package com.jporm.core.query.delete;

import java.util.ArrayList;
import java.util.List;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.AQueryRoot;
import com.jporm.core.query.namesolver.NameSolverImpl;
import com.jporm.query.delete.CustomDeleteQuery;
import com.jporm.query.namesolver.NameSolver;
import com.jporm.session.SqlExecutor;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class CustomDeleteQueryImpl<BEAN> extends AQueryRoot implements CustomDeleteQuery<BEAN> {

	private final CustomDeleteQueryWhereImpl<BEAN> where = new CustomDeleteQueryWhereImpl<BEAN>(this);
	private final ServiceCatalog serviceCatalog;
	private int _queryTimeout = 0;
	private final Class<BEAN> clazz;
	private final NameSolver nameSolver;
	private boolean executed = false;

	public CustomDeleteQueryImpl(final Class<BEAN> clazz, final ServiceCatalog serviceCatalog) {
		super(serviceCatalog);
		this.clazz = clazz;
		this.serviceCatalog = serviceCatalog;
		nameSolver = new NameSolverImpl(serviceCatalog, true);
		nameSolver.register(clazz, clazz.getSimpleName());
	}

	@Override
	public CustomDeleteQueryWhereImpl<BEAN> where() {
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
		where.appendElementValues(values);
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
	public int getStatusVersion() {
		return where.getElementStatusVersion();
	}

	@Override
	public final void renderSql(final StringBuilder queryBuilder) {
		queryBuilder.append("DELETE FROM "); //$NON-NLS-1$
		queryBuilder.append(serviceCatalog.getClassToolMap().get(clazz).getDescriptor().getTableInfo().getTableNameWithSchema() );
		queryBuilder.append(" "); //$NON-NLS-1$
		where.renderSqlElement(queryBuilder, nameSolver);
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
