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
package com.jporm.rm.quasar.session;

import java.util.Collection;
import java.util.function.Function;

import com.jporm.commons.core.connection.AsyncConnection;
import com.jporm.commons.core.connection.Connection;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public class QuasarConnection implements Connection {

    private final AsyncConnection connection;

    public QuasarConnection(final AsyncConnection connection) {
        this.connection = connection;
    }

    @Override
    public int[] batchUpdate(final Collection<String> sqls, Function<String, String> sqlPreProcessor) throws JpoException {
        return JpoCompletableWrapper.get(connection.batchUpdate(sqls, sqlPreProcessor));

    }

    @Override
    public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
        return JpoCompletableWrapper.get(connection.batchUpdate(sql, psc));

    }

    @Override
    public int[] batchUpdate(final String sql, final Collection<StatementSetter> args) throws JpoException {
        return JpoCompletableWrapper.get(connection.batchUpdate(sql, args));
    }

    @Override
    public void close() {
        JpoCompletableWrapper.get(connection.close());
    }

    @Override
    public void commit() {
        JpoCompletableWrapper.get(connection.commit());
    }

    @Override
    public void execute(final String sql) throws JpoException {
        JpoCompletableWrapper.get(connection.execute(sql));
    }

    @Override
    public <T> T query(final String sql, final StatementSetter pss, final ResultSetReader<T> rse) throws JpoException {
        return JpoCompletableWrapper.get(connection.query(sql, pss, rse));
    }

    @Override
    public void rollback() {
        JpoCompletableWrapper.get(connection.rollback());
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
    public <R> R update(final String sql, final GeneratedKeyReader<R> generatedKeyReader, final StatementSetter pss) throws JpoException {
        return JpoCompletableWrapper.get(connection.update(sql, generatedKeyReader, pss));
    }

    @Override
    public int update(String sql, StatementSetter pss) {
        return JpoCompletableWrapper.get(connection.update(sql, pss));
    }
}
