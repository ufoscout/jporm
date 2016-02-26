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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;

import com.jporm.commons.core.connection.AsyncConnection;
import com.jporm.commons.core.connection.AsyncConnectionProvider;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.BaseTestApi;
import com.jporm.rx.query.update.UpdateResult;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.dsl.dialect.DBType;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public class SqlExecutorImplTest extends BaseTestApi {

    class ConnectionTestImpl implements AsyncConnection {

        public boolean closed = false;

        @Override
        public CompletableFuture<int[]> batchUpdate(final Collection<String> sqls) {
            return null;
        }

        @Override
        public CompletableFuture<int[]> batchUpdate(final String sql, final BatchPreparedStatementSetter psc) {
            return null;
        }

        @Override
        public CompletableFuture<int[]> batchUpdate(final String sql, final Collection<StatementSetter> args) {
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
        public <T> CompletableFuture<T> query(final String sql, final StatementSetter pss, final ResultSetReader<T> rse) {
            return CompletableFuture.supplyAsync(() -> rse.read(null), Executors.newFixedThreadPool(1));
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
        public CompletableFuture<Integer> update(final String sql, final GeneratedKeyReader generatedKeyReader, final StatementSetter pss) {
            return CompletableFuture.supplyAsync(() -> {
                generatedKeyReader.read(null);
                return 0;
            } , Executors.newFixedThreadPool(1));
        }

    }

    private ConnectionTestImpl conn = new ConnectionTestImpl();

    private SqlExecutor sqlExecutor;

    @Test
    public void connection_should_be_closed_after_query_exception() throws JpoException, InterruptedException, ExecutionException {
        CompletableFuture<Object> future = sqlExecutor.query("", new ArrayList<Object>(), rsr -> {
            getLogger().info("Throwing exception");
            throw new RuntimeException("exception during query execution");
        });

        try {
            future.get();
            fail("It should throw an exception before");
        } catch (Exception e) {
            // ignore it
        }

        assertTrue(future.isCompletedExceptionally());

        assertTrue(conn.closed);
    }

    @Test
    public void connection_should_be_closed_after_query_execution() throws JpoException, InterruptedException, ExecutionException {
        String result = sqlExecutor.query("", new ArrayList<Object>(), rsr -> {
            return "helloWorld";
        }).get();

        assertEquals("helloWorld", result);

        assertTrue(conn.closed);
    }

    @Test
    public void connection_should_be_closed_after_update_exception() throws JpoException, InterruptedException, ExecutionException {
        CompletableFuture<UpdateResult> future = sqlExecutor.update("", new ArrayList<Object>(), new GeneratedKeyReader() {
            @Override
            public String[] generatedColumnNames() {
                return new String[0];
            }

            @Override
            public void read(final ResultSet generatedKeyResultSet) {
                throw new RuntimeException("exception during query execution");
            }
        });

        try {
            future.get();
            fail("It should throw an exception before");
        } catch (Exception e) {
            // ignore it
        }

        assertTrue(future.isCompletedExceptionally());
        assertTrue(conn.closed);
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
        sqlExecutor = new SqlExecutorImpl(new TypeConverterFactory(), new AsyncConnectionProvider() {
            @Override
            public CompletableFuture<AsyncConnection> getConnection(final boolean autoCommit) {
                return CompletableFuture.<AsyncConnection> completedFuture(conn);
            }

            @Override
            public CompletableFuture<DBType> getDBType() {
                return CompletableFuture.completedFuture(DBType.UNKNOWN);
            }
        }, false);

    }
}
