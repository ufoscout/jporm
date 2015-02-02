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
import com.jporm.core.JPO;
import com.jporm.core.async.AsyncTaskExecutor;
import com.jporm.core.async.impl.ThreadPoolAsyncTaskExecutor;
import com.jporm.core.query.cache.SqlCache;
import com.jporm.core.query.cache.impl.SqlCacheImpl;
import com.jporm.core.query.find.impl.cache.CacheStrategy;
import com.jporm.core.query.find.impl.cache.CacheStrategyImpl;
import com.jporm.core.query.strategy.QueryExecutionStrategy;
import com.jporm.core.query.strategy.QueryExecutionStrategySimpleUpdate;
import com.jporm.core.session.Session;
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
public class ServiceCatalogImpl implements ServiceCatalog {

	private TypeFactory typeFactory;
	private ClassToolMap classToolMap;
	private DBProfile dbProfile;
	private ValidatorService validatorService;
	private CacheManager cacheManager;
	private PropertiesFactory propertiesFactory;
	private CacheStrategy cacheStrategy;
	private Session session;
	private SessionProvider sessionProvider;
	private SqlCache crudQueryCache;
	private AsyncTaskExecutor asyncTaskExecutor;
	private QueryExecutionStrategy queryExecutionStrategy;

	public ServiceCatalogImpl(final JPO jpOrm) {
		init(jpOrm);
	}

	private void init(final JPO jpOrm) {
		typeFactory = new TypeFactory();
		classToolMap = new ClassToolMapImpl(jpOrm);
		dbProfile = new UnknownDBProfile();
		validatorService = new NullValidatorService();
		cacheManager = new SimpleCacheManager();
		propertiesFactory = new PropertiesFactory();
		cacheStrategy = new CacheStrategyImpl(this);
		session = new SessionImpl(this, new NullSessionProvider());
		crudQueryCache = new SqlCacheImpl();
		sessionProvider = new NullSessionProvider();
		asyncTaskExecutor = new ThreadPoolAsyncTaskExecutor(10);
		queryExecutionStrategy = new QueryExecutionStrategySimpleUpdate();
	}

	@Override
	public TypeFactory getTypeFactory() {
		return typeFactory;
	}

	/**
	 * @return the dbProfile
	 */
	@Override
	public DBProfile getDbProfile() {
		return dbProfile;
	}

	/**
	 * @param dbProfile the dbProfile to set
	 */
	public void setDbProfile(final DBProfile dbProfile) {
		this.dbProfile = dbProfile;
	}

	/**
	 * @return the validatorService
	 */
	@Override
	public ValidatorService getValidatorService() {
		return validatorService;
	}

	/**
	 * @param validatorService the validatorService to set
	 */
	public void setValidatorService(final ValidatorService validatorService) {
		this.validatorService = validatorService;
	}

	/**
	 * @return the cacheManager
	 */
	@Override
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * @param cacheManager the cacheManager to set
	 */
	public void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public PropertiesFactory getPropertiesFactory() {
		return propertiesFactory;
	}

	/**
	 * @return the cacheStrategy
	 */
	@Override
	public CacheStrategy getCacheStrategy() {
		return cacheStrategy;
	}

	/**
	 * @return the session
	 */
	@Override
	public Session getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(final SessionImpl session) {
		this.session = session;
	}

	@Override
	public SqlCache getSqlCache() {
		return crudQueryCache;
	}

	@Override
	public ClassToolMap getClassToolMap() {
		return classToolMap;
	}

	@Override
	public void destroy() {
		init(null);
	}

	@Override
	public SessionProvider getSessionProvider() {
		return sessionProvider;
	}

	public void setSessionProvider(final SessionProvider sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	@Override
	public AsyncTaskExecutor getAsyncTaskExecutor() {
		return asyncTaskExecutor;
	}

	public void setAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor) {
		this.asyncTaskExecutor = asyncTaskExecutor;
	}

	/**
	 * @return the queryExecutionStrategy
	 */
	@Override
	public QueryExecutionStrategy getQueryExecutionStrategy() {
		return queryExecutionStrategy;
	}

	/**
	 * @param queryExecutionStrategy the queryExecutionStrategy to set
	 */
	public void setQueryExecutionStrategy(QueryExecutionStrategy queryExecutionStrategy) {
		this.queryExecutionStrategy = queryExecutionStrategy;
	}

}
