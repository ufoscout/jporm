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
import java.util.function.Supplier;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rm.session.Connection;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public class QuasarConnection implements Connection {

	private final AsyncTaskExecutor executor;
	private final Connection connection;

	public QuasarConnection(Connection connection, AsyncTaskExecutor executor) {
		this.connection = connection;
		this.executor = executor;
	}

	@Override
	public int[] batchUpdate(Collection<String> sqls) throws JpoException {
		return doAsync(() -> connection.batchUpdate(sqls));

	}

	@Override
	public int[] batchUpdate(String sql, BatchPreparedStatementSetter psc) throws JpoException {
		return doAsync(() -> connection.batchUpdate(sql, psc));

	}

	@Override
	public int[] batchUpdate(String sql, Collection<StatementSetter> args) throws JpoException {
		return doAsync(() -> connection.batchUpdate(sql, args));
	}

	@Override
	public void execute(String sql) throws JpoException {
		doAsyncVoid(() -> connection.execute(sql));
	}

	@Override
	public <T> T query(String sql, StatementSetter pss, ResultSetReader<T> rse) throws JpoException {
		return doAsync(() -> connection.query(sql, pss, rse));
	}

	@Override
	public int update(String sql, GeneratedKeyReader generatedKeyReader, StatementSetter pss) throws JpoException {
		return doAsync(() -> connection.update(sql, generatedKeyReader, pss));
	}

	@Override
	public void close() {
		doAsyncVoid(() -> connection.close());
	}

	@Override
	public void commit() {
		doAsyncVoid(() -> connection.commit());
	}

	@Override
	public void rollback() {
		doAsyncVoid(() -> connection.rollback());
	}

	@Override
	public void setTransactionIsolation(TransactionIsolation isolationLevel) {
		connection.setTransactionIsolation(isolationLevel);
	}

	@Override
	public void setTimeout(int timeout) {
		connection.setTimeout(timeout);
	}

	private <T> T doAsync(Supplier<T> task) {
		return JpoCompletableWrapper.get(executor.execute(task));
	}

	private void doAsyncVoid(Runnable task) {
		JpoCompletableWrapper.get(executor.execute(task));
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		connection.setReadOnly(readOnly);
	}
}
