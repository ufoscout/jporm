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
package com.jporm.core.inject;

import com.jporm.cache.CacheManager;
import com.jporm.core.cache.SimpleCacheManager;
import com.jporm.core.dialect.DBProfile;
import com.jporm.core.dialect.UnknownDBProfile;
import com.jporm.core.mapper.ClassToolMap;
import com.jporm.core.mapper.NullClassToolMap;
import com.jporm.core.persistor.type.TypeFactory;
import com.jporm.core.query.crud.cache.CRUDQueryCache;
import com.jporm.core.query.crud.cache.CRUDQueryCacheImpl;
import com.jporm.core.query.crud.executor.OrmCRUDQueryExecutor;
import com.jporm.core.query.crud.executor.OrmCRUDQueryExecutorImpl;
import com.jporm.core.query.find.cache.CacheStrategy;
import com.jporm.core.query.find.cache.CacheStrategyImpl;
import com.jporm.core.query.namesolver.PropertiesFactory;
import com.jporm.core.session.NullSessionProvider;
import com.jporm.core.session.SessionImpl;
import com.jporm.core.session.SessionProvider;
import com.jporm.core.validator.NullValidatorService;
import com.jporm.validator.ValidatorService;


/**
 *
 * @author Francesco Cina
 *
 * 22/mag/2011
 */
public class NullServiceCatalog implements ServiceCatalog {

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

	@Override
	public ClassToolMap getClassToolMap() {
		return new NullClassToolMap();
	}

	@Override
	public void destroy() {
	}

	@Override
	public SessionProvider getSessionProvider() {
		return new NullSessionProvider();
	}

}
