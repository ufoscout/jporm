/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rx.core.session.datasource;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.commons.core.util.SpringBasedSQLStateSQLExceptionTranslator;
import com.jporm.rx.core.connection.Connection;
import com.jporm.rx.core.session.SessionProvider;
import com.jporm.sql.dialect.DBType;

public class DataSourceRxSessionProvider implements SessionProvider {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final DBType dbType;
	private DataSource dataSource;
	private final AsyncTaskExecutor executor;

	public DataSourceRxSessionProvider(DataSource dataSource, AsyncTaskExecutor executor) {
		this.dataSource = dataSource;
		this.executor = executor;
		dbType = getDBType(dataSource);
		logger.info("DB type is {}", dbType);
	}

	public DataSourceRxSessionProvider(DataSource dataSource, AsyncTaskExecutor executor, DBType dbType) {
		this.dataSource = dataSource;
		this.executor = executor;
		this.dbType = dbType;
		logger.info("DB type is {}", dbType);
	}

	@Override
	public DBType getDBType() {
		return dbType;
	}

	private DBType getDBType(DataSource dataSource) {
		DBTypeDescription dbTypeDescription = DBTypeDescription.build(dataSource);
		DBType dbType = dbTypeDescription.getDBType();
		logger.info("DB username: {}", dbTypeDescription.getUsername());
		logger.info("DB driver name: {}", dbTypeDescription.getDriverName());
		logger.info("DB driver version: {}", dbTypeDescription.getDriverVersion());
		logger.info("DB url: {}", dbTypeDescription.getUrl());
		logger.info("DB product name: {}", dbTypeDescription.getDatabaseProductName());
		logger.info("DB product version: {}", dbTypeDescription.getDatabaseProductVersion());
		return dbType;
	}

	@Override
	public CompletableFuture<Connection> getConnection(boolean autoCommit) {
		return executor.execute(() -> {
			try {
				logger.debug("getting new connection");
				java.sql.Connection connection = dataSource.getConnection();
				connection.setAutoCommit(autoCommit);
				return new DatasourceConnection(connection, executor);
			} catch (SQLException e) {
				throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("getConnection", "", e);
			}
		});

	}

}
