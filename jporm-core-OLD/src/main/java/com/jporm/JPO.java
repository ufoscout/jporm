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
package com.jporm;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.jporm.cache.CacheManager;
import com.jporm.cache.SimpleCacheManager;
import com.jporm.exception.OrmConfigurationException;
import com.jporm.exception.OrmException;
import com.jporm.persistor.type.TypeWrapper;
import com.jporm.persistor.type.TypeWrapperBuilder;
import com.jporm.session.Session;
import com.jporm.validator.NullValidatorService;
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
    Session session() throws OrmException;

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
    <T> void register(Class<T> clazz) throws OrmConfigurationException;

    /**
     * Register a list of classes to be managed as a beans.
     * 
     * @param <T>
     * @param clazz
     * @throws OrmConfigurationException
     */
    void register(List<Class<?>> classes) throws OrmConfigurationException;

    /**
     * Register a new {@link TypeWrapper}.
     * If a {@link TypeWrapper} wraps a Class that is already mapped, the last registered {@link TypeWrapper} will be used.
     * 
     * @param typeWrapper
     * @throws OrmConfigurationException
     */
    void register(TypeWrapper<?, ?> typeWrapper) throws OrmConfigurationException;

    /**
     * Register a new {@link TypeWrapperBuilder}.
     * If a {@link TypeWrapper} wraps a Class that is already mapped, the last registered {@link TypeWrapper} will be used.
     * 
     * @param typeWrapperBuilder
     * @throws OrmConfigurationException
     */
    void register(TypeWrapperBuilder<?, ?> typeWrapperBuilder) throws OrmConfigurationException;
    
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
}
