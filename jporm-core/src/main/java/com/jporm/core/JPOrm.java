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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.ClassDescriptorBuilderImpl;
import com.jporm.async.AsyncTaskExecutor;
import com.jporm.cache.CacheManager;
import com.jporm.core.inject.ClassTool;
import com.jporm.core.inject.ClassToolImpl;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.inject.ServiceCatalogImpl;
import com.jporm.core.session.SessionImpl;
import com.jporm.core.session.SessionProvider;
import com.jporm.exception.OrmConfigurationException;
import com.jporm.persistor.Persistor;
import com.jporm.persistor.PersistorGeneratorImpl;
import com.jporm.session.Session;
import com.jporm.types.TypeFactory;
import com.jporm.types.TypeWrapper;
import com.jporm.types.TypeWrapperBuilder;
import com.jporm.validator.ValidatorService;

/**
 *
 * @author Francesco Cina'
 *
 * 26/ago/2011
 */
public class JPOrm implements JPO {

	private static Integer JPORM_INSTANCES_COUNT = Integer.valueOf(0);
	private final ServiceCatalogImpl serviceCatalog;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Integer instanceCount;

	/**
	 * Create a new instance of JPOrm.
	 *
	 * @param sessionProvider
	 */
	public JPOrm(final SessionProvider sessionProvider) {
		synchronized (JPORM_INSTANCES_COUNT) {
			instanceCount = JPORM_INSTANCES_COUNT++;
		}
		logger.info("Building new instance of JPO (instance [{}])", instanceCount); //$NON-NLS-1$
		serviceCatalog = new ServiceCatalogImpl(this);
		serviceCatalog.setSessionProvider(sessionProvider);
		serviceCatalog.setSession(new SessionImpl(serviceCatalog, sessionProvider));
		serviceCatalog.setDbProfile(sessionProvider.getDBType().getDBProfile());
	}

	@Override
	public final Session session() {
		return serviceCatalog.getSession();
	}

	@Override
	public synchronized <BEAN> void register(final Class<BEAN> clazz) throws OrmConfigurationException {
		try {
			if (!getServiceCatalog().getClassToolMap().containsTool(clazz)) {
				logger.debug("register new class: " + clazz.getName()); //$NON-NLS-1$
				final ClassDescriptor<BEAN> classDescriptor = new ClassDescriptorBuilderImpl<BEAN>(clazz, getServiceCatalog().getTypeFactory()).build();
				final Persistor<BEAN> ormPersistor =  new PersistorGeneratorImpl<BEAN>(classDescriptor, getTypeFactory()).generate();
				ClassTool<BEAN> classTool = new ClassToolImpl<BEAN>(classDescriptor, ormPersistor);
				serviceCatalog.getClassToolMap().put(clazz, classTool);
			}
		} catch (final Exception e) {
			throw new OrmConfigurationException(e);
		}
	}

	@Override
	public synchronized void destory() {
		serviceCatalog.destroy();
	}

	@Override
	public synchronized void register(final List<Class<?>> classes) throws OrmConfigurationException {
		for (final Class<?> clazz : classes) {
			this.register(clazz);
		}
	}

	@Override
	public synchronized void register(final TypeWrapper<?, ?> typeWrapper) throws OrmConfigurationException {
		getTypeFactory().addTypeWrapper(typeWrapper);
	}

	@Override
	public void register(final TypeWrapperBuilder<?, ?> typeWrapperBuilder) throws OrmConfigurationException {
		getTypeFactory().addTypeWrapper(typeWrapperBuilder);
	}

	@Override
	public synchronized void setValidatorService(final ValidatorService validatorService) {
		if (validatorService!=null) {
			serviceCatalog.setValidatorService(validatorService);
		}
	}

	public TypeFactory getTypeFactory() {
		return getServiceCatalog().getTypeFactory();
	}

	public ServiceCatalog getServiceCatalog() {
		return serviceCatalog;
	}

	@Override
	public void setCacheManager(final CacheManager cacheManager) {
		if (cacheManager!=null) {
			serviceCatalog.setCacheManager(cacheManager);
		}
	}

	@Override
	public void setAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor) {
		if (asyncTaskExecutor!=null) {
			serviceCatalog.setAsyncTaskExecutor(asyncTaskExecutor);
		}
	}

}
