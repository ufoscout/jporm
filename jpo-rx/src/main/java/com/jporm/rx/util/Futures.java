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

import java.util.concurrent.Executor;
import java.util.function.Supplier;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class Futures {

    public static <T> Maybe<T> toMaybe(Executor executor, Supplier<T> task) {
        return Maybe.create(subscriber -> {
            executor.execute(() -> {
                try {
                    T result = task.get();
                    if (result!=null) {
                        subscriber.onSuccess(result);
                    } else {
                        subscriber.onComplete();
                    }
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            });
        });
    }

    public static <T> Single<T> toSingle(Executor executor, Supplier<T> task) {
        return Single.create(subscriber -> {
            executor.execute(() -> {
                try {
                    subscriber.onSuccess(task.get());
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            });
        });
    }

    public static Completable toCompletable(Executor executor, Runnable task) {
        return Completable.create(subscriber -> {
            executor.execute(() -> {
                try {
                    task.run();
                    subscriber.onComplete();
                } catch (Throwable e) {
                    subscriber.onError(e);
                }
            });
        });
    }

}
