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

import com.jporm.cache.CacheManager;
import com.jporm.cache.simple.SimpleCacheManager;
import com.jporm.commons.core.JPOConfig;
import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.async.impl.ThreadPoolAsyncTaskExecutor;
import com.jporm.commons.core.inject.config.ConfigServiceImpl;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.query.cache.impl.SqlCacheImpl;
import com.jporm.commons.core.query.find.cache.CacheStrategy;
import com.jporm.commons.core.query.find.cache.CacheStrategyImpl;
import com.jporm.commons.core.query.strategy.QueryExecutionStrategy;
import com.jporm.commons.core.query.strategy.QueryExecutionStrategySimpleUpdate;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.dialect.UnknownDBProfile;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;
import com.jporm.types.TypeConverterFactory;
import com.jporm.validator.NullValidatorService;
import com.jporm.validator.ValidatorService;


/**
 *
 * @author Francesco Cina
 *
 * 22/mag/2011
 */
public class ServiceCatalogImpl<SESSION> implements ServiceCatalog<SESSION> {

	private final TypeConverterFactory typeFactory;
	private final ClassToolMap classToolMap;
	private final PropertiesFactory propertiesFactory;
	private final CacheStrategy cacheStrategy;
	private final SqlCache crudQueryCache;
	private final ConfigServiceImpl configService;

	private SESSION session;
	private DBProfile dbProfile;
	private ValidatorService validatorService;
	private CacheManager cacheManager;
	private AsyncTaskExecutor asyncTaskExecutor;
	private QueryExecutionStrategy queryExecutionStrategy;
	private SqlFactory sqlFactory;

	public ServiceCatalogImpl(final JPOConfig jpOrmConfig) {
		typeFactory = new TypeConverterFactory();
		configService = new ConfigServiceImpl();
		classToolMap = new ClassToolMapImpl(jpOrmConfig);
		dbProfile = new UnknownDBProfile();
		validatorService = new NullValidatorService();
		cacheManager = new SimpleCacheManager();
		propertiesFactory = new PropertiesFactory();
		cacheStrategy = new CacheStrategyImpl(this);
		crudQueryCache = new SqlCacheImpl();
		asyncTaskExecutor = new ThreadPoolAsyncTaskExecutor(10);
		queryExecutionStrategy = new QueryExecutionStrategySimpleUpdate();
	}

	@Override
	public TypeConverterFactory getTypeFactory() {
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
		queryExecutionStrategy = QueryExecutionStrategy.build(dbProfile.getDbFeatures().isReturnCountsOnBatchUpdate());
		sqlFactory = new SqlFactory(dbProfile, getClassToolMap(), getPropertiesFactory());

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
	public SESSION getSession() {
		return session;
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
	 * @return the configService
	 */
	@Override
	public ConfigServiceImpl getConfigService() {
		return configService;
	}

	@Override
	public SqlFactory getSqlFactory() {
		return sqlFactory;
	}

	public void setSession(SESSION session) {
		this.session = session;
	}

}
