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
package com.jporm.rx.query.connection.datasource;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import javax.sql.DataSource;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.rm.connection.datasource.DataSourceConnectionImpl;
import com.jporm.rx.query.connection.AsyncConnectionProvider;
import com.jporm.sql.dialect.DBProfile;

public class DataSourceAsyncConnectionProvider implements AsyncConnectionProvider<DataSourceAsyncConnection> {

	private final DataSource dataSource;
	private final DBProfile dbProfile;
	private final AsyncTaskExecutor connectionExecutor;
	private final AsyncTaskExecutor executor;

	public DataSourceAsyncConnectionProvider(final DataSource dataSource, final DBProfile dbProfile, AsyncTaskExecutor connectionExecutor, AsyncTaskExecutor executor) {
		this.dataSource = dataSource;
		this.dbProfile = dbProfile;
		this.connectionExecutor = connectionExecutor;
		this.executor = executor;
	}

	@Override
	public <T> CompletableFuture<T> getConnection(boolean autoCommit, Function<DataSourceAsyncConnection, CompletableFuture<T>> session) {

		return connectionExecutor.execute(() -> {
			try {
				final DataSourceConnectionImpl connection = new DataSourceConnectionImpl(dataSource.getConnection(), dbProfile);
				connection.setAutoCommit(autoCommit);
				return connection;
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}).thenCompose(connection -> {
			return session
					.apply(new DataSourceAsyncConnection(connection, executor))
					.whenComplete((r, t) -> {
						connection.close();
					});
		});

	}

}
