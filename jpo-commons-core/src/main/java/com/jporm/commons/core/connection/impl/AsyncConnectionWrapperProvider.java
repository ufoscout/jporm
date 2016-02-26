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
package com.jporm.commons.core.connection.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.async.impl.ThreadPoolAsyncTaskExecutor;
import com.jporm.commons.core.connection.AsyncConnection;
import com.jporm.commons.core.connection.AsyncConnectionProvider;
import com.jporm.sql.dsl.dialect.DBType;

public class AsyncConnectionWrapperProvider implements AsyncConnectionProvider {

    private final static AtomicInteger COUNT = new AtomicInteger(0);
    private final AsyncTaskExecutor connectionExecutor = new ThreadPoolAsyncTaskExecutor(2, "jpo-connection-get-pool-" + COUNT.getAndIncrement());
    private final AsyncTaskExecutor executor;
    private final com.jporm.commons.core.connection.ConnectionProvider rmConnectionProvider;

    public AsyncConnectionWrapperProvider(final com.jporm.commons.core.connection.ConnectionProvider rmConnectionProvider, final AsyncTaskExecutor executor) {
        this.rmConnectionProvider = rmConnectionProvider;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<AsyncConnection> getConnection(final boolean autoCommit) {
        return connectionExecutor.execute(() -> {
            return new AsyncConnectionWrapper(rmConnectionProvider.getConnection(autoCommit), executor);
        });
    }

    @Override
    public CompletableFuture<DBType> getDBType() {
        return CompletableFuture.completedFuture(rmConnectionProvider.getDBType());
    }

}
