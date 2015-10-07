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
package com.jporm.rx.session.datasource;

import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.connection.Connection;
import com.jporm.rx.connection.UpdateResult;
import com.jporm.rx.connection.UpdateResultImpl;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public class DataSourceConnection implements Connection {

	private final com.jporm.rm.session.Connection rmConnection;
	private final AsyncTaskExecutor executor;

	public DataSourceConnection(com.jporm.rm.session.Connection rmConnection, AsyncTaskExecutor executor) {
		this.rmConnection = rmConnection;
		this.executor = executor;
	}

	@Override
	public <T> CompletableFuture<T> query(String sql, final StatementSetter pss, ResultSetReader<T> rse) {
		return executor.execute(() -> {
			return rmConnection.query(sql, pss, rse);
		});
	}

	@Override
	public CompletableFuture<UpdateResult> update(String sql, GeneratedKeyReader generatedKeyReader, StatementSetter pss) {
		return executor.execute(() -> {
			return new UpdateResultImpl(rmConnection.update(sql, generatedKeyReader, pss));
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

}
