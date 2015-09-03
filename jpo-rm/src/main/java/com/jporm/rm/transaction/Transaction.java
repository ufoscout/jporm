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
package com.jporm.rm.transaction;

import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.transaction.TransactionIsolation;

public interface Transaction {

	/**
	 * Executes the current transaction
	 * @return
	 */
	<T> T execute(TransactionCallback<T> callback);

	/**
	 * Executes the current transaction
	 * @return
	 */
	void executeVoid(TransactionVoidCallback callback);

	/**
	 * Execute asynchronously the transaction and returns a {@link CompletableFuture} with the transaction result
	 * @param transactionCallback
	 * @return
	 */
	<T> CompletableFuture<T> executeAsync(TransactionCallback<T> callback);

	/**
	 * Execute asynchronously the transaction and returns a {@link CompletableFuture} with the transaction result
	 * @param transactionCallback
	 * @return
	 */
	CompletableFuture<Void> executevoidAsync(TransactionVoidCallback callback);

	/**
	 * Set the transaction isolation level for the current transaction.
	 * @param isolation
	 * @return
	 */
	Transaction isolation(TransactionIsolation isolation);

	/**
	 * Set the transaction timeout in seconds
	 * @param seconds
	 * @return
	 */
	Transaction timeout(int seconds);

	/**
	 * Whether the transaction is read only.
	 * Default is false.
	 * @param seconds
	 * @return
	 */
	Transaction readOnly(boolean readOnly);

}
