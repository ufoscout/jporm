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
package com.jporm.rx.session.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReader;
import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReaderUnique;
import com.jporm.commons.core.session.ASqlExecutor;
import com.jporm.commons.core.util.BigDecimalUtil;
import com.jporm.rx.connection.ConnectionUtils;
import com.jporm.rx.connection.UpdateResult;
import com.jporm.rx.session.ConnectionProvider;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.dialect.DBType;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;
import com.jporm.types.io.StatementSetter;

public class SqlExecutorImpl extends ASqlExecutor implements SqlExecutor {

	private final Logger LOGGER = LoggerFactory.getLogger(SqlExecutorImpl.class);
	private final ConnectionProvider connectionProvider;
	private final boolean autoCommit;

	public SqlExecutorImpl(final TypeConverterFactory typeFactory, ConnectionProvider connectionProvider, boolean autoCommit) {
		super(typeFactory);
		this.connectionProvider = connectionProvider;
		this.autoCommit = autoCommit;
	}

	@Override
	public CompletableFuture<DBType> dbType() {
		return connectionProvider.getDBType();
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	public <T> CompletableFuture<T> query(String sql, Collection<?> args, ResultSetReader<T> rsrr) {
		return connectionProvider.getConnection(false).thenCompose(connection -> {
			try {
				CompletableFuture<T> result = connection.query(sql, new PrepareStatementSetterCollectionWrapper(args), rsrr::read);
				return ConnectionUtils.close(result, connection);
			}
			catch (RuntimeException e) {
				LOGGER.error("Error during query execution");
				connection.close();
				throw e;
			}
		});
	}

	@Override
	public <T> CompletableFuture<T> query(String sql, Object[] args, ResultSetReader<T> rse) {
		return connectionProvider.getConnection(false).thenCompose(connection -> {
			try {
				CompletableFuture<T> result = connection.query(sql, new PrepareStatementSetterArrayWrapper(args), rse::read);
				return ConnectionUtils.close(result, connection);
			}
			catch (RuntimeException e) {
				LOGGER.error("Error during query execution");
				connection.close();
				throw e;
			}
		});
	}

	@Override
	public <T> CompletableFuture<List<T>> query(String sql, Collection<?> args, ResultSetRowReader<T> rsrr) {
		return query(sql, args, new ResultSetRowReaderToResultSetReader<T>(rsrr));
	}

	@Override
	public <T> CompletableFuture<List<T>> query(String sql, Object[] args, ResultSetRowReader<T> rsrr) {
		return query(sql, args, new ResultSetRowReaderToResultSetReader<T>(rsrr));
	}

	@Override
	public CompletableFuture<BigDecimal> queryForBigDecimal(final String sql, final Collection<?> args) {
		return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
	}

	@Override
	public CompletableFuture<BigDecimal> queryForBigDecimal(final String sql, final Object[] args) {
		return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
	}

	@Override
	public final CompletableFuture<BigDecimal> queryForBigDecimalUnique(final String sql, final Collection<?> args) {
		return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
	}

	@Override
	public final CompletableFuture<BigDecimal> queryForBigDecimalUnique(final String sql, final Object[] args) {
		return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
	}

	@Override
	public CompletableFuture<Boolean> queryForBoolean(final String sql, final Collection<?> args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toBoolean);
	}

	@Override
	public CompletableFuture<Boolean> queryForBoolean(final String sql, final Object[] args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toBoolean);
	}

	@Override
	public final CompletableFuture<Boolean> queryForBooleanUnique(final String sql, final Collection<?> args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toBoolean);
	}

	@Override
	public final CompletableFuture<Boolean> queryForBooleanUnique(final String sql, final Object[] args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toBoolean);
	}

	@Override
	public CompletableFuture<Double> queryForDouble(final String sql, final Collection<?> args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toDouble);
	}

	@Override
	public CompletableFuture<Double> queryForDouble(final String sql, final Object[] args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toDouble);
	}

	@Override
	public final CompletableFuture<Double> queryForDoubleUnique(final String sql, final Collection<?> args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toDouble);
	}

	@Override
	public final CompletableFuture<Double> queryForDoubleUnique(final String sql, final Object[] args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toDouble);
	}

	@Override
	public CompletableFuture<Float> queryForFloat(final String sql, final Collection<?> args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toFloat);
	}

	@Override
	public CompletableFuture<Float> queryForFloat(final String sql, final Object[] args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toFloat);
	}

	@Override
	public final CompletableFuture<Float> queryForFloatUnique(final String sql, final Collection<?> args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toFloat);
	}

	@Override
	public final CompletableFuture<Float> queryForFloatUnique(final String sql, final Object[] args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toFloat);
	}

	@Override
	public CompletableFuture<Integer> queryForInt(final String sql, final Collection<?> args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toInteger);
	}

	@Override
	public CompletableFuture<Integer> queryForInt(final String sql, final Object[] args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toInteger);
	}

	@Override
	public final CompletableFuture<Integer> queryForIntUnique(final String sql, final Collection<?> args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toInteger);
	}

	@Override
	public final CompletableFuture<Integer> queryForIntUnique(final String sql, final Object[] args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toInteger);
	}

	@Override
	public CompletableFuture<Long> queryForLong(final String sql, final Collection<?> args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toLong);
	}

	@Override
	public CompletableFuture<Long> queryForLong(final String sql, final Object[] args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toLong);
	}

	@Override
	public final CompletableFuture<Long> queryForLongUnique(final String sql, final Collection<?> args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toLong);
	}

	@Override
	public final CompletableFuture<Long> queryForLongUnique(final String sql, final Object[] args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toLong);
	}

	@Override
	public CompletableFuture<String> queryForString(final String sql, final Collection<?> args) {
		return this.query(sql, args, RESULT_SET_READER_STRING);
	}

	@Override
	public CompletableFuture<String> queryForString(final String sql, final Object[] args) {
		return this.query(sql, args, RESULT_SET_READER_STRING);
	}

	@Override
	public final CompletableFuture<String> queryForStringUnique(final String sql, final Collection<?> args) {
		return this.query(sql, args, RESULT_SET_READER_STRING_UNIQUE);
	}

	@Override
	public final CompletableFuture<String> queryForStringUnique(final String sql, final Object[] args) {
		return this.query(sql, args, RESULT_SET_READER_STRING_UNIQUE);
	}

	@Override
	public <T> CompletableFuture<T> queryForUnique(String sql, Collection<?> args, ResultSetRowReader<T> rsrr) {
		return query(sql, args, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr));
	}

	@Override
	public <T> CompletableFuture<T> queryForUnique(String sql, Object[] args, ResultSetRowReader<T> rsrr) {
		return query(sql, args, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr));

	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final Collection<?> args) {
		StatementSetter pss = new PrepareStatementSetterCollectionWrapper(args);
		return update(sql, pss);
	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final Collection<?> args, final GeneratedKeyReader generatedKeyReader) {
		StatementSetter pss = new PrepareStatementSetterCollectionWrapper(args);
		return update(sql, pss, generatedKeyReader);
	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final Object[] args, final GeneratedKeyReader generatedKeyReader) {
		StatementSetter pss = new PrepareStatementSetterArrayWrapper(args);
		return update(sql, pss, generatedKeyReader);
	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final StatementSetter psc, final GeneratedKeyReader generatedKeyReader) {
		return connectionProvider.getConnection(autoCommit).thenCompose(connection -> {
			try {
				CompletableFuture<UpdateResult> result = connection.update(sql, generatedKeyReader, psc);
				return ConnectionUtils.close(result, connection);
			}
			catch (RuntimeException e) {
				LOGGER.error("Error during update execution");
				connection.close();
				throw e;
			}
		});

	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final Object[] args) {
		StatementSetter pss = new PrepareStatementSetterArrayWrapper(args);
		return update(sql, pss);
	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final StatementSetter psc) {
		return update(sql, psc, GENERATING_KEY_READER_DO_NOTHING);
	}

}
