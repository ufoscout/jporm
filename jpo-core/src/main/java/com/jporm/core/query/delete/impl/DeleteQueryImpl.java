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
package com.jporm.core.query.delete.impl;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.delete.ADeleteQuery;
import com.jporm.commons.core.query.strategy.DeleteExecutionStrategy;
import com.jporm.commons.core.query.strategy.QueryExecutionStrategy;
import com.jporm.core.query.delete.DeleteQuery;
import com.jporm.core.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dialect.DBType;

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
public class DeleteQueryImpl<BEAN> extends ADeleteQuery<BEAN> implements DeleteQuery, DeleteExecutionStrategy {

	//private final BEAN bean;
	private boolean executed;
	private final Stream<BEAN> beans;
	private final SqlExecutor sqlExecutor;
	private final DBType dbType;

	/**
	 * @param newBean
	 * @param serviceCatalog
	 * @param ormSession
	 */
	public DeleteQueryImpl(final Stream<BEAN> beans, Class<BEAN> clazz, final ServiceCatalog serviceCatalog, SqlExecutor sqlExecutor, SqlFactory sqlFactory, DBType dbType) {
		super(clazz, serviceCatalog.getClassToolMap().get(clazz), serviceCatalog.getSqlCache(), sqlFactory);
		this.beans = beans;
		this.sqlExecutor = sqlExecutor;
		this.dbType = dbType;
	}

	@Override
	public int execute() {
		return QueryExecutionStrategy.build(dbType.getDBProfile()).executeDelete(this);
	}

	@Override
	public int executeWithBatchUpdate() {
		executed = true;
		String query = getQuery(dbType.getDBProfile());
		String[] pks = getOrmClassTool().getDescriptor().getPrimaryKeyColumnJavaNames();

		// WITH BATCH UPDATE VERSION:
		Stream<Object[]> valuesStream = beans.map(bean -> getOrmClassTool().getPersistor().getPropertyValues(pks, bean));
		int[] result = sqlExecutor.batchUpdate(query, valuesStream);
		return IntStream.of(result).sum();
	}

	@Override
	public int executeWithSimpleUpdate() {
		executed = true;
		String query = getQuery(dbType.getDBProfile());
		String[] pks = getOrmClassTool().getDescriptor().getPrimaryKeyColumnJavaNames();

		// WITHOUT BATCH UPDATE VERSION:
		return beans.mapToInt(bean -> {
			Object[] values = getOrmClassTool().getPersistor().getPropertyValues(pks, bean);
			return sqlExecutor.update(query , values);
		}).sum();

	}


}
