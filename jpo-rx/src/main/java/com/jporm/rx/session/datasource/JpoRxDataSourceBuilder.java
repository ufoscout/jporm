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
package com.jporm.rx.session.datasource;

import javax.sql.DataSource;

import com.jporm.commons.core.builder.AbstractJpoBuilder;
import com.jporm.rm.session.ConnectionProvider;
import com.jporm.rx.JpoRx;
import com.jporm.rx.JpoRxImpl;
import com.jporm.sql.dialect.DBType;

/**
 *
 * @author cinafr
 *
 */
public class JpoRxDataSourceBuilder extends AbstractJpoBuilder<JpoRxDataSourceBuilder> {

	public static JpoRxDataSourceBuilder get() {
		return new JpoRxDataSourceBuilder();
	}

	private JpoRxDataSourceBuilder() {

	}

	/**
	 * Create a {@link JPO} instance
	 * @param maxParallelConnections
	 * @param dbType
	 * @param sessionProvider
	 * @return
	 */
	public JpoRx build(final DataSource dataSource, DBType dbType) {
		ConnectionProvider rmConnectionProvider = new com.jporm.rm.session.datasource.DataSourceConnectionProvider(dataSource, dbType) ;
		return new JpoRxImpl(new DataSourceConnectionProvider(rmConnectionProvider, getServiceCatalog().getAsyncTaskExecutor()), getServiceCatalog());
	}

	/**
	 * Create a {@link JPO} instance
	 * @param maxParallelConnections
	 * @param dbType
	 * @param sessionProvider
	 * @return
	 */
	public JpoRx build(final DataSource dataSource) {
		ConnectionProvider rmConnectionProvider = new com.jporm.rm.session.datasource.DataSourceConnectionProvider(dataSource) ;
		return new JpoRxImpl(new DataSourceConnectionProvider(rmConnectionProvider, getServiceCatalog().getAsyncTaskExecutor()), getServiceCatalog());
	}

}
