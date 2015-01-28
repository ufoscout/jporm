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
package com.jporm.core.cache;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jporm.cache.Cache;
import com.jporm.cache.CacheManager;

/**
 * 
 * @author Francesco Cina'
 *
 * 24/set/2011
 */
public class SimpleCacheManager implements CacheManager, Serializable {

    private static final long serialVersionUID = 1L;
    private final Map<String, Cache> cachesMap = new ConcurrentHashMap<String, Cache>();

    @Override
    public Cache getCache(final String cacheName) {
        Cache cache = cachesMap.get(cacheName);
        if (cache == null) {
            cache = new SimpleCache();
            cachesMap.put(cacheName, cache);
        }
        return cache;
    }

    @Override
    public void stopCacheManager() {
        cachesMap.clear();
    }

}
