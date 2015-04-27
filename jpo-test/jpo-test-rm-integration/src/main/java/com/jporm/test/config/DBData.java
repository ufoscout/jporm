/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.test.config;

import javax.sql.DataSource;

import com.jporm.rm.session.SessionProvider;
import com.jporm.sql.dialect.DBType;

public class DBData {

	private SessionProvider dataSourceSessionProvider;
	private SessionProvider jdbcTemplateSessionProvider;
	private DBType dbType;
	private boolean dbAvailable;
	private boolean multipleSchemaSupport;
	public DataSource dataSource;

	public SessionProvider getDataSourceSessionProvider() {
		return dataSourceSessionProvider;
	}
	public void setDataSourceSessionProvider(final SessionProvider sessionProvider) {
		dataSourceSessionProvider = sessionProvider;
	}

	public SessionProvider getJdbcTemplateSessionProvider() {
		return jdbcTemplateSessionProvider;
	}
	public void setJdbcTemplateSessionProvider(final SessionProvider jdbcTemplateSessionProvider) {
		this.jdbcTemplateSessionProvider = jdbcTemplateSessionProvider;
	}

	public DBType getDBType() {
		return dbType;
	}
	public void setDBType(final DBType dbType) {
		this.dbType = dbType;
	}

	public boolean isDbAvailable() {
		return dbAvailable;
	}
	public void setDbAvailable(final boolean dbAvailable) {
		this.dbAvailable = dbAvailable;
	}

	public boolean isMultipleSchemaSupport() {
		return multipleSchemaSupport;
	}
	public void setMultipleSchemaSupport(final boolean multipleSchemaSupport) {
		this.multipleSchemaSupport = multipleSchemaSupport;
	}

	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
