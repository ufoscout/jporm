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
package com.jporm.core.query.save.impl;

import java.util.ArrayList;
import java.util.List;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.AQueryRoot;
import com.jporm.core.query.namesolver.NameSolver;
import com.jporm.core.query.namesolver.impl.NameSolverImpl;
import com.jporm.core.query.save.CustomSaveQuery;
import com.jporm.core.query.save.CustomSaveQueryValues;
import com.jporm.core.session.Session;
import com.jporm.core.session.SqlExecutor;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class CustomSaveQueryImpl<BEAN> extends AQueryRoot implements CustomSaveQuery {

	private final CustomSaveQueryValuesImpl<BEAN> elemValues;
	private final NameSolver nameSolver;
	private int _queryTimeout = 0;
	private final Class<BEAN> clazz;
	private final ServiceCatalog serviceCatalog;
	private final Session session;
	private boolean executed = false;

	public CustomSaveQueryImpl(final Class<BEAN> clazz, final ServiceCatalog serviceCatalog) {
		super(serviceCatalog);
		this.clazz = clazz;
		this.serviceCatalog = serviceCatalog;
		session = serviceCatalog.getSession();
		nameSolver = new NameSolverImpl(serviceCatalog, true);
		nameSolver.register(clazz, clazz.getSimpleName());
		elemValues = new CustomSaveQueryValuesImpl<BEAN>(this, clazz, serviceCatalog);
	}

	@Override
	public int now() {
		executed = true;
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		final SqlExecutor sqlExec = session.sqlExecutor();
		sqlExec.setTimeout(getTimeout());
		return sqlExec.update(renderSql(), values);
	}

	@Override
	public final void appendValues(final List<Object> values) {
		elemValues.appendElementValues(values);
	}

	@Override
	public CustomSaveQuery timeout(final int queryTimeout) {
		_queryTimeout = queryTimeout;
		return this;
	}

	@Override
	public int getTimeout() {
		return _queryTimeout;
	}

	@Override
	public int getStatusVersion() {
		return elemValues.getElementStatusVersion();
	}

	@Override
	public final void renderSql(final StringBuilder queryBuilder) {
		queryBuilder.append("INSERT INTO "); //$NON-NLS-1$
		queryBuilder.append(serviceCatalog.getClassToolMap().get(clazz).getDescriptor().getTableInfo().getTableNameWithSchema() );
		queryBuilder.append(" "); //$NON-NLS-1$
		elemValues.renderSqlElement(queryBuilder, nameSolver);
	}


	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed  ;
	}

	@Override
	public CustomSaveQueryValues values() {
		return elemValues;
	}

	public boolean isUseGenerators() {
		return elemValues.isUseGenerators();
	}

	@Override
	public CustomSaveQuery useGenerators(boolean useGenerators) {
		elemValues.setUseGenerators(useGenerators);
		return this;
	}
}
