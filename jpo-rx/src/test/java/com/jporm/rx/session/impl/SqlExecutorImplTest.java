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
package com.jporm.rx.session.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.BaseTestApi;
import com.jporm.rx.connection.RxConnection;
import com.jporm.rx.connection.RxConnectionProvider;
import com.jporm.rx.query.update.UpdateResult;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

public class SqlExecutorImplTest extends BaseTestApi {

    class ConnectionTestImpl implements RxConnection {

        @Override
        public Single<int[]> batchUpdate(final Collection<String> sqls, Function<String, String> sqlPreProcessor) {
            return null;
        }

        @Override
        public Single<int[]> batchUpdate(final String sql, final BatchPreparedStatementSetter psc) {
            return null;
        }

        @Override
        public Single<int[]> batchUpdate(final String sql, final Collection<Consumer<Statement>> args) {
            return null;
        }

        @Override
        public Completable execute(final String sql) {
            return null;
        }

        @Override
        public <T> Observable<T> query(final String sql, final Consumer<Statement> pss, final IntBiFunction<ResultEntry, T> rse) {
            return Observable.fromCallable(() -> rse.apply(null, 0)).subscribeOn(Schedulers.newThread());
        }

        @Override
        public void setReadOnly(final boolean readOnly) {
        }

        @Override
        public void setTimeout(final int timeout) {
        }

        @Override
        public void setTransactionIsolation(final TransactionIsolation isolation) {
        }

        @Override
        public Single<Integer> update(final String sql, final Consumer<Statement> pss) {
            return Single.fromCallable(() -> 0).subscribeOn(Schedulers.newThread());
        }

        @Override
        public <R> Single<R> update(final String sql, final GeneratedKeyReader<R> generatedKeyReader, final Consumer<Statement> pss) {
            return Single.fromCallable(() ->
                generatedKeyReader.read(null, 0)
            ).subscribeOn(Schedulers.newThread());
        }

    }

    private ConnectionTestImpl conn = new ConnectionTestImpl();

    private SqlExecutor sqlExecutor;

    @Test
    public void connection_should_be_closed_after_query_exception() throws JpoException, InterruptedException, ExecutionException {

        TestObserver<Object> subscriber = new TestObserver<>();

        sqlExecutor.query("", new ArrayList<>(), (ResultEntry rsr, int count) -> {
            getLogger().info("Throwing exception");
            throw new RuntimeException("exception during query execution");
        })
        .subscribe(subscriber);

        subscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);

    }

    @Test
    public void connection_should_be_closed_after_query_execution() throws JpoException, InterruptedException, ExecutionException {
        Observable<String> result = sqlExecutor.query("", new ArrayList<>(), (rsr, count) -> {
            return "helloWorld";
        });

        result.subscribe(text -> getLogger().info("next"), e -> getLogger().info("error"), () -> getLogger().info("complete"));

        assertEquals("helloWorld", result.lastElement().blockingGet());
    }

    @Test
    public void connection_should_be_closed_after_update_exception() throws JpoException, InterruptedException, ExecutionException {
        Single<UpdateResult> future = sqlExecutor.update("", new ArrayList<>(), new GeneratedKeyReader<UpdateResult>() {
            @Override
            public String[] generatedColumnNames() {
                return new String[0];
            }

            @Override
            public UpdateResult read(final ResultSet generatedKeyResultSet, int affectedRows) {
                throw new RuntimeException("exception during query execution");
            }
        });

        TestObserver<UpdateResult> subscriber = new TestObserver<>();
        future.subscribe(subscriber);

        subscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        subscriber.assertError(Exception.class);

    }

    @Test
    public void connection_should_be_closed_after_update_execution() throws JpoException, InterruptedException, ExecutionException {
        int result = sqlExecutor.update("", new ArrayList<>()).blockingGet().updated();

        assertEquals(0, result);
    }

    @Before
    public void setUp() {
        sqlExecutor =
                new com.jporm.rx.session.SqlExecutorImpl(new TypeConverterFactory(), getConnectionProvider(conn));

    }

    private RxConnectionProvider<? extends RxConnection> getConnectionProvider(final RxConnection conn) {
        return new RxConnectionProvider<RxConnection>() {
            @Override
            public <R> Observable<R> getConnection(boolean autoCommit, Function<RxConnection, Observable<R>> connection) {
                return connection.apply(conn);
            }
        };
    }

}
