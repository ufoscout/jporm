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
package com.jporm.rx.vertx.session.vertx3.jdbcclient;

import java.util.concurrent.CompletableFuture;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.connection.AsyncConnection;
import com.jporm.commons.core.connection.AsyncConnectionProvider;
import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.sql.dsl.dialect.DBType;

import io.vertx.ext.jdbc.JDBCClient;

public class Vertx3JdbcClientSessionProvider implements AsyncConnectionProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DBType dbType;
    private final JDBCClient jdbcService;

    /**
     * Create a {@link Vertx3JdbcClientSessionProvider} provider based on a
     * vertx {@link JdbcService}. The database dialect is automatically detected
     * from the datasource.
     * 
     * @param jdbcService
     * @param dbType
     *            the database type needed to set the correct dialect
     */
    public Vertx3JdbcClientSessionProvider(final JDBCClient jdbcService, final DataSource dataSource) {
        this.jdbcService = jdbcService;
        dbType = getDBType(dataSource);
        logger.info("DB type is {}", dbType);
    }

    /**
     * Create a {@link Vertx3JdbcClientSessionProvider} provider based on a
     * vertx {@link JdbcService}. The database dialect is specified by the
     * dbType parameter.
     * 
     * @param jdbcService
     * @param dbType
     *            the database type needed to set the correct dialect
     */
    public Vertx3JdbcClientSessionProvider(final JDBCClient jdbcService, final DBType dbType) {
        this.jdbcService = jdbcService;
        this.dbType = dbType;
        logger.info("DB type is {}", dbType);
    }

    @Override
    public CompletableFuture<AsyncConnection> getConnection(final boolean autoCommit) {
        CompletableFuture<AsyncConnection> connection = new CompletableFuture<>();
        jdbcService.getConnection(handler -> {
            if (handler.succeeded()) {
                handler.result().setAutoCommit(true, autoCommitHandler -> {
                    if (autoCommitHandler.succeeded()) {
                        connection.complete(new Vertx3AsyncConnection(handler.result()));
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

    @Override
    public CompletableFuture<DBType> getDBType() {
        return CompletableFuture.completedFuture(dbType);
    }

    private DBType getDBType(final DataSource dataSource) {
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

}
