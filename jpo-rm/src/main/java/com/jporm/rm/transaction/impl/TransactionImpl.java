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

import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.connection.Connection;
import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.inject.config.ConfigService;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rm.session.Session;
import com.jporm.rm.session.impl.SessionImpl;
import com.jporm.rm.transaction.Transaction;
import com.jporm.rm.transaction.TransactionCallback;
import com.jporm.rm.transaction.TransactionVoidCallback;

public class TransactionImpl implements Transaction {

	private final ConnectionProvider sessionProvider;
	private final ServiceCatalog serviceCatalog;
	private TransactionIsolation transactionIsolation;
	private int timeout;
	private boolean readOnly = false;

	public TransactionImpl(ConnectionProvider sessionProvider, ServiceCatalog serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
		this.sessionProvider = sessionProvider;

		ConfigService configService = serviceCatalog.getConfigService();
		transactionIsolation = configService.getDefaultTransactionIsolation();
		timeout = configService.getTransactionDefaultTimeoutSeconds();

	}

	@Override
	public <T> T execute(TransactionCallback<T> callback) {
		Connection connection = null;
		try {
			connection = sessionProvider.getConnection(false);
			setTransactionIsolation(connection);
			setTimeout(connection);
			connection.setReadOnly(readOnly);
			ConnectionProvider decoratedConnectionProvider = new TransactionalConnectionProviderDecorator(connection, sessionProvider);
			decoratedConnectionProvider.getConnection(false).commit();
			Session session = new SessionImpl(serviceCatalog, decoratedConnectionProvider, false);
			T result = callback.doInTransaction(session);
			if (!readOnly) {
				connection.commit();
			} else {
				connection.rollback();
			}
			return result;
		} catch (RuntimeException e) {
			connection.rollback();
			throw e;
		} finally {
			connection.close();
		}
	}

	@Override
	public <T> CompletableFuture<T> executeAsync(TransactionCallback<T> callback) {
		return serviceCatalog.getAsyncTaskExecutor().execute(() -> {
			return execute(callback);
		});
	}

	@Override
	public void executeVoid(TransactionVoidCallback callback) {
		execute((session) -> {
			callback.doInTransaction(session);
			return null;
		});
	}

	@Override
	public CompletableFuture<Void> executevoidAsync(TransactionVoidCallback callback) {
		return serviceCatalog.getAsyncTaskExecutor().execute(() -> {
			executeVoid(callback);
		});
	}

	private void setTransactionIsolation(Connection connection) {
		connection.setTransactionIsolation(transactionIsolation);
	}

	private void setTimeout(Connection connection) {
		if (timeout >= 0) {
			connection.setTimeout(timeout);
		}
	}

	@Override
	public Transaction timeout(int seconds) {
		timeout = seconds;
		return this;
	}

	@Override
	public Transaction readOnly(boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}

	@Override
	public Transaction isolation(TransactionIsolation isolation) {
		transactionIsolation = isolation;
		return this;
	}

}
