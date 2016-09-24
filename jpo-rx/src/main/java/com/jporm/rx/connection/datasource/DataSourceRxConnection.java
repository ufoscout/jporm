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

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rm.connection.datasource.DataSourceConnection;
import com.jporm.rx.connection.RxConnection;
import com.jporm.rx.util.Futures;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.Statement;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class DataSourceRxConnection implements RxConnection {

    private final DataSourceConnection rmConnection;
    private final AsyncTaskExecutor executor;

    public DataSourceRxConnection(final DataSourceConnection rmConnection, AsyncTaskExecutor executor) {
        this.rmConnection = rmConnection;
        this.executor = executor;
    }

    @Override
    public Single<int[]> batchUpdate(final Collection<String> sqls, Function<String, String> sqlPreProcessor) {
        return Futures.toSingle(executor, () -> {
            return rmConnection.batchUpdate(sqls, sqlPreProcessor);
        });

    }

    @Override
    public Single<int[]> batchUpdate(final String sql, final BatchPreparedStatementSetter psc) {
        return Futures.toSingle(executor, () -> {
            return rmConnection.batchUpdate(sql, psc);
        })
        ;
    }

    @Override
    public Single<int[]> batchUpdate(final String sql, final Collection<Consumer<Statement>> args) {
        return Futures.toSingle(executor, () -> {
            return rmConnection.batchUpdate(sql, args);
        })
        ;
    }

    @Override
    public Completable execute(final String sql) {
        return Futures.toCompletable(executor, () -> {
            rmConnection.execute(sql);
        });
    }

    @Override
    public <T> Observable<T> query(final String sql, final Consumer<Statement> pss, final IntBiFunction<ResultEntry, T> rse) {
        return Observable.<T>create(onSubscribe -> {
            executor.execute(() -> {
                rmConnection.query(sql, pss, rs -> {
                    int count = 0;
                    while (rs.hasNext()) {
                        onSubscribe.onNext(rse.apply(rs.next(), count++));
                    }
                    return null;
                });
                onSubscribe.onCompleted();
            });
        })
        ;
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
    public <R> Single<R> update(final String sql, final GeneratedKeyReader<R> generatedKeyReader, final Consumer<Statement> pss) {
        return Futures.toSingle(executor, () -> {
            return rmConnection.update(sql, generatedKeyReader, pss);
        })
        ;
    }

    @Override
    public Single<Integer> update(String sql, Consumer<Statement> pss) {
        return Futures.toSingle(executor, () -> {
            return rmConnection.update(sql, pss);
        })
        ;
    }

}
