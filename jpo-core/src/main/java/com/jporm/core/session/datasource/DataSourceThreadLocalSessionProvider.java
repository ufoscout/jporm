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
package com.jporm.core.session.datasource;

import javax.sql.DataSource;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.transaction.TransactionDefinition;
import com.jporm.core.session.Session;
import com.jporm.core.session.SqlPerformerStrategy;
import com.jporm.core.transaction.TransactionCallback;

/**
 *
 * @author Francesco Cina
 *
 * 21/mag/2011
 */
public class DataSourceThreadLocalSessionProvider extends DataSourceSessionProvider {

	private final ThreadLocal<DataSourceConnectionImpl> threadLocalConnection = new ThreadLocal<DataSourceConnectionImpl>();
	private final DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
	private final DataSource dataSource;
	private final SqlPerformerStrategy sqlPerformerStrategy = new DataSourceSqlPerformerStrategy(this);
	private final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();

	public DataSourceThreadLocalSessionProvider(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	final DataSourceConnectionImpl getConnection(final boolean readOnly) {
		getLogger().debug("Connection asked" ); //$NON-NLS-1$
		DataSourceConnectionImpl conn = threadLocalConnection.get();
		if ((conn==null) || !conn.isValid()) {
			getLogger().debug("No valid connections found, a new one will be created"); //$NON-NLS-1$
			conn = new DataSourceConnectionImpl(dataSource, readOnly);
			threadLocalConnection.set(conn);
		}
		conn.addCaller();
		return conn;
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @return the dataSourceTransactionManager
	 */
	public DataSourceTransactionManager getDataSourceTransactionManager() {
		return dataSourceTransactionManager;
	}

	@Override
	public SqlPerformerStrategy sqlPerformerStrategy() throws JpoException {
		return sqlPerformerStrategy;
	}

	private Transaction getTransaction(final TransactionDefinition transactionDefinition) throws JpoException {
		return transactionManager.startTransaction(this, transactionDefinition);
	}

	@Override
	public <T> T doInTransaction(final Session session, final TransactionDefinition transactionDefinition, final TransactionCallback<T> transactionCallback) {
		T result = null;
		Transaction tx = getTransaction(transactionDefinition);
		try {
			result = transactionCallback.doInTransaction(session);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}
		return result;
	}
}
