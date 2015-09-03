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
package com.jporm.rm;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import com.jporm.cache.CacheManager;
import com.jporm.cache.simple.SimpleCacheManager;
import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.inject.ServiceCatalogImpl;
import com.jporm.rm.session.ConnectionProvider;
import com.jporm.types.TypeConverter;
import com.jporm.types.TypeConverterBuilder;
import com.jporm.types.TypeConverterFactory;
import com.jporm.validator.NullValidatorService;
import com.jporm.validator.ValidatorService;

public class JpoRmBuilder {

	protected final ServiceCatalogImpl serviceCatalog = new ServiceCatalogImpl();

	public static JpoRmBuilder get() {
		return new JpoRmBuilder();
	}

	/**
	 * Register a new {@link TypeConverter}.
	 * If a {@link TypeConverter} wraps a Class that is already mapped, the last registered {@link TypeConverter} will be used.
	 *
	 * @param typeConverter
	 * @throws OrmConfigurationException
	 */
	public JpoRmBuilder register(final TypeConverter<?, ?> typeWrapper) {
		getTypeFactory().addTypeConverter(typeWrapper);
		return this;
	}

	/**
	 * Register a new {@link TypeConverterBuilder}.
	 * If a {@link TypeConverter} wraps a Class that is already mapped, the last registered {@link TypeConverter} will be used.
	 *
	 * @param typeConverterBuilder
	 * @throws OrmConfigurationException
	 */
	public JpoRmBuilder register(final TypeConverterBuilder<?, ?> typeWrapperBuilder) {
		getTypeFactory().addTypeConverter(typeWrapperBuilder);
		return this;
	}

	/**
	 * Set the {@link ValidatorService}.
	 * The default one is {@link NullValidatorService} that performs no validation.
	 * @param validator
	 */
	public JpoRmBuilder setValidatorService(final ValidatorService validatorService) {
		if (validatorService!=null) {
			serviceCatalog.setValidatorService(validatorService);
		}
		return this;
	}

	private TypeConverterFactory getTypeFactory() {
		return serviceCatalog.getTypeFactory();
	}

	/**
	 * Set the {@link CacheManager}.
	 * The default is {@link SimpleCacheManager} that uses {@link ConcurrentHashMap} as simple cache system.
	 * @param cacheManager
	 */
	public JpoRmBuilder setCacheManager(final CacheManager cacheManager) {
		if (cacheManager!=null) {
			serviceCatalog.setCacheManager(cacheManager);
		}
		return this;
	}

	/**
	 * Set the {@link AsyncTaskExecutor} for the asynchronous Transaction execution.
	 * By default {@link JpoRm} uses a {@link ThreadPoolExecutor} with 10 {@link Thread}.
	 * The number of available {@link Thread}s is the number of maximum parallel queries that can run asynchronously;
	 * this number should not be higher than the maximum number of available connections.
	 *
	 * @param asyncTaskExecutor
	 */
	public JpoRmBuilder setAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor) {
		if (asyncTaskExecutor!=null) {
			serviceCatalog.setAsyncTaskExecutor(asyncTaskExecutor);
		}
		return this;
	}

	/**
	 * Set the default timeout for a transaction in seconds.
	 * Default is 0 (no timeout).
	 * @param seconds
	 * @return
	 */
	public JpoRmBuilder setTransactionDefaultTimeout(int seconds) {
		serviceCatalog.getConfigService().setTransactionDefaultTimeoutSeconds(seconds);
		return this;
	}

	/**
	 * Create a {@link JpoRm} instance
	 * @param sessionProvider
	 * @return
	 */
	public JpoRm build(final ConnectionProvider connectionProvider) {
		return new JpoRmImpl(connectionProvider, serviceCatalog);
	}

}
