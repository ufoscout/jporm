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

import static org.junit.Assert.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.mockito.Mockito;

import com.jporm.rx.core.BaseTestApi;

public class ConnectionUtilsTest extends BaseTestApi {

	@Test
	public void connection_shoul_be_closed_after_execution() throws Throwable {

		final AtomicBoolean closed = new AtomicBoolean(false);
		Connection connection = Mockito.mock(Connection.class);
		Mockito.when(connection.close()).then(invocation -> {
			closed.set(true);
			return null;
		}).thenReturn(CompletableFuture.completedFuture(null));

		CompletableFuture<String> action = new CompletableFuture<>();

		CompletableFuture<String> afterConnectionClose = ConnectionUtils.closeConnection(action, connection);
		action.complete("hello");

		assertTrue(closed.get());
		assertEquals("hello", afterConnectionClose.get());
	}

	@Test
	public void connection_shoul_be_closed_after_exception() throws Throwable {

		final AtomicBoolean closed = new AtomicBoolean(false);
		Connection connection = Mockito.mock(Connection.class);
		Mockito.when(connection.close()).then(invocation -> {
			closed.set(true);
			return null;
		}).thenReturn(CompletableFuture.completedFuture(null));

		CompletableFuture<Void> action = new CompletableFuture<>();
		CompletableFuture<Void> afterConnectionClose = ConnectionUtils.closeConnection(action, connection);

		action.completeExceptionally(new RuntimeException("helloException"));
		assertTrue(closed.get());

		afterConnectionClose.exceptionally(ex -> {
			assertEquals("helloException", ex.getMessage());
			return null;
		});


	}

}
