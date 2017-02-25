/*******************************************************************************
 * Copyright 2013 Francesco Cina'
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
package com.jporm.rm.quasar.connection.datasource;

import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.async.ThreadPoolAsyncTaskExecutor;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.commons.json.JsonService;
import com.jporm.rm.connection.Transaction;
import com.jporm.rm.connection.TransactionProvider;
import com.jporm.rm.connection.datasource.DataSourceTransaction;
import com.jporm.rm.quasar.connection.JpoCompletableWrapper;
import com.jporm.sql.dialect.DBProfile;

/**
 *
 * @author Francesco Cina
 *
 *         21/mag/2011
 */
public class QuasarDataSourceTransactionProvider implements TransactionProvider {

	private final static AtomicInteger COUNT = new AtomicInteger(0);
	private final AsyncTaskExecutor connectionExecutor = new ThreadPoolAsyncTaskExecutor(2, "jpo-connection-get-pool-" + COUNT.getAndIncrement());
	private final AsyncTaskExecutor executor;
	private final DataSource dataSource;
	private final JsonService jsonService;
	private QuasarDataSourceConnectionProvider connectionProvider;
	private DBProfile dbType;

	public QuasarDataSourceTransactionProvider(final DataSource dataSource, JsonService jsonService, final AsyncTaskExecutor executor) {
		this(dataSource, jsonService, executor, null);
	}

	public QuasarDataSourceTransactionProvider(final DataSource dataSource, JsonService jsonService, final AsyncTaskExecutor executor, final DBProfile dbType) {
		this.dataSource = dataSource;
		this.jsonService = jsonService;
		this.executor = executor;
		this.dbType = dbType;
	}

	@Override
	public final DBProfile getDBProfile() {
		if (dbType == null) {
			dbType = JpoCompletableWrapper.get(executor.execute(() -> {
				return DBTypeDescription.build(dataSource).getDBType().getDBProfile();
			}));
		}
		return dbType;
	}

	@Override
	public Transaction getTransaction(ServiceCatalog serviceCatalog, SqlCache sqlCache, SqlFactory sqlFactory) {
		return new DataSourceTransaction(serviceCatalog, getDBProfile(), sqlCache, sqlFactory, getConnectionProvider());
	}

	@Override
	public QuasarDataSourceConnectionProvider getConnectionProvider() {
		if (connectionProvider == null) {
			connectionProvider = new QuasarDataSourceConnectionProvider(dataSource, jsonService, getDBProfile(), connectionExecutor, executor);
		}
		return connectionProvider;
	}

}
