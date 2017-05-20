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

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.async.ThreadPoolAsyncTaskExecutor;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.rx.query.connection.AsyncConnection;
import com.jporm.rx.query.connection.AsyncConnectionProvider;
import com.jporm.rx.query.connection.AsyncTransaction;
import com.jporm.rx.query.connection.AsyncTranscationProvider;
import com.jporm.sql.dialect.DBProfile;

public class DataSourceAsyncTransactionProvider implements AsyncTranscationProvider {

	//    private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceRxTransactionProvider.class);
	private final static AtomicInteger COUNT = new AtomicInteger(0);
	private final AsyncTaskExecutor connectionExecutor = new ThreadPoolAsyncTaskExecutor(new ThreadPoolAsyncTaskExecutor(2, "jpo-connection-get-pool-" + COUNT.getAndIncrement()).getExecutor());
	private final AsyncTaskExecutor executor;
	private final DataSource dataSource;
	private DBProfile dbType;
	private AsyncConnectionProvider<? extends AsyncConnection> connectionProvider;

	public DataSourceAsyncTransactionProvider(final DataSource dataSource, final Executor executor) {
		this(dataSource, executor, null);
	}

	public DataSourceAsyncTransactionProvider(final DataSource dataSource, final Executor executor, final DBProfile dbType) {
		this.dataSource = dataSource;
		this.executor = new ThreadPoolAsyncTaskExecutor(executor);
		this.dbType = dbType;
	}

	@Override
	public final DBProfile getDBProfile() {
		if (dbType == null) {
			dbType = DBTypeDescription.build(dataSource).getDBType().getDBProfile();
		}
		return dbType;
	}

	@Override
	public AsyncTransaction getTransaction(ServiceCatalog serviceCatalog, SqlCache sqlCache, SqlFactory sqlFactory) {
		return new DataSourceAsyncTransaction(serviceCatalog, getDBProfile(), sqlCache, sqlFactory, dataSource, connectionExecutor, executor);
	}

	@Override
	public AsyncConnectionProvider<? extends AsyncConnection> getConnectionProvider() {
		if (connectionProvider == null) {
			connectionProvider = new DataSourceAsyncConnectionProvider(dataSource, getDBProfile(), connectionExecutor, executor);
		}
		return connectionProvider;
	}

}
