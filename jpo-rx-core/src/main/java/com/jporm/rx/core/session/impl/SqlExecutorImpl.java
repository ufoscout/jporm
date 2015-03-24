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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.session.ASqlExecutor;
import com.jporm.rx.core.connection.Connection;
import com.jporm.rx.core.session.SqlExecutor;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.ResultSetReader;

public class SqlExecutorImpl extends ASqlExecutor implements SqlExecutor {

	private final Logger LOGGER = LoggerFactory.getLogger(SqlExecutorImpl.class);
	private final Supplier<CompletableFuture<Connection>> connectionSupplier;

	public SqlExecutorImpl(final TypeConverterFactory typeFactory, Supplier<CompletableFuture<Connection>> connectionSupplier) {
		super(typeFactory);
		this.connectionSupplier = connectionSupplier;
	}

	@Override
	public <T> CompletableFuture<T> query(String sql, ResultSetReader<T> rsrr, Collection<?> args) throws JpoException {
		return connectionSupplier.get().thenCompose(connection -> {
			LOGGER.debug("Execute query: [{}]", sql);
			CompletableFuture<T> result = connection.query(sql, pss -> {
				int index = 0;
				for (Object object : args) {
					pss.setObject(++index, object);
				};
			}, rse -> {
				return rsrr.read(rse);
			});

			result.handle((callResult, ex) -> {
				connection.close();
				return null;
			});

			return result;
		});
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

}
