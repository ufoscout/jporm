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
package com.jporm.rm.connection.datasource;

import javax.sql.DataSource;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.rm.connection.Transaction;
import com.jporm.rm.connection.TransactionProvider;
import com.jporm.sql.dialect.DBProfile;

/**
 *
 * @author Francesco Cina
 *
 *         21/mag/2011
 */
public class DataSourceTransactionProvider implements TransactionProvider {

	private DataSourceConnectionProvider connectionProvider;
	private final DataSource dataSource;
	private DBProfile dbType;

	public DataSourceTransactionProvider(final DataSource dataSource) {
		this(dataSource, null);
	}

	public DataSourceTransactionProvider(final DataSource dataSource, final DBProfile dbType) {
		this.dataSource = dataSource;
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
	public Transaction getTransaction(ServiceCatalog serviceCatalog, SqlCache sqlCache, SqlFactory sqlFactory) {
		return new DataSourceTransaction(serviceCatalog, getDBProfile(), sqlCache, sqlFactory, getConnectionProvider());
	}

	/**
	 * @return the connectionProvider
	 */
	@Override
	public DataSourceConnectionProvider getConnectionProvider() {
		if (connectionProvider == null) {
			connectionProvider = new DataSourceConnectionProvider(dataSource, getDBProfile());
		}
		return connectionProvider;
	}

}
