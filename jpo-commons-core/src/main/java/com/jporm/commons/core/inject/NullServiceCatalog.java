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
import com.jporm.commons.core.async.BlockingAsyncTaskExecutor;
import com.jporm.commons.core.inject.config.ConfigService;
import com.jporm.commons.core.inject.config.ConfigServiceImpl;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.query.cache.SqlCacheImpl;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;
import com.jporm.types.TypeConverterFactory;
import com.jporm.validator.NullValidatorService;
import com.jporm.validator.ValidatorService;

/**
 *
 * @author Francesco Cina
 *
 *         22/mag/2011
 */
public class NullServiceCatalog implements ServiceCatalog {

    @Override
    public AsyncTaskExecutor getAsyncTaskExecutor() {
        return new BlockingAsyncTaskExecutor();
    }

    @Override
    public ClassToolMap getClassToolMap() {
        return new ClassToolMapImpl(getTypeFactory());
    }

    @Override
    public ConfigService getConfigService() {
        return new ConfigServiceImpl();
    }

    @Override
    public PropertiesFactory getPropertiesFactory() {
        return new PropertiesFactory();
    }

    @Override
    public SqlCache getSqlCache() {
        return new SqlCacheImpl();
    }

    @Override
    public TypeConverterFactory getTypeFactory() {
        return new TypeConverterFactory();
    }

    @Override
    public ValidatorService getValidatorService() {
        return new NullValidatorService();
    }

}
