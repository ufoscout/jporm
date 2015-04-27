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
import com.jporm.commons.core.transaction.TransactionPropagation;
import com.jporm.commons.core.transaction.impl.TransactionDefinitionImpl;
import com.jporm.rm.session.Session;
import com.jporm.rm.session.SessionProvider;
import com.jporm.rm.transaction.Transaction;
import com.jporm.rm.transaction.TransactionCallback;
import com.jporm.rm.transaction.TransactionVoidCallback;

public class TransactionImpl implements Transaction {

	private final Session session;
	private final TransactionDefinition transactionDefinition = new TransactionDefinitionImpl();
	private final SessionProvider sessionProvider;
	private final ServiceCatalog serviceCatalog;

	public TransactionImpl(Session session, SessionProvider sessionProvider, ServiceCatalog serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
		this.session = session;
		this.sessionProvider = sessionProvider;
		setTimeout(transactionDefinition, serviceCatalog);
	}

	@Override
	public <T> T execute(TransactionCallback<T> callback) {
		return sessionProvider.sqlPerformerStrategy().doInTransaction(session, transactionDefinition, (s) -> {
			return callback.doInTransaction(session);
		});
	}

	@Override
	public <T> CompletableFuture<T> executeAsync(TransactionCallback<T> callback) {
		return serviceCatalog.getAsyncTaskExecutor().execute(() -> {
			return execute(callback);
		});
	}

	@Override
	public void executeVoid(TransactionVoidCallback callback) {
		sessionProvider.sqlPerformerStrategy().doInTransaction(session, transactionDefinition, (s) -> {
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

	private void setTimeout(TransactionDefinition txDef, ServiceCatalog serviceCatalog) {
		if (txDef.getTimeout() == TransactionDefinition.TIMEOUT_DEFAULT) {
			txDef.timeout(serviceCatalog.getConfigService().getTransactionDefaultTimeoutSeconds());
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
	public Transaction propagation(TransactionPropagation propagation) {
		transactionDefinition.propagation(propagation);
		return this;
	}

	@Override
	public Transaction isolation(TransactionIsolation isolation) {
		transactionDefinition.isolation(isolation);
		return this;
	}

}
