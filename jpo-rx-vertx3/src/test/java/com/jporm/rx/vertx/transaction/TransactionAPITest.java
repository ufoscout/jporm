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
package com.jporm.rx.vertx.transaction;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.h2.engine.User;
import org.junit.Test;

import com.jporm.rx.core.session.Session;
import com.jporm.rx.vertx.BaseTestApi;

public class TransactionAPITest extends BaseTestApi {

	@Test
	public void test() {
		new TxProvider()
		.tx(session -> {
			return session.find(User.class, 1).get();
		})
		.then((session, user) -> {
			return session.find(String.class, 2).get();
		});

	}


	class TxProvider {

		<T> Transaction<T> tx(Function<Session, CompletableFuture<T>> session) {
			return new Transaction<T>() {
				@Override
				public <R> void then(BiFunction<Session, T, CompletableFuture<R>> result ) {
					// TODO Auto-generated method stub
				}
				@Override
				public <R> void then(Function<Session, CompletableFuture<R>> result) {
					// TODO Auto-generated method stub

				}
			};
		}

	}
}
