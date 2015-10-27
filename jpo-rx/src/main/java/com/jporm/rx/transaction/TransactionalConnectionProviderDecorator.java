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
package com.jporm.rx.transaction;

import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.connection.Connection;
import com.jporm.rx.connection.UpdateResult;
import com.jporm.rx.session.ConnectionProvider;
import com.jporm.sql.dialect.DBType;
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
	public CompletableFuture<DBType> getDBType() {
		return connectionProvider.getDBType();
	}

	@Override
	public CompletableFuture<Connection> getConnection(boolean autoCommit) {
		return CompletableFuture.completedFuture(new Connection() {

			@Override
			public CompletableFuture<UpdateResult> update(String sql, GeneratedKeyReader generatedKeyReader, StatementSetter pss) {
				return connection.update(sql, generatedKeyReader, pss);
			}

			@Override
			public CompletableFuture<Void> rollback() {
				return CompletableFuture.completedFuture(null);
			}

			@Override
			public <T> CompletableFuture<T> query(String sql, StatementSetter pss, ResultSetReader<T> rse) {
				return connection.query(sql, pss, rse);
			}

			@Override
			public CompletableFuture<Void> commit() {
				return CompletableFuture.completedFuture(null);
			}

			@Override
			public CompletableFuture<Void> close() {
				return CompletableFuture.completedFuture(null);
			}

			@Override
			public void setTransactionIsolation(TransactionIsolation isolation) {
				connection.setTransactionIsolation(isolation);
			}

			@Override
			public void setTimeout(int timeout) {
				connection.setTimeout(timeout);
			}

			@Override
			public void setReadOnly(boolean readOnly) {
			}
		});
	}

}
