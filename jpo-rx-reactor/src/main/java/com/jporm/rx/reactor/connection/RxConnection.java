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
package com.jporm.rx.reactor.connection;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.function.IntBiFunction;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.Statement;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface RxConnection {

    Single<int[]> batchUpdate(Collection<String> sqls, Function<String, String> sqlPreProcessor);

    Single<int[]> batchUpdate(String sql, BatchPreparedStatementSetter psc);

    Single<int[]> batchUpdate(String sql, Collection<Consumer<Statement>> statementSetters);

    Completable close();

    Completable commit();

    Completable execute(String sql);

    <T> Observable<T> query(String sql, final Consumer<Statement> statementSetter, IntBiFunction<ResultEntry, T> resultSetReader);

    Completable rollback();

    void setReadOnly(boolean readOnly);

    void setTimeout(int timeout);

    void setTransactionIsolation(TransactionIsolation isolation);

    Single<Integer> update(String sql, final Consumer<Statement> statementSetter);

    <T> Single<T> update(String sql, GeneratedKeyReader<T> generatedKeyReader, final Consumer<Statement> statementSetter);

}
