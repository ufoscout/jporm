/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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

import java.util.function.Function;

import rx.Completable;
import rx.Observable;

public class CloseConnectionStrategyFullImpl implements CloseConnectionStrategy {

    @Override
    public <T> Observable<T> commitOrRollback(Observable<T> result, RxConnection rxConnection, boolean readOnly) {
        try {
            Completable commitOrRollback;
            if (!readOnly) {
                commitOrRollback = rxConnection.commit();
            } else {
                commitOrRollback = rxConnection.rollback();
            }

            return result
                .onErrorResumeNext(e -> {
                    return rxConnection.rollback().<T>toObservable().concatWith(Observable.error(e));
                })
                .concatWith(commitOrRollback.toObservable());
        } catch (Exception e) {
            return rxConnection.rollback().<T>toObservable().concatWith(Observable.error(e));
        }
    }

    @Override
    public <T> Observable<T> autoClose(RxConnection rxConnection, Function<RxConnection, Observable<T>> connection) {
        try {
            return connection.apply(rxConnection)
                    .onErrorResumeNext(e ->
                        rxConnection.close().<T>toObservable().concatWith(Observable.error(e)))
                    .concatWith(rxConnection.close().toObservable());
        } catch (Exception e) {
            return rxConnection.rollback().<T>toObservable()
                    .onErrorResumeNext(ex -> rxConnection.close().<T>toObservable().concatWith(Observable.error(e)))
                    .concatWith(rxConnection.close().toObservable()).concatWith(Observable.error(e));
        }
    }

}
