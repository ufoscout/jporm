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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 25, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.core.query.find;

import java.util.Optional;

import com.jporm.core.inject.ClassTool;
import com.jporm.introspector.annotation.cache.CacheInfo;
import com.jporm.query.find.Find;
import com.jporm.query.find.FindQuery;
import com.jporm.query.find.FindQueryWhere;
import com.jporm.session.Session;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 25, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class FindImpl<BEAN> implements Find<BEAN> {

	private String cache = "";
	private String[] _ignoredFields = new String[0];
	private Class<BEAN> clazz;
	private Object[] values;
	private ClassTool<BEAN> ormClassTool;
	private Session session;

	public FindImpl(final Class<BEAN> clazz, final Object[] values, ClassTool<BEAN> ormClassTool, Session session) {
		this.clazz = clazz;
		this.values = values;
		this.ormClassTool = ormClassTool;
		this.session = session;
	}

	@Override
	public final Find<BEAN> cache(final String cacheName) {
		this.cache = cacheName;
		return this;
	}

	@Override
	public boolean exist() {
		FindQueryWhere<BEAN> query = session.findQuery(clazz).where();
		String[] pks = ormClassTool.getDescriptor().getPrimaryKeyColumnJavaNames();
		for (int i = 0; i < pks.length; i++) {
			query.eq(pks[i], values[i]);
		}
		return query.maxRows(1).getRowCount()>0;
	}

	@Override
	public BEAN get() {
		return getQuery().get();
	}

	@Override
	public Optional<BEAN> getOptional() {
		return getQuery().getOptional();
	}

	@Override
	public BEAN getUnique() {
		return getQuery().getUnique();
	}

	private FindQuery<BEAN> getQuery() {
		CacheInfo cacheInfo = ormClassTool.getDescriptor().getCacheInfo();
		FindQueryWhere<BEAN> query = session.findQuery(clazz, clazz.getSimpleName())
				.cache(cacheInfo.cacheToUse(cache)).ignore(_ignoredFields).where();
		String[] pks = ormClassTool.getDescriptor().getPrimaryKeyColumnJavaNames();
		for (int i = 0; i < pks.length; i++) {
			query.eq(pks[i], values[i]);
		}
		return query.maxRows(1);
	}

	@Override
	public final Find<BEAN> ignore(final boolean excludeFieldsCondition, final String... fields) {
		if(excludeFieldsCondition) {
			_ignoredFields = fields;
		}
		return this;
	}

	@Override
	public final Find<BEAN> ignore(final String... fields) {
		return ignore(true, fields);
	}

}
