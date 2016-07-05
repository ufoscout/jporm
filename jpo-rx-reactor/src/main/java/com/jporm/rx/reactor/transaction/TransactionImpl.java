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
package com.jporm.rx.reactor.transaction;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.inject.config.ConfigService;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.reactor.connection.CloseConnectionStrategy;
import com.jporm.rx.reactor.connection.CloseConnectionStrategyFullImpl;
import com.jporm.rx.reactor.connection.CloseConnectionStrategyNoOps;
import com.jporm.rx.reactor.connection.RxConnection;
import com.jporm.rx.reactor.connection.RxConnectionProvider;
import com.jporm.rx.reactor.session.Session;
import com.jporm.rx.reactor.session.SessionImpl;

import rx.Observable;

public class TransactionImpl implements Transaction {

    private final static Logger LOGGER = LoggerFactory.getLogger(TransactionImpl.class);
    private final static CloseConnectionStrategy CONN_STRATEGY = new CloseConnectionStrategyFullImpl();
    private final static CloseConnectionStrategy SESSION_CONN_STRATEGY = new CloseConnectionStrategyNoOps();

    private static final BiFunction<TransactionImpl, RxConnection, Session> DEFAULT_SESSION_PROVIDER =
            (TransactionImpl tx, RxConnection connection) -> {
                return new SessionImpl(tx.serviceCatalog,
                        new SingleRxConnectionProvider(connection, tx.connectionProvider), SESSION_CONN_STRATEGY, tx.sqlCache, tx.sqlFactory);
            };

    private final RxConnectionProvider connectionProvider;
    private final ServiceCatalog serviceCatalog;
    private final SqlCache sqlCache;
    private final SqlFactory sqlFactory;
    private BiFunction<TransactionImpl, RxConnection, Session> sessionProvider = DEFAULT_SESSION_PROVIDER;

    private TransactionIsolation transactionIsolation;
    private int timeout;
    private boolean readOnly = false;

    public TransactionImpl(final ServiceCatalog serviceCatalog, final RxConnectionProvider connectionProvider, SqlCache sqlCache, SqlFactory sqlFactory) {
        this.serviceCatalog = serviceCatalog;
        this.connectionProvider = connectionProvider;
        this.sqlCache = sqlCache;
        this.sqlFactory = sqlFactory;

        ConfigService configService = serviceCatalog.getConfigService();
        transactionIsolation = configService.getDefaultTransactionIsolation();
        timeout = configService.getTransactionDefaultTimeoutSeconds();

    }

    @Override
    public <T> Observable<T> execute(Function<Session, Observable<T>> txSession) {
        return connectionProvider.getConnection(false)
                .flatMapObservable(connection -> {
                    return CONN_STRATEGY.autoClose(connection, conn -> {
                        LOGGER.debug("Start new transaction");
                        setTransactionIsolation(conn);
                        setTimeout(conn);
                        conn.setReadOnly(readOnly);
                        Session session = sessionProvider.apply(this, conn);
                        return CONN_STRATEGY.commitOrRollback(txSession.apply(session), conn, readOnly);
                    });
                });
    }

    @Override
    public Transaction isolation(final TransactionIsolation isolation) {
        transactionIsolation = isolation;
        return this;
    }

    @Override
    public Transaction readOnly(final boolean readOnly) {
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
    public Transaction timeout(final int seconds) {
        timeout = seconds;
        return this;
    }

    /**
     * @param sessionProvider the sessionProvider to set
     */
    void setSessionProvider(BiFunction<TransactionImpl, RxConnection, Session> sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

}
