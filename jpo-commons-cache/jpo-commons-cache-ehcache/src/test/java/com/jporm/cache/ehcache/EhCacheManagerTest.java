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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 27, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.cache.ehcache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.jporm.cache.BaseEhcacheTestApi;
import com.jporm.cache.Cache;
import com.jporm.cache.CacheManager;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 27, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class EhCacheManagerTest extends BaseEhcacheTestApi {

    private static String CACHE_NAME = "query.ObjectShortTermCache";
    @Resource
    private CacheManager cacheManager;

    @Before
    public void setUp() {
        assertNotNull(cacheManager);
    }

    @Test
    public void testCache2() {
        Cache<String, String> nullStringCache = cacheManager.getCache(CACHE_NAME);
        assertNotNull(nullStringCache);
        assertNull(nullStringCache.get("hello"));
        assertNull(nullStringCache.get(null));
        nullStringCache.put("key", "value");
        nullStringCache.put("key", null);
        nullStringCache.put(null, "value");
        nullStringCache.put(null, null);
    }

    @Test
    public void testCache3() {
        Cache<String, String> cache = cacheManager.getCache(CACHE_NAME);
        assertNotNull(cache);
        String key = "test-key-" + new Date().getTime();
        assertNull(cache.get(key));
        assertNull(cache.get(null));
        assertFalse(cache.contains(key));

        cache.put(key, "value");
        assertTrue(cache.contains(key));
        assertNotNull(cache.get(key));
        assertEquals("value", cache.get(key, String.class));

        cache.clear();
        assertNull(cache.get(key));

        cache.put("key", null);
        cache.put(null, "value");
        cache.put(null, null);

        cache.clear();
    }

    @Test
    public void testCache4() {
        Cache<String, String> cache = cacheManager.getCache(CACHE_NAME);
        assertNotNull(cache);
        String key1 = "test-key1-" + new Date().getTime();
        String key2 = "test-key2-" + new Date().getTime();
        String key3 = "test-key3-" + new Date().getTime();
        assertNull(cache.get(key1));
        assertNull(cache.get(key2));
        assertNull(cache.get(key3));

        cache.put(key1, "value1");
        assertNotNull(cache.get(key1));
        assertEquals("value1", cache.get(key1, String.class));

        cache.put(key2, "value2");
        assertNotNull(cache.get(key2));
        assertEquals("value2", cache.get(key2, String.class));

        cache.put(key3, "value3");
        assertNotNull(cache.get(key3));
        assertEquals("value3", cache.get(key3, String.class));

        cache.remove(key2);
        assertNotNull(cache.get(key1));
        assertNull(cache.get(key2));
        assertNotNull(cache.get(key3));

        cache.clear();
        assertNull(cache.get(key1));
        assertNull(cache.get(key2));
        assertNull(cache.get(key3));
        cache.clear();
    }

}
