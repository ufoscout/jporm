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
package com.jporm.mapper;

import com.jporm.cache.CacheManager;
import com.jporm.cache.SimpleCacheManager;
import com.jporm.dialect.DBProfile;
import com.jporm.dialect.UnknownDBProfile;
import com.jporm.persistor.type.TypeFactory;
import com.jporm.query.crud.cache.CRUDQueryCache;
import com.jporm.query.crud.cache.CRUDQueryCacheImpl;
import com.jporm.query.crud.executor.OrmCRUDQueryExecutor;
import com.jporm.query.crud.executor.OrmCRUDQueryExecutorImpl;
import com.jporm.query.find.cache.CacheStrategy;
import com.jporm.query.find.cache.CacheStrategyImpl;
import com.jporm.query.namesolver.PropertiesFactory;
import com.jporm.session.NullSessionProvider;
import com.jporm.session.SessionImpl;
import com.jporm.validator.NullValidatorService;
import com.jporm.validator.ValidatorService;


/**
 * 
 * @author Francesco Cina
 *
 * 22/mag/2011
 */
public class NullServiceCatalog implements ServiceCatalog {

    @Override
    public boolean containsTool(final Class<?> clazz) {
        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public <T> OrmClassTool<T> getOrmClassTool(final Class<T> clazz) {
        return new NullOrmClassTool();
    }

    @Override
    public TypeFactory getTypeFactory() {
        return new TypeFactory();
    }

    @Override
    public DBProfile getDbProfile() {
        return new UnknownDBProfile();
    }

    @Override
    public ValidatorService getValidatorService() {
        return new NullValidatorService();
    }

    @Override
    public CacheManager getCacheManager() {
        return new SimpleCacheManager();
    }

    @Override
    public PropertiesFactory getPropertiesFactory() {
        return new PropertiesFactory();
    }

    @Override
    public CacheStrategy getCacheStrategy() {
        return new CacheStrategyImpl(this);
    }

    @Override
    public OrmCRUDQueryExecutor getOrmQueryExecutor() {
        return new OrmCRUDQueryExecutorImpl(this);
    }

    @Override
    public SessionImpl getSession() {
        return new SessionImpl(this, new NullSessionProvider());
    }

    @Override
    public CRUDQueryCache getCrudQueryCache() {
        return new CRUDQueryCacheImpl();
    }

}