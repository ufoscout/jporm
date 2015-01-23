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

import com.jporm.core.session.SessionProvider;
import com.jporm.transaction.TransactionDefinition;
import com.jporm.transaction.TransactionVoid;
import com.jporm.transaction.TransactionVoidCallback;
import com.jporm.transaction.TransactionalSession;

public class TransactionVoidImpl implements TransactionVoid {

	private TransactionVoidCallback callback;
	private TransactionalSession session;
	private TransactionDefinition transactionDefinition;
	private SessionProvider sessionProvider;

	public TransactionVoidImpl(TransactionVoidCallback callback, final TransactionDefinition transactionDefinition, TransactionalSession session, SessionProvider sessionProvider) {
		this.callback = callback;
		this.transactionDefinition = transactionDefinition;
		this.session = session;
		this.sessionProvider = sessionProvider;
	}

	@Override
	public void now() {
		sessionProvider.doInTransaction(session, transactionDefinition, (s) -> {
			callback.doInTransaction(session);
			return null;
		});
	}

}
