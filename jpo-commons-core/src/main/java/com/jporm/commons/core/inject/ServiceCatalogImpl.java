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
package com.jporm.commons.core.inject;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.async.ThreadPoolAsyncTaskExecutor;
import com.jporm.commons.core.inject.config.ConfigServiceImpl;
import com.jporm.commons.core.query.processor.PropertiesFactory;
import com.jporm.persistor.PersistorFactory;
import com.jporm.types.TypeConverterFactory;
import com.jporm.validator.NullValidatorService;
import com.jporm.validator.ValidatorService;

/**
 *
 * @author Francesco Cina
 *
 *         22/mag/2011
 */
public class ServiceCatalogImpl implements ServiceCatalog {

	private final TypeConverterFactory typeFactory;
	private final ClassToolMap classToolMap;
	private final PropertiesFactory propertiesFactory;
	private final ConfigServiceImpl configService;

	private ValidatorService validatorService;
	private AsyncTaskExecutor asyncTaskExecutor;

	public ServiceCatalogImpl() {
		typeFactory = new TypeConverterFactory();
		configService = new ConfigServiceImpl();
		classToolMap = new ClassToolMapImpl(new PersistorFactory(typeFactory));
		validatorService = new NullValidatorService();
		propertiesFactory = new PropertiesFactory();
		asyncTaskExecutor = new ThreadPoolAsyncTaskExecutor(10);
	}

	@Override
	public AsyncTaskExecutor getAsyncTaskExecutor() {
		return asyncTaskExecutor;
	}

	@Override
	public ClassToolMap getClassToolMap() {
		return classToolMap;
	}

	/**
	 * @return the configService
	 */
	@Override
	public ConfigServiceImpl getConfigService() {
		return configService;
	}

	@Override
	public PropertiesFactory getPropertiesFactory() {
		return propertiesFactory;
	}

	@Override
	public TypeConverterFactory getTypeFactory() {
		return typeFactory;
	}

	/**
	 * @return the validatorService
	 */
	@Override
	public ValidatorService getValidatorService() {
		return validatorService;
	}

	public void setAsyncTaskExecutor(final AsyncTaskExecutor asyncTaskExecutor) {
		this.asyncTaskExecutor = asyncTaskExecutor;
	}

	/**
	 * @param validatorService
	 *            the validatorService to set
	 */
	public void setValidatorService(final ValidatorService validatorService) {
		this.validatorService = validatorService;
	}

}
