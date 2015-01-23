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
package com.jporm.core.transaction;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.session.SessionProvider;
import com.jporm.session.Session;
import com.jporm.transaction.TransactionDefinition;
import com.jporm.transaction.TransactionVoid;
import com.jporm.transaction.TransactionVoidCallback;
import com.jporm.transaction.TransactionalSession;

public class TransactionVoidImpl extends ATransaction implements TransactionVoid {

	private final TransactionVoidCallback callback;
	private final TransactionalSessionImpl session;
	private final TransactionDefinition transactionDefinition;
	private final SessionProvider sessionProvider;
	private final ServiceCatalog serviceCatalog;

	public TransactionVoidImpl(TransactionVoidCallback callback, final TransactionDefinition transactionDefinition, Session session, SessionProvider sessionProvider, ServiceCatalog serviceCatalog) {
		this.callback = callback;
		this.transactionDefinition = transactionDefinition;
		this.serviceCatalog = serviceCatalog;
		this.session = new TransactionalSessionImpl(session);
		this.sessionProvider = sessionProvider;
	}

	@Override
	public void now() {
		exec();
	}

	@Override
	public CompletableFuture<Void> async() {
		return serviceCatalog.getAsyncTaskExecutor().execute(this::exec);
	}

	private Void exec() {
		return now(session, transactionDefinition, sessionProvider, new Function<TransactionalSession, Void>() {
			@Override
			public Void apply(TransactionalSession txSession) {
				callback.doInTransaction(session);
				return null;
			}
		});
	}
}
