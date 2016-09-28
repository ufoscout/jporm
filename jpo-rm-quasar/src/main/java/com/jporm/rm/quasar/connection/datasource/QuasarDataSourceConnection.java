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
package com.jporm.rm.quasar.connection.datasource;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rm.connection.datasource.DataSourceConnection;
import com.jporm.rm.quasar.connection.JpoCompletableWrapper;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

public class QuasarDataSourceConnection implements DataSourceConnection {

    private DataSourceConnection connection;
    private AsyncTaskExecutor executor;

    public QuasarDataSourceConnection(final DataSourceConnection rmConnection, final AsyncTaskExecutor executor) {
        connection = rmConnection;
        this.executor = executor;
    }

    @Override
    public int[] batchUpdate(final Collection<String> sqls, Function<String, String> sqlPreProcessor) throws JpoException {
        return JpoCompletableWrapper.get(executor.execute(() -> {
            return connection.batchUpdate(sqls, sqlPreProcessor);
        }));

    }

    @Override
    public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
        return JpoCompletableWrapper.get(executor.execute(() -> {
            return connection.batchUpdate(sql, psc);
        }));

    }

    @Override
    public int[] batchUpdate(final String sql, final Collection<Consumer<Statement>> args) throws JpoException {
        return JpoCompletableWrapper.get(executor.execute(() -> {
            return connection.batchUpdate(sql, args);
        }));
    }

    @Override
    public void execute(final String sql) throws JpoException {
        JpoCompletableWrapper.get(executor.execute(() -> {
            connection.execute(sql);
        }));
    }

    @Override
    public <T> T query(final String sql, final Consumer<Statement> pss, final Function<ResultSet, T> rse) throws JpoException {
        return JpoCompletableWrapper.get(executor.execute(() -> {
            return connection.query(sql, pss, rse);
        }));
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        connection.setReadOnly(readOnly);
    }

    @Override
    public void setTimeout(final int timeout) {
        connection.setTimeout(timeout);
    }

    @Override
    public void setTransactionIsolation(final TransactionIsolation isolationLevel) {
        connection.setTransactionIsolation(isolationLevel);
    }

    @Override
    public <R> R update(final String sql, final GeneratedKeyReader<R> generatedKeyReader, final Consumer<Statement> pss) throws JpoException {
        return JpoCompletableWrapper.get(executor.execute(() -> {
            return connection.update(sql, generatedKeyReader, pss);
        }));
    }

    @Override
    public int update(String sql, Consumer<Statement> pss) {
        return JpoCompletableWrapper.get(executor.execute(() -> {
            return connection.update(sql, pss);
        }));
    }

    @Override
    public void close() {
        JpoCompletableWrapper.get(executor.execute(() -> {
            connection.close();
        }));
    }

    @Override
    public void commit() {
        JpoCompletableWrapper.get(executor.execute(() -> {
            connection.commit();
        }));
    }

    @Override
    public void rollback() {
        JpoCompletableWrapper.get(executor.execute(() -> {
            connection.rollback();
        }));
    }

    @Override
    public void setAutoCommit(boolean autoCommit) {
        connection.setAutoCommit(autoCommit);
    }

    @Override
    public boolean isClosed() {
        return connection.isClosed();
    }

}
