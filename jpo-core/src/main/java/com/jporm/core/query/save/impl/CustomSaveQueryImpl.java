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

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.core.query.AQueryRoot;
import com.jporm.core.query.save.CustomSaveQuery;
import com.jporm.core.query.save.CustomSaveQueryValues;
import com.jporm.core.session.Session;
import com.jporm.core.session.SqlExecutor;
import com.jporm.sql.query.clause.Insert;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class CustomSaveQueryImpl<BEAN> extends AQueryRoot implements CustomSaveQuery {

	private final CustomSaveQueryValuesImpl<BEAN> elemValues;
	private final Session session;
	private final Insert insert;
	private boolean executed = false;

	public CustomSaveQueryImpl(final Class<BEAN> clazz, final ServiceCatalog<Session> serviceCatalog) {
		super(serviceCatalog.getSqlCache());
		session = serviceCatalog.getSession();
		insert = serviceCatalog.getSqlFactory().insert(clazz);
		elemValues = new CustomSaveQueryValuesImpl<BEAN>(insert.values(), this);
	}

	@Override
	public int now() {
		executed = true;
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		final SqlExecutor sqlExec = session.sqlExecutor();
		return sqlExec.update(renderSql(), values);
	}

	@Override
	public final void appendValues(final List<Object> values) {
		insert.appendValues(values);
	}

	@Override
	public int getVersion() {
		return insert.getVersion();
	}

	@Override
	public final void renderSql(final StringBuilder queryBuilder) {
		insert.renderSql(queryBuilder);
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

	@Override
	public CustomSaveQuery useGenerators(boolean useGenerators) {
		insert.useGenerators(useGenerators);
		return this;
	}
}
