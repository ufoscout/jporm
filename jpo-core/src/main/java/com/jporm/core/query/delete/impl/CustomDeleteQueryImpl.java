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

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.delete.impl.CommonDeleteQueryImpl;
import com.jporm.core.query.delete.CustomDeleteQuery;
import com.jporm.core.query.delete.CustomDeleteQueryWhere;
import com.jporm.core.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dialect.DBType;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class CustomDeleteQueryImpl<BEAN> extends CommonDeleteQueryImpl<CustomDeleteQuery<BEAN>, CustomDeleteQueryWhere<BEAN>> implements CustomDeleteQuery<BEAN> {

	private boolean executed = false;
	private SqlExecutor sqlExecutor;
	private final DBType dbType;

	public CustomDeleteQueryImpl(final Class<BEAN> clazz, final ServiceCatalog serviceCatalog, SqlExecutor sqlExecutor, SqlFactory sqlFactory, DBType dbType) {
		super(clazz, sqlFactory);
		this.sqlExecutor = sqlExecutor;
		this.dbType = dbType;
		setWhere(new CustomDeleteQueryWhereImpl<>(getDelete().where(), this));
	}

	@Override
	public int now() {
		executed = true;
		final List<Object> values = new ArrayList<Object>();
		sql().appendValues(values);
		return sqlExecutor.update(renderSql(), values);
	}

	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed;
	}

	@Override
	public String renderSql() {
		return sql().renderSql(dbType.getDBProfile());
	}


}
