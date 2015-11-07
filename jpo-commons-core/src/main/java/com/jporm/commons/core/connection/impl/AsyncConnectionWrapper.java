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
package com.jporm.commons.core.connection.impl;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.connection.AsyncConnection;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public class AsyncConnectionWrapper implements AsyncConnection {

	private final com.jporm.commons.core.connection.Connection rmConnection;
	private final AsyncTaskExecutor executor;

	public AsyncConnectionWrapper(com.jporm.commons.core.connection.Connection rmConnection, AsyncTaskExecutor executor) {
		this.rmConnection = rmConnection;
		this.executor = executor;
	}


	@Override
	public CompletableFuture<int[]> batchUpdate(Collection<String> sqls) {
		return executor.execute(() -> {
			return rmConnection.batchUpdate(sqls);
		});
	}

	@Override
	public CompletableFuture<int[]> batchUpdate(String sql, BatchPreparedStatementSetter psc) {
		return executor.execute(() -> {
			return rmConnection.batchUpdate(sql, psc);
		});
	}

	@Override
	public CompletableFuture<int[]> batchUpdate(String sql, Collection<StatementSetter> args) {
		return executor.execute(() -> {
			return rmConnection.batchUpdate(sql, args);
		});
	}

	@Override
	public CompletableFuture<Void> execute(String sql) {
		return executor.execute(() -> {
			rmConnection.execute(sql);
		});
	}

	@Override
	public <T> CompletableFuture<T> query(String sql, final StatementSetter pss, ResultSetReader<T> rse) {
		return executor.execute(() -> {
			return rmConnection.query(sql, pss, rse);
		});
	}

	@Override
	public CompletableFuture<Integer> update(String sql, GeneratedKeyReader generatedKeyReader, StatementSetter pss) {
		return executor.execute(() -> {
			return rmConnection.update(sql, generatedKeyReader, pss);
		});
	}

	@Override
	public CompletableFuture<Void> close() {
		return executor.execute(() -> {
			rmConnection.close();
		});
	}

	@Override
	public CompletableFuture<Void> commit() {
		return executor.execute(() -> {
			rmConnection.commit();
		});
	}

	@Override
	public CompletableFuture<Void> rollback() {
		return executor.execute(() -> {
			rmConnection.rollback();
		});
	}

	@Override
	public void setTransactionIsolation(TransactionIsolation isolation) {
		rmConnection.setTransactionIsolation(isolation);
	}

	@Override
	public void setTimeout(int timeout) {
		rmConnection.setTimeout(timeout);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		rmConnection.setReadOnly(readOnly);
	}

}
