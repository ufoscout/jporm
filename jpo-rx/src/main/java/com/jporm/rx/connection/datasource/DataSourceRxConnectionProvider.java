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

import java.util.concurrent.Executor;
import java.util.function.Function;

import javax.sql.DataSource;

import com.jporm.rm.connection.datasource.DataSourceConnectionImpl;
import com.jporm.rx.connection.RxConnectionProvider;
import com.jporm.rx.util.Futures;
import com.jporm.sql.dialect.DBProfile;

import io.reactivex.Observable;

public class DataSourceRxConnectionProvider implements RxConnectionProvider<DataSourceRxConnection> {

	private final DataSource dataSource;
	private final DBProfile dbProfile;
	private final Executor connectionExecutor;
	private final Executor executor;

	public DataSourceRxConnectionProvider(final DataSource dataSource, final DBProfile dbProfile, Executor connectionExecutor, Executor executor) {
		this.dataSource = dataSource;
		this.dbProfile = dbProfile;
		this.connectionExecutor = connectionExecutor;
		this.executor = executor;
	}

	@Override
	public <T> Observable<T> getConnection(boolean autoCommit, Function<DataSourceRxConnection, Observable<T>> callback) {

		return Futures.toSingle(connectionExecutor, () -> {
			try {
				return new DataSourceConnectionImpl(dataSource.getConnection(), dbProfile);
			} catch (final Throwable e) {
				throw new RuntimeException(e);
			}
		})
				.flatMapObservable(dsConnection -> {
					return Observable.using(() -> dsConnection, conn -> {
						conn.setAutoCommit(autoCommit);
						final DataSourceRxConnection connection = new DataSourceRxConnection(conn, executor);

						return callback.apply(connection);
					}, conn -> {
						executor.execute(() -> conn.close());
					}, false);
				});

	}

}
