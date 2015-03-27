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
package com.jporm.rx.core.query.delete.impl;

import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.delete.ADeleteQuery;
import com.jporm.rx.core.connection.DeleteResult;
import com.jporm.rx.core.connection.DeleteResultImpl;
import com.jporm.rx.core.query.delete.DeleteQuery;
import com.jporm.rx.core.session.SqlExecutor;
import com.jporm.sql.SqlFactory;

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
public class DeleteQueryImpl<BEAN> extends ADeleteQuery<BEAN> implements DeleteQuery {

	//private final BEAN bean;
	private BEAN bean;
	private SqlExecutor sqlExecutor;

	/**
	 * @param newBean
	 * @param serviceCatalog
	 * @param ormSession
	 */
	public DeleteQueryImpl(final BEAN bean, Class<BEAN> clazz, final ServiceCatalog serviceCatalog, SqlExecutor sqlExecutor, SqlFactory sqlFactory) {
		super(clazz, serviceCatalog.getClassToolMap().get(clazz), serviceCatalog.getSqlCache(), sqlFactory);
		this.bean = bean;
		this.sqlExecutor = sqlExecutor;
	}


	@Override
	public CompletableFuture<DeleteResult> now() {
		String[] pks = getOrmClassTool().getDescriptor().getPrimaryKeyColumnJavaNames();
		Object[] values = getOrmClassTool().getPersistor().getPropertyValues(pks, bean);

		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.update(getQuery(dbType.getDBProfile()), values);
		})
		.thenApply(updatedResult -> new DeleteResultImpl(updatedResult.updated()));

	}

}
