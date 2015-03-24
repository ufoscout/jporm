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
package com.jporm.commons.core.query.save;

import com.jporm.cache.Cache;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.query.clause.Insert;
import com.jporm.sql.query.clause.Values;


/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class ASaveQuery<BEAN> {

	private final Class<BEAN> clazz;
	private final ClassTool<BEAN> ormClassTool;
	private final SqlFactory sqlFactory;
	private SqlCache sqlCache;

	public ASaveQuery(ClassTool<BEAN> ormClassTool, Class<BEAN> clazz, final SqlCache sqlCache, SqlFactory sqlFactory) {
		this.ormClassTool = ormClassTool;
		this.sqlCache = sqlCache;
		this.sqlFactory = sqlFactory;
		this.clazz = clazz;
	}


	protected String getQuery(final boolean useGenerator) {

		Cache<Class<?>, String> cache = null;
		if (useGenerator) {
			cache = sqlCache.saveWithGenerators();
		} else {
			cache = sqlCache.saveWithoutGenerators();
		}

		return cache.get(clazz, key -> {
			Insert insert = sqlFactory.insert(clazz);
			insert.useGenerators(useGenerator);
			Values queryValues = insert.values();
			String[] fields = getOrmClassTool().getDescriptor().getAllColumnJavaNames();
			for (String field : fields) {
				queryValues.eq(field, "");
			}
			return insert.renderSql();
		});

	}


	/**
	 * @return the ormClassTool
	 */
	public ClassTool<BEAN> getOrmClassTool() {
		return ormClassTool;
	}

}
