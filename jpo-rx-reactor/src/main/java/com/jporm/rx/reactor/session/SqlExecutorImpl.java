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
package com.jporm.rx.reactor.session;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.function.IntBiConsumer;
import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.rx.query.update.UpdateResult;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SqlExecutorImpl implements SqlExecutor {

    private final com.jporm.rx.session.SqlExecutor sqlExecutor;

    public SqlExecutorImpl(com.jporm.rx.session.SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public Mono<int[]> batchUpdate(final Collection<String> sqls) throws JpoException {
        return Mono.fromCompletableFuture(sqlExecutor.batchUpdate(sqls));
    }

    @Override
    public Mono<int[]> batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
        return Mono.fromCompletableFuture(sqlExecutor.batchUpdate(sql, psc));
    }

    @Override
    public Mono<int[]> batchUpdate(final String sql, final Collection<Object[]> args) throws JpoException {
        return Mono.fromCompletableFuture(sqlExecutor.batchUpdate(sql, args));
    }

    @Override
    public Flux<Void> execute(final String sql) throws JpoException {
        return Mono.fromCompletableFuture(sqlExecutor.execute(sql)).flux();
    }

    @Override
    public <T> Flux<T> query(final String sql, final Collection<?> args, final IntBiFunction<ResultEntry, T> resultSetRowReader) {
        return Flux.from(publisher -> {
            try {
                sqlExecutor.query(sql, args, (resultSet) -> {
                    int count = 0;
                    while (resultSet.hasNext()) {
                        publisher.onNext(resultSetRowReader.apply(resultSet.next(), count++));
                    }
                    publisher.onComplete();
                });
            } catch (Throwable e) {
                publisher.onError(e);
            }
        });
    }

    @Override
    public Flux<Void> query(final String sql, final Collection<?> args, final IntBiConsumer<ResultEntry> resultSetRowReader) throws JpoException {
        return Mono.fromCompletableFuture(sqlExecutor.query(sql, args, resultSetRowReader)).flux();
    }

    @Override
    public <T> Mono<T> query(final String sql, final Collection<?> args, final Function<ResultSet, T> resultSetReader) throws JpoException {
        return Mono.fromCompletableFuture(sqlExecutor.query(sql, args, resultSetReader));
    }

    @Override
    public Flux<Void> query(final String sql, final Collection<?> args, final Consumer<ResultSet> resultSetReader) throws JpoException {
        return Mono.fromCompletableFuture(sqlExecutor.query(sql, args, resultSetReader)).flux();
    }

    @Override
    public <T> Flux<T> query(final String sql, final Object[] args, final IntBiFunction<ResultEntry, T> resultSetRowReader) {
        return Flux.from(publisher -> {
            try {
                sqlExecutor.query(sql, args, (resultSet) -> {
                    int count = 0;
                    while (resultSet.hasNext()) {
                        publisher.onNext(resultSetRowReader.apply(resultSet.next(), count++));
                    }
                    publisher.onComplete();
                });
            } catch (Throwable e) {
                publisher.onError(e);
            }
        });
    }

    @Override
    public Flux<Void> query(final String sql, final Object[] args, final IntBiConsumer<ResultEntry> resultSetRowReader) throws JpoException {
        return Mono.fromCompletableFuture(sqlExecutor.query(sql, args, resultSetRowReader)).flux();
    }

    @Override
    public <T> Mono<T> query(final String sql, final Object[] args, final Function<ResultSet, T> resultSetReader) throws JpoException {
        return Mono.fromCompletableFuture(sqlExecutor.query(sql, args, resultSetReader));
    }

    @Override
    public Flux<Void> query(final String sql, final Object[] args, final Consumer<ResultSet> resultSetReader) throws JpoException {
        return Mono.fromCompletableFuture(sqlExecutor.query(sql, args, resultSetReader)).flux();
    }

    @Override
    public Mono<BigDecimal> queryForBigDecimal(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForBigDecimal(sql, args));
    }

    @Override
    public Mono<BigDecimal> queryForBigDecimal(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForBigDecimal(sql, args));
    }

    @Override
    public final Mono<BigDecimal> queryForBigDecimalUnique(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForBigDecimalUnique(sql, args));
    }

    @Override
    public final Mono<BigDecimal> queryForBigDecimalUnique(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForBigDecimalUnique(sql, args));
    }

    @Override
    public final Mono<Optional<BigDecimal>> queryForBigDecimalOptional(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForBigDecimalOptional(sql, args));
    }

    @Override
    public final Mono<Optional<BigDecimal>> queryForBigDecimalOptional(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForBigDecimalOptional(sql, args));
    }

    @Override
    public Mono<Boolean> queryForBoolean(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForBoolean(sql, args));
    }

    @Override
    public Mono<Boolean> queryForBoolean(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForBoolean(sql, args));
    }

    @Override
    public final Mono<Boolean> queryForBooleanUnique(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForBooleanUnique(sql, args));
    }

    @Override
    public final Mono<Boolean> queryForBooleanUnique(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForBooleanUnique(sql, args));
    }

    @Override
    public final Mono<Optional<Boolean>> queryForBooleanOptional(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForBooleanOptional(sql, args));
    }

    @Override
    public final Mono<Optional<Boolean>> queryForBooleanOptional(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForBooleanOptional(sql, args));
    }

    @Override
    public Mono<Double> queryForDouble(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForDouble(sql, args));
    }

    @Override
    public Mono<Double> queryForDouble(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForDouble(sql, args));
    }

    @Override
    public final Mono<Double> queryForDoubleUnique(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForDoubleUnique(sql, args));
    }

    @Override
    public final Mono<Double> queryForDoubleUnique(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForDoubleUnique(sql, args));
    }

    @Override
    public final Mono<Optional<Double>> queryForDoubleOptional(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForDoubleOptional(sql, args));
    }

    @Override
    public final Mono<Optional<Double>> queryForDoubleOptional(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForDoubleOptional(sql, args));
    }

    @Override
    public Mono<Float> queryForFloat(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForFloat(sql, args));
    }

    @Override
    public Mono<Float> queryForFloat(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForFloat(sql, args));
    }

    @Override
    public final Mono<Float> queryForFloatUnique(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForFloatUnique(sql, args));
    }

    @Override
    public final Mono<Float> queryForFloatUnique(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForFloatUnique(sql, args));
    }

    @Override
    public final Mono<Optional<Float>> queryForFloatOptional(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForFloatOptional(sql, args));
    }

    @Override
    public final Mono<Optional<Float>> queryForFloatOptional(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForFloatOptional(sql, args));
    }

    @Override
    public Mono<Integer> queryForInt(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForInt(sql, args));
    }

    @Override
    public Mono<Integer> queryForInt(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForInt(sql, args));
    }

    @Override
    public final Mono<Integer> queryForIntUnique(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForIntUnique(sql, args));
    }

    @Override
    public final Mono<Integer> queryForIntUnique(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForIntUnique(sql, args));
    }

    @Override
    public final Mono<Optional<Integer>> queryForIntOptional(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForIntOptional(sql, args));
    }

    @Override
    public final Mono<Optional<Integer>> queryForIntOptional(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForIntOptional(sql, args));
    }

    @Override
    public Mono<Long> queryForLong(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForLong(sql, args));
    }

    @Override
    public Mono<Long> queryForLong(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForLong(sql, args));
    }

    @Override
    public final Mono<Long> queryForLongUnique(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForLongUnique(sql, args));
    }

    @Override
    public final Mono<Long> queryForLongUnique(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForLongUnique(sql, args));
    }

    @Override
    public final Mono<Optional<Long>> queryForLongOptional(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForLongOptional(sql, args));
    }

    @Override
    public final Mono<Optional<Long>> queryForLongOptional(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForLongOptional(sql, args));
    }

    @Override
    public Mono<String> queryForString(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForString(sql, args));
    }

    @Override
    public Mono<String> queryForString(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForString(sql, args));
    }

    @Override
    public final Mono<String> queryForStringUnique(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForStringUnique(sql, args));
    }

    @Override
    public final Mono<String> queryForStringUnique(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForStringUnique(sql, args));
    }

    @Override
    public final Mono<Optional<String>> queryForStringOptional(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForStringOptional(sql, args));
    }

    @Override
    public final Mono<Optional<String>> queryForStringOptional(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForStringOptional(sql, args));
    }

    @Override
    public <T> Mono<T> queryForUnique(final String sql, final Collection<?> args, final IntBiFunction<ResultEntry, T> resultSetRowReader) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForUnique(sql, args, resultSetRowReader));
    }

    @Override
    public <T> Mono<T> queryForUnique(final String sql, final Object[] args, final IntBiFunction<ResultEntry, T> resultSetRowReader) {
        return Mono.fromCompletableFuture(sqlExecutor.queryForUnique(sql, args, resultSetRowReader));
    }

    @Override
    public Mono<UpdateResult> update(final String sql, final Collection<?> args) {
        return Mono.fromCompletableFuture(sqlExecutor.update(sql, args));
    }

    @Override
    public <R> Mono<R> update(final String sql, final Collection<?> args, final GeneratedKeyReader<R> generatedKeyReader) {
        return Mono.fromCompletableFuture(sqlExecutor.update(sql, args, generatedKeyReader));
    }

    @Override
    public Mono<UpdateResult> update(final String sql, final Object... args) {
        return Mono.fromCompletableFuture(sqlExecutor.update(sql, args));
    }

    @Override
    public <R> Mono<R> update(final String sql, final Object[] args, final GeneratedKeyReader<R> generatedKeyReader) {
        return Mono.fromCompletableFuture(sqlExecutor.update(sql, args, generatedKeyReader));
    }

    @Override
    public Mono<UpdateResult> update(final String sql, final Consumer<Statement> psc) {
        return Mono.fromCompletableFuture(sqlExecutor.update(sql, psc));
    }

    @Override
    public <R> Mono<R> update(final String sql, final Consumer<Statement> psc, final GeneratedKeyReader<R> generatedKeyReader) {
        return Mono.fromCompletableFuture(sqlExecutor.update(sql, psc, generatedKeyReader));

    }

    @Override
    public <T> Mono<Optional<T>> queryForOptional(String sql, Collection<?> args, IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException {
        return Mono.fromCompletableFuture(sqlExecutor.queryForOptional(sql, args, resultSetRowReader));
    }

    @Override
    public <T> Mono<Optional<T>> queryForOptional(String sql, Object[] args, IntBiFunction<ResultEntry, T> resultSetRowReader) throws JpoException {
        return Mono.fromCompletableFuture(sqlExecutor.queryForOptional(sql, args, resultSetRowReader));
    }

}
