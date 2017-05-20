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
package com.jporm.rx.rxjava2.connection.datasource;

import java.util.concurrent.Executor;
import java.util.function.Function;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.inject.config.ConfigService;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rm.connection.datasource.DataSourceConnectionImpl;
import com.jporm.rx.rxjava2.connection.MaybeFunction;
import com.jporm.rx.rxjava2.connection.RxConnection;
import com.jporm.rx.rxjava2.connection.RxConnectionProvider;
import com.jporm.rx.rxjava2.connection.RxTransaction;
import com.jporm.rx.rxjava2.connection.SingleFunction;
import com.jporm.rx.rxjava2.session.Session;
import com.jporm.rx.rxjava2.session.SessionImpl;
import com.jporm.rx.rxjava2.util.Futures;
import com.jporm.sql.dialect.DBProfile;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class DataSourceRxTransaction implements RxTransaction {

	private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceRxTransaction.class);
	private final ServiceCatalog serviceCatalog;
	private final SqlCache sqlCache;
	private final SqlFactory sqlFactory;
	private final DBProfile dbProfile;
	private final Executor connectionExecutor;
	private final Executor executor;

	private TransactionIsolation transactionIsolation;
	private int timeout;
	private boolean readOnly = false;
	private final DataSource dataSource;

	public DataSourceRxTransaction(final ServiceCatalog serviceCatalog, DBProfile dbProfile, SqlCache sqlCache,
			SqlFactory sqlFactory, DataSource dataSource, Executor connectionExecutor, Executor executor) {
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
	public RxTransaction isolation(final TransactionIsolation isolation) {
		transactionIsolation = isolation;
		return this;
	}

	@Override
	public RxTransaction readOnly(final boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}

	private void setTimeout(final RxConnection connection) {
		if (timeout > 0) {
			connection.setTimeout(timeout);
		}
	}

	private void setTransactionIsolation(final RxConnection connection) {
		connection.setTransactionIsolation(transactionIsolation);
	}

	@Override
	public RxTransaction timeout(final int seconds) {
		timeout = seconds;
		return this;
	}

	@Override
	public <T> Maybe<T> execute(MaybeFunction<T> txSession) {
		return Futures.toMaybe(connectionExecutor, () -> {
			try {
				return new DataSourceConnectionImpl(dataSource.getConnection(), dbProfile);
			} catch (final Throwable e) {
				throw new RuntimeException(e);
			}
		}).flatMap(dsConnection -> {
			dsConnection.setAutoCommit(false);
			final RxConnection rxConnection = new DataSourceRxConnection(dsConnection, executor);
			setTransactionIsolation(rxConnection);
			setTimeout(rxConnection);
			rxConnection.setReadOnly(readOnly);
			final Session session = new SessionImpl(serviceCatalog, dbProfile, new RxConnectionProvider<RxConnection>() {
				@Override
				public <R> Observable<R> getConnection(boolean autoCommit, Function<RxConnection, Observable<R>> connection) {
					return connection.apply(rxConnection);
				}
			}, sqlCache, sqlFactory);

			try {
				return txSession.apply(session)
						.toObservable().concatWith(Futures.toCompletable(executor, () -> {
							try {
								if (readOnly) {
									dsConnection.rollback();
								} else {
									dsConnection.commit();
								}
							} finally {
								dsConnection.close();
							}
						}).toObservable())
						.singleElement()
						.doOnError(e -> {
							LOGGER.trace("doOnError -> Error received", e);
							if (!dsConnection.isClosed()) {
								executor.execute(() -> {
									try {
										dsConnection.rollback();
									} finally {
										dsConnection.close();
									}
								});
							}
						});
			} catch (final RuntimeException e) {
				try {
					dsConnection.rollback();
				} finally {
					dsConnection.close();
				}
				throw e;
			} catch (final Throwable e) {
				try {
					dsConnection.rollback();
				} finally {
					dsConnection.close();
				}
				throw new RuntimeException(e);
			}
		});
	}


	@Override
	public <T> Single<T> execute(SingleFunction<T> txSession) {
		return execute(MaybeFunction.from(txSession)).toSingle();
	}

}
