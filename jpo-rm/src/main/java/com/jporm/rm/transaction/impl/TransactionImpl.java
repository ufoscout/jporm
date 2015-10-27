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

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.transaction.TransactionDefinition;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.commons.core.transaction.impl.TransactionDefinitionImpl;
import com.jporm.rm.session.Connection;
import com.jporm.rm.session.ConnectionProvider;
import com.jporm.rm.session.Session;
import com.jporm.rm.session.impl.SessionImpl;
import com.jporm.rm.transaction.Transaction;
import com.jporm.rm.transaction.TransactionCallback;
import com.jporm.rm.transaction.TransactionVoidCallback;

public class TransactionImpl implements Transaction {

	private final TransactionDefinition transactionDefinition = new TransactionDefinitionImpl();
	private final ConnectionProvider sessionProvider;
	private final ServiceCatalog serviceCatalog;

	public TransactionImpl(ConnectionProvider sessionProvider, ServiceCatalog serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
		this.sessionProvider = sessionProvider;
	}

	@Override
	public <T> T execute(TransactionCallback<T> callback) {
		Connection connection = null;
		try {
			connection = sessionProvider.getConnection(false);
			setTransactionIsolation(connection);
			setTimeout(connection);
			ConnectionProvider decoratedConnectionProvider = new TransactionalConnectionProviderDecorator(connection, sessionProvider);
			decoratedConnectionProvider.getConnection(false).commit();
			Session session = new SessionImpl(serviceCatalog, decoratedConnectionProvider, false);
			T result = callback.doInTransaction(session);
			connection.commit();
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
		connection.setTransactionIsolation(transactionDefinition.getIsolationLevel());
	}

	private void setTimeout(Connection connection) {
		if (transactionDefinition.getTimeout() >= 0) {
			connection.setTimeout(transactionDefinition.getTimeout());
		} else {
			connection.setTimeout(serviceCatalog.getConfigService().getTransactionDefaultTimeoutSeconds());
		}
	}


	@Override
	public Transaction timeout(int seconds) {
		transactionDefinition.timeout(seconds);
		return this;
	}

	@Override
	public Transaction readOnly(boolean readOnly) {
		transactionDefinition.readOnly(readOnly);
		return this;
	}

	@Override
	public Transaction isolation(TransactionIsolation isolation) {
		transactionDefinition.isolation(isolation);
		return this;
	}

}
