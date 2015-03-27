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
package com.jporm.rx.vertx.session.vertx3.datasource;

import io.vertx.core.Vertx;
import io.vertx.ext.jdbc.JdbcService;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.rx.core.connection.Connection;
import com.jporm.rx.core.session.ConnectionProvider;
import com.jporm.sql.dialect.DBType;

public class Vertx3RxSessionProvider implements ConnectionProvider {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private DBType dbType;
	private final DataSource dataSource;
	private Vertx vertx;

	public Vertx3RxSessionProvider(DataSource dataSource, Vertx vertx) {
		this(dataSource, vertx, null);
	}

	public Vertx3RxSessionProvider(DataSource dataSource, Vertx vertx, DBType dbType) {
		this.dataSource = dataSource;
		this.vertx = vertx;
		setDBType(dbType);
	}

	private void setDBType(DBType dbType) {
		if (dbType!=null) {
			this.dbType = dbType;
			logger.info("DB type is {}", dbType);
		}
	}

	private DBType getDBType(java.sql.Connection jdbcConnection) {
		if(dbType==null) {
			synchronized (this) {
				if(dbType==null) {
					try {
						DBTypeDescription dbTypeDescription = DBTypeDescription.build(jdbcConnection.getMetaData());
						setDBType(dbTypeDescription.getDBType());
						logger.info("DB username: {}", dbTypeDescription.getUsername());
						logger.info("DB driver name: {}", dbTypeDescription.getDriverName());
						logger.info("DB driver version: {}", dbTypeDescription.getDriverVersion());
						logger.info("DB url: {}", dbTypeDescription.getUrl());
						logger.info("DB product name: {}", dbTypeDescription.getDatabaseProductName());
						logger.info("DB product version: {}", dbTypeDescription.getDatabaseProductVersion());
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return dbType;
	}

	@Override
	public CompletableFuture<Connection> getConnection(boolean autoCommit) {
		CompletableFuture<Connection> connection = new CompletableFuture<>();
		jdbcService.getConnection(handler -> {
			if (handler.succeeded()) {
				handler.result().setAutoCommit(true, autoCommitHandler -> {
					if (autoCommitHandler.succeeded()) {
						connection.complete(new Vertx3Connection(handler.result()));
					} else {
						connection.completeExceptionally(handler.cause());
					}
				});
			} else {
				connection.completeExceptionally(handler.cause());
			}
		});
		return connection;
	}

}
