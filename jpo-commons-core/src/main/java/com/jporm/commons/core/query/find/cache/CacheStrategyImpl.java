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
 *          ON : Mar 5, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.commons.core.query.find.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.cache.Cache;
import com.jporm.commons.core.inject.ServiceCatalog;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 5, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class CacheStrategyImpl implements CacheStrategy {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final CacheStrategyEntry<Object> nullCallback = new CacheStrategyEntry<Object>() {
        @Override
        public void add(final Object bean) {//do nothing
        }
        @Override
        public void end() {//do nothing
        }
    };

    private ServiceCatalog serviceCatalog;

    public CacheStrategyImpl(final ServiceCatalog serviceCatalog) {
        this.serviceCatalog = serviceCatalog;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <BEAN> void find(final String cacheName, final String sql, final List<Object> values,
            final List<String> ignoredFields, final Consumer<List<BEAN>> ifFoundCallback,
            final CacheStrategyCallback<BEAN> cacheStrategyCallback) {

        if ((cacheName==null) || cacheName.isEmpty()) {
            logger.trace("Cache disabled for query [{}]", sql); //$NON-NLS-1$
            cacheStrategyCallback.doWhenNotInCache((CacheStrategyEntry<BEAN>) nullCallback);
            return;
        }

        final Cache<CacheKey, List<BEAN>> cache = serviceCatalog.getCacheManager().getCache(cacheName);
        final CacheKey key = new CacheKey(sql, values, ignoredFields);
        if (logger.isDebugEnabled()) {
            logger.debug("Using cache [{}] for query [{}], ignoreFields {}, values {}", new Object[]{cacheName, sql, ignoredFields, values}); //$NON-NLS-1$
            logger.debug("Query key hashcode [{}]", key.hashCode()); //$NON-NLS-1$
        }

        List<BEAN> cached = cache.get(key);
        if (cached!=null) {
            logger.debug("Sql results found in cache"); //$NON-NLS-1$
            ifFoundCallback.accept(cached);
        } else {
            logger.debug("Sql results NOT found in cache"); //$NON-NLS-1$
            cacheStrategyCallback.doWhenNotInCache(new CacheStrategyEntry<BEAN>() {

                List<BEAN> beans = new ArrayList<BEAN>();

                @Override
                public void add(final BEAN bean) {
                    beans.add(bean);
                }

                @Override
                public void end() {
                    if(!beans.isEmpty()) {
                        cache.put(key, beans);
                    }
                }
            });
        }

    }

}
