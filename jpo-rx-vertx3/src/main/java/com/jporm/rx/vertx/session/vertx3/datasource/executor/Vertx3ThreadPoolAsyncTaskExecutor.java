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
package com.jporm.rx.vertx.session.vertx3.datasource.executor;

import io.vertx.core.Context;
import io.vertx.core.Vertx;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.async.impl.ThreadPoolAsyncTaskExecutor;

/**
 * {@link AsyncTaskExecutor} that executes async tasks in a Thread pool then add the result to the {@link Vertx} {@link Context}
 * @author Francesco Cina
 *
 */
public class Vertx3ThreadPoolAsyncTaskExecutor implements AsyncTaskExecutor {

	private final AsyncTaskExecutor connectionExecutor = new ThreadPoolAsyncTaskExecutor(1, "jpo-vertx-connection-get-pool");
	private Vertx vertx;

	public Vertx3ThreadPoolAsyncTaskExecutor(int nThreads, Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	public <T> CompletableFuture<T> execute(Supplier<T> task) {
		return connectionExecutor.execute(task);
	}

	@Override
	public CompletableFuture<Void> execute(Runnable task) {
		return execute(() -> {
			task.run();
			return null;
		});
	}

}
