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
package com.jporm.rx.core.session.impl;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rx.core.connection.Connection;
import com.jporm.rx.core.session.SqlExecutor;
import com.jporm.types.io.ResultSetReader;

public class SqlExecutorImpl implements SqlExecutor {

	private final Supplier<CompletableFuture<Connection>> connectionSupplier;

	public SqlExecutorImpl(Supplier<CompletableFuture<Connection>> connectionSupplier) {
		this.connectionSupplier = connectionSupplier;
	}

	@Override
	public <T> CompletableFuture<T> query(String sql, ResultSetReader<T> rsrr, Collection<?> args) throws JpoException {
		return connectionSupplier.get()
				.thenCompose(connection -> {
					 return connection.query(sql, pss -> {
						int index = 0;
						for (Object object : args) {
							pss.setObject(++index, object);
						}
					},
					rse -> {
						return rsrr.read(rse);
					})
					.thenCombine(connection.close(), (queryResult, voidArg) -> {
						return queryResult;
					});
				});
	}

}
