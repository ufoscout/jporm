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
package com.jporm.rx.core.connection;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionUtils.class);

	public static CompletableFuture<Connection> start(Supplier<CompletableFuture<Connection>> t) {
		LOGGER.debug("Asking for a connection");
		return t.get();
	}

	public static <R> CompletableFuture<R> commitOrRollback(CompletableFuture<R> lastAction, Connection connection) {
		return lastAction.handle((result, ex) -> {
			if (ex == null) {
				LOGGER.debug("Commit transaction");
				return connection.commit();
			}
			LOGGER.debug("Rollback transaction");
			return connection.rollback();
		}).thenCompose(fn -> fn).thenCompose(fn -> lastAction);
	}

	public static <R> CompletableFuture<R> close(CompletableFuture<R> lastAction, Connection connection) {
		return lastAction.handle((result, ex) -> {
			LOGGER.debug("Closing connection");
			return connection.close();
		}).thenCompose(fn -> fn).thenCompose(fn -> lastAction);
	}
}
