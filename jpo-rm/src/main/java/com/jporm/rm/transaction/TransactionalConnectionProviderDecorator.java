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
package com.jporm.rm.transaction;

import java.util.Collection;

import com.jporm.commons.core.connection.Connection;
import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public class TransactionalConnectionProviderDecorator implements ConnectionProvider {

    private final Connection connection;
    private final ConnectionProvider connectionProvider;

    public TransactionalConnectionProviderDecorator(final Connection connection, final ConnectionProvider connectionProvider) {
        this.connection = connection;
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Connection getConnection(final boolean autoCommit) {
        return new Connection() {

            @Override
            public int[] batchUpdate(final Collection<String> sqls) throws JpoException {
                return connection.batchUpdate(sqls);
            }

            @Override
            public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
                return connection.batchUpdate(sql, psc);
            }

            @Override
            public int[] batchUpdate(final String sql, final Collection<StatementSetter> args) throws JpoException {
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
            public <T> T query(final String sql, final StatementSetter pss, final ResultSetReader<T> rse) throws JpoException {
                return connection.query(sql, pss, rse);
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
            public int update(final String sql, final GeneratedKeyReader generatedKeyReader, final StatementSetter pss) throws JpoException {
                return connection.update(sql, generatedKeyReader, pss);
            }
        };
    }

    @Override
    public DBProfile getDBProfile() {
        return connectionProvider.getDBProfile();
    }

}
