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

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Functions {

    static <T, R> Consumer<T> chain(Function<T, R> function, Consumer<R> consumer) {
        return (T t) -> {
            consumer.accept(function.apply(t));
        };
    }

    static <T, U, R> BiConsumer<T, U> chain(BiFunction<T, U, R> function, Consumer<R> consumer) {
        return (T t, U u) -> {
            consumer.accept(function.apply(t, u));
        };
    }

    static <T> Stream<T> toFiniteStream(final Iterator<T> iterator) {
        final Iterable<T> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

//    static <T> Stream<T> toInfiniteStream(final Iterator<T> iterator) {
//        return Stream.generate(iterator::next);
//    }

}
