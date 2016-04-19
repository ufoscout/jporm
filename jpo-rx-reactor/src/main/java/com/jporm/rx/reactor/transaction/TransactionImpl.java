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

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.connection.AsyncConnection;
import com.jporm.commons.core.connection.AsyncConnectionProvider;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.inject.config.ConfigService;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.commons.core.util.AsyncConnectionUtils;
import com.jporm.rx.reactor.session.Session;
import com.jporm.rx.reactor.session.SessionImpl;

public class TransactionImpl implements Transaction {

    private final static Logger LOGGER = LoggerFactory.getLogger(TransactionImpl.class);
    private final AsyncConnectionProvider connectionProvider;
    private final ServiceCatalog serviceCatalog;
    private final SqlCache sqlCache;
    private final SqlFactory sqlFactory;

    private TransactionIsolation transactionIsolation;
    private int timeout;
    private boolean readOnly = false;

    public TransactionImpl(final ServiceCatalog serviceCatalog, final AsyncConnectionProvider connectionProvider, SqlCache sqlCache, SqlFactory sqlFactory) {
        this.serviceCatalog = serviceCatalog;
        this.connectionProvider = connectionProvider;
        this.sqlCache = sqlCache;
        this.sqlFactory = sqlFactory;

        ConfigService configService = serviceCatalog.getConfigService();
        transactionIsolation = configService.getDefaultTransactionIsolation();
        timeout = configService.getTransactionDefaultTimeoutSeconds();

    }

    @Override
    public <T> CompletableFuture<T> execute(final Function<Session, CompletableFuture<T>> txSession) {
        return connectionProvider.getConnection(false).thenCompose(connection -> {
            try {
                setTransactionIsolation(connection);
                setTimeout(connection);
                connection.setReadOnly(readOnly);
                LOGGER.debug("Start new transaction");
                Session session = new SessionImpl(serviceCatalog, new TransactionalConnectionProviderDecorator(connection, connectionProvider), false, sqlCache, sqlFactory);
                CompletableFuture<T> result = txSession.apply(session);
                CompletableFuture<T> committedResult = AsyncConnectionUtils.commitOrRollback(readOnly, result, connection);
                return AsyncConnectionUtils.close(committedResult, connection);
            } catch (Throwable e) {
                LOGGER.error("Error during transaction execution");
                connection.rollback().whenComplete((obj, ex) -> {
                    connection.close();
                });
                throw e;
            }
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

    private void setTimeout(final AsyncConnection connection) {
        if (timeout > 0) {
            connection.setTimeout(timeout);
        }
    }

    private void setTransactionIsolation(final AsyncConnection connection) {
        connection.setTransactionIsolation(transactionIsolation);
    }

    @Override
    public Transaction timeout(final int seconds) {
        timeout = seconds;
        return this;
    }

}
