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

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.reactor.connection.RxConnection;
import com.jporm.rx.reactor.connection.RxConnectionProvider;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

import rx.Completable;
import rx.Observable;

public class TransactionalRxConnectionProviderDecorator implements RxConnectionProvider {

    private final RxConnection connection;
    private final RxConnectionProvider connectionProvider;

    public TransactionalRxConnectionProviderDecorator(final RxConnection connection, final RxConnectionProvider connectionProvider) {
        this.connection = connection;
        this.connectionProvider = connectionProvider;

    }

    @Override
    public Observable<RxConnection> getConnection(final boolean autoCommit) {
        return Observable.just(new RxConnection() {

            @Override
            public Observable<int[]> batchUpdate(final Collection<String> sqls, Function<String, String> sqlPreProcessor) {
                return connection.batchUpdate(sqls, sqlPreProcessor);
            }

            @Override
            public Observable<int[]> batchUpdate(final String sql, final BatchPreparedStatementSetter psc) {
                return connection.batchUpdate(sql, psc);
            }

            @Override
            public Observable<int[]> batchUpdate(final String sql, final Collection<Consumer<Statement>> args) {
                return connection.batchUpdate(sql, args);
            }

            @Override
            public Completable close() {
                return Completable.complete();
            }

            @Override
            public Completable commit() {
                return Completable.complete();
            }

            @Override
            public Completable execute(final String sql) {
                return connection.execute(sql);
            }

            @Override
            public <T> Observable<T> query(final String sql, final Consumer<Statement> pss, final Function<ResultSet, T> rse) {
                return connection.query(sql, pss, rse);
            }

            @Override
            public Completable rollback() {
                return Completable.complete();
            }

            @Override
            public void setReadOnly(final boolean readOnly) {
            }

            @Override
            public void setTimeout(final int timeout) {
                connection.setTimeout(timeout);
            }

            @Override
            public void setTransactionIsolation(final TransactionIsolation isolation) {
                connection.setTransactionIsolation(isolation);
            }

            @Override
            public <R> Observable<R> update(final String sql, final GeneratedKeyReader<R> generatedKeyReader, final Consumer<Statement> pss) {
                return connection.update(sql, generatedKeyReader, pss);
            }

            @Override
            public Observable<Integer> update(final String sql, final Consumer<Statement> pss) {
                return connection.update(sql, pss);
            }
        });
    }

    @Override
    public DBProfile getDBProfile() {
        return connectionProvider.getDBProfile();
    }

}
