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
package com.jporm.rx.vertx.session.vertx3;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.jporm.commons.core.async.AsyncTaskExecutor;

/**
 * {@link AsyncTaskExecutor} that executes async tasks in a {@link Vertx} executeBloking call
 * @author Francesco Cina
 *
 */
public class Vertx3AsyncTaskExecutor implements AsyncTaskExecutor {

	private Vertx vertx;

	public Vertx3AsyncTaskExecutor(Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	public <T> CompletableFuture<T> execute(Supplier<T> task) {
		CompletableFuture<T> future = new CompletableFuture<>();
		vertx.executeBlocking((Future<T> futureHandler) -> {
			try {
				futureHandler.complete(task.get());
			} catch (RuntimeException ex) {
				futureHandler.fail(ex);
			}
		}, resultHandler -> {
			if (resultHandler.succeeded()) {
				future.complete(resultHandler.result());
			} else {
				future.completeExceptionally(resultHandler.cause());
			}
		});
		return future;
	}

	@Override
	public CompletableFuture<Void> execute(Runnable task) {
		return execute(() -> {
			task.run();
			return null;
		});
	}

}
