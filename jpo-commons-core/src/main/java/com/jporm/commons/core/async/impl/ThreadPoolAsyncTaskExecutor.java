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
package com.jporm.commons.core.async.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import com.jporm.commons.core.async.AsyncTaskExecutor;

public class ThreadPoolAsyncTaskExecutor implements AsyncTaskExecutor {

    private static final AtomicInteger INSTANCE_COUNT = new AtomicInteger(0);
    private static final String DEFAULT_JPO_THREAD_POOL_NAME = "jpo-pool-";
    private final Executor executor;

    public ThreadPoolAsyncTaskExecutor(final int nThreads) {
        this(nThreads, DEFAULT_JPO_THREAD_POOL_NAME + INSTANCE_COUNT.getAndIncrement());
    }

    public ThreadPoolAsyncTaskExecutor(final int nThreads, final String threadPoolName) {
        executor = new ThreadPoolExecutor(nThreads, nThreads, 1000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                new NamedThreadPoolFactory(threadPoolName, false));
    }

    @Override
    public CompletableFuture<Void> execute(final Runnable task) {
        return CompletableFuture.runAsync(task, executor);
    }

    @Override
    public <T> CompletableFuture<T> execute(final Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, executor);
    }

}
