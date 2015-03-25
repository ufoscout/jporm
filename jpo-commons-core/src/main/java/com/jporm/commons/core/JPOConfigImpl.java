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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.ClassDescriptorBuilderImpl;
import com.jporm.cache.CacheManager;
import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.inject.ClassToolImpl;
import com.jporm.commons.core.inject.ServiceCatalogImpl;
import com.jporm.persistor.Persistor;
import com.jporm.persistor.PersistorGeneratorImpl;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.TypeConverter;
import com.jporm.types.TypeConverterBuilder;
import com.jporm.validator.ValidatorService;

public class JPOConfigImpl implements JPOConfig {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ServiceCatalogImpl serviceCatalog;

	public JPOConfigImpl() {
		serviceCatalog = new ServiceCatalogImpl(this);
	}

	@Override
	public synchronized <BEAN> JPOConfig register(final Class<BEAN> clazz) {
		try {
			if (!getServiceCatalog().getClassToolMap().containsTool(clazz)) {
				logger.debug("register new class: " + clazz.getName());
				final ClassDescriptor<BEAN> classDescriptor = new ClassDescriptorBuilderImpl<BEAN>(clazz, getServiceCatalog().getTypeFactory()).build();
				final Persistor<BEAN> ormPersistor =  new PersistorGeneratorImpl<BEAN>(classDescriptor, getTypeFactory()).generate();
				ClassTool<BEAN> classTool = new ClassToolImpl<BEAN>(classDescriptor, ormPersistor);
				serviceCatalog.getClassToolMap().put(clazz, classTool);
			}
		} catch (final Exception e) {
			throw new JpoException(e);
		}
		return this;
	}

	public ServiceCatalogImpl getServiceCatalog() {
		return serviceCatalog;
	}

	@Override
	public synchronized JPOConfig register(final List<Class<?>> classes) {
		for (final Class<?> clazz : classes) {
			this.register(clazz);
		}
		return this;
	}

	@Override
	public synchronized JPOConfig register(final TypeConverter<?, ?> typeWrapper) {
		getTypeFactory().addTypeConverter(typeWrapper);
		return this;
	}

	@Override
	public synchronized JPOConfig register(final TypeConverterBuilder<?, ?> typeWrapperBuilder) {
		getTypeFactory().addTypeConverter(typeWrapperBuilder);
		return this;
	}

	@Override
	public synchronized JPOConfig setValidatorService(final ValidatorService validatorService) {
		if (validatorService!=null) {
			serviceCatalog.setValidatorService(validatorService);
		}
		return this;
	}

	public TypeConverterFactory getTypeFactory() {
		return getServiceCatalog().getTypeFactory();
	}

	@Override
	public JPOConfig setCacheManager(final CacheManager cacheManager) {
		if (cacheManager!=null) {
			serviceCatalog.setCacheManager(cacheManager);
		}
		return this;
	}

	@Override
	public JPOConfig setAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor) {
		if (asyncTaskExecutor!=null) {
			serviceCatalog.setAsyncTaskExecutor(asyncTaskExecutor);
		}
		return this;
	}

	@Override
	public JPOConfig setTransactionDefaultTimeout(int seconds) {
		serviceCatalog.getConfigService().setTransactionDefaultTimeoutSeconds(seconds);
		return this;
	}
}
