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
package com.jporm.rm.transaction.impl;

import java.util.Collection;

import com.jporm.commons.core.connection.Connection;
import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.sql.dialect.DBType;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public class TransactionalConnectionProviderDecorator implements ConnectionProvider {

	private final Connection connection;
	private final ConnectionProvider connectionProvider;

	public TransactionalConnectionProviderDecorator(Connection connection, ConnectionProvider connectionProvider) {
		this.connection = connection;
		this.connectionProvider = connectionProvider;
	}

	@Override
	public Connection getConnection(boolean autoCommit) {
		return new Connection() {

			@Override
			public int update(String sql, GeneratedKeyReader generatedKeyReader, StatementSetter pss) throws JpoException {
				return connection.update(sql, generatedKeyReader, pss);
			}

			@Override
			public void rollback() {
			}

			@Override
			public <T> T query(String sql, StatementSetter pss, ResultSetReader<T> rse) throws JpoException {
				return connection.query(sql, pss, rse);
			}

			@Override
			public void execute(String sql) throws JpoException {
				connection.execute(sql);
			}

			@Override
			public void commit() {
			}

			@Override
			public void close() {
			}

			@Override
			public int[] batchUpdate(String sql, Collection<StatementSetter> args) throws JpoException {
				return connection.batchUpdate(sql, args);
			}

			@Override
			public int[] batchUpdate(String sql, BatchPreparedStatementSetter psc) throws JpoException {
				return connection.batchUpdate(sql, psc);
			}

			@Override
			public int[] batchUpdate(Collection<String> sqls) throws JpoException {
				return connection.batchUpdate(sqls);
			}

			@Override
			public void setTransactionIsolation(TransactionIsolation isolationLevel) {
			}

			@Override
			public void setTimeout(int timeout) {
			}

			@Override
			public void setReadOnly(boolean readOnly) {
			}
		};
	}

	@Override
	public DBType getDBType() {
		return connectionProvider.getDBType();
	}

}
