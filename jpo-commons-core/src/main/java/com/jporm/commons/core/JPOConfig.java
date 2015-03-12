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
package com.jporm.commons.core;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import com.jporm.cache.CacheManager;
import com.jporm.cache.simple.SimpleCacheManager;
import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.types.TypeConverter;
import com.jporm.types.TypeConverterBuilder;
import com.jporm.validator.NullValidatorService;
import com.jporm.validator.ValidatorService;

public interface JPOConfig {

	/**
	 * Register a new class to be managed as a bean.
	 *
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws OrmConfigurationException
	 */
	<T> JPOConfig register(Class<T> clazz) ;

	/**
	 * Register a list of classes to be managed as a beans.
	 *
	 * @param <T>
	 * @param clazz
	 * @throws OrmConfigurationException
	 */
	JPOConfig register(List<Class<?>> classes) ;

	/**
	 * Register a new {@link TypeConverter}.
	 * If a {@link TypeConverter} wraps a Class that is already mapped, the last registered {@link TypeConverter} will be used.
	 *
	 * @param typeConverter
	 * @throws OrmConfigurationException
	 */
	JPOConfig register(TypeConverter<?, ?> typeConverter);

	/**
	 * Register a new {@link TypeConverterBuilder}.
	 * If a {@link TypeConverter} wraps a Class that is already mapped, the last registered {@link TypeConverter} will be used.
	 *
	 * @param typeConverterBuilder
	 * @throws OrmConfigurationException
	 */
	JPOConfig register(TypeConverterBuilder<?, ?> typeConverterBuilder);

	/**
	 * Set the {@link ValidatorService}.
	 * The default one is {@link NullValidatorService} that performs no validation.
	 * @param validator
	 */
	JPOConfig setValidatorService(ValidatorService validator);

	/**
	 * Set the {@link CacheManager}.
	 * The default is {@link SimpleCacheManager} that uses {@link ConcurrentHashMap} as simple cache system.
	 * @param cacheManager
	 */
	JPOConfig setCacheManager(CacheManager cacheManager);

	/**
	 * Set the {@link AsyncTaskExecutor} for the asynchronous Transaction execution.
	 * By default {@link JPO} uses a {@link ThreadPoolExecutor} with 10 {@link Thread}.
	 * The number of available {@link Thread}s is the number of maximum parallel queries that can run asynchronously;
	 * this number should not be higher than the maximum number of available connections.
	 *
	 * @param asyncTaskExecutor
	 */
	JPOConfig setAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor);

	/**
	 * Set the default timeout for a transaction in seconds.
	 * Default is 0 (no timeout).
	 * @param seconds
	 * @return
	 */
	JPOConfig setTransactionDefaultTimeout(int seconds);

}
