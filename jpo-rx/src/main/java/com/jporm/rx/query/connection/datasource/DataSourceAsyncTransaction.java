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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.inject.config.ConfigService;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rm.connection.datasource.DataSourceConnectionImpl;
import com.jporm.rx.query.connection.AsyncConnection;
import com.jporm.rx.query.connection.AsyncConnectionProvider;
import com.jporm.rx.query.connection.AsyncTransaction;
import com.jporm.rx.session.Session;
import com.jporm.rx.session.SessionImpl;
import com.jporm.sql.dialect.DBProfile;

public class DataSourceAsyncTransaction implements AsyncTransaction {

	private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceAsyncTransaction.class);
	private final ServiceCatalog serviceCatalog;
	private final SqlCache sqlCache;
	private final SqlFactory sqlFactory;
	private final DBProfile dbProfile;
	private final AsyncTaskExecutor connectionExecutor;
	private final AsyncTaskExecutor executor;

	private TransactionIsolation transactionIsolation;
	private int timeout;
	private boolean readOnly = false;
	private final DataSource dataSource;

	public DataSourceAsyncTransaction(final ServiceCatalog serviceCatalog, DBProfile dbProfile, SqlCache sqlCache,
			SqlFactory sqlFactory, DataSource dataSource, AsyncTaskExecutor connectionExecutor, AsyncTaskExecutor executor) {
		this.serviceCatalog = serviceCatalog;
		this.dbProfile = dbProfile;
		this.sqlCache = sqlCache;
		this.sqlFactory = sqlFactory;
		this.dataSource = dataSource;
		this.connectionExecutor = connectionExecutor;
		this.executor = executor;

		final ConfigService configService = serviceCatalog.getConfigService();
		transactionIsolation = configService.getDefaultTransactionIsolation();
		timeout = configService.getTransactionDefaultTimeoutSeconds();

	}


	@Override
	public AsyncTransaction isolation(final TransactionIsolation isolation) {
		transactionIsolation = isolation;
		return this;
	}

	@Override
	public AsyncTransaction readOnly(final boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}

	private void setTimeout(final AsyncConnection connection) {
		if (timeout > 0) {
			connection.setTimeout(timeout);
		}
	}

	private void setTransactionIsolation(final AsyncConnection connection) {
		connection.setTransactionIsolation(transactionIsolation);
	}

	@Override
	public AsyncTransaction timeout(final int seconds) {
		timeout = seconds;
		return this;
	}

	@Override
	public <T> CompletableFuture<T> execute(Function<Session, CompletableFuture<T>> session) {

		final CompletableFuture<DataSourceConnectionImpl> conn = connectionExecutor.execute(() -> {
			try {
				final DataSourceConnectionImpl connection = new DataSourceConnectionImpl(dataSource.getConnection(), dbProfile);
				connection.setAutoCommit(false);
				return connection;
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		});

		return conn.thenCompose(rmConnection -> {

			final AsyncConnection rxConnection = new DataSourceAsyncConnection(rmConnection, executor);
			setTransactionIsolation(rxConnection);
			setTimeout(rxConnection);
			rxConnection.setReadOnly(readOnly);

			final SessionImpl sess = new SessionImpl(serviceCatalog, dbProfile,
					new AsyncConnectionProvider<AsyncConnection>() {
						@Override
						public <R> CompletableFuture<R> getConnection(boolean autoCommit, Function<AsyncConnection, CompletableFuture<R>> connection) {
							return connection.apply(rxConnection);
						}
					},
					sqlCache, sqlFactory);

			return session.apply(sess).whenComplete((r, t) -> {
				try {
					if (t==null) {
						rmConnection.commit();
					} else {
						rmConnection.rollback();
					}
				}
				finally {
					rmConnection.close();
				}
			});

		});

	}

}
