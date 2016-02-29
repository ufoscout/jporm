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
import com.jporm.commons.core.inject.config.ConfigService;
import com.jporm.commons.core.query.processor.PropertiesFactory;
import com.jporm.types.TypeConverterFactory;
import com.jporm.validator.ValidatorService;

/**
 *
 * @author Francesco Cina
 *
 *         22/mag/2011
 *
 */
public interface ServiceCatalog {

    AsyncTaskExecutor getAsyncTaskExecutor();

    ClassToolMap getClassToolMap();

    ConfigService getConfigService();

    PropertiesFactory getPropertiesFactory();

    TypeConverterFactory getTypeFactory();

    ValidatorService getValidatorService();

}
