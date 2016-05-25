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
package com.jporm.rx.reactor.connection;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Random;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.jporm.commons.core.connection.Connection;
import com.jporm.commons.core.connection.DataSourceConnection;
import com.jporm.rx.reactor.BaseTestApi;
import com.jporm.sql.dialect.h2.H2DBProfile;

import rx.Observable;
import rx.schedulers.Schedulers;

public class RxConnectionWrapperTest extends BaseTestApi {

    @Test
    public void connectionShouldBeClosed() throws SQLException {

        java.sql.Connection sqlConnection = Mockito.mock(java.sql.Connection.class);
        Connection rmConnection = new DataSourceConnection(sqlConnection, new H2DBProfile());

        RxConnection rxConnection = new RxConnectionWrapper(rmConnection, Schedulers.immediate());
        rxConnection.close()
        .subscribe();

        Mockito.verify(sqlConnection, Mockito.times(1)).close();

    }

    @Test
    public void connectionShouldBeCommittedAndClosed() throws SQLException {

        Connection rmConnection = Mockito.mock(Connection.class);

        RxConnection rxConnection = new RxConnectionWrapper(rmConnection, Schedulers.immediate());

        Integer result = new Random().nextInt();
        Mockito.when(rmConnection.update(Matchers.anyString(), Matchers.any())).thenReturn(result);

        Observable<Integer> updateResult = rxConnection.update("", statement -> {});
        Observable<Integer> updateResult2 = updateResult
                .flatMap(intResult ->
                    rxConnection.commit()
                    .endWith(rxConnection.close())
                    .endWith(updateResult)
                );

        updateResult2.subscribe();

        Mockito.verify(rmConnection, Mockito.times(1)).commit();
        Mockito.verify(rmConnection, Mockito.times(1)).close();

        assertEquals(result, updateResult2.toBlocking().last());

    }

    @Test
    public void connectionShouldBeRollbackedAndClosed() throws SQLException {

        Connection rmConnection = Mockito.mock(Connection.class);

        RxConnection rxConnection = new RxConnectionWrapper(rmConnection, Schedulers.immediate());

        Mockito.when(rmConnection.update(Matchers.anyString(), Matchers.any())).thenThrow(new RuntimeException());

        Observable<Integer> updateResult = rxConnection.update("", statement -> {});

        Observable<Integer> updateResult2 = updateResult
                .flatMap(intResult ->
                    rxConnection.commit()
                    .endWith(rxConnection.close())
                    .endWith(updateResult)
                ).doOnError(ex -> {
                        getLogger().info("updateResult2 doOnError");
                        rxConnection.rollback()
                        .doAfterTerminate(() -> rxConnection.close().subscribe()).subscribe();
                });

        updateResult2.subscribe(
                value -> {},
                ex -> {
                    getLogger().info("Error received");
                });

        Mockito.verify(rmConnection, Mockito.times(0)).commit();
        Mockito.verify(rmConnection, Mockito.times(1)).rollback();
        Mockito.verify(rmConnection, Mockito.times(1)).close();

    }

    @Test
    public void connectionShouldBeRollbackedAndClosed2() throws SQLException {

        Connection rmConnection = Mockito.mock(Connection.class);

        RxConnection rxConnection = new RxConnectionWrapper(rmConnection, Schedulers.immediate());

        Mockito.when(rmConnection.update(Matchers.anyString(), Matchers.any())).thenThrow(new RuntimeException());

        Observable<Integer> updateResult = rxConnection.update("", statement -> {});

        Observable<Integer> updateResult2 = updateResult
                .flatMap(intResult ->
                    rxConnection.commit()
                    .endWith(rxConnection.close())
                    .onErrorResumeNext(ex2 -> rxConnection.close())
                    .endWith(updateResult)
                ).onErrorResumeNext(ex -> {
                        getLogger().info("updateResult2 doOnError");
                        return rxConnection.rollback()
                                .onErrorResumeNext(ex2 -> rxConnection.close())
                                .endWith(rxConnection.close())
                                .endWith(updateResult);
                });

        updateResult2.subscribe(
                value -> {},
                ex -> {
                    getLogger().info("Error received");
                });

        Mockito.verify(rmConnection, Mockito.times(0)).commit();
        Mockito.verify(rmConnection, Mockito.times(1)).rollback();
        Mockito.verify(rmConnection, Mockito.times(1)).close();

    }


}
