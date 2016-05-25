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
package com.jporm.rx.reactor.connection;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

import rx.Completable;
import rx.Observable;
import rx.Scheduler;

public class RxConnectionWrapper implements RxConnection {

    private final com.jporm.commons.core.connection.Connection rmConnection;
    private final Scheduler executionScheduler;

    public RxConnectionWrapper(final com.jporm.commons.core.connection.Connection rmConnection, Scheduler executionScheduler) {
        this.rmConnection = rmConnection;
        this.executionScheduler = executionScheduler;
    }

    @Override
    public Observable<int[]> batchUpdate(final Collection<String> sqls, Function<String, String> sqlPreProcessor) {
        return Observable.fromCallable(() -> {
            return rmConnection.batchUpdate(sqls, sqlPreProcessor);
        })
        .subscribeOn(executionScheduler);

    }

    @Override
    public Observable<int[]> batchUpdate(final String sql, final BatchPreparedStatementSetter psc) {
        return Observable.fromCallable(() -> {
            return rmConnection.batchUpdate(sql, psc);
        })
        .subscribeOn(executionScheduler);
    }

    @Override
    public Observable<int[]> batchUpdate(final String sql, final Collection<Consumer<Statement>> args) {
        return Observable.fromCallable(() -> {
            return rmConnection.batchUpdate(sql, args);
        })
        .subscribeOn(executionScheduler);
    }

    @Override
    public Completable close() {
        return Completable.fromAction(() -> {
            rmConnection.close();
        });
    }

    @Override
    public Completable commit() {
        return Completable.fromAction(() -> {
            rmConnection.commit();
        });
    }

    @Override
    public Completable execute(final String sql) {
        return Completable.fromAction(() -> {
            rmConnection.execute(sql);
        })
        .subscribeOn(executionScheduler);
    }

    @Override
    public <T> Observable<T> query(final String sql, final Consumer<Statement> pss, final Function<ResultSet, T> rse) {
        return Observable.fromCallable(() -> {
            return rmConnection.query(sql, pss, rse);
        })
        .subscribeOn(executionScheduler);
    }

    @Override
    public Completable rollback() {
        return Completable.fromAction(() -> {
            rmConnection.rollback();
        });
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
    public <R> Observable<R> update(final String sql, final GeneratedKeyReader<R> generatedKeyReader, final Consumer<Statement> pss) {
        return Observable.fromCallable(() -> {
            return rmConnection.update(sql, generatedKeyReader, pss);
        })
        .subscribeOn(executionScheduler);
    }

    @Override
    public Observable<Integer> update(String sql, Consumer<Statement> pss) {
        return Observable.fromCallable(() -> {
            return rmConnection.update(sql, pss);
        })
        .subscribeOn(executionScheduler);
    }

}
