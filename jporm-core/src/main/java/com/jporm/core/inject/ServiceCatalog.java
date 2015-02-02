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
import com.jporm.core.async.AsyncTaskExecutor;
import com.jporm.core.query.cache.SqlCache;
import com.jporm.core.query.find.impl.cache.CacheStrategy;
import com.jporm.core.query.strategy.QueryExecutionStrategy;
import com.jporm.core.session.Session;
import com.jporm.core.session.SessionProvider;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;
import com.jporm.types.TypeFactory;
import com.jporm.validator.ValidatorService;

/**
 *
 * @author Francesco Cina
 *
 * 22/mag/2011
 *
 */
public interface ServiceCatalog {

	TypeFactory getTypeFactory();

	DBProfile getDbProfile();

	QueryExecutionStrategy getQueryExecutionStrategy();

	ValidatorService getValidatorService();

	CacheManager getCacheManager();

	CacheStrategy getCacheStrategy();

	PropertiesFactory getPropertiesFactory();

	Session getSession();

	SqlCache getSqlCache();

	ClassToolMap getClassToolMap();

	void destroy();

	SessionProvider getSessionProvider();

	AsyncTaskExecutor getAsyncTaskExecutor();

}
