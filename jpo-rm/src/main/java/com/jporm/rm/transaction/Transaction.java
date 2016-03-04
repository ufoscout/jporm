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
import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rm.session.Session;

public interface Transaction {

    /**
     * Executes the current transaction
     *
     * @return
     */
    <T> T execute(Function<Session, T> session);

    /**
     * Execute asynchronously the transaction and returns a
     * {@link CompletableFuture} with the transaction result
     *
     * @param transactionCallback
     * @return
     */
    <T> CompletableFuture<T> executeAsync(Function<Session, T> session);

    /**
     * Executes the current transaction
     *
     * @return
     */
    void execute(Consumer<Session> session);

    /**
     * Execute asynchronously the transaction and returns a
     * {@link CompletableFuture} with the transaction result
     *
     * @param transactionCallback
     * @return
     */
    CompletableFuture<Void> executeAsync(Consumer<Session> session);

    /**
     * Set the transaction isolation level for the current transaction.
     *
     * @param isolation
     * @return
     */
    Transaction isolation(TransactionIsolation isolation);

    /**
     * Whether the transaction is read only. Default is false.
     *
     * @param seconds
     * @return
     */
    Transaction readOnly(boolean readOnly);

    /**
     * Set the transaction timeout in seconds
     *
     * @param seconds
     * @return
     */
    Transaction timeout(int seconds);

}
