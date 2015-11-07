/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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

public class NullCache<K, V> extends ACache<K, V> {

    @Override
    public void clear() {
    }

    @Override
    public boolean contains(final K key) {
        return false;
    }

    @Override
    public <K1, V1> V1 get(final K1 key, final Class<V1> clazz) {
        return null;
    }

    @Override
    protected <K1, V1> V1 getValue(final K1 key) {
        return null;
    }

    @Override
    public <K1, V1> void put(final K1 key, final V1 value) {
    }

    @Override
    public <K1> void remove(final K1 key) {
    }

}
