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
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.commons.core.session.ASqlExecutor;
import com.jporm.commons.core.util.BigDecimalUtil;
import com.jporm.rx.connection.RxConnection;
import com.jporm.rx.connection.RxConnectionProvider;
import com.jporm.rx.query.update.UpdateResult;
import com.jporm.rx.query.update.UpdateResultImpl;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.Statement;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class SqlExecutorImpl extends ASqlExecutor implements SqlExecutor {

    private static final Function<String, String> SQL_PRE_PROCESSOR_DEFAULT = (sql) -> sql;
    private final static Logger LOGGER = LoggerFactory.getLogger(SqlExecutorImpl.class);
    private final Function<String, String> sqlPreProcessor;
    private final RxConnectionProvider<? extends RxConnection> connectionProvider;

    public SqlExecutorImpl(final TypeConverterFactory typeFactory, final RxConnectionProvider<? extends RxConnection> connectionProvider) {
        this(typeFactory, connectionProvider, SQL_PRE_PROCESSOR_DEFAULT);
    }

    public SqlExecutorImpl(final TypeConverterFactory typeFactory, final RxConnectionProvider<? extends RxConnection> connectionProvider,
            Function<String, String> sqlPreProcessor) {
        super(typeFactory);
        this.connectionProvider = connectionProvider;
        this.sqlPreProcessor = sqlPreProcessor;
    }

    @Override
    public Single<int[]> batchUpdate(final Collection<String> sqls) throws JpoException {
        return connectionProvider.getConnection(true, connection -> {
            return connection.batchUpdate(sqls, sqlPreProcessor).toObservable();
        }).toSingle();

    }

    @Override
    public Single<int[]> batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
        return connectionProvider.getConnection(true, connection -> {
            return connection.batchUpdate(sqlPreProcessor.apply(sql), psc).toObservable();
        }).toSingle();
    }

    @Override
    public Single<int[]> batchUpdate(final String sql, final Collection<Object[]> args) throws JpoException {
        String sqlProcessed = sqlPreProcessor.apply(sql);
        Collection<Consumer<Statement>> statements = new ArrayList<>();
        args.forEach(array -> statements.add(new PrepareStatementSetterArrayWrapper(array)));
        return connectionProvider.getConnection(true, connection -> {
            return connection.batchUpdate(sqlProcessed, statements).toObservable();
        }).toSingle();
    }

    @Override
    public Completable execute(final String sql) throws JpoException {
        String sqlProcessed = sqlPreProcessor.apply(sql);
        return connectionProvider.getConnection(true, connection -> {
            return connection.execute(sqlProcessed).toObservable();
        }).toCompletable();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    public <T> Observable<T> query(final String sql, final Collection<?> args, final IntBiFunction<ResultEntry, T> rsrr) {
        String sqlProcessed = sqlPreProcessor.apply(sql);
        return connectionProvider.getConnection(true, connection -> {
            return connection.query(sqlProcessed, new PrepareStatementSetterCollectionWrapper(args), rsrr::apply);
        });
    }

    @Override
    public <T> Observable<T> query(final String sql, final Object[] args, final IntBiFunction<ResultEntry, T> rse) {
        String sqlProcessed = sqlPreProcessor.apply(sql);
        return connectionProvider.getConnection(true, connection -> {
            return connection.query(sqlProcessed, new PrepareStatementSetterArrayWrapper(args), rse::apply);
        });
    }

    @Override
    public Observable<BigDecimal> queryForBigDecimal(final String sql, final Collection<?> args) {
        return this.query(sql, args, (ResultEntry re, int count) -> {
            return re.getBigDecimal(0);
        });
    }

    @Override
    public Observable<BigDecimal> queryForBigDecimal(final String sql, final Object... args) {
        return this.query(sql, args, (ResultEntry re, int count) -> {
            return re.getBigDecimal(0);
        });
    }

    @Override
    public final Single<BigDecimal> queryForBigDecimalUnique(final String sql, final Collection<?> args) {
        return queryForBigDecimal(sql, args).toSingle();
    }

    @Override
    public final Single<BigDecimal> queryForBigDecimalUnique(final String sql, final Object... args) {
        return queryForBigDecimal(sql, args).toSingle();
    }

    @Override
    public Single<Optional<BigDecimal>> queryForBigDecimalOptional(final String sql, final Collection<?> args) {
        return queryForBigDecimal(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public Single<Optional<BigDecimal>> queryForBigDecimalOptional(final String sql, final Object... args) {
        return queryForBigDecimal(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public Observable<Boolean> queryForBoolean(final String sql, final Collection<?> args) {
        return this.queryForBigDecimal(sql, args).map(BigDecimalUtil::toBoolean);
    }

    @Override
    public Observable<Boolean> queryForBoolean(final String sql, final Object... args) {
        return this.queryForBigDecimal(sql, args).map(BigDecimalUtil::toBoolean);
    }

    @Override
    public final Single<Boolean> queryForBooleanUnique(final String sql, final Collection<?> args) {
        return this.queryForBigDecimalUnique(sql, args).map(BigDecimalUtil::toBoolean);
    }

    @Override
    public final Single<Boolean> queryForBooleanUnique(final String sql, final Object... args) {
        return this.queryForBigDecimalUnique(sql, args).map(BigDecimalUtil::toBoolean);
    }

    @Override
    public Single<Optional<Boolean>> queryForBooleanOptional(final String sql, final Collection<?> args) {
        return this.queryForBoolean(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public Single<Optional<Boolean>> queryForBooleanOptional(final String sql, final Object... args) {
        return this.queryForBoolean(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public Observable<Double> queryForDouble(final String sql, final Collection<?> args) {
        return this.queryForBigDecimal(sql, args).map(BigDecimalUtil::toDouble);
    }

    @Override
    public Observable<Double> queryForDouble(final String sql, final Object... args) {
        return this.queryForBigDecimal(sql, args).map(BigDecimalUtil::toDouble);
    }

    @Override
    public final Single<Double> queryForDoubleUnique(final String sql, final Collection<?> args) {
        return this.queryForBigDecimalUnique(sql, args).map(BigDecimalUtil::toDouble);
    }

    @Override
    public final Single<Double> queryForDoubleUnique(final String sql, final Object... args) {
        return this.queryForBigDecimalUnique(sql, args).map(BigDecimalUtil::toDouble);
    }

    @Override
    public Single<Optional<Double>> queryForDoubleOptional(final String sql, final Collection<?> args) {
        return this.queryForDouble(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public Single<Optional<Double>> queryForDoubleOptional(final String sql, final Object... args) {
        return this.queryForDouble(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public Observable<Float> queryForFloat(final String sql, final Collection<?> args) {
        return this.queryForBigDecimal(sql, args).map(BigDecimalUtil::toFloat);
    }

    @Override
    public Observable<Float> queryForFloat(final String sql, final Object... args) {
        return this.queryForBigDecimal(sql, args).map(BigDecimalUtil::toFloat);
    }

    @Override
    public final Single<Float> queryForFloatUnique(final String sql, final Collection<?> args) {
        return this.queryForBigDecimalUnique(sql, args).map(BigDecimalUtil::toFloat);
    }

    @Override
    public final Single<Float> queryForFloatUnique(final String sql, final Object... args) {
        return this.queryForBigDecimalUnique(sql, args).map(BigDecimalUtil::toFloat);
    }

    @Override
    public Single<Optional<Float>> queryForFloatOptional(final String sql, final Collection<?> args) {
        return this.queryForFloat(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public Single<Optional<Float>> queryForFloatOptional(final String sql, final Object... args) {
        return this.queryForFloat(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public Observable<Integer> queryForInt(final String sql, final Collection<?> args) {
        return this.queryForBigDecimal(sql, args).map(BigDecimalUtil::toInteger);
    }

    @Override
    public Observable<Integer> queryForInt(final String sql, final Object... args) {
        return this.queryForBigDecimal(sql, args).map(BigDecimalUtil::toInteger);
    }

    @Override
    public final Single<Integer> queryForIntUnique(final String sql, final Collection<?> args) {
        return this.queryForBigDecimalUnique(sql, args).map(BigDecimalUtil::toInteger);
    }

    @Override
    public final Single<Integer> queryForIntUnique(final String sql, final Object... args) {
        return this.queryForBigDecimalUnique(sql, args).map(BigDecimalUtil::toInteger);
    }

    @Override
    public Single<Optional<Integer>> queryForIntOptional(final String sql, final Collection<?> args) {
        return this.queryForInt(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public Single<Optional<Integer>> queryForIntOptional(final String sql, final Object... args) {
        return this.queryForInt(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public Observable<Long> queryForLong(final String sql, final Collection<?> args) {
        return this.queryForBigDecimal(sql, args).map(BigDecimalUtil::toLong);
    }

    @Override
    public Observable<Long> queryForLong(final String sql, final Object... args) {
        return this.queryForBigDecimal(sql, args).map(BigDecimalUtil::toLong);
    }

    @Override
    public final Single<Long> queryForLongUnique(final String sql, final Collection<?> args) {
        return this.queryForBigDecimalUnique(sql, args).map(BigDecimalUtil::toLong);
    }

    @Override
    public final Single<Long> queryForLongUnique(final String sql, final Object... args) {
        return this.queryForBigDecimalUnique(sql, args).map(BigDecimalUtil::toLong);
    }

    @Override
    public Single<Optional<Long>> queryForLongOptional(final String sql, final Collection<?> args) {
        return this.queryForLong(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public Single<Optional<Long>> queryForLongOptional(final String sql, final Object... args) {
        return this.queryForLong(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public Observable<String> queryForString(final String sql, final Collection<?> args) {
        return this.query(sql, args, (rs, count) -> {
            return rs.getString(0);
        });
    }

    @Override
    public Observable<String> queryForString(final String sql, final Object... args) {
        return this.query(sql, args, (rs, count) -> {
            return rs.getString(0);
        });
    }

    @Override
    public final Single<String> queryForStringUnique(final String sql, final Collection<?> args) {
        return this.queryForString(sql, args).toSingle();
    }

    @Override
    public final Single<String> queryForStringUnique(final String sql, final Object... args) {
        return this.queryForString(sql, args).toSingle();
    }

    @Override
    public Single<Optional<String>> queryForStringOptional(final String sql, final Collection<?> args) {
        return this.queryForString(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public Single<Optional<String>> queryForStringOptional(final String sql, final Object... args) {
        return this.queryForString(sql, args).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public <T> Single<T> queryForUnique(final String sql, final Collection<?> args, final IntBiFunction<ResultEntry, T> resultSetRowReader) {
        return query(sql, args, resultSetRowReader).toSingle();
    }

    @Override
    public <T> Single<T> queryForUnique(final String sql, final Object[] args, final IntBiFunction<ResultEntry, T> resultSetRowReader) {
        return query(sql, args, resultSetRowReader).toSingle();
    }

    @Override
    public Single<UpdateResult> update(final String sql, final Collection<?> args) {
        Consumer<Statement> pss = new PrepareStatementSetterCollectionWrapper(args);
        return update(sql, pss);
    }

    @Override
    public <R> Single<R> update(final String sql, final Collection<?> args, final GeneratedKeyReader<R> generatedKeyReader) {
        Consumer<Statement> pss = new PrepareStatementSetterCollectionWrapper(args);
        return update(sql, pss, generatedKeyReader);
    }

    @Override
    public Single<UpdateResult> update(final String sql, final Object... args) {
        Consumer<Statement> pss = new PrepareStatementSetterArrayWrapper(args);
        return update(sql, pss);
    }

    @Override
    public <R> Single<R> update(final String sql, final Object[] args, final GeneratedKeyReader<R> generatedKeyReader) {
        Consumer<Statement> pss = new PrepareStatementSetterArrayWrapper(args);
        return update(sql, pss, generatedKeyReader);
    }

    @Override
    public Single<UpdateResult> update(final String sql, final Consumer<Statement> psc) {
        String sqlProcessed = sqlPreProcessor.apply(sql);
        return connectionProvider.getConnection(true, connection -> {
            return connection.update(sqlProcessed, psc).<UpdateResult> map(updated -> new UpdateResultImpl(updated)).toObservable();
        }).toSingle();
    }

    @Override
    public <R> Single<R> update(final String sql, final Consumer<Statement> psc, final GeneratedKeyReader<R> generatedKeyReader) {
        String sqlProcessed = sqlPreProcessor.apply(sql);
        return connectionProvider.getConnection(true, connection -> {
            return connection.update(sqlProcessed, generatedKeyReader, psc).toObservable();
        }).toSingle();
    }

    @Override
    public <T> Single<Optional<T>> queryForOptional(String sql, Collection<?> args, IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException {
        return query(sql, args, resultSetRowReader).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

    @Override
    public <T> Single<Optional<T>> queryForOptional(String sql, Object[] args, IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException {
        return query(sql, args, resultSetRowReader).map(value -> Optional.of(value)).defaultIfEmpty(Optional.empty()).first().toSingle();
    }

}
