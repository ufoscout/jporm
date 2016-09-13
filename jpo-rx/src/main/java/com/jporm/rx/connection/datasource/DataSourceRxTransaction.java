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

import java.util.function.Function;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.inject.config.ConfigService;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.connection.CompletableFunction;
import com.jporm.rx.connection.ObservableFunction;
import com.jporm.rx.connection.RxConnection;
import com.jporm.rx.connection.RxConnectionProvider;
import com.jporm.rx.connection.RxTransaction;
import com.jporm.rx.connection.SingleFunction;
import com.jporm.rx.session.Session;
import com.jporm.rx.session.SessionImpl;
import com.jporm.sql.dialect.DBProfile;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class DataSourceRxTransaction implements RxTransaction {

    private final ServiceCatalog serviceCatalog;
    private final SqlCache sqlCache;
    private final SqlFactory sqlFactory;
    private final DBProfile dbProfile;
    private final RxConnectionProvider<DataSourceRxConnection> connectionProvider;

    private TransactionIsolation transactionIsolation;
    private int timeout;
    private boolean readOnly = false;

    public DataSourceRxTransaction(final ServiceCatalog serviceCatalog, DBProfile dbProfile, SqlCache sqlCache, SqlFactory sqlFactory,
            RxConnectionProvider<DataSourceRxConnection> connectionProvider) {
        this.serviceCatalog = serviceCatalog;
        this.dbProfile = dbProfile;
        this.sqlCache = sqlCache;
        this.sqlFactory = sqlFactory;
        this.connectionProvider = connectionProvider;

        ConfigService configService = serviceCatalog.getConfigService();
        transactionIsolation = configService.getDefaultTransactionIsolation();
        timeout = configService.getTransactionDefaultTimeoutSeconds();

    }

    @Override
    public <T> Observable<T> execute(ObservableFunction<T> txSession) {
        return connectionProvider.getConnection(false, rxConnection -> {
            setTransactionIsolation(rxConnection);
            setTimeout(rxConnection);
            rxConnection.setReadOnly(readOnly);
            Session session = new SessionImpl(serviceCatalog, dbProfile, new RxConnectionProvider<RxConnection>() {
                @Override
                public <R> Observable<R> getConnection(boolean autoCommit, Function<RxConnection, Observable<R>> connection) {
                    return connection.apply(rxConnection);
                }
            }, sqlCache, sqlFactory);

            try {
                return txSession.apply(session)
                .doOnCompleted(() -> {
                    if (!readOnly) {
                        rxConnection.commit();
                    } else {
                        rxConnection.rollback();
                    }
                }).doOnError(e -> {
                    rxConnection.rollback();
                });
            } catch (RuntimeException e) {
                rxConnection.rollback();
                throw e;
            } catch (Throwable e) {
                rxConnection.rollback();
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

}
