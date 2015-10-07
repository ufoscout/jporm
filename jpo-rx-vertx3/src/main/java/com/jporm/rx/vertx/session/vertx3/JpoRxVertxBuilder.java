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
package com.jporm.rx.vertx.session.vertx3;

import io.vertx.core.Vertx;

import javax.sql.DataSource;

import com.jporm.rx.JpoRx;
import com.jporm.rx.JpoRxBuilder;
import com.jporm.rx.vertx.session.vertx3.datasource.Vertx3DataSourceConnectionProvider;
import com.jporm.sql.dialect.DBType;

/**
 *
 * @author cinafr
 *
 */
public class JpoRxVertxBuilder extends JpoRxBuilder {

	public static JpoRxVertxBuilder get() {
		return new JpoRxVertxBuilder();
	}

	/**
	 * Create a {@link JPO} instance
	 * @param maxParallelConnections
	 * @param dbType
	 * @param sessionProvider
	 * @return
	 */
	public JpoRx build(DataSource dataSource, Vertx vertx, DBType dbType) {
		return build(new Vertx3DataSourceConnectionProvider(new com.jporm.rm.session.datasource.DataSourceConnectionProvider(dataSource, dbType), vertx));
	}

	/**
	 * Create a {@link JPO} instance
	 * @param maxParallelConnections
	 * @param dbType
	 * @param sessionProvider
	 * @return
	 */
	public JpoRx build(DataSource dataSource, Vertx vertx) {
		return build(new Vertx3DataSourceConnectionProvider(new com.jporm.rm.session.datasource.DataSourceConnectionProvider(dataSource), vertx));
	}

}
