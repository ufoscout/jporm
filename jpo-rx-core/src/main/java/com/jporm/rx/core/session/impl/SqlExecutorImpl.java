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
import com.jporm.rx.core.connection.ConnectionUtils;
import com.jporm.rx.core.connection.UpdateResult;
import com.jporm.rx.core.session.SqlExecutor;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

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
			CompletableFuture<T> result = connection.query(sql, new PrepareStatementSetterCollectionWrapper(args), rsrr::read);
			return ConnectionUtils.closeConnection(result, connection);
		});
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final Collection<?> args) throws JpoException {
		StatementSetter pss = new PrepareStatementSetterCollectionWrapper(args);
		return update(sql, pss);
	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final GeneratedKeyReader generatedKeyReader, final Collection<?> args)
			throws JpoException {
		StatementSetter pss = new PrepareStatementSetterCollectionWrapper(args);
		return update(sql, generatedKeyReader, pss);
	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final GeneratedKeyReader generatedKeyReader, final Object... args)
			throws JpoException {
		StatementSetter pss = new PrepareStatementSetterArrayWrapper(args);
		return update(sql, generatedKeyReader, pss);
	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final GeneratedKeyReader generatedKeyReader, final StatementSetter psc)
			throws JpoException {
		return connectionSupplier.get().thenCompose(connection -> {
			LOGGER.debug("Execute update query: [{}]", sql);
			CompletableFuture<UpdateResult> result = connection.update(sql, generatedKeyReader, psc);
			return ConnectionUtils.closeConnection(result, connection);
		});

	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final Object... args) throws JpoException {
		StatementSetter pss = new PrepareStatementSetterArrayWrapper(args);
		return update(sql, pss);
	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final StatementSetter psc) throws JpoException {
		return update(sql, GENERATING_KEY_READER_DO_NOTHING, psc);
	}

}
