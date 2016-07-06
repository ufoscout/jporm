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
package com.jporm.rx.reactor.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.RuntimeErrorException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.jporm.commons.core.connection.Connection;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.inject.ServiceCatalogImpl;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.rx.reactor.BaseTestApi;
import com.jporm.rx.reactor.connection.RxConnection;
import com.jporm.rx.reactor.connection.RxConnectionProvider;
import com.jporm.rx.reactor.connection.RxConnectionWrapper;
import com.jporm.rx.reactor.session.Session;

import rx.Observable;
import rx.Single;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

public class TransactionImplTest extends BaseTestApi {

    private TransactionImpl tx;
    private Connection rmConnection;
    private RxConnectionProvider connectionProvider;

    @Before
    public void setUp() {
        ServiceCatalog serviceCatalog =  new ServiceCatalogImpl();
        connectionProvider = Mockito.mock(RxConnectionProvider.class);
        SqlCache sqlCache = Mockito.mock(SqlCache.class);

        rmConnection =  Mockito.mock(Connection.class);
        RxConnection rxConnection = new RxConnectionWrapper(rmConnection, Schedulers.immediate());

        Session session = Mockito.mock(Session.class);

        Mockito.when(connectionProvider.getConnection(Matchers.anyBoolean())).thenReturn(Single.just(rxConnection));

        tx = new TransactionImpl(serviceCatalog, connectionProvider, sqlCache, getSqlFactory());
        tx.setSessionProvider((TransactionImpl txImpl, RxConnection rxConn) -> session);
    }

    @Test
    public void connectionShouldNotBeCalledWithoutSubscriber() throws InterruptedException {

        AtomicBoolean created = new AtomicBoolean(false);

        Mockito.when(connectionProvider.getConnection(Matchers.anyBoolean())).thenReturn(Single.create(s -> {
            created.set(true);
        }));

        AtomicBoolean called = new AtomicBoolean(false);
        tx.execute((Session txSession) -> {
            getLogger().info("Execute");
            called.set(true);
            return Observable.just("");
        });

        assertFalse(created.get());
        assertFalse(called.get());

        Mockito.verify(rmConnection, Mockito.times(0)).commit();
        Mockito.verify(rmConnection, Mockito.times(0)).rollback();
        Mockito.verify(rmConnection, Mockito.times(0)).close();

    }

    @Test
    public void connectionShouldBeCommittedAndClosed() throws InterruptedException {

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

        Mockito.verify(connectionProvider, Mockito.times(1)).getConnection(false);
        Mockito.verify(connectionProvider, Mockito.times(0)).getConnection(true);
        Mockito.verify(rmConnection, Mockito.times(1)).commit();
        Mockito.verify(rmConnection, Mockito.times(0)).rollback();
        Mockito.verify(rmConnection, Mockito.times(1)).close();

    }

    @Test
    public void connectionShouldNotBeCommittedOnReadOnlyTransactions() throws InterruptedException {

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

        Mockito.verify(connectionProvider, Mockito.times(1)).getConnection(false);
        Mockito.verify(connectionProvider, Mockito.times(0)).getConnection(true);
        Mockito.verify(rmConnection, Mockito.times(0)).commit();
        Mockito.verify(rmConnection, Mockito.times(1)).rollback();
        Mockito.verify(rmConnection, Mockito.times(1)).close();

    }

    @Test
    public void connectionShouldBeRollbackedAndClosed() throws InterruptedException {

        AtomicInteger called = new AtomicInteger(0);

        Observable<Integer> rxResult = tx.<Integer>execute((Session txSession) -> {
            getLogger().info("Execute");
            called.getAndIncrement();
            throw new RuntimeException();
        });

        TestSubscriber<Integer> subscriber = new TestSubscriber<>();
        rxResult.subscribe(subscriber);

        assertTrue(called.get() == 1);

        subscriber.awaitTerminalEvent();
        subscriber.assertError(RuntimeException.class);

        Mockito.verify(connectionProvider, Mockito.times(1)).getConnection(false);
        Mockito.verify(connectionProvider, Mockito.times(0)).getConnection(true);
        Mockito.verify(rmConnection, Mockito.times(0)).commit();
        Mockito.verify(rmConnection, Mockito.times(1)).rollback();
        Mockito.verify(rmConnection, Mockito.times(1)).close();

    }

    @Test
    public void connectionShouldBeCommittedAndClosedOnlyOnce() throws InterruptedException {

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

        Mockito.verify(connectionProvider, Mockito.times(1)).getConnection(false);
        Mockito.verify(connectionProvider, Mockito.times(0)).getConnection(true);
        Mockito.verify(rmConnection, Mockito.times(1)).commit();
        Mockito.verify(rmConnection, Mockito.times(0)).rollback();
        Mockito.verify(rmConnection, Mockito.times(1)).close();

    }

    @Test
    public void connectionShouldBeRolledBackAndClosedOnlyOnce() throws InterruptedException {

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

        Mockito.verify(connectionProvider, Mockito.times(1)).getConnection(false);
        Mockito.verify(connectionProvider, Mockito.times(0)).getConnection(true);
        Mockito.verify(rmConnection, Mockito.times(0)).commit();
        Mockito.verify(rmConnection, Mockito.times(1)).rollback();
        Mockito.verify(rmConnection, Mockito.times(1)).close();

    }
}
