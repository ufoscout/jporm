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

import java.util.List;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.SmartRenderableSqlQuery;
import com.jporm.core.query.namesolver.NameSolverImpl;
import com.jporm.query.delete.DeleteQuery;
import com.jporm.query.namesolver.NameSolver;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class DeleteQueryOrm<BEAN> extends SmartRenderableSqlQuery implements DeleteQuery<BEAN> {

	private final DeleteWhereImpl<BEAN> where = new DeleteWhereImpl<BEAN>(this);
	private final ServiceCatalog serviceCatalog;
	private int _queryTimeout = 0;
	private final Class<BEAN> clazz;
	private final NameSolver nameSolver;

	public DeleteQueryOrm(final Class<BEAN> clazz, final ServiceCatalog serviceCatalog) {
		super(serviceCatalog);
		this.clazz = clazz;
		this.serviceCatalog = serviceCatalog;
		nameSolver = new NameSolverImpl(serviceCatalog, true);
		nameSolver.register(clazz, clazz.getSimpleName());
	}

	@Override
	public DeleteWhereImpl<BEAN> where() {
		return where;
	}

	@Override
	public int now() {
		return serviceCatalog.getOrmQueryExecutor().delete().delete(this, clazz);
	}

	@Override
	public final void appendValues(final List<Object> values) {
		where.appendElementValues(values);
	}

	@Override
	public DeleteQuery<BEAN> queryTimeout(final int queryTimeout) {
		this._queryTimeout = queryTimeout;
		return this;
	}

	public int getQueryTimeout() {
		return _queryTimeout;
	}

	@Override
	public int getStatusVersion() {
		return where.getElementStatusVersion();
	}

	@Override
	public final void renderSql(final StringBuilder queryBuilder) {
		queryBuilder.append("DELETE FROM "); //$NON-NLS-1$
		queryBuilder.append(serviceCatalog.getClassToolMap().getOrmClassTool(clazz).getClassMap().getTableInfo().getTableNameWithSchema() );
		queryBuilder.append(" "); //$NON-NLS-1$
		where.renderSqlElement(queryBuilder, nameSolver);
	}

}
