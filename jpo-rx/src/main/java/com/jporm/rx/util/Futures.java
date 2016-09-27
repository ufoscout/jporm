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
package com.jporm.rx.util;

import java.util.function.Supplier;

import com.jporm.commons.core.async.AsyncTaskExecutor;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class Futures {

    public static <T> Observable<T> toObservable(AsyncTaskExecutor executor, Supplier<T> task) {
        return Observable.create(subscriber ->
                executor.execute(task)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        subscriber.onError(error);
                    } else {
                        if (result!=null) {
                            subscriber.onNext(result);
                        }
                        subscriber.onCompleted();
                    }
                }));
    }

    public static <T> Single<T> toSingle(AsyncTaskExecutor executor, Supplier<T> task) {
        return Single.create(subscriber ->
                executor.execute(task)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        subscriber.onError(error);
                    } else {
                        subscriber.onSuccess(result);
                    }
                }));
    }

    public static Completable toCompletable(AsyncTaskExecutor executor, Runnable task) {
        return Completable.create((rx.CompletableSubscriber subscriber) ->
                executor.execute(task)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        subscriber.onError(error);
                    } else {
                        subscriber.onCompleted();
                    }
                }));
    }

}
