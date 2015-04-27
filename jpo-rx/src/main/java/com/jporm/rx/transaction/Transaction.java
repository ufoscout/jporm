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
package com.jporm.rx.transaction;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.session.Session;

public interface Transaction {

	/**
	 * Set the transaction isolation level for the current transaction.
	 * @param isolation
	 * @return
	 */
	Transaction isolation(TransactionIsolation isolation);

	/**
	 * Executes the transaction.
	 * All the actions performed on the session are executed in a transaction.
	 * The transaction is committed only if all the performed actions succeed.
	 * @param txSession
	 * @return
	 */
	<T> CompletableFuture<T> execute(Function<Session, CompletableFuture<T>> session);

}
