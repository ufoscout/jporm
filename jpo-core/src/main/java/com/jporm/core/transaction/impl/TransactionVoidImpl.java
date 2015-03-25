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
package com.jporm.core.transaction.impl;

import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.transaction.TransactionDefinition;
import com.jporm.core.session.Session;
import com.jporm.core.session.SessionProvider;
import com.jporm.core.transaction.TransactionVoid;
import com.jporm.core.transaction.TransactionVoidCallback;

public class TransactionVoidImpl extends ATransaction implements TransactionVoid {

	private final TransactionVoidCallback callback;
	private final Session session;
	private final TransactionDefinition transactionDefinition;
	private final SessionProvider sessionProvider;
	private final ServiceCatalog serviceCatalog;

	public TransactionVoidImpl(TransactionVoidCallback callback, final TransactionDefinition transactionDefinition, Session session, SessionProvider sessionProvider, ServiceCatalog serviceCatalog) {
		this.callback = callback;
		this.transactionDefinition = transactionDefinition;
		this.serviceCatalog = serviceCatalog;
		this.session = session;
		this.sessionProvider = sessionProvider;
		setTimeout(transactionDefinition, serviceCatalog);
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
		return sessionProvider.sqlPerformerStrategy().doInTransaction(session, transactionDefinition, (s) -> {
			callback.doInTransaction(session);
			return null;
		});
	}
}
