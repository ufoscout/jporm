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
package com.jporm.rx;

import java.util.concurrent.ConcurrentHashMap;

import com.jporm.cache.CacheManager;
import com.jporm.cache.simple.SimpleCacheManager;
import com.jporm.commons.core.inject.ServiceCatalogImpl;
import com.jporm.rx.core.session.ConnectionProvider;
import com.jporm.types.TypeConverter;
import com.jporm.types.TypeConverterBuilder;
import com.jporm.types.TypeConverterFactory;

public class JpoRxBuilder {

	private final ServiceCatalogImpl serviceCatalog = new ServiceCatalogImpl();

	/**
	 * Register a new {@link TypeConverter}.
	 * If a {@link TypeConverter} wraps a Class that is already mapped, the last registered {@link TypeConverter} will be used.
	 *
	 * @param typeConverter
	 * @throws OrmConfigurationException
	 */
	public JpoRxBuilder register(final TypeConverter<?, ?> typeWrapper) {
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
	public JpoRxBuilder register(final TypeConverterBuilder<?, ?> typeWrapperBuilder) {
		getTypeFactory().addTypeConverter(typeWrapperBuilder);
		return this;
	}

//	/**
//	 * Set the {@link ValidatorService}.
//	 * The default one is {@link NullValidatorService} that performs no validation.
//	 * @param validator
//	 */
//	public JpoRxBuilder setValidatorService(final ValidatorService validatorService) {
//		if (validatorService!=null) {
//			serviceCatalog.setValidatorService(validatorService);
//		}
//		return this;
//	}

	private TypeConverterFactory getTypeFactory() {
		return serviceCatalog.getTypeFactory();
	}

	/**
	 * Set the {@link CacheManager}.
	 * The default is {@link SimpleCacheManager} that uses {@link ConcurrentHashMap} as simple cache system.
	 * @param cacheManager
	 */
	public JpoRxBuilder setCacheManager(final CacheManager cacheManager) {
		if (cacheManager!=null) {
			serviceCatalog.setCacheManager(cacheManager);
		}
		return this;
	}

//	/**
//	 * Set the default timeout for a transaction in seconds.
//	 * Default is 0 (no timeout).
//	 * @param seconds
//	 * @return
//	 */
//	public JpoRxBuilder setTransactionDefaultTimeout(int seconds) {
//		serviceCatalog.getConfigService().setTransactionDefaultTimeoutSeconds(seconds);
//		return this;
//	}

	/**
	 * Create a {@link JPO} instance
	 * @param sessionProvider
	 * @return
	 */
	public JpoRX build(final ConnectionProvider connectionProvider) {
		return new JpoRxImpl(connectionProvider, serviceCatalog);
	}

}
