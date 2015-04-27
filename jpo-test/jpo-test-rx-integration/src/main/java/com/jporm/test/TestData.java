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
package com.jporm.test;

import javax.sql.DataSource;

import com.jporm.rx.session.ConnectionProvider;
import com.jporm.sql.dialect.DBType;

public class TestData {

	private final ConnectionProvider sessionProvider;
	private final DBType dbType;
	private final boolean supportMultipleSchemas;
	private final DataSource dataSource;

	public TestData(final ConnectionProvider sessionProvider, final DataSource dataSource, final DBType dbType, final boolean supportMultipleSchemas) {
		this.sessionProvider = sessionProvider;
		this.dataSource = dataSource;
		this.dbType = dbType;
		this.supportMultipleSchemas = supportMultipleSchemas;
	}

	public ConnectionProvider getConnectionProvider() {
		return sessionProvider;
	}

	public DBType getDBType() {
		return dbType;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public boolean isSupportMultipleSchemas() {
		return supportMultipleSchemas;
	}

}
