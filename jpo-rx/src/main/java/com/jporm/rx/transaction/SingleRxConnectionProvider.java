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
package com.jporm.rx.transaction;

import com.jporm.rx.connection.RxConnection;
import com.jporm.rx.connection.RxConnectionProvider;
import com.jporm.sql.dialect.DBProfile;

import rx.Single;

public class SingleRxConnectionProvider implements RxConnectionProvider {

    private final RxConnection connection;
    private final RxConnectionProvider connectionProvider;

    public SingleRxConnectionProvider(final RxConnection connection, final RxConnectionProvider connectionProvider) {
        this.connection = connection;
        this.connectionProvider = connectionProvider;

    }

    @Override
    public Single<RxConnection> getConnection(final boolean autoCommit) {
        return Single.just(connection);
    }

    @Override
    public DBProfile getDBProfile() {
        return connectionProvider.getDBProfile();
    }

}
