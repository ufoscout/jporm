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
package com.jporm.rm.session.datasource;

import java.sql.SQLException;
import java.util.function.BiFunction;

import javax.sql.DataSource;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.rm.session.ConnectionProvider;
import com.jporm.rm.transaction.Transaction;
import com.jporm.rm.transaction.impl.TransactionImpl;
import com.jporm.sql.dialect.DBType;
import com.jporm.rm.session.Connection;

/**
 *
 * @author Francesco Cina
 *
 * 21/mag/2011
 */
public class DataSourceConnectionProvider implements ConnectionProvider {

	private final BiFunction<ConnectionProvider, ServiceCatalog, Transaction> transactionFactory =
			(_connectionProvider, _serviceCatalog) -> {
				return new TransactionImpl(_connectionProvider, _serviceCatalog);
			};
	private final DataSource dataSource;
	private DBType dbType;

	public DataSourceConnectionProvider(final DataSource dataSource) {
		this(dataSource, null);
	}

	public DataSourceConnectionProvider(final DataSource dataSource, DBType dbType) {
		this.dataSource = dataSource;
		this.dbType = dbType;
	}

	@Override
	public Connection getConnection(boolean autoCommit) throws JpoException {
		java.sql.Connection connection;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(autoCommit);
			return new DataSourceConnection( connection , getDBType() );
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public final DBType getDBType() {
		if (dbType==null) {
				dbType = DBTypeDescription.build(dataSource).getDBType();
		}
		return dbType;
	}

	@Override
	public BiFunction<ConnectionProvider, ServiceCatalog, Transaction> getTransactionFactory() {
		return transactionFactory;
	}

}
