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
package com.jporm.rx.query.connection.datasource;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.query.connection.AsyncConnection;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

public class DataSourceAsyncConnection implements AsyncConnection {

	private final com.jporm.rm.connection.Connection rmConnection;
	private final AsyncTaskExecutor executor;

	public DataSourceAsyncConnection(final com.jporm.rm.connection.Connection rmConnection, final AsyncTaskExecutor executor) {
		this.rmConnection = rmConnection;
		this.executor = executor;
	}

	@Override
	public CompletableFuture<int[]> batchUpdate(final Collection<String> sqls, Function<String, String> sqlPreProcessor) {
		return executor.execute(() -> {
			return rmConnection.batchUpdate(sqls, sqlPreProcessor);
		});
	}

	@Override
	public CompletableFuture<int[]> batchUpdate(final String sql, final BatchPreparedStatementSetter psc) {
		return executor.execute(() -> {
			return rmConnection.batchUpdate(sql, psc);
		});
	}

	@Override
	public CompletableFuture<int[]> batchUpdate(final String sql, final Collection<Consumer<Statement>> args) {
		return executor.execute(() -> {
			return rmConnection.batchUpdate(sql, args);
		});
	}

	@Override
	public CompletableFuture<Void> execute(final String sql) {
		return executor.execute(() -> {
			rmConnection.execute(sql);
		});
	}

	@Override
	public <T> CompletableFuture<T> query(final String sql, final Consumer<Statement> pss, final Function<ResultSet, T> rse) {
		return executor.execute(() -> {
			return rmConnection.query(sql, pss, rse);
		});
	}

	@Override
	public void setReadOnly(final boolean readOnly) {
		rmConnection.setReadOnly(readOnly);
	}

	@Override
	public void setTimeout(final int timeout) {
		rmConnection.setTimeout(timeout);
	}

	@Override
	public void setTransactionIsolation(final TransactionIsolation isolation) {
		rmConnection.setTransactionIsolation(isolation);
	}

	@Override
	public <R> CompletableFuture<R> update(final String sql, final GeneratedKeyReader<R> generatedKeyReader, final Consumer<Statement> pss) {
		return executor.execute(() -> {
			return rmConnection.update(sql, generatedKeyReader, pss);
		});
	}

	@Override
	public CompletableFuture<Integer> update(String sql, Consumer<Statement> pss) {
		return executor.execute(() -> {
			return rmConnection.update(sql, pss);
		});
	}

}
