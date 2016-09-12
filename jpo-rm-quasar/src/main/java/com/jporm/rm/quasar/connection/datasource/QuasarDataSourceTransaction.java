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
package com.jporm.rm.quasar.connection.datasource;

import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.rm.connection.AbstractTransaction;
import com.jporm.rm.connection.Connection;
import com.jporm.rm.connection.datasource.DataSourceConnection;
import com.jporm.rm.quasar.connection.JpoCompletableWrapper;
import com.jporm.rm.session.Session;
import com.jporm.sql.dialect.DBProfile;

public class QuasarDataSourceTransaction extends AbstractTransaction {

    private final static Logger LOGGER = LoggerFactory.getLogger(QuasarDataSourceTransaction.class);
    private final DataSource dataSource;
    private final AsyncTaskExecutor connectionExecutor;
    private final AsyncTaskExecutor executor;

    public QuasarDataSourceTransaction(final ServiceCatalog serviceCatalog, DBProfile dbProfile, SqlCache sqlCache, SqlFactory sqlFactory,
            DataSource dataSource, AsyncTaskExecutor connectionExecutor, AsyncTaskExecutor executor) {
        super(serviceCatalog, dbProfile, sqlCache, sqlFactory);
        this.dataSource = dataSource;
        this.connectionExecutor = connectionExecutor;
        this.executor = executor;
    }

    @Override
    public <T> T execute(final Function<Session, T> callback) {
        java.sql.Connection sqlConnection = null;
        try {
            sqlConnection = getSqlConnection();
            sqlConnection.setAutoCommit(false);

            Connection connection = new QuasarDataSourceConnection(new DataSourceConnection(sqlConnection, getDbProfile()), executor);
            setTransactionIsolation(connection);
            setTimeout(connection, getTimeout());
            connection.setReadOnly(isReadOnly());
            Session session = newSession(connection);

            T result = callback.apply(session);
            if (!isReadOnly()) {
                commit(sqlConnection);
            } else {
                rollback(sqlConnection);
            }
            return result;
        } catch (RuntimeException e) {
            rollback(sqlConnection);
            throw e;
        } catch (Throwable e) {
            rollback(sqlConnection);
            throw new RuntimeException(e);
        } finally {
            close(sqlConnection);
        }
    }

    @Override
    public void executeVoid(final Consumer<Session> callback) {
        execute((session) -> {
            callback.accept(session);
            return null;
        });
    }

    private void setTimeout(final Connection connection, int timeout) {
        if (timeout >= 0) {
            connection.setTimeout(timeout);
        }
    }

    private void setTransactionIsolation(final Connection connection) {
        connection.setTransactionIsolation(getTransactionIsolation());
    }

    private java.sql.Connection getSqlConnection() throws SQLException {
        return JpoCompletableWrapper.get(connectionExecutor.execute(() -> {
            try {
                LOGGER.debug("Open Connection");
                return dataSource.getConnection();
            } catch (SQLException e) {
                throw DataSourceConnection.translateException("close", "", e);
            }
        }));
    }

    private void close(java.sql.Connection connection) {
        JpoCompletableWrapper.get(executor.execute(() -> {
            if (connection != null) {
                try {
                    LOGGER.debug("Connection close");
                    connection.close();
                } catch (SQLException e) {
                    throw DataSourceConnection.translateException("close", "", e);
                }
            }
        }));
    }

    private void commit(java.sql.Connection connection) {
        JpoCompletableWrapper.get(executor.execute(() -> {
            if (connection != null) {
                try {
                    LOGGER.debug("Connection commit");
                    connection.commit();
                } catch (SQLException e) {
                    throw DataSourceConnection.translateException("commit", "", e);
                }
            }
        }));
    }

    private void rollback(java.sql.Connection connection) {
        JpoCompletableWrapper.get(executor.execute(() -> {
            if (connection != null) {
                try {
                    LOGGER.debug("Connection rollback");
                    connection.rollback();
                } catch (SQLException e) {
                    throw DataSourceConnection.translateException("rollback", "", e);
                }
            }
        }));
    }

}