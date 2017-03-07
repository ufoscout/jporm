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
package com.jporm.rx.query.delete;

import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.rx.session.SqlExecutor;

import io.reactivex.Single;

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
public class DeleteQueryImpl<BEAN> implements DeleteQuery {

	// private final BEAN bean;
	private final BEAN bean;
	private final SqlExecutor sqlExecutor;
	private final Class<BEAN> clazz;
	private final SqlCache sqlCache;
	private final ClassTool<BEAN> ormClassTool;

	/**
	 * @param newBean
	 * @param serviceCatalog
	 * @param ormSession
	 */
	public DeleteQueryImpl(final BEAN bean, final Class<BEAN> clazz, final ClassTool<BEAN> ormClassTool, final SqlCache sqlCache, final SqlExecutor sqlExecutor) {
		this.bean = bean;
		this.clazz = clazz;
		this.ormClassTool = ormClassTool;
		this.sqlCache = sqlCache;
		this.sqlExecutor = sqlExecutor;
	}

	@Override
	public Single<DeleteResult> execute() {
		final String query = sqlCache.delete(clazz);
		final String[] pks = ormClassTool.getDescriptor().getPrimaryKeyColumnJavaNames();

		return sqlExecutor.update(query, statement -> ormClassTool.getPersistor().setBeanValuesToStatement(pks, bean, statement, 0)).
				map(updatedResult -> new DeleteResultImpl(updatedResult.updated()));

	}

}
