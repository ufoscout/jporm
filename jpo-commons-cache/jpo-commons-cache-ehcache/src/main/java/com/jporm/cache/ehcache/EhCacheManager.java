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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.Ehcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.cache.Cache;
import com.jporm.cache.CacheManager;
import com.jporm.cache.NullCache;

/**
 *
 * @author Francesco Cina'
 *
 * 2 May 2011
 */
public class EhCacheManager implements CacheManager {

	private final net.sf.ehcache.CacheManager cacheManager;
	private final Map<String, Cache<?,?>> cachesMap = new ConcurrentHashMap<>();
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public EhCacheManager(final net.sf.ehcache.CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public <K,V> Cache<K,V> getCache(final String cacheName) {
		Cache<K,V> cache = (Cache<K,V>) cachesMap.get(cacheName);
		if (cache == null) {
			Ehcache ehCache = cacheManager.getEhcache(cacheName);
			if (ehCache != null) {
				cache = new EhCache<>(ehCache);
				cachesMap.put(cacheName, cache);
			} else {
				cache = new NullCache<K,V>();
				logger.warn("Cache [{}] does not exist! No cache will be used.", cacheName);
			}
		}
		return cache;
	}

	@Override
	public void stopCacheManager() {
		cacheManager.shutdown();
	}

}
