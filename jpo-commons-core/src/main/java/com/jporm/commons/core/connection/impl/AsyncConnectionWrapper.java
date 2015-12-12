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
package com.jporm.commons.core.connection.impl;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.connection.AsyncConnection;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.commons.core.util.CompletableFutureUtils;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public class AsyncConnectionWrapper implements AsyncConnection {

    private final com.jporm.commons.core.connection.Connection rmConnection;
    private final AsyncTaskExecutor executor;

    public AsyncConnectionWrapper(final com.jporm.commons.core.connection.Connection rmConnection, final AsyncTaskExecutor executor) {
        this.rmConnection = rmConnection;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<int[]> batchUpdate(final Collection<String> sqls) {
        return executor.execute(() -> {
            return rmConnection.batchUpdate(sqls);
        });
    }

    @Override
    public CompletableFuture<int[]> batchUpdate(final String sql, final BatchPreparedStatementSetter psc) {
        return executor.execute(() -> {
            return rmConnection.batchUpdate(sql, psc);
        });
    }

    @Override
    public CompletableFuture<int[]> batchUpdate(final String sql, final Collection<StatementSetter> args) {
        return executor.execute(() -> {
            return rmConnection.batchUpdate(sql, args);
        });
    }

    @Override
    public CompletableFuture<Void> close() {
        return CompletableFutureUtils.toCompletableFuture(() -> rmConnection.close());
    }

    @Override
    public CompletableFuture<Void> commit() {
        return CompletableFutureUtils.toCompletableFuture(() -> rmConnection.commit());
    }

    @Override
    public CompletableFuture<Void> execute(final String sql) {
        return executor.execute(() -> {
            rmConnection.execute(sql);
        });
    }

    @Override
    public <T> CompletableFuture<T> query(final String sql, final StatementSetter pss, final ResultSetReader<T> rse) {
        return executor.execute(() -> {
            return rmConnection.query(sql, pss, rse);
        });
    }

    @Override
    public CompletableFuture<Void> rollback() {
        return CompletableFutureUtils.toCompletableFuture(() -> rmConnection.rollback());
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        rmConnection.setReadOnly(readOnly);
    }

    @Override
    public void setTimeout(final int timeout) {
        rmConnection.setTimeout(timeout);
    }

    @Override
    public void setTransactionIsolation(final TransactionIsolation isolation) {
        rmConnection.setTransactionIsolation(isolation);
    }

    @Override
    public CompletableFuture<Integer> update(final String sql, final GeneratedKeyReader generatedKeyReader, final StatementSetter pss) {
        return executor.execute(() -> {
            return rmConnection.update(sql, generatedKeyReader, pss);
        });
    }

}
