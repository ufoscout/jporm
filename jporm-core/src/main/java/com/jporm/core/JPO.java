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
package com.jporm.core;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import com.jporm.cache.CacheManager;
import com.jporm.core.async.AsyncTaskExecutor;
import com.jporm.core.session.Session;
import com.jporm.core.transaction.Transaction;
import com.jporm.types.TypeWrapper;
import com.jporm.types.TypeWrapperBuilder;
import com.jporm.validator.ValidatorService;

/**
 *
 * @author Francesco Cina
 *
 * 21/mag/2011
 */
public interface JPO {

	/**
	 * Return a {@link Session} from the current {@link JPO} implementation
	 * @return
	 */
	Session session();

	/**
	 * Destroy the current {@link JPO} instance and all it's references.
	 */
	void destory();

	/**
	 * Register a new class to be managed as a bean.
	 *
	 * @param <T>
	 * @param clazz
	 * @throws OrmConfigurationException
	 */
	<T> void register(Class<T> clazz) ;

	/**
	 * Register a list of classes to be managed as a beans.
	 *
	 * @param <T>
	 * @param clazz
	 * @throws OrmConfigurationException
	 */
	void register(List<Class<?>> classes) ;

	/**
	 * Register a new {@link TypeWrapper}.
	 * If a {@link TypeWrapper} wraps a Class that is already mapped, the last registered {@link TypeWrapper} will be used.
	 *
	 * @param typeWrapper
	 * @throws OrmConfigurationException
	 */
	void register(TypeWrapper<?, ?> typeWrapper);

	/**
	 * Register a new {@link TypeWrapperBuilder}.
	 * If a {@link TypeWrapper} wraps a Class that is already mapped, the last registered {@link TypeWrapper} will be used.
	 *
	 * @param typeWrapperBuilder
	 * @throws OrmConfigurationException
	 */
	void register(TypeWrapperBuilder<?, ?> typeWrapperBuilder);

	/**
	 * Set the {@link ValidatorService}.
	 * The default one is {@link NullValidatorService} that performs no validation.
	 * @param validator
	 */
	void setValidatorService(ValidatorService validator);

	/**
	 * Set the {@link CacheManager}.
	 * The default is {@link SimpleCacheManager} that uses {@link ConcurrentHashMap} as simple cache system.
	 * @param cacheManager
	 */
	void setCacheManager(CacheManager cacheManager);

	/**
	 * Set the {@link AsyncTaskExecutor} for the asynchronous {@link Transaction} execution.
	 * By default {@link JPO} uses a {@link ThreadPoolExecutor} with 10 {@link Thread}.
	 * The number of available {@link Thread}s is the number of maximum parallel queries that can run asynchronously;
	 * this number should not be higher than the maximum number of available connections.
	 *
	 * @param asyncTaskExecutor
	 */
	void setAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor);
}
