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
/*
 * ---------------------------------------------------------------------------- PROJECT : JPOrm CREATED BY : Francesco
 * Cina' ON : Feb 23, 2013 ----------------------------------------------------------------------------
 */
package com.jporm.commons.core.query.update;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.cache.Cache;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.clause.Select;
import com.jporm.sql.query.clause.Set;
import com.jporm.sql.query.clause.Update;
import com.jporm.sql.query.clause.Where;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class AUpdateQuery<BEAN>  {

	private final Class<BEAN> clazz;
	private final ClassTool<BEAN> ormClassTool;
	private final SqlFactory sqlFactory;
	private final SqlCache sqlCache;

	/**
	 * @param newBean
	 * @param serviceCatalog
	 * @param ormSession
	 */
	public AUpdateQuery(Class<BEAN> clazz, ClassTool<BEAN> ormClassTool, final SqlCache sqlCache, SqlFactory sqlFactory) {
		this.ormClassTool = ormClassTool;
		this.sqlCache = sqlCache;
		this.clazz = clazz;
		this.sqlFactory = sqlFactory;
	}


	protected String getQuery(DBProfile dbProfile) {
		Cache<Class<?>, String> cache = sqlCache.update();

		return cache.get(clazz, key -> {

			ClassDescriptor<BEAN> descriptor = ormClassTool.getDescriptor();
			String[] pkAndVersionFieldNames = descriptor.getPrimaryKeyAndVersionColumnJavaNames();
			String[] notPksFieldNames = descriptor.getNotPrimaryKeyColumnJavaNames();

			Update update = sqlFactory.update(clazz);

			Where updateQueryWhere = update.where();
			for (int i = 0; i < pkAndVersionFieldNames.length; i++) {
				updateQueryWhere.eq(pkAndVersionFieldNames[i], "");
			}

			Set updateQuerySet = update.set();

			for (int i = 0; i < notPksFieldNames.length; i++) {
				updateQuerySet.eq(notPksFieldNames[i], "");
			}

			return update.renderSql(dbProfile);
		});

	}

	protected String getLockQuery(DBProfile dbProfile) {

		Cache<Class<?>, String> cache = sqlCache.updateLock();

		return cache.get(clazz, key -> {

			String[] pkAndVersionFieldNames = ormClassTool.getDescriptor().getPrimaryKeyAndVersionColumnJavaNames();

			Select query = sqlFactory.select(clazz);
			query.lockMode(getOrmClassTool().getPersistor().getVersionableLockMode());
			Where where = query.where();
			for (int i = 0; i < pkAndVersionFieldNames.length; i++) {
				where.eq(pkAndVersionFieldNames[i], "");
			}
			return query.renderRowCountSql(dbProfile);
		});

	}


	/**
	 * @return the ormClassTool
	 */
	public ClassTool<BEAN> getOrmClassTool() {
		return ormClassTool;
	}

}
