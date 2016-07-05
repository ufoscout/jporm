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
package com.jporm.rx.reactor.session.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.reactor.BaseTestApi;
import com.jporm.rx.connection.AsyncConnection;
import com.jporm.rx.connection.AsyncConnectionProvider;
import com.jporm.rx.query.update.UpdateResult;
import com.jporm.rx.reactor.session.SqlExecutor;
import com.jporm.rx.reactor.session.SqlExecutorImpl;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.dialect.DBType;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

import reactor.core.publisher.Mono;

public class SqlExecutorImplTest extends BaseTestApi {

    class ConnectionTestImpl implements AsyncConnection {

        public boolean closed = false;

        @Override
        public CompletableFuture<int[]> batchUpdate(final Collection<String> sqls, Function<String, String> sqlPreProcessor) {
            return null;
        }

        @Override
        public CompletableFuture<int[]> batchUpdate(final String sql, final BatchPreparedStatementSetter psc) {
            return null;
        }

        @Override
        public CompletableFuture<int[]> batchUpdate(final String sql, final Collection<Consumer<Statement>> args) {
            return null;
        }

        @Override
        public CompletableFuture<Void> close() {
            closed = true;
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletableFuture<Void> commit() {
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletableFuture<Void> execute(final String sql) {
            return null;
        }

        @Override
        public <T> CompletableFuture<T> query(final String sql, final Consumer<Statement> pss, final Function<ResultSet, T> rse) {
            return CompletableFuture.supplyAsync(() -> rse.apply(null), Executors.newFixedThreadPool(1));
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
        }

        @Override
        public void setTransactionIsolation(final TransactionIsolation isolation) {
        }

        @Override
        public CompletableFuture<Integer> update(final String sql, final Consumer<Statement> pss) {
            return CompletableFuture.supplyAsync(() -> {
                return 0;
            } , Executors.newFixedThreadPool(1));
        }

        @Override
        public <R> CompletableFuture<R> update(final String sql, final GeneratedKeyReader<R> generatedKeyReader, final Consumer<Statement> pss) {
            return CompletableFuture.supplyAsync(() -> {
                generatedKeyReader.read(null, 0);
                return null;
            } , Executors.newFixedThreadPool(1));
        }

    }

    private ConnectionTestImpl conn = new ConnectionTestImpl();

    private SqlExecutor sqlExecutor;

    @Test
    public void connection_should_be_closed_after_query_exception() throws JpoException, InterruptedException, ExecutionException {
        Mono<Object> future = sqlExecutor.query("", new ArrayList<Object>(), (ResultSet rsr) -> {
            getLogger().info("Throwing exception");
            throw new RuntimeException("exception during query execution");
        });

        assertTrue(future.isStarted());
        assertTrue(conn.closed);
        assertTrue(future.isTerminated());

        try {
            future.get();
            fail("It should throw an exception before");
        } catch (Exception e) {
            // ignore it
        }

        assertTrue(future.isStarted());
        assertTrue(conn.closed);
        assertTrue(future.isTerminated());

    }

    @Test
    public void connection_should_be_closed_after_query_execution() throws JpoException, InterruptedException, ExecutionException {
        Mono<String> result = sqlExecutor.query("", new ArrayList<Object>(), rsr -> {
            return "helloWorld";
        });

        result.consume(text -> getLogger().info("next"), e -> getLogger().info("error"), () -> getLogger().info("complete"));

        assertEquals("helloWorld", result.get());
        //assertTrue(result.isStarted());
        assertTrue(result.isTerminated());
        assertTrue(conn.closed);
    }

    @Test
    public void testIsTerminated() throws InterruptedException {
        Mono<String> result = Mono.just("helloWorld");

        result.consume(
                text -> System.out.println("next"),
                e -> System.out.println("error"),
                () -> System.out.println("complete"));

        assertEquals("helloWorld", result.get());

        Thread.sleep(100);

       //assertTrue(result.isTerminated());
        assertTrue(result.isStarted());
    }

    @Test
    public void connection_should_be_closed_after_update_exception() throws JpoException, InterruptedException, ExecutionException {
        Mono<UpdateResult> future = sqlExecutor.update("", new ArrayList<Object>(), new GeneratedKeyReader<UpdateResult>() {
            @Override
            public String[] generatedColumnNames() {
                return new String[0];
            }

            @Override
            public UpdateResult read(final ResultSet generatedKeyResultSet, int affectedRows) {
                throw new RuntimeException("exception during query execution");
            }
        });

        try {
            future.get();
            fail("It should throw an exception before");
        } catch (Exception e) {
            // ignore it
        }

        assertTrue(future.isStarted());

        assertTrue(conn.closed);
        assertTrue(future.isTerminated());
    }

    @Test
    public void connection_should_be_closed_after_update_execution() throws JpoException, InterruptedException, ExecutionException {
        int result = sqlExecutor.update("", new ArrayList<Object>()).get().updated();

        assertEquals(0, result);
        assertTrue(conn.closed);
    }

    @Before
    public void setUp() {
        assertFalse(conn.closed);
        com.jporm.rx.session.SqlExecutor rxSqlExecutor =
                new com.jporm.rx.session.SqlExecutorImpl(new TypeConverterFactory(), new AsyncConnectionProvider() {
                    @Override
                    public CompletableFuture<AsyncConnection> getConnection(final boolean autoCommit) {
                        return CompletableFuture.<AsyncConnection> completedFuture(conn);
                    }

                    @Override
                    public DBProfile getDBProfile() {
                        return DBType.UNKNOWN.getDBProfile();
                    }
                }, false);


        sqlExecutor = new SqlExecutorImpl(rxSqlExecutor);

    }
}
