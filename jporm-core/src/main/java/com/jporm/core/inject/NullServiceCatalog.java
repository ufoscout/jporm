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
import com.jporm.cache.simple.SimpleCacheManager;
import com.jporm.core.JPOrm;
import com.jporm.core.async.AsyncTaskExecutor;
import com.jporm.core.async.impl.BlockingAsyncTaskExecutor;
import com.jporm.core.query.cache.SqlCache;
import com.jporm.core.query.cache.impl.SqlCacheImpl;
import com.jporm.core.query.find.impl.cache.CacheStrategy;
import com.jporm.core.query.find.impl.cache.CacheStrategyImpl;
import com.jporm.core.session.SessionProvider;
import com.jporm.core.session.impl.NullSessionProvider;
import com.jporm.core.session.impl.SessionImpl;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.dialect.UnknownDBProfile;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;
import com.jporm.types.TypeFactory;
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
	public SessionImpl getSession() {
		return new SessionImpl(this, new NullSessionProvider());
	}

	@Override
	public SqlCache getSqlCache() {
		return new SqlCacheImpl();
	}

	@Override
	public ClassToolMap getClassToolMap() {
		return new ClassToolMapImpl(new JPOrm(new NullSessionProvider()));
	}

	@Override
	public void destroy() {
	}

	@Override
	public SessionProvider getSessionProvider() {
		return new NullSessionProvider();
	}

	@Override
	public AsyncTaskExecutor getAsyncTaskExecutor() {
		return new BlockingAsyncTaskExecutor();
	}

}
