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
package com.jporm.rx.session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.function.IntBiConsumer;
import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReader;
import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReaderUnique;
import com.jporm.commons.core.session.ASqlExecutor;
import com.jporm.commons.core.util.BigDecimalUtil;
import com.jporm.rx.query.connection.AsyncConnection;
import com.jporm.rx.query.connection.AsyncConnectionProvider;
import com.jporm.rx.query.update.UpdateResult;
import com.jporm.rx.query.update.UpdateResultImpl;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

public class SqlExecutorImpl extends ASqlExecutor implements SqlExecutor {

	private static final Function<String, String> SQL_PRE_PROCESSOR_DEFAULT = (sql) -> sql;
	private final static Logger LOGGER = LoggerFactory.getLogger(SqlExecutorImpl.class);
	private final AsyncConnectionProvider<? extends AsyncConnection> connectionProvider;
	private final Function<String, String> sqlPreProcessor;

	public SqlExecutorImpl(final TypeConverterFactory typeFactory, final AsyncConnectionProvider<? extends AsyncConnection> connectionProvider) {
		this(typeFactory, connectionProvider, SQL_PRE_PROCESSOR_DEFAULT);
	}

	public SqlExecutorImpl(final TypeConverterFactory typeFactory, final AsyncConnectionProvider<? extends AsyncConnection> connectionProvider, Function<String, String> sqlPreProcessor) {
		super(typeFactory);
		this.connectionProvider = connectionProvider;
		this.sqlPreProcessor = sqlPreProcessor;
	}

	@Override
	public CompletableFuture<int[]> batchUpdate(final Collection<String> sqls) throws JpoException {
		return connectionProvider.getConnection(true, connection -> {
				return connection.batchUpdate(sqls, sqlPreProcessor);
		});
	}

	@Override
	public CompletableFuture<int[]> batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
		return connectionProvider.getConnection(true, connection -> {
				final String sqlProcessed = sqlPreProcessor.apply(sql);
				return connection.batchUpdate(sqlProcessed, psc);
		});
	}

	@Override
	public CompletableFuture<int[]> batchUpdate(final String sql, final Collection<Object[]> args) throws JpoException {
		return connectionProvider.getConnection(true, connection -> {
				final String sqlProcessed = sqlPreProcessor.apply(sql);
				final Collection<Consumer<Statement>> statements = new ArrayList<>();
				args.forEach(array -> statements.add(new PrepareStatementSetterArrayWrapper(array)));
				return connection.batchUpdate(sqlProcessed, statements);
		});
	}

	@Override
	public CompletableFuture<Void> execute(final String sql) throws JpoException {
		return connectionProvider.getConnection(true, connection -> {
				final String sqlProcessed = sqlPreProcessor.apply(sql);
				return connection.execute(sqlProcessed);
		});
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	public <T> CompletableFuture<T> query(final String sql, final Collection<?> args, final Function<ResultSet, T> rsrr) {
		return connectionProvider.getConnection(false, connection -> {
			final String sqlProcessed = sqlPreProcessor.apply(sql);
			return connection.query(sqlProcessed, new PrepareStatementSetterCollectionWrapper(args), rsrr::apply);
		});
	}

	@Override
	public CompletableFuture<Void> query(String sql, final Collection<?> args, final Consumer<ResultSet> rse) throws JpoException {
		return query(sql, args, (resultSet) -> {
			rse.accept(resultSet);
			return null;
		});
	}

	@Override
	public <T> CompletableFuture<List<T>> query(final String sql, final Collection<?> args, final IntBiFunction<ResultEntry, T> resultSetRowReader) {
		return query(sql, args, new ResultSetRowReaderToResultSetReader<T>(resultSetRowReader));
	}

	@Override
	public CompletableFuture<Void> query(final String sql, final Collection<?> args, final IntBiConsumer<ResultEntry> resultSetRowReader) throws JpoException {
		return query(sql, args, (final ResultSet resultSet) -> {
			int rowNum = 0;
			while (resultSet.hasNext()) {
				final ResultEntry entry = resultSet.next();
				resultSetRowReader.accept(entry, rowNum++);
			}
		});
	}

	@Override
	public <T> CompletableFuture<T> query(final String sql, final Object[] args, final Function<ResultSet, T> rse) {
		return connectionProvider.getConnection(false, connection -> {
			final String sqlProcessed = sqlPreProcessor.apply(sql);
			return connection.query(sqlProcessed, new PrepareStatementSetterArrayWrapper(args), rse::apply);
		});
	}

	@Override
	public CompletableFuture<Void> query(String sql, final Object[] args, final Consumer<ResultSet> rse) throws JpoException {
		return query(sql, args, (resultSet) -> {
			rse.accept(resultSet);
			return null;
		});
	}

	@Override
	public <T> CompletableFuture<List<T>> query(final String sql, final Object[] args, final IntBiFunction<ResultEntry, T> resultSetRowReader) {
		return query(sql, args, new ResultSetRowReaderToResultSetReader<T>(resultSetRowReader));
	}

	@Override
	public CompletableFuture<Void> query(final String sql, final Object[] args, final IntBiConsumer<ResultEntry> resultSetRowReader) throws JpoException {
		return query(sql, args, (final ResultSet resultSet) -> {
			int rowNum = 0;
			while (resultSet.hasNext()) {
				final ResultEntry entry = resultSet.next();
				resultSetRowReader.accept(entry, rowNum++);
			}
		});
	}

	@Override
	public CompletableFuture<BigDecimal> queryForBigDecimal(final String sql, final Collection<?> args) {
		return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
	}

	@Override
	public CompletableFuture<BigDecimal> queryForBigDecimal(final String sql, final Object... args) {
		return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
	}

	@Override
	public final CompletableFuture<BigDecimal> queryForBigDecimalUnique(final String sql, final Collection<?> args) {
		return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
	}

	@Override
	public final CompletableFuture<BigDecimal> queryForBigDecimalUnique(final String sql, final Object... args) {
		return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
	}

	@Override
	public CompletableFuture<Boolean> queryForBoolean(final String sql, final Collection<?> args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toBoolean);
	}

	@Override
	public CompletableFuture<Boolean> queryForBoolean(final String sql, final Object... args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toBoolean);
	}

	@Override
	public final CompletableFuture<Boolean> queryForBooleanUnique(final String sql, final Collection<?> args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toBoolean);
	}

	@Override
	public final CompletableFuture<Boolean> queryForBooleanUnique(final String sql, final Object... args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toBoolean);
	}

	@Override
	public CompletableFuture<Double> queryForDouble(final String sql, final Collection<?> args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toDouble);
	}

	@Override
	public CompletableFuture<Double> queryForDouble(final String sql, final Object... args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toDouble);
	}

	@Override
	public final CompletableFuture<Double> queryForDoubleUnique(final String sql, final Collection<?> args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toDouble);
	}

	@Override
	public final CompletableFuture<Double> queryForDoubleUnique(final String sql, final Object... args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toDouble);
	}

	@Override
	public CompletableFuture<Float> queryForFloat(final String sql, final Collection<?> args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toFloat);
	}

	@Override
	public CompletableFuture<Float> queryForFloat(final String sql, final Object... args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toFloat);
	}

	@Override
	public final CompletableFuture<Float> queryForFloatUnique(final String sql, final Collection<?> args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toFloat);
	}

	@Override
	public final CompletableFuture<Float> queryForFloatUnique(final String sql, final Object... args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toFloat);
	}

	@Override
	public CompletableFuture<Integer> queryForInt(final String sql, final Collection<?> args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toInteger);
	}

	@Override
	public CompletableFuture<Integer> queryForInt(final String sql, final Object... args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toInteger);
	}

	@Override
	public final CompletableFuture<Integer> queryForIntUnique(final String sql, final Collection<?> args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toInteger);
	}

	@Override
	public final CompletableFuture<Integer> queryForIntUnique(final String sql, final Object... args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toInteger);
	}

	@Override
	public CompletableFuture<Long> queryForLong(final String sql, final Collection<?> args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toLong);
	}

	@Override
	public CompletableFuture<Long> queryForLong(final String sql, final Object... args) {
		return this.queryForBigDecimal(sql, args).thenApply(BigDecimalUtil::toLong);
	}

	@Override
	public final CompletableFuture<Long> queryForLongUnique(final String sql, final Collection<?> args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toLong);
	}

	@Override
	public final CompletableFuture<Long> queryForLongUnique(final String sql, final Object... args) {
		return this.queryForBigDecimalUnique(sql, args).thenApply(BigDecimalUtil::toLong);
	}

	@Override
	public CompletableFuture<String> queryForString(final String sql, final Collection<?> args) {
		return this.query(sql, args, RESULT_SET_READER_STRING);
	}

	@Override
	public CompletableFuture<String> queryForString(final String sql, final Object... args) {
		return this.query(sql, args, RESULT_SET_READER_STRING);
	}

	@Override
	public final CompletableFuture<String> queryForStringUnique(final String sql, final Collection<?> args) {
		return this.query(sql, args, RESULT_SET_READER_STRING_UNIQUE);
	}

	@Override
	public final CompletableFuture<String> queryForStringUnique(final String sql, final Object... args) {
		return this.query(sql, args, RESULT_SET_READER_STRING_UNIQUE);
	}

	@Override
	public <T> CompletableFuture<T> queryForUnique(final String sql, final Collection<?> args, final IntBiFunction<ResultEntry, T> resultSetRowReader) {
		return query(sql, args, new ResultSetRowReaderToResultSetReaderUnique<T>(resultSetRowReader));
	}

	@Override
	public <T> CompletableFuture<T> queryForUnique(final String sql, final Object[] args, final IntBiFunction<ResultEntry, T> resultSetRowReader) {
		return query(sql, args, new ResultSetRowReaderToResultSetReaderUnique<T>(resultSetRowReader));

	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final Collection<?> args) {
		final Consumer<Statement> pss = new PrepareStatementSetterCollectionWrapper(args);
		return update(sql, pss);
	}

	@Override
	public <R> CompletableFuture<R> update(final String sql, final Collection<?> args, final GeneratedKeyReader<R> generatedKeyReader) {
		final Consumer<Statement> pss = new PrepareStatementSetterCollectionWrapper(args);
		return update(sql, pss, generatedKeyReader);
	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final Object... args) {
		final Consumer<Statement> pss = new PrepareStatementSetterArrayWrapper(args);
		return update(sql, pss);
	}

	@Override
	public <R> CompletableFuture<R> update(final String sql, final Object[] args, final GeneratedKeyReader<R> generatedKeyReader) {
		final Consumer<Statement> pss = new PrepareStatementSetterArrayWrapper(args);
		return update(sql, pss, generatedKeyReader);
	}

	@Override
	public CompletableFuture<UpdateResult> update(final String sql, final Consumer<Statement> psc) {
		return connectionProvider.getConnection(true, connection -> {
			final String sqlProcessed = sqlPreProcessor.apply(sql);
			return connection.update(sqlProcessed, psc).thenApply(updated -> new UpdateResultImpl(updated));
		});
	}

	@Override
	public <R> CompletableFuture<R> update(final String sql, final Consumer<Statement> psc, final GeneratedKeyReader<R> generatedKeyReader) {
		return connectionProvider.getConnection(true, connection -> {
			final String sqlProcessed = sqlPreProcessor.apply(sql);
			return connection.update(sqlProcessed, generatedKeyReader, psc);
		});

	}

	@Override
	public <T> CompletableFuture<Optional<T>> queryForOptional(String sql, Collection<?> args, IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException {
		return query(sql, args,  rs -> {
			if (rs.hasNext()) {
				return resultSetRowReader.apply(rs.next(), 0);
			}
			return null;
		}).thenApply(result -> {
			return Optional.ofNullable(result);
		});
	}

	@Override
	public <T> CompletableFuture<Optional<T>> queryForOptional(String sql, Object[] args, IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException {
		return query(sql, args,  rs -> {
			if (rs.hasNext()) {
				return resultSetRowReader.apply(rs.next(), 0);
			}
			return null;
		}).thenApply(result -> {
			return Optional.ofNullable(result);
		});
	}

}
