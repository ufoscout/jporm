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
package com.jporm.core.cache;

public class NullCache extends ACache {

	@Override
	public void put(final Object key, final Object value) {
	}

	@Override
	public void remove(final Object key) {
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean contains(final Object key) {
		return false;
	}

	@Override
	protected Object getValue(final Object key) {
		return null;
	}

}
