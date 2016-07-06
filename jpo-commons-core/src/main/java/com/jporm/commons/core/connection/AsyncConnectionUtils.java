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
package com.jporm.commons.core.connection;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncConnectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConnectionUtils.class);

    public static <R> CompletableFuture<R> close(final CompletableFuture<R> lastAction, final AsyncConnection connection) {
        return lastAction.handle((result, ex) -> {
            return connection.close();
        }).thenCompose(fn -> fn).thenCompose(fn -> lastAction);
    }

    public static <R> CompletableFuture<R> commitOrRollback(final boolean readOnly, final CompletableFuture<R> lastAction, final AsyncConnection connection) {
        return lastAction.handle((result, ex) -> {
            if (!readOnly && (ex == null)) {
                return connection.commit();
            }
            return connection.rollback();
        }).thenCompose(fn -> fn).thenCompose(fn -> lastAction);
    }

    public static CompletableFuture<AsyncConnection> start(final Supplier<CompletableFuture<AsyncConnection>> t) {
        LOGGER.debug("Asking for a connection");
        return t.get();
    }
}
