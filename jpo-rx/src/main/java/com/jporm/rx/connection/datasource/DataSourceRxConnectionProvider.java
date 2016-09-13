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
package com.jporm.rx.connection.datasource;

import java.util.function.Function;

import javax.sql.DataSource;

import com.jporm.rm.connection.datasource.DataSourceConnectionImpl;
import com.jporm.rx.connection.RxConnectionProvider;
import com.jporm.sql.dialect.DBProfile;

import rx.Observable;
import rx.Single;

public class DataSourceRxConnectionProvider implements RxConnectionProvider<DataSourceRxConnection> {

    private final DataSource dataSource;
    private final DBProfile dbProfile;

    public DataSourceRxConnectionProvider(final DataSource dataSource, final DBProfile dbProfile) {
        this.dataSource = dataSource;
        this.dbProfile = dbProfile;
    }


    @Override
    public <T> Observable<T> getConnection(boolean autoCommit, Function<DataSourceRxConnection, Observable<T>> callback) {
        return Single.fromCallable(() -> {
            try {
                return new DataSourceConnectionImpl(dataSource.getConnection(), dbProfile);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }).flatMapObservable(dsConnection -> {
            try {
                dsConnection.setAutoCommit(autoCommit);
                DataSourceRxConnection connection = new DataSourceRxConnection(dsConnection);
                return callback.apply(connection).doAfterTerminate(() -> {
                    dsConnection.close();
                });
            } catch (RuntimeException e) {
                dsConnection.close();
                throw e;
            } catch (Throwable e) {
                dsConnection.close();
                throw new RuntimeException(e);
            }
        });
    }

}
