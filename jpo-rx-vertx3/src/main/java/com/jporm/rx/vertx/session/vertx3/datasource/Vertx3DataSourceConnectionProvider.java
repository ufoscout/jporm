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

import com.jporm.rx.session.datasource.DataSourceConnectionProvider;
import com.jporm.rx.vertx.session.vertx3.datasource.executor.Vertx3ExecuteBlockingAsyncTaskExecutor;

import io.vertx.core.Vertx;

public class Vertx3DataSourceConnectionProvider extends DataSourceConnectionProvider {

	public Vertx3DataSourceConnectionProvider(com.jporm.rm.session.ConnectionProvider rmConnectionProvider, Vertx vertx) {
		super(rmConnectionProvider, new Vertx3ExecuteBlockingAsyncTaskExecutor(vertx));
	}

}
