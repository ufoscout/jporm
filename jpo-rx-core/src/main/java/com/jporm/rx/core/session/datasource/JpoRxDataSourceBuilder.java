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
package com.jporm.rx.core.session.datasource;

import javax.sql.DataSource;

import com.jporm.rx.JpoRX;
import com.jporm.rx.JpoRxBuilder;
import com.jporm.sql.dialect.DBType;

/**
 *
 * @author cinafr
 *
 */
public class JpoRxDataSourceBuilder extends JpoRxBuilder {

	/**
	 * Create a {@link JPO} instance
	 * @param maxParallelConnections
	 * @param dbType
	 * @param sessionProvider
	 * @return
	 */
	public JpoRX build(final DataSource dataSource, int maxParallelConnections, DBType dbType) {
		return build(new DataSourceConnectionProvider(dataSource, maxParallelConnections, dbType));
	}

	/**
	 * Create a {@link JPO} instance
	 * @param maxParallelConnections
	 * @param dbType
	 * @param sessionProvider
	 * @return
	 */
	public JpoRX build(final DataSource dataSource, int maxParallelConnections) {
		return build(new DataSourceConnectionProvider(dataSource, maxParallelConnections));
	}

}
