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
package com.jporm.cache.ehcache;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.jporm.cache.ACache;

/**
 *
 * @author Francesco Cina'
 *
 * 2 May 2011
 */
public class EhCache extends ACache {

	private final Ehcache ehcache;

	public EhCache(final Ehcache ehcache) {
		this.ehcache = ehcache;
	}

	@Override
	public Object getValue(final Object key) {
		Element element = ehcache.get(key);
		if (element != null) {
			return element.getValue();
		}
		return null;
	}

	@Override
	public void put(final Object key, final Object value) {
		ehcache.put(new Element(key, value));
	}

	@Override
	public void clear() {
		ehcache.removeAll();
	}

	@Override
	public void remove(final Object key) {
		ehcache.remove(key);
	}

	@Override
	public boolean contains(final Object key) {
		return ehcache.isKeyInCache(key);
	}

}
