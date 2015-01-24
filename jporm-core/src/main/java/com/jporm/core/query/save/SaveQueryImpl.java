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
package com.jporm.core.query.save;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.query.save.SaveQuery;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class SaveQueryImpl<BEAN> implements SaveQuery<BEAN> {

	private int _queryTimeout = 0;
	private final Class<BEAN> clazz;
	private final BEAN bean;
	private final ServiceCatalog serviceCatalog;
	private boolean executed = false;

	public SaveQueryImpl(final BEAN bean, final ServiceCatalog serviceCatalog) {
		this.bean = bean;
		this.serviceCatalog = serviceCatalog;
		this.clazz = (Class<BEAN>) bean.getClass();
	}

	@Override
	public BEAN now() {
		executed = true;
		return serviceCatalog.getOrmQueryExecutor().save(bean, clazz, _queryTimeout);
	}

	@Override
	public SaveQuery<BEAN> timeout(final int queryTimeout) {
		this._queryTimeout = queryTimeout;
		return this;
	}

	@Override
	public int getTimeout() {
		return this._queryTimeout;
	}

	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed ;
	}

}
