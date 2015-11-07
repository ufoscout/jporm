/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.commons.core.builder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import com.jporm.cache.CacheManager;
import com.jporm.cache.simple.SimpleCacheManager;
import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.async.impl.ThreadPoolAsyncTaskExecutor;
import com.jporm.commons.core.inject.ServiceCatalogImpl;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.types.TypeConverter;
import com.jporm.types.TypeConverterBuilder;
import com.jporm.types.TypeConverterFactory;
import com.jporm.validator.NullValidatorService;
import com.jporm.validator.ValidatorService;

public abstract class AbstractJpoBuilder<T extends AbstractJpoBuilder<?>> {

    private final ServiceCatalogImpl serviceCatalog = new ServiceCatalogImpl();

    /**
     * @return the serviceCatalog
     */
    protected ServiceCatalogImpl getServiceCatalog() {
        return serviceCatalog;
    }

    private TypeConverterFactory getTypeFactory() {
        return serviceCatalog.getTypeFactory();
    }

    /**
     * Register a new {@link TypeConverter}. If a {@link TypeConverter} wraps a
     * Class that is already mapped, the last registered {@link TypeConverter}
     * will be used.
     *
     * @param typeConverter
     * @throws OrmConfigurationException
     */
    public T register(final TypeConverter<?, ?> typeWrapper) {
        getTypeFactory().addTypeConverter(typeWrapper);
        return (T) this;
    }

    /**
     * Register a new {@link TypeConverterBuilder}. If a {@link TypeConverter}
     * wraps a Class that is already mapped, the last registered
     * {@link TypeConverter} will be used.
     *
     * @param typeConverterBuilder
     * @throws OrmConfigurationException
     */
    public T register(final TypeConverterBuilder<?, ?> typeWrapperBuilder) {
        getTypeFactory().addTypeConverter(typeWrapperBuilder);
        return (T) this;
    }

    /**
     * Set a {@link ThreadPoolAsyncTaskExecutor} with a fixed number of
     * {@link Thread}s as {@link AsyncTaskExecutor}. The number of available
     * {@link Thread}s is the number of maximum parallel queries that can run
     * asynchronously; this number should not be higher than the maximum number
     * of available connections.
     *
     * @param maxParallelThreads
     *            should be equals to the max number of allowed parallel
     *            connections
     */
    public T setAsynchTaskExecutorWithMaxParallelThread(final int maxParallelThreads) {
        serviceCatalog.setAsyncTaskExecutor(new ThreadPoolAsyncTaskExecutor(maxParallelThreads));
        return (T) this;
    }

    /**
     * Set the {@link AsyncTaskExecutor} for the asynchronous Transaction
     * execution. By default {@link JpoRm} uses a {@link ThreadPoolExecutor}
     * with 10 {@link Thread}. The number of available {@link Thread}s is the
     * number of maximum parallel queries that can run asynchronously; this
     * number should not be higher than the maximum number of available
     * connections.
     *
     * @param asyncTaskExecutor
     */
    public T setAsyncTaskExecutor(final AsyncTaskExecutor asyncTaskExecutor) {
        if (asyncTaskExecutor != null) {
            serviceCatalog.setAsyncTaskExecutor(asyncTaskExecutor);
        }
        return (T) this;
    }

    /**
     * Set the {@link CacheManager}. The default is {@link SimpleCacheManager}
     * that uses {@link ConcurrentHashMap} as simple cache system.
     * 
     * @param cacheManager
     */
    public T setCacheManager(final CacheManager cacheManager) {
        if (cacheManager != null) {
            serviceCatalog.setCacheManager(cacheManager);
        }
        return (T) this;
    }

    /**
     * Set the default transaction isolation. Default is READ_COMMITTED.
     * 
     * @param seconds
     * @param defaultTransactionIsolation
     * @return
     */
    public T setDefaultTransactionIsolation(final TransactionIsolation defaultTransactionIsolation) {
        serviceCatalog.getConfigService().setDefaultTransactionIsolation(defaultTransactionIsolation);
        return (T) this;
    }

    /**
     * Set the default timeout for a transaction in seconds. Default is -1 (no
     * timeout).
     * 
     * @param seconds
     * @return
     */
    public T setTransactionDefaultTimeout(final int seconds) {
        serviceCatalog.getConfigService().setTransactionDefaultTimeoutSeconds(seconds);
        return (T) this;
    }

    /**
     * Set the {@link ValidatorService}. The default one is
     * {@link NullValidatorService} that performs no validation.
     * 
     * @param validator
     */
    public T setValidatorService(final ValidatorService validatorService) {
        if (validatorService != null) {
            serviceCatalog.setValidatorService(validatorService);
        }
        return (T) this;
    }

}
