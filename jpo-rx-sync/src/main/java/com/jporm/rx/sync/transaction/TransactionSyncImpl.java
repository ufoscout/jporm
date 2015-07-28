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
package com.jporm.rx.sync.transaction;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.session.Session;
import com.jporm.rx.sync.quasar.JpoCompletableWrapper;
import com.jporm.rx.sync.session.SessionSync;
import com.jporm.rx.sync.session.impl.SessionSyncImpl;
import com.jporm.rx.transaction.Transaction;

import co.paralleluniverse.fibers.Suspendable;

@Suspendable
public class TransactionSyncImpl implements TransactionSync {

	private final Transaction tx;

	public TransactionSyncImpl(Transaction tx) {
		this.tx = tx;
	}

	@Override
	public TransactionSync isolation(TransactionIsolation isolation) {
		tx.isolation(isolation);
		return this;
	}

	@Override
	public <T> T execute(Function<SessionSync, T> session) {
		return JpoCompletableWrapper.get(tx.execute((Session asyncSsession) -> {
			CompletableFuture<T> result = new CompletableFuture<T>();
			SessionSync syncSession = new SessionSyncImpl(asyncSsession);
			try {
				result.complete( session.apply(syncSession) );
			} catch (Exception e) {
				result.completeExceptionally(e);
			}
			return result;
		}));
	}

}
