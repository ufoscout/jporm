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
package com.jporm.core.query;

import com.jporm.cache.Cache;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.query.RenderableSqlQuery;

/**
 * An {@link RenderableSqlQuery} that keep track of the status of the object.
 * After a call to one of the render methods the result is stored and used for future calls
 * if the status of the object doen't change
 *
 * @author ufo
 *
 */
public abstract class SmartRenderableSqlQuery implements RenderableSqlQuery {

	private final ServiceCatalog serviceCatalog;

	private int lastStatusVersion = -1;
	private String lastRender = "";
	private String cacheUniqueKey = "";

	public SmartRenderableSqlQuery(final ServiceCatalog serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
	}

	@Override
	public final String renderSql() {
		int currentVersion = getStatusVersion();
		if (currentVersion != lastStatusVersion) {
			lastStatusVersion = currentVersion;
			final StringBuilder queryBuilder = new StringBuilder();

			if (!cacheUniqueKey.isEmpty()) {
				String cacheUniqueKeyWithVersion = cacheUniqueKey + lastStatusVersion;
				Cache cache = serviceCatalog.getCrudQueryCache().find();
				String cachedRender = cache.get(cacheUniqueKeyWithVersion, String.class);
				if (cachedRender==null) {
					renderSql(queryBuilder);
					cachedRender = queryBuilder.toString();
					cache.put(cacheUniqueKeyWithVersion, cachedRender);
				}
				lastRender = cachedRender;
			} else {
				renderSql(queryBuilder);
				lastRender = queryBuilder.toString();
			}

		}
		return lastRender;
	}

	public abstract int getStatusVersion();

	public void cachedRender(final String uniqueKey) {
		cacheUniqueKey = uniqueKey;
	}

}
