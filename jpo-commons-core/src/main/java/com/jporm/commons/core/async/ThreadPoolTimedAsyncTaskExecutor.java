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
package com.jporm.commons.core.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadPoolTimedAsyncTaskExecutor extends ThreadPoolAsyncTaskExecutor implements AsyncTimedTaskExecutor {

    private final ScheduledExecutorService scheduler;

    public ThreadPoolTimedAsyncTaskExecutor(final int nThreads, final String baseThreadPoolName) {
        super(nThreads, baseThreadPoolName);
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int schedulerThreads = ((availableProcessors > nThreads ? nThreads : availableProcessors) / 2) + 1;
        scheduler = Executors.newScheduledThreadPool(schedulerThreads, new NamedThreadPoolFactory("jpoSchedulerPool", false));
    }

    @Override
    public CompletableFuture<Void> execute(final Runnable task, final long timeout, final TimeUnit timeUnit) {
        if (timeout > 0) {
            return within(execute(task), timeout, timeUnit);
        }
        return execute(task);
    }

    @Override
    public <T> CompletableFuture<T> execute(final Supplier<T> task, final long timeout, final TimeUnit timeUnit) {
        if (timeout > 0) {
            return within(execute(task), timeout, timeUnit);
        }
        return execute(task);
    }

    private <T> CompletableFuture<T> failAfter(final CompletableFuture<T> future, final long timeout, final TimeUnit timeUnit) {
        final CompletableFuture<T> promise = new CompletableFuture<>();
        scheduler.schedule(() -> {
            if (!future.isDone()) {
                final RuntimeException ex = new RuntimeException("timeout after " + timeout + " " + timeUnit);
                promise.completeExceptionally(ex);
            }
        } , timeout, timeUnit);
        return promise;
    }

    private <T> CompletableFuture<T> within(final CompletableFuture<T> future, final long timeout, final TimeUnit timeUnit) {
        final CompletableFuture<T> timeoutFuture = failAfter(future, timeout, timeUnit);
        return future.applyToEither(timeoutFuture, Function.identity());
    }

}
