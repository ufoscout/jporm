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
package com.jporm.core.query.update;

import java.util.ArrayList;
import java.util.List;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.SmartRenderableSqlQuery;
import com.jporm.core.query.namesolver.NameSolverImpl;
import com.jporm.query.namesolver.NameSolver;
import com.jporm.query.update.CustomUpdateQuery;
import com.jporm.query.update.CustomUpdateSet;
import com.jporm.query.update.CustomUpdateWhere;
import com.jporm.session.Session;
import com.jporm.session.SqlExecutor;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class CustomUpdateQueryImpl extends SmartRenderableSqlQuery implements CustomUpdateQuery {

	private final CustomUpdateSetImpl set = new CustomUpdateSetImpl(this);
	private final CustomUpdateWhereImpl where = new CustomUpdateWhereImpl(this);
	private final NameSolver nameSolver;
	private int _queryTimeout = 0;
	private final Class<?> clazz;
	private final ServiceCatalog serviceCatalog;
	private final Session session;
	private boolean executed = false;

	public CustomUpdateQueryImpl(final Class<?> clazz, final ServiceCatalog serviceCatalog) {
		super(serviceCatalog);
		this.clazz = clazz;
		this.serviceCatalog = serviceCatalog;
		session = serviceCatalog.getSession();
		nameSolver = new NameSolverImpl(serviceCatalog, true);
		nameSolver.register(clazz, clazz.getSimpleName());
	}

	@Override
	public CustomUpdateWhere where() {
		return where;
	}

	@Override
	public int now() {
		executed = true;
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		final SqlExecutor sqlExec = session.sqlExecutor();
		sqlExec.setQueryTimeout(getTimeout());
		return sqlExec.update(renderSql(), values);
	}

	@Override
	public final void appendValues(final List<Object> values) {
		set.appendElementValues(values);
		where.appendElementValues(values);
	}

	@Override
	public CustomUpdateQuery timeout(final int queryTimeout) {
		_queryTimeout = queryTimeout;
		return this;
	}

	@Override
	public int getTimeout() {
		return _queryTimeout;
	}

	@Override
	public int getStatusVersion() {
		return set.getElementStatusVersion() + where.getElementStatusVersion();
	}

	@Override
	public final void renderSql(final StringBuilder queryBuilder) {
		queryBuilder.append("UPDATE "); //$NON-NLS-1$
		queryBuilder.append(serviceCatalog.getClassToolMap().get(clazz).getDescriptor().getTableInfo().getTableNameWithSchema() );
		queryBuilder.append(" "); //$NON-NLS-1$
		set.renderSqlElement(queryBuilder, nameSolver);
		where.renderSqlElement(queryBuilder, nameSolver);
	}

	@Override
	public CustomUpdateSet set() {
		return set;
	}

	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed  ;
	}
}
