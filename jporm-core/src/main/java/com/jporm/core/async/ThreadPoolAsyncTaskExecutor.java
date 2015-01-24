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
package com.jporm.core.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import com.jporm.async.AsyncTaskExecutor;

public class ThreadPoolAsyncTaskExecutor implements AsyncTaskExecutor {

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(), new NamedThreadPoolFactory("jpoSchedulerPool", false));
	private final Executor executor;

	public ThreadPoolAsyncTaskExecutor(int nThreads) {
		executor = Executors.newFixedThreadPool(nThreads, new NamedThreadPoolFactory("jpoPool", false));
	}

	private <T> CompletableFuture<T> failAfter(CompletableFuture<T> future, long timeout, TimeUnit timeUnit) {
		final CompletableFuture<T> promise = new CompletableFuture<>();
		scheduler.schedule(() -> {
			if (!future.isDone()) {
				final RuntimeException ex = new RuntimeException("timeout after " + timeout + " " + timeUnit);
				promise.completeExceptionally(ex);
			}
		}, timeout, timeUnit);
		return promise;
	}

	private <T> CompletableFuture<T> within(CompletableFuture<T> future, long timeout, TimeUnit timeUnit) {
		final CompletableFuture<T> timeoutFuture = failAfter(future, timeout, timeUnit);
		return future.applyToEither(timeoutFuture, Function.identity());
	}

	@Override
	public <T> CompletableFuture<T> execute(Supplier<T> task) {
		return CompletableFuture.supplyAsync(() -> {
			return task.get();
		}, executor);
	}

	@Override
	public <T> CompletableFuture<T> execute(Supplier<T> task, long timeout, TimeUnit timeUnit) {
		if (timeout>0) {
			return within(execute(task), timeout, timeUnit);
		}
		return execute(task);
	}

}
