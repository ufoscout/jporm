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

import com.jporm.rx.core.session.ConnectionProvider;
import com.jporm.sql.dialect.DBType;

public class DBData {

	private ConnectionProvider connectionProvider;
	private DBType dbType;
	private boolean dbAvailable;
	private boolean multipleSchemaSupport;
	private DataSource dataSource;
	private String description;


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
	/**
	 * @return the connectionProvider
	 */
	public ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}
	/**
	 * @param connectionProvider the connectionProvider to set
	 */
	public void setConnectionProvider(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
