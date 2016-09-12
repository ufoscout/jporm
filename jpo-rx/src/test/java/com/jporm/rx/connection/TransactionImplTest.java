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

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.inject.ServiceCatalogImpl;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.rx.BaseTestApi;
import com.jporm.rx.connection.datasource.DataSourceRxTransaction;
import com.jporm.rx.session.Session;
import com.jporm.sql.dialect.h2.H2DBProfile;

import rx.Observable;
import rx.observers.TestSubscriber;

public class TransactionImplTest extends BaseTestApi {

    private DataSourceRxTransaction tx;
    private Connection sqlConnection;

    @Before
    public void setUp() throws SQLException {
        ServiceCatalog serviceCatalog =  new ServiceCatalogImpl();
        SqlCache sqlCache = Mockito.mock(SqlCache.class);
        sqlConnection =  Mockito.mock(Connection.class);
        DataSource dataSource = Mockito.mock(DataSource.class);
        Mockito.when(dataSource.getConnection()).thenReturn(sqlConnection);
        tx = new DataSourceRxTransaction(serviceCatalog, new H2DBProfile(), sqlCache, getSqlFactory(), dataSource);
    }

    @Test
    public void connectionShouldNotBeCalledWithoutSubscriber() throws InterruptedException, SQLException {

        AtomicBoolean called = new AtomicBoolean(false);
        tx.execute((Session txSession) -> {
            getLogger().info("Execute");
            called.set(true);
            return Observable.just("");
        });

        assertFalse(called.get());

        Mockito.verify(sqlConnection, Mockito.times(0)).commit();
        Mockito.verify(sqlConnection, Mockito.times(0)).rollback();
        Mockito.verify(sqlConnection, Mockito.times(0)).close();

    }

    @Test
    public void connectionShouldBeCommittedAndClosed() throws InterruptedException, SQLException {

        Integer result = new Random().nextInt();

        AtomicInteger called = new AtomicInteger(0);

        Observable<Integer> rxResult = tx.execute((Session txSession) -> {
            getLogger().info("Execute");
            called.getAndIncrement();
            return Observable.just(result);
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
        Mockito.verify(sqlConnection, Mockito.times(0)).rollback();
        Mockito.verify(sqlConnection, Mockito.times(1)).close();

    }

    @Test
    public void connectionShouldNotBeCommittedOnReadOnlyTransactions() throws InterruptedException, SQLException {

        Integer result = new Random().nextInt();

        AtomicInteger called = new AtomicInteger(0);

        Observable<Integer> rxResult = tx.readOnly(true).execute((Session txSession) -> {
            getLogger().info("Execute");
            called.getAndIncrement();
            return Observable.just(result);
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

        AtomicInteger called = new AtomicInteger(0);

        Observable<Integer> rxResult = tx.execute(new ObservableFunction<Integer>() {
            @Override
            public Observable<Integer> apply(Session t) {
                getLogger().info("Execute");
                called.getAndIncrement();
                throw new RuntimeException();
            }
        });

        TestSubscriber<Integer> subscriber = new TestSubscriber<>();
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

        AtomicInteger called = new AtomicInteger(0);

        Observable<Integer> rxResult = tx.execute((Session txSession) -> {
            getLogger().info("Execute");
            called.getAndIncrement();
            return Observable.just(1, 2, 3);
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

        AtomicInteger called = new AtomicInteger(0);

        Observable<Integer> rxResult = tx.execute((Session txSession) -> {
            getLogger().info("Execute");
            called.getAndIncrement();
            return Observable.create(s -> {
                s.onNext(1);
                s.onNext(2);
                s.onError(new RuntimeErrorException(new Error()));
            });
        });

        TestSubscriber<Integer> subscriber = new TestSubscriber<>();
        rxResult.subscribe(subscriber);

        assertTrue(called.get() == 1);

        subscriber.awaitTerminalEvent();
        subscriber.assertError(RuntimeErrorException.class);

        Mockito.verify(sqlConnection, Mockito.times(0)).commit();
        Mockito.verify(sqlConnection, Mockito.times(1)).rollback();
        Mockito.verify(sqlConnection, Mockito.times(1)).close();

    }
}
