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
package com.jporm.core.session.datasource;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceConnectionProviderThreadLocalImpl implements DataSourceConnectionProvider {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final ThreadLocal<DataSourceConnectionImpl> threadLocalConnection = new ThreadLocal<DataSourceConnectionImpl>();
	private final DataSource dataSource;

	public DataSourceConnectionProviderThreadLocalImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public final DataSourceConnectionImpl getConnection(final boolean readOnly) {
		logger.debug("Connection asked" );
		DataSourceConnectionImpl conn = threadLocalConnection.get();
		if ((conn==null) || !conn.isValid()) {
			logger.debug("No valid connections found, a new one will be created");
			conn = new DataSourceConnectionImpl(dataSource, readOnly);
			threadLocalConnection.set(conn);
		}
		conn.addCaller();
		return conn;
	}
}
