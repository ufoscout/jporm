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
package com.jporm.rx.vertx.session.vertx3.sqlservice;

import io.vertx.ext.jdbc.JdbcService;

import java.util.concurrent.CompletableFuture;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.rx.connection.Connection;
import com.jporm.rx.session.ConnectionProvider;
import com.jporm.sql.dialect.DBType;

public class Vertx3RxSessionProvider implements ConnectionProvider {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final DBType dbType;
	private final JdbcService jdbcService;

	/**
	 * Create a {@link Vertx3RxSessionProvider} provider based on a vertx {@link JdbcService}.
	 * The database dialect is automatically detected from the datasource (note that this is a blocking action that is mandatory
	 * to initialize Jpo).
	 * @param jdbcService
	 * @param dbType the database type needed to set the correct dialect
	 */
	public Vertx3RxSessionProvider(JdbcService jdbcService, DataSource dataSource) {
		this.jdbcService = jdbcService;
		dbType = getDBType(dataSource);
		logger.info("DB type is {}", dbType);
	}

	/**
	 * Create a {@link Vertx3RxSessionProvider} provider based on a vertx {@link JdbcService}.
	 * The database dialect is specified by the dbType parameter.
	 * @param jdbcService
	 * @param dbType the database type needed to set the correct dialect
	 */
	public Vertx3RxSessionProvider(JdbcService jdbcService, DBType dbType) {
		this.jdbcService = jdbcService;
		this.dbType = dbType;
		logger.info("DB type is {}", dbType);
	}

	@Override
	public CompletableFuture<DBType> getDBType() {
		return null;
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
