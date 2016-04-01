/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.rm.transaction;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.connection.Connection;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

public class CloseSuppressingConnectionDecorator implements Connection {

    private final Connection connection;

    public CloseSuppressingConnectionDecorator(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int[] batchUpdate(final Collection<String> sqls, Function<String, String> sqlPreProcessor) throws JpoException {
        return connection.batchUpdate(sqls, sqlPreProcessor);
    }

    @Override
    public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
        return connection.batchUpdate(sql, psc);
    }

    @Override
    public int[] batchUpdate(final String sql, final Collection<Consumer<Statement>> args) throws JpoException {
        return connection.batchUpdate(sql, args);
    }

    @Override
    public void close() {
    }

    @Override
    public void commit() {
    }

    @Override
    public void execute(final String sql) throws JpoException {
        connection.execute(sql);
    }

    @Override
    public <T> T query(String sql, final Consumer<Statement> statementSetter, Function<ResultSet, T> resultSetReader) throws JpoException {
        return connection.query(sql, statementSetter, resultSetReader);
    }

    @Override
    public void rollback() {
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
    }

    @Override
    public void setTimeout(final int timeout) {
    }

    @Override
    public void setTransactionIsolation(final TransactionIsolation isolationLevel) {
    }

    @Override
    public <R> R update(final String sql, final GeneratedKeyReader<R> generatedKeyReader, final Consumer<Statement> pss) throws JpoException {
        return connection.update(sql, generatedKeyReader, pss);
    }

    @Override
    public int update(String sql, Consumer<Statement> pss) {
        return connection.update(sql, pss);
    }

}
