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
package com.jporm.session.datasource;

import javax.sql.DataSource;

import com.jporm.exception.OrmException;
import com.jporm.session.SessionProvider;
import com.jporm.session.SqlPerformerStrategy;
import com.jporm.transaction.Transaction;
import com.jporm.transaction.TransactionDefinition;

/**
 * 
 * @author Francesco Cina
 *
 * 21/mag/2011
 */
public class DataSourceSessionProvider extends SessionProvider {

    private final ThreadLocal<DataSourceConnectionImpl> threadLocalConnection = new ThreadLocal<DataSourceConnectionImpl>();
    private final DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
    private final DataSource dataSource;
    private final SqlPerformerStrategy sqlPerformerStrategy  = new DataSourceSqlPerformerStrategy(this);
    private final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();

    public DataSourceSessionProvider(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    final DataSourceConnectionImpl getConnection(final boolean readOnly, final DataSourceConnectionCaller connectionCaller) {
        getLogger().debug("Connection asked by [{}]", connectionCaller ); //$NON-NLS-1$
        DataSourceConnectionImpl conn = threadLocalConnection.get();
        if ((conn==null) || !conn.isValid()) {
            getLogger().debug("No valid connections found, a new one will be created"); //$NON-NLS-1$
            conn = new DataSourceConnectionImpl(dataSource, readOnly);
            threadLocalConnection.set(conn);
        }
        conn.setReadOnly(readOnly);
        conn.addCaller(connectionCaller);
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
    public Transaction getTransaction(final TransactionDefinition transactionDefinition) throws OrmException {
        return transactionManager.startTransaction(this, transactionDefinition);
    }

    @Override
    public SqlPerformerStrategy sqlPerformerStrategy() throws OrmException {
        return sqlPerformerStrategy;
    }
}
