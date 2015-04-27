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
package com.jporm.rm.query.update.impl;

import java.util.ArrayList;
import java.util.List;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.update.impl.CommonUpdateQueryImpl;
import com.jporm.rm.query.update.CustomUpdateQuery;
import com.jporm.rm.query.update.CustomUpdateQuerySet;
import com.jporm.rm.query.update.CustomUpdateQueryWhere;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dialect.DBType;
import com.jporm.sql.query.clause.Update;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class CustomUpdateQueryImpl extends CommonUpdateQueryImpl<CustomUpdateQuery, CustomUpdateQueryWhere, CustomUpdateQuerySet> implements CustomUpdateQuery {

	private final SqlExecutor sqlExecutor;
	private final DBType dbType;

	public CustomUpdateQueryImpl(final Class<?> clazz, final ServiceCatalog serviceCatalog, SqlExecutor sqlExecutor, SqlFactory sqlFactory, DBType dbType) {
		super(clazz, sqlFactory);
		this.sqlExecutor = sqlExecutor;
		this.dbType = dbType;
		Update update = query();
		setWhere(new CustomUpdateQueryWhereImpl(update.where(), this));
		setSet(new CustomUpdateQuerySetImpl(update.set(), this));
	}

	@Override
	public int execute() {
		final List<Object> values = new ArrayList<>();
		sql().appendValues(values);
		return sqlExecutor.update(renderSql(), values);
	}

	@Override
	public String renderSql() {
		return sql().renderSql(dbType.getDBProfile());
	}
}
