/*******************************************************************************
 * Copyright 2013 Francesco Cina'
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
package com.jporm.cache;

import java.util.function.Function;

/**
 *
 * @author Francesco Cina'
 *
 *         2 May 2011
 */
public interface Cache<K, V> {

    void clear();

    boolean contains(K key);

    V get(K key);

    V get(K key, Function<K, V> providerIfAbsent);

    <K1, V1> V1 get(K1 key, Class<V1> clazz);

    <K1, V1> V1 get(K1 key, Class<V1> clazz, Function<K1, V1> providerIfAbsent);

    <K1, V1> void put(K1 key, V1 value);

    <K1> void remove(K1 key);

}
