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
package com.jporm.async.spike;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

import com.jporm.JPO;
import com.jporm.async.BaseTestAsync;
import com.jporm.async.domain.People;
import com.jporm.session.Session;

public class CompletableFuturesTest extends BaseTestAsync {

	private JPO jpo;
	private People people;

	@Before
	public void setUp() {
		jpo = getJPO();

		Session session = jpo.session();
		people = session.txNow(_session -> {
			People _people = new People();
			_people.setFirstname(UUID.randomUUID().toString());
			return _session.save(_people).now();
		});
		assertNotNull(people);

	}

	@Test
	public void testCompletableFuturesWithSession() throws InterruptedException, ExecutionException {
		assertEquals(people.getId(), new FutureSession().find(people.getId()).get().getId() );
	}

	@Test
	public void testCompletableFuturesChain() throws InterruptedException, ExecutionException {
		FutureSession futureSession = new FutureSession();
		CompletableFuture<People> future = futureSession.find(people.getId())
				.thenApply(resultPeople -> resultPeople.getFirstname())
				.thenCompose(resultName -> futureSession.findByFirstName(resultName));

		assertEquals(people.getId(), future.get().getId() );
	}

	@Test
	public void testCompletableFuturesHandlers() throws InterruptedException, ExecutionException {

		FutureSession futureSession = new FutureSession();
		CompletableFuture<People> future = futureSession.find(people.getId())
				.thenApply(resultPeople -> resultPeople.getFirstname())
				.thenCompose(resultName -> futureSession.findByFirstName(resultName));

		BlockingQueue<People> queue = new ArrayBlockingQueue<People>(10);
		future.whenComplete((people, ex) -> queue.offer(people));

		People futurePeople = queue.poll(2, TimeUnit.SECONDS);

		assertEquals(people.getId(), futurePeople.getId() );
	}

	@Test
	public void testCompletableFuturesExceptions() throws InterruptedException, ExecutionException {

		FutureSession futureSession = new FutureSession();
		CompletableFuture<Object> future = futureSession.exception();

		future.whenComplete((obj, ex) -> {
			getLogger().info("received obj [{}]", obj);
			getLogger().info("received exception [{}]", ex.getMessage());
		});
	}

	@Test
	public void testCompletableFutureEndBeforeTimeout() throws InterruptedException, ExecutionException {
		FutureSession futureSession = new FutureSession();

		CompletableFuture<String> future = futureSession.timeout("value", 100, 500);
		assertEquals("value", future.get());

		Thread.sleep(1000);
	}

	@Test
	public void testCompletableFutureEndAfterTimeout() throws InterruptedException, ExecutionException {
		FutureSession futureSession = new FutureSession();
		CompletableFuture<String> future = futureSession.timeout("value", 500, 100);

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

	}

	class FutureSession {

		Executor executor = Executors.newFixedThreadPool(10);

		CompletableFuture<People> find(Object id) {
			CompletableFuture<People> future = new CompletableFuture<>();
			executor.execute(new Runnable() {
				@Override
				public void run() {
					People bean = jpo.session().find(People.class, id).get();
					future.complete(bean);
				}
			});
			return future;
		}

		CompletableFuture<People> findByFirstName(String name) {
			return CompletableFuture.supplyAsync(() -> {
				return jpo.session().findQuery(People.class).where("firstname = ?", name).get();
			}, executor);
		}

		CompletableFuture<Object> exception() {
			return CompletableFuture.supplyAsync(() -> {
				throw new RuntimeException("Manually thrown exception");
			}, executor);
		}


		//TIMEOUT HANDLING REQUIRES A DEDICATED THREAD
		private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		private <T> CompletableFuture<T> failAfter(CompletableFuture<T> future, long millis) {
			final CompletableFuture<T> promise = new CompletableFuture<>();
			scheduler.schedule(() -> {
				if (!future.isDone()) {
					getLogger().warn("Throwing timeout exception after {}ms", millis);
					final RuntimeException ex = new RuntimeException("timeout after " + millis);
					promise.completeExceptionally(ex);
				}
			}, millis, TimeUnit.MILLISECONDS);
			return promise;
		}

		private <T> CompletableFuture<T> within(CompletableFuture<T> future, long millis) {
			final CompletableFuture<T> timeout = failAfter(future, millis);
			return future.applyToEither(timeout, Function.identity());
		}

		<T> CompletableFuture<T> timeout(T value, long wait, long timeout) {
			CompletableFuture<T> future = new CompletableFuture<>();
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(wait);
					} catch (InterruptedException e) {
					}
					future.complete(value);
				}
			});
			return within(future, timeout);
		}

	}

}
