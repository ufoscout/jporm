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

import java.util.function.Function;

import com.jporm.core.session.SessionProvider;
import com.jporm.session.Session;
import com.jporm.transaction.TransactionDefinition;
import com.jporm.transaction.TransactionVoid;
import com.jporm.transaction.TransactionVoidCallback;
import com.jporm.transaction.TransactionalSession;

public class TransactionVoidImpl extends ATransaction implements TransactionVoid {

	private TransactionVoidCallback callback;
	private TransactionalSessionImpl session;
	private TransactionDefinition transactionDefinition;
	private SessionProvider sessionProvider;

	public TransactionVoidImpl(TransactionVoidCallback callback, final TransactionDefinition transactionDefinition, Session session, SessionProvider sessionProvider) {
		this.callback = callback;
		this.transactionDefinition = transactionDefinition;
		this.session = new TransactionalSessionImpl(session);
		this.sessionProvider = sessionProvider;
	}

	@Override
	public void now() {
		now(session, transactionDefinition, sessionProvider, new Function<TransactionalSession, Void>() {
			@Override
			public Void apply(TransactionalSession txSession) {
				callback.doInTransaction(session);
				return null;
			}
		});
	}

}
