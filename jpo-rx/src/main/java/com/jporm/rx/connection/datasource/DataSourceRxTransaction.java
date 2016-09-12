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
package com.jporm.rx.connection.datasource;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.inject.config.ConfigService;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rm.connection.datasource.DataSourceConnection;
import com.jporm.rx.connection.CompletableFunction;
import com.jporm.rx.connection.ObservableFunction;
import com.jporm.rx.connection.RxConnection;
import com.jporm.rx.connection.RxTransaction;
import com.jporm.rx.connection.SingleFunction;
import com.jporm.rx.session.Session;
import com.jporm.rx.session.SessionImpl;
import com.jporm.sql.dialect.DBProfile;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class DataSourceRxTransaction implements RxTransaction {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceRxTransaction.class);

    private final ServiceCatalog serviceCatalog;
    private final SqlCache sqlCache;
    private final SqlFactory sqlFactory;
    private final DataSource dataSource;
    private final DBProfile dbProfile;

    private TransactionIsolation transactionIsolation;
    private int timeout;
    private boolean readOnly = false;


    public DataSourceRxTransaction(final ServiceCatalog serviceCatalog, DBProfile dbProfile, SqlCache sqlCache, SqlFactory sqlFactory, DataSource dataSource) {
        this.serviceCatalog = serviceCatalog;
        this.dbProfile = dbProfile;
        this.sqlCache = sqlCache;
        this.sqlFactory = sqlFactory;
        this.dataSource = dataSource;

        ConfigService configService = serviceCatalog.getConfigService();
        transactionIsolation = configService.getDefaultTransactionIsolation();
        timeout = configService.getTransactionDefaultTimeoutSeconds();

    }

    @Override
    public <T> Observable<T> execute(ObservableFunction<T> txSession) {
        return Observable.fromCallable(() -> {
            try {
                java.sql.Connection sqlConnection = dataSource.getConnection();
                sqlConnection.setAutoCommit(false);
                return sqlConnection;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }).flatMap(sqlConnection -> {
            RxConnection connection = new DataSourceRxConnection(new DataSourceConnection(sqlConnection, dbProfile));
            setTransactionIsolation(connection);
            setTimeout(connection);
            connection.setReadOnly(readOnly);
            Session session = new SessionImpl(serviceCatalog, dbProfile, connection, sqlCache, sqlFactory);

            try {
            return txSession.apply(session)
            .doOnCompleted(() -> {
                try {
                    if (!readOnly) {
                        commit(sqlConnection);
                    } else {
                        rollback(sqlConnection);
                    }
                } finally {
                    close(sqlConnection);
                }
            })
            .doOnError(e -> {
                try {
                    rollback(sqlConnection);
                } finally {
                    close(sqlConnection);
                }
            });
            } catch (Throwable e) {
                try {
                    rollback(sqlConnection);
                } finally {
                    close(sqlConnection);
                }
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public RxTransaction isolation(final TransactionIsolation isolation) {
        transactionIsolation = isolation;
        return this;
    }

    @Override
    public RxTransaction readOnly(final boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    private void setTimeout(final RxConnection connection) {
        if (timeout > 0) {
            connection.setTimeout(timeout);
        }
    }

    private void setTransactionIsolation(final RxConnection connection) {
        connection.setTransactionIsolation(transactionIsolation);
    }

    @Override
    public RxTransaction timeout(final int seconds) {
        timeout = seconds;
        return this;
    }

    @Override
    public <T> Single<T> execute(SingleFunction<T> txSession) {
        return execute((Session session) -> {
            return txSession.apply(session).toObservable();
        }).toSingle();
    }

    @Override
    public Completable execute(CompletableFunction txSession) {
        return execute((Session session) -> {
            return txSession.apply(session).toObservable();
        }).toCompletable();
    }

    private void close(java.sql.Connection connection) {
        if (connection != null) {
            try {
                LOGGER.debug("Connection close");
                connection.close();
            } catch (SQLException e) {
                throw DataSourceConnection.translateException("close", "", e);
            }
        }
    }

    private void commit(java.sql.Connection connection) {
        if (connection != null) {
            try {
                LOGGER.debug("Connection commit");
                connection.commit();
            } catch (SQLException e) {
                throw DataSourceConnection.translateException("commit", "", e);
            }
        }
    }

    private void rollback(java.sql.Connection connection) {
        if (connection != null) {
            try {
                LOGGER.debug("Connection rollback");
                connection.rollback();
            } catch (SQLException e) {
                throw DataSourceConnection.translateException("rollback", "", e);
            }
        }
    }


}
