/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.rx.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.RuntimeErrorException;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jporm.commons.core.async.BlockingAsyncTaskExecutor;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.inject.ServiceCatalogImpl;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.json.NullJsonService;
import com.jporm.rx.BaseTestApi;
import com.jporm.rx.connection.datasource.DataSourceRxTransaction;
import com.jporm.rx.session.Session;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.dialect.h2.H2DBProfile;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;

public class TransactionImplTest extends BaseTestApi {

	private DataSourceRxTransaction tx;
	private Connection sqlConnection;

	@Before
	public void setUp() throws SQLException {
		final ServiceCatalog serviceCatalog =  new ServiceCatalogImpl();
		final SqlCache sqlCache = Mockito.mock(SqlCache.class);
		sqlConnection =  Mockito.mock(Connection.class);

		Mockito.when(sqlConnection.isClosed()).then(invocation -> {
			try {
				Mockito.verify(sqlConnection, Mockito.times(0)).close();
				return false;
			} catch (final Throwable e) {
				return true;
			}
		});

		final DataSource dataSource = Mockito.mock(DataSource.class);
		Mockito.when(dataSource.getConnection()).thenReturn(sqlConnection);
		final DBProfile dbProfile = new H2DBProfile();
		tx = new DataSourceRxTransaction(serviceCatalog, dbProfile, sqlCache, getSqlFactory(), dataSource, new NullJsonService(), new BlockingAsyncTaskExecutor().getExecutor(), new BlockingAsyncTaskExecutor().getExecutor());
	}

	@Test
	public void connectionShouldNotBeCalledWithoutSubscriber() throws InterruptedException, SQLException {

		final AtomicBoolean called = new AtomicBoolean(false);
		tx.execute((Session txSession) -> {
			getLogger().info("Execute");
			called.set(true);
			return Maybe.empty();
		});

		assertFalse(called.get());

		Mockito.verify(sqlConnection, Mockito.times(0)).commit();
		Mockito.verify(sqlConnection, Mockito.times(0)).rollback();
		Mockito.verify(sqlConnection, Mockito.times(0)).close();

	}

	@Test
	public void connectionShouldBeCommittedAndClosed() throws InterruptedException, SQLException {

		final Integer result = new Random().nextInt();

		final AtomicInteger called = new AtomicInteger(0);

		final Maybe<Integer> rxResult = tx.execute((Session txSession) -> {
			getLogger().info("Execute");
			called.getAndIncrement();
			return Maybe.just(result);
		});

		rxResult.subscribe(
				value -> {
					getLogger().info("Received [{}]", value);
					assertEquals(result, value);
				},
				ex -> {
					getLogger().error("Error [{}]", ex.getMessage(), ex);
					fail("The flow should not throw exceptions");
				});

		assertTrue(called.get() == 1);

		Mockito.verify(sqlConnection, Mockito.times(1)).commit();
		Mockito.verify(sqlConnection, Mockito.times(1)).close();
		Mockito.verify(sqlConnection, Mockito.times(0)).rollback();

	}

	@Test
	public void connectionShouldNotBeCommittedOnReadOnlyTransactions() throws InterruptedException, SQLException {

		final Integer result = new Random().nextInt();

		final AtomicInteger called = new AtomicInteger(0);

		final Maybe<Integer> rxResult = tx.readOnly(true).execute((Session txSession) -> {
			getLogger().info("Execute");
			called.getAndIncrement();
			return Maybe.just(result);
		});

		rxResult.subscribe(
				value -> {
					getLogger().info("Received [{}]", value);
					assertEquals(result, value);
				},
				ex -> {
					getLogger().error("Error [{}]", ex.getMessage(), ex);
					fail("The flow should not throw exceptions");
				});

		assertTrue(called.get() == 1);

		Mockito.verify(sqlConnection, Mockito.times(0)).commit();
		Mockito.verify(sqlConnection, Mockito.times(1)).rollback();
		Mockito.verify(sqlConnection, Mockito.times(1)).close();

	}

	@Test
	public void connectionShouldBeRollbackedAndClosed() throws InterruptedException, SQLException {

		final AtomicInteger called = new AtomicInteger(0);

		final Maybe<Integer> rxResult = tx.execute(new MaybeFunction<Integer>() {
			@Override
			public Maybe<Integer> apply(Session t) {
				getLogger().info("Execute");
				called.getAndIncrement();
				throw new RuntimeException();
			}
		});

		final TestObserver<Integer> subscriber = new TestObserver<>();
		rxResult.subscribe(subscriber);

		assertTrue(called.get() == 1);

		subscriber.awaitTerminalEvent();
		subscriber.assertError(RuntimeException.class);

		Mockito.verify(sqlConnection, Mockito.times(0)).commit();
		Mockito.verify(sqlConnection, Mockito.times(1)).rollback();
		Mockito.verify(sqlConnection, Mockito.times(1)).close();

	}

	@Test
	public void connectionShouldBeCommittedAndClosedOnlyOnce() throws InterruptedException, SQLException {

		final AtomicInteger called = new AtomicInteger(0);

		final Maybe<Integer> rxResult = tx.execute((Session txSession) -> {
			getLogger().info("Execute");
			called.getAndIncrement();
			return Maybe.just(3);
		});

		rxResult.subscribe(
				value -> {
					getLogger().info("Received [{}]", value);
				},
				ex -> {
					getLogger().error("Error [{}]", ex.getMessage(), ex);
					fail("The flow should not throw exceptions");
				});

		assertTrue(called.get() == 1);

		Mockito.verify(sqlConnection, Mockito.times(1)).commit();
		Mockito.verify(sqlConnection, Mockito.times(0)).rollback();
		Mockito.verify(sqlConnection, Mockito.times(1)).close();

	}

	@Test
	public void connectionShouldBeRolledBackAndClosedOnlyOnce() throws InterruptedException, SQLException {

		final AtomicInteger called = new AtomicInteger(0);

		final Maybe<Integer> rxResult = tx.execute((Session txSession) -> {
			getLogger().info("Execute");
			called.getAndIncrement();
			return Maybe.create(s -> {
				s.onError(new RuntimeErrorException(new Error()));
			});
		});

		final TestObserver<Integer> subscriber = new TestObserver<>();
		rxResult.subscribe(subscriber);

		assertTrue(called.get() == 1);

		subscriber.awaitTerminalEvent();
		subscriber.assertError(RuntimeErrorException.class);

		Mockito.verify(sqlConnection, Mockito.times(0)).commit();
		Mockito.verify(sqlConnection, Mockito.times(1)).rollback();
		Mockito.verify(sqlConnection, Mockito.times(1)).close();

	}
}
