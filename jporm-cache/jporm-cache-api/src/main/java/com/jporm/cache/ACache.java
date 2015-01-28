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
 * 24/set/2011
 */
public abstract class ACache implements Cache {

	@Override
	@SuppressWarnings("unchecked")
	public final <T> T get(final Object key, final Class<T> clazz) {
		return (T) get(key);
	}

	@Override
	public final Object get(final Object key) {
		return getValue(key);
	}

	@Override
	public final <K, T> T get(K key, Class<T> clazz, Function<K, T> providerIfAbsent) {
		T value = getValue(key);
		if (value == null) {
			value = providerIfAbsent.apply(key);
			put(key, value);
		}
		return value;
	}

	protected abstract<K, T> T getValue(K key);

}
