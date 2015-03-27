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
package com.jporm.rx.vertx.session.vertx3.datasource;

import io.vertx.core.Vertx;

import javax.sql.DataSource;

import com.jporm.rx.core.session.datasource.DataSourceConnectionProvider;
import com.jporm.rx.vertx.session.vertx3.datasource.executor.Vertx3ExecuteBlockingAsyncTaskExecutor;
import com.jporm.sql.dialect.DBType;

public class Vertx3DataSourceConnectionProvider extends DataSourceConnectionProvider {

	//private final Logger logger = LoggerFactory.getLogger(getClass());

	public Vertx3DataSourceConnectionProvider(DataSource dataSource, Vertx vertx) {
		this(dataSource, vertx, null);
	}

	public Vertx3DataSourceConnectionProvider(DataSource dataSource, Vertx vertx, DBType dbType) {
		super(dataSource, new Vertx3ExecuteBlockingAsyncTaskExecutor(vertx), dbType);
		setDBType(dbType);
	}

}
