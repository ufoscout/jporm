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
import com.jporm.core.session.SessionProvider;
import com.jporm.core.session.SqlPerformerStrategy;

/**
 *
 * @author Francesco Cina
 *
 * 21/mag/2011
 */
public class DataSourceThreadLocalSessionProvider extends SessionProvider {

	private final DataSource dataSource;
	private final DataSourceConnectionProvider connectionProvider;

	public DataSourceThreadLocalSessionProvider(final DataSource dataSource) {
		this.dataSource = dataSource;
		connectionProvider = new DataSourceConnectionProviderThreadLocalImpl(dataSource);
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public SqlPerformerStrategy sqlPerformerStrategy() throws JpoException {
		return new DataSourceSqlPerformerStrategy( connectionProvider , getDBType().getDBProfile().getStatementStrategy() );
	}

}
