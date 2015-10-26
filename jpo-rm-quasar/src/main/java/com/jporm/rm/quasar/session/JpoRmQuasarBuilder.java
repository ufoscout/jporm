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

import javax.sql.DataSource;

import com.jporm.rm.JpoBuilder;
import com.jporm.rm.JpoRm;
import com.jporm.rm.JpoRmImpl;
import com.jporm.rm.session.ConnectionProvider;
import com.jporm.rm.session.datasource.DataSourceConnectionProvider;
import com.jporm.sql.dialect.DBType;

public class JpoRmQuasarBuilder extends JpoBuilder<JpoRmQuasarBuilder> {

	public static JpoRmQuasarBuilder get() {
		return new JpoRmQuasarBuilder();
	}

	/**
	 * Create a {@link JPO} instance
	 * @param maxParallelConnections
	 * @param dbType
	 * @param sessionProvider
	 * @return
	 */
	public JpoRm build(final DataSource dataSource, int maxParallelConnections, DBType dbType) {
		return build(new DataSourceConnectionProvider(dataSource, dbType), maxParallelConnections);
	}

	/**
	 * Create a {@link JPO} instance
	 * @param maxParallelConnections
	 * @param dbType
	 * @param sessionProvider
	 * @return
	 */
	public JpoRm build(final DataSource dataSource, int maxParallelConnections) {
		return build(new DataSourceConnectionProvider(dataSource), maxParallelConnections);
	}

	/**
	 * Create a {@link JpoRm} instance
	 * @param connectionProvider
	 * @return
	 */
	public JpoRm build(final ConnectionProvider connectionProvider, int maxParallelConnections) {
		return new JpoRmImpl( new QuasarConnectionProvider(connectionProvider, maxParallelConnections), getServiceCatalog());
	}

	/**
	 * Create a {@link JpoRm} instance. It uses by default a maximum of 10 connections
	 * @param connectionProvider
	 * @return
	 */
	public JpoRm build(final ConnectionProvider connectionProvider) {
		return build(connectionProvider, 10);
	}

}
