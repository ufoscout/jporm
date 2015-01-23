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
import com.jporm.transaction.TransactionDefinition;
import com.jporm.transaction.TransactionalSession;

public abstract class ATransaction {

	public <T> T now(TransactionalSessionImpl session, TransactionDefinition transactionDefinition, SessionProvider sessionProvider, Function<TransactionalSession, T> function) {
		return sessionProvider.doInTransaction(session, transactionDefinition, (s) -> {
			T result = function.apply(session);
			session.getSaveUpdateDeleteQueries().stream().filter(query -> !query.isExecuted()).forEach(query -> query.execute());
			return result;
		});
	}

}
