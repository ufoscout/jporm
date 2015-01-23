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

import com.jporm.async.AsyncTaskExecutor;
import com.jporm.cache.CacheManager;
import com.jporm.core.dialect.DBProfile;
import com.jporm.core.query.crud.cache.CRUDQueryCache;
import com.jporm.core.query.crud.executor.OrmCRUDQueryExecutor;
import com.jporm.core.query.find.cache.CacheStrategy;
import com.jporm.core.query.namesolver.PropertiesFactory;
import com.jporm.core.session.SessionProvider;
import com.jporm.session.Session;
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

	ValidatorService getValidatorService();

	CacheManager getCacheManager();

	CacheStrategy getCacheStrategy();

	PropertiesFactory getPropertiesFactory();

	OrmCRUDQueryExecutor getOrmQueryExecutor();

	Session getSession();

	CRUDQueryCache getCrudQueryCache();

	ClassToolMap getClassToolMap();

	void destroy();

	SessionProvider getSessionProvider();

	AsyncTaskExecutor getAsyncTaskExecutor();

}
