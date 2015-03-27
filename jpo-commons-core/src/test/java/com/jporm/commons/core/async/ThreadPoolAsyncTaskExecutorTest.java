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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.jporm.commons.core.BaseCommonsCoreTestApi;
import com.jporm.commons.core.async.impl.ThreadPoolTimedAsyncTaskExecutor;

public class ThreadPoolAsyncTaskExecutorTest extends BaseCommonsCoreTestApi {

	private AsyncTimedTaskExecutor executor = new ThreadPoolTimedAsyncTaskExecutor(10, "executor-test");

	@Test
	public void testCompletableFuturesWithSession() throws InterruptedException, ExecutionException {
		assertEquals(Boolean.TRUE, find(Boolean.TRUE).get() );
	}

	@Test
	public void testCompletableFuturesChain() throws InterruptedException, ExecutionException {
		CompletableFuture<Boolean> future = find(Boolean.FALSE)
				.thenApply(result -> !result);

		assertEquals(Boolean.TRUE, future.get() );
	}

	@Test
	public void testCompletableFuturesHandlers() throws InterruptedException, ExecutionException {

		CompletableFuture<Boolean> future = find(Boolean.FALSE)
				.thenApply(result -> !result);

		BlockingQueue<Boolean> queue = new ArrayBlockingQueue<Boolean>(10);
		future.whenComplete((people, ex) -> queue.offer(people));

		Boolean futureBoolean = queue.poll(2, TimeUnit.SECONDS);

		assertEquals(Boolean.TRUE, futureBoolean );
	}

	@Test
	public void testCompletableFuturesExceptions() throws InterruptedException, ExecutionException {

		CompletableFuture<Void> future = exception();

		BlockingQueue<Throwable> queue = new ArrayBlockingQueue<>(10);
		future.whenComplete((obj, ex) -> {
			getLogger().info("received obj [{}]", obj);
			getLogger().info("received exception [{}]", ex.getMessage());
			queue.offer(ex);
		});

		assertTrue(queue.poll(500, TimeUnit.MILLISECONDS).getMessage().contains("Manually thrown exception"));
	}

	@Test
	public void testCompletableFutureEndBeforeTimeout() throws InterruptedException, ExecutionException {
		CompletableFuture<String> future = timeout("value", 100, 500);
		assertEquals("value", future.get());
		Thread.sleep(100);
	}

	@Test
	public void testCompletableFutureEndAfterTimeout() throws InterruptedException, ExecutionException {
		CompletableFuture<String> future = timeout("value", 500, 100);

		BlockingQueue<Throwable> queue = new ArrayBlockingQueue<Throwable>(1);
		future.whenComplete((result, ex) -> queue.offer(ex));

		Throwable ex = queue.poll(2, TimeUnit.SECONDS);
		assertTrue( ex instanceof RuntimeException );
		assertTrue( ex.getMessage().contains("timeout"));
	}

	@Test
	public void testExecutorSchedulerExecutionOrder() throws InterruptedException {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

		List<Integer> numbers = new ArrayList<Integer>();

		scheduler.schedule(() -> numbers.add(30), 30, TimeUnit.MILLISECONDS);
		scheduler.schedule(() -> numbers.add(20), 20, TimeUnit.MILLISECONDS);
		scheduler.schedule(() -> numbers.add(40), 40, TimeUnit.MILLISECONDS);
		scheduler.schedule(() -> numbers.add(50), 50, TimeUnit.MILLISECONDS);
		scheduler.schedule(() -> numbers.add(10), 10, TimeUnit.MILLISECONDS);

		Thread.sleep(100);

		getLogger().info("Result is {}", numbers);

		int index = 0;
		assertEquals(10, numbers.get(index++).intValue());
		assertEquals(20, numbers.get(index++).intValue());
		assertEquals(30, numbers.get(index++).intValue());
		assertEquals(40, numbers.get(index++).intValue());
		assertEquals(50, numbers.get(index++).intValue());

	}

	private <T> CompletableFuture<T> find(T value) {
		return executor.execute(() -> {
			return value;
		});
	}

	private CompletableFuture<Void> exception() {
		return executor.execute(() -> {
			throw new RuntimeException("Manually thrown exception");
		});
	}

	private <T> CompletableFuture<T> timeout(T value, long wait, long timeout) {
		return executor.execute(() -> {
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
			}
			return value;
		}, timeout, TimeUnit.MILLISECONDS);
	}
}
