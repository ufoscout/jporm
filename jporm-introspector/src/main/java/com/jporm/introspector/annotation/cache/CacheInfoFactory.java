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
package com.jporm.core.annotation.cache;

import com.jporm.annotation.Cache;
import com.jporm.core.factory.ObjectBuilder;

/**
 * 
 * @author cinafr
 *
 */
public class CacheInfoFactory {

    private CacheInfoFactory() {}

    public static CacheInfo getCacheInfo(final Class<?> clazz) {
        Cache cache = clazz.getAnnotation(Cache.class);
        if (cache!=null) {
            return new CacheInfoImpl(true, cache.cacheName());
        }
        return new CacheInfoImpl(false, ObjectBuilder.EMPTY_STRING);
    }

}
