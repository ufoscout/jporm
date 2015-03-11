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

public interface TransactionApi {

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	<T> Transaction<T> tx(TransactionCallback<T> transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	<T> Transaction<T> tx(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback);

	/**
	 * Execute asynchronously a Transaction
	 * @param transactionCallback
	 * @return
	 */
	<T> CompletableFuture<T> txAsync(TransactionCallback<T> transactionCallback);

	/**
	 * Execute asynchronously a Transaction
	 * @param transactionCallback
	 * @return
	 */
	<T> CompletableFuture<T> txAsync(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	<T> T txNow(TransactionCallback<T> transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	<T> T txNow(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	TransactionVoid txVoid(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	TransactionVoid txVoid(TransactionVoidCallback transactionCallback);

	/**
	 * Execute asynchronously a Transaction
	 * @param transactionCallback
	 * @return
	 */
	CompletableFuture<Void> txVoidAsync(TransactionVoidCallback transactionCallback);

	/**
	 * Execute asynchronously a Transaction
	 * @param transactionCallback
	 * @return
	 */
	CompletableFuture<Void> txVoidAsync(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	void txVoidNow(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback);

	/**
	 * Execute a block of code inside a Transaction or participate to an existing one
	 * @param transactionCallback
	 * @return
	 */
	void txVoidNow(TransactionVoidCallback transactionCallback);

}
