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
package com.jporm.commons.core.function;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Functions {

    static <T, R> Consumer<T> chain(Function<T, R> function, Consumer<R> consumer) {
        return (T t) -> {
            consumer.accept(function.apply(t));
        };
    }

    static <T, V, R> Function<T, R> chain(Function<T, V> before, Function<V, R> after) {
        return (T t) -> {
            return after.apply(before.apply(t));
        };
    }

    static <T, R> IntBiConsumer<T> chain(IntBiFunction<T, R> function, Consumer<R> consumer) {
        return (T t, int rowCount) -> {
            consumer.accept(function.apply(t, rowCount));
        };
    }

    static <T, V, R> IntBiFunction<T, R> chain(IntBiFunction<T, V> before, Function<V, R> after) {
        return (T t, int rowCount) -> {
            return after.apply(before.apply(t, rowCount));
        };
    }

    static <T> IntBiConsumer<T> chain(IntBiConsumer<T>... consumers) {
        return (entry, rowCount) -> {
            for (IntBiConsumer<T> consumer : consumers) {
                consumer.accept(entry, rowCount);
            }
        };
    }

}
