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
package com.jporm.rm.quasar.session;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.async.impl.ThreadPoolAsyncTaskExecutor;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.rm.session.Connection;
import com.jporm.rm.session.ConnectionProvider;
import com.jporm.rm.transaction.Transaction;
import com.jporm.sql.dialect.DBType;

public class QuasarConnectionProvider implements ConnectionProvider {

	private final static AtomicInteger COUNT = new AtomicInteger(0);
	private final AsyncTaskExecutor connectionExecutor = new ThreadPoolAsyncTaskExecutor(2, "jpo-connection-get-pool-" + COUNT.getAndIncrement());
	private final AsyncTaskExecutor executor;
	private final ConnectionProvider connectionProvider;

	public QuasarConnectionProvider(ConnectionProvider connectionProvider, AsyncTaskExecutor executor) {
		this.connectionProvider = connectionProvider;
		this.executor = executor;
	}

	@Override
	public DBType getDBType() {
		return JpoCompletableWrapper.get(executor.execute(()-> {
			return connectionProvider.getDBType();
		}));
	}

	@Override
	public Connection getConnection(boolean autoCommit) {
		Connection connection = JpoCompletableWrapper.get(connectionExecutor.execute(()-> {
			return connectionProvider.getConnection(autoCommit);
		}));
		return new QuasarConnection(connection, executor);
	}

	@Override
	public BiFunction<ConnectionProvider, ServiceCatalog, Transaction> getTransactionFactory() {
		return connectionProvider.getTransactionFactory();
	}

}
