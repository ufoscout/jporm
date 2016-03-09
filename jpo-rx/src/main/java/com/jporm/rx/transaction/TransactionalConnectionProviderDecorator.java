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
package com.jporm.rx.transaction;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.connection.AsyncConnection;
import com.jporm.commons.core.connection.AsyncConnectionProvider;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public class TransactionalConnectionProviderDecorator implements AsyncConnectionProvider {

    private final AsyncConnection connection;
    private final AsyncConnectionProvider connectionProvider;

    public TransactionalConnectionProviderDecorator(final AsyncConnection connection, final AsyncConnectionProvider connectionProvider) {
        this.connection = connection;
        this.connectionProvider = connectionProvider;

    }

    @Override
    public CompletableFuture<AsyncConnection> getConnection(final boolean autoCommit) {
        return CompletableFuture.completedFuture(new AsyncConnection() {

            @Override
            public CompletableFuture<int[]> batchUpdate(final Collection<String> sqls) {
                return connection.batchUpdate(sqls);
            }

            @Override
            public CompletableFuture<int[]> batchUpdate(final String sql, final BatchPreparedStatementSetter psc) {
                return connection.batchUpdate(sql, psc);
            }

            @Override
            public CompletableFuture<int[]> batchUpdate(final String sql, final Collection<StatementSetter> args) {
                return connection.batchUpdate(sql, args);
            }

            @Override
            public CompletableFuture<Void> close() {
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletableFuture<Void> commit() {
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletableFuture<Void> execute(final String sql) {
                return connection.execute(sql);
            }

            @Override
            public <T> CompletableFuture<T> query(final String sql, final StatementSetter pss, final ResultSetReader<T> rse) {
                return connection.query(sql, pss, rse);
            }

            @Override
            public CompletableFuture<Void> rollback() {
                return CompletableFuture.completedFuture(null);
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
            public <R> CompletableFuture<R> update(final String sql, final GeneratedKeyReader<R> generatedKeyReader, final StatementSetter pss) {
                return connection.update(sql, generatedKeyReader, pss);
            }

            @Override
            public CompletableFuture<Integer> update(final String sql, final StatementSetter pss) {
                return connection.update(sql, pss);
            }
        });
    }

    @Override
    public DBProfile getDBProfile() {
        return connectionProvider.getDBProfile();
    }

}
