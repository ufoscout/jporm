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
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.connection.AsyncConnectionProvider;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReader;
import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReaderUnique;
import com.jporm.commons.core.session.ASqlExecutor;
import com.jporm.commons.core.util.AsyncConnectionUtils;
import com.jporm.commons.core.util.BigDecimalUtil;
import com.jporm.rx.query.update.UpdateResult;
import com.jporm.rx.query.update.UpdateResultImpl;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;
import com.jporm.types.io.StatementSetter;

public class SqlExecutorImpl extends ASqlExecutor implements SqlExecutor {

    private static final Function<String, String> SQL_PRE_PROCESSOR_DEFAULT = (sql) -> sql;
    private final static Logger LOGGER = LoggerFactory.getLogger(SqlExecutorImpl.class);
    private final AsyncConnectionProvider connectionProvider;
    private final Function<String, String> sqlPreProcessor;
    private final boolean autoCommit;

    public SqlExecutorImpl(final TypeConverterFactory typeFactory, final AsyncConnectionProvider connectionProvider, final boolean autoCommit) {
        this(typeFactory, connectionProvider, autoCommit, SQL_PRE_PROCESSOR_DEFAULT);
    }

    public SqlExecutorImpl(final TypeConverterFactory typeFactory, final AsyncConnectionProvider connectionProvider, final boolean autoCommit, Function<String, String> sqlPreProcessor) {
        super(typeFactory);
        this.connectionProvider = connectionProvider;
        this.autoCommit = autoCommit;
        this.sqlPreProcessor = sqlPreProcessor;
    }

    @Override
    public CompletableFuture<int[]> batchUpdate(final Collection<String> sqls) throws JpoException {
        return connectionProvider.getConnection(false).thenCompose(connection -> {
            try {
                CompletableFuture<int[]> result = connection.batchUpdate(sqls, sqlPreProcessor);
                return AsyncConnectionUtils.close(result, connection);
            } catch (RuntimeException e) {
                LOGGER.error("Error during query execution");
                connection.close();
                throw e;
            }
        });
    }

    @Override
    public CompletableFuture<int[]> batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
        String sqlProcessed = sqlPreProcessor.apply(sql);
        return connectionProvider.getConnection(false).thenCompose(connection -> {
            try {
                CompletableFuture<int[]> result = connection.batchUpdate(sqlProcessed, psc);
                return AsyncConnectionUtils.close(result, connection);
            } catch (RuntimeException e) {
                LOGGER.error("Error during query execution");
                connection.close();
                throw e;
            }
        });
    }

    @Override
    public CompletableFuture<int[]> batchUpdate(final String sql, final Collection<Object[]> args) throws JpoException {
        String sqlProcessed = sqlPreProcessor.apply(sql);
        return connectionProvider.getConnection(false).thenCompose(connection -> {
            try {
                Collection<StatementSetter> statements = new ArrayList<>();
                args.forEach(array -> statements.add(new PrepareStatementSetterArrayWrapper(array)));
                CompletableFuture<int[]> result = connection.batchUpdate(sqlProcessed, statements);
                return AsyncConnectionUtils.close(result, connection);
            } catch (RuntimeException e) {
                LOGGER.error("Error during query execution");
                connection.close();
                throw e;
            }
        });
    }

    @Override
    public CompletableFuture<Void> execute(final String sql) throws JpoException {
        String sqlProcessed = sqlPreProcessor.apply(sql);
        return connectionProvider.getConnection(false).thenCompose(connection -> {
            try {
                CompletableFuture<Void> result = connection.execute(sqlProcessed);
                return AsyncConnectionUtils.close(result, connection);
            } catch (RuntimeException e) {
                LOGGER.error("Error during query execution");
                connection.close();
                throw e;
            }
        });
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    public <T> CompletableFuture<T> query(final String sql, final Collection<?> args, final ResultSetReader<T> rsrr) {
        String sqlProcessed = sqlPreProcessor.apply(sql);
        return connectionProvider.getConnection(false).thenCompose(connection -> {
            try {
                CompletableFuture<T> result = connection.query(sqlProcessed, new PrepareStatementSetterCollectionWrapper(args), rsrr::read);
                return AsyncConnectionUtils.close(result, connection);
            } catch (RuntimeException e) {
                LOGGER.error("Error during query execution");
                connection.close();
                throw e;
            }
        });
    }

    @Override
    public <T> CompletableFuture<List<T>> query(final String sql, final Collection<?> args, final ResultSetRowReader<T> rsrr) {
        return query(sql, args, new ResultSetRowReaderToResultSetReader<T>(rsrr));
    }

    @Override
    public <T> CompletableFuture<T> query(final String sql, final Object[] args, final ResultSetReader<T> rse) {
        String sqlProcessed = sqlPreProcessor.apply(sql);
        return connectionProvider.getConnection(false).thenCompose(connection -> {
            try {
                CompletableFuture<T> result = connection.query(sqlProcessed, new PrepareStatementSetterArrayWrapper(args), rse::read);
                return AsyncConnectionUtils.close(result, connection);
            } catch (RuntimeException e) {
                LOGGER.error("Error during query execution");
                connection.close();
                throw e;
            }
        });
    }

    @Override
    public <T> CompletableFuture<List<T>> query(final String sql, final Object[] args, final ResultSetRowReader<T> rsrr) {
        return query(sql, args, new ResultSetRowReaderToResultSetReader<T>(rsrr));
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
    public <T> CompletableFuture<T> queryForUnique(final String sql, final Collection<?> args, final ResultSetRowReader<T> rsrr) {
        return query(sql, args, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr));
    }

    @Override
    public <T> CompletableFuture<T> queryForUnique(final String sql, final Object[] args, final ResultSetRowReader<T> rsrr) {
        return query(sql, args, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr));

    }

    @Override
    public CompletableFuture<UpdateResult> update(final String sql, final Collection<?> args) {
        StatementSetter pss = new PrepareStatementSetterCollectionWrapper(args);
        return update(sql, pss);
    }

    @Override
    public <R> CompletableFuture<R> update(final String sql, final Collection<?> args, final GeneratedKeyReader<R> generatedKeyReader) {
        StatementSetter pss = new PrepareStatementSetterCollectionWrapper(args);
        return update(sql, pss, generatedKeyReader);
    }

    @Override
    public CompletableFuture<UpdateResult> update(final String sql, final Object... args) {
        StatementSetter pss = new PrepareStatementSetterArrayWrapper(args);
        return update(sql, pss);
    }

    @Override
    public <R> CompletableFuture<R> update(final String sql, final Object[] args, final GeneratedKeyReader<R> generatedKeyReader) {
        StatementSetter pss = new PrepareStatementSetterArrayWrapper(args);
        return update(sql, pss, generatedKeyReader);
    }

    @Override
    public CompletableFuture<UpdateResult> update(final String sql, final StatementSetter psc) {
        String sqlProcessed = sqlPreProcessor.apply(sql);
        return connectionProvider.getConnection(autoCommit).thenCompose(connection -> {
            try {
                CompletableFuture<UpdateResult> result = connection.update(sqlProcessed, psc).thenApply(updated -> new UpdateResultImpl(updated));
                return AsyncConnectionUtils.close(result, connection);
            } catch (RuntimeException e) {
                LOGGER.error("Error during update execution");
                connection.close();
                throw e;
            }
        });
    }

    @Override
    public <R> CompletableFuture<R> update(final String sql, final StatementSetter psc, final GeneratedKeyReader<R> generatedKeyReader) {
        String sqlProcessed = sqlPreProcessor.apply(sql);
        return connectionProvider.getConnection(autoCommit).thenCompose(connection -> {
            try {
                CompletableFuture<R> result = connection.update(sqlProcessed, generatedKeyReader, psc);
                return AsyncConnectionUtils.close(result, connection);
            } catch (RuntimeException e) {
                LOGGER.error("Error during update execution");
                connection.close();
                throw e;
            }
        });

    }

    @Override
    public <T> CompletableFuture<Optional<T>> queryForOptional(String sql, Collection<?> args, ResultSetRowReader<T> rsrr) throws JpoException {
        return query(sql, args,  rs -> {
            if (rs.next()) {
                return rsrr.readRow(rs, 0);
            }
            return null;
         }).thenApply(result -> {
             return Optional.ofNullable(result);
         });
    }

    @Override
    public <T> CompletableFuture<Optional<T>> queryForOptional(String sql, Object[] args, ResultSetRowReader<T> rsrr) throws JpoException {
        return query(sql, args,  rs -> {
            if (rs.next()) {
                return rsrr.readRow(rs, 0);
            }
            return null;
         }).thenApply(result -> {
             return Optional.ofNullable(result);
         });
    }

}
