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
package com.jporm.rx.reactor.connection;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.jporm.sql.dialect.DBProfile;

import rx.Scheduler;
import rx.Single;
import rx.schedulers.Schedulers;

public class RxConnectionWrapperProvider implements RxConnectionProvider {

    private final Scheduler connectionScheduler = Schedulers.from(Executors.newFixedThreadPool(2));
    private final Scheduler executionScheduler;
    private final com.jporm.commons.core.connection.ConnectionProvider rmConnectionProvider;

    public RxConnectionWrapperProvider(final com.jporm.commons.core.connection.ConnectionProvider rmConnectionProvider, final Executor executor) {
        this.rmConnectionProvider = rmConnectionProvider;
        executionScheduler = Schedulers.from(executor);
    }

    @Override
    public Single<RxConnection> getConnection(final boolean autoCommit) {
        return Single.<RxConnection>fromCallable(() -> {
            return new RxConnectionWrapper(rmConnectionProvider.getConnection(autoCommit), executionScheduler);
        })
        .subscribeOn(connectionScheduler);
    }

    @Override
    public DBProfile getDBProfile() {
        return rmConnectionProvider.getDBProfile();
    }

}
