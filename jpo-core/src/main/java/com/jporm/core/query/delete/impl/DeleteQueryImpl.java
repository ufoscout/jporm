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
import com.jporm.core.session.Session;
import com.jporm.core.session.SqlExecutor;
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
public class DeleteQueryImpl<BEAN> extends ADeleteQuery<BEAN> implements DeleteQuery, DeleteExecutionStrategy {

	//private final BEAN bean;
	private final ServiceCatalog<Session> serviceCatalog;
	private boolean executed;
	private Stream<BEAN> beans;
	private SqlFactory sqlFactory;

	/**
	 * @param newBean
	 * @param serviceCatalog
	 * @param ormSession
	 */
	public DeleteQueryImpl(final Stream<BEAN> beans, Class<BEAN> clazz, final ServiceCatalog<Session> serviceCatalog, SqlFactory sqlFactory) {
		super(clazz, serviceCatalog.getClassToolMap().get(clazz), serviceCatalog.getSqlCache(), sqlFactory);
		this.beans = beans;
		this.serviceCatalog = serviceCatalog;
		this.sqlFactory = sqlFactory;
	}

	@Override
	public int now() {
		return QueryExecutionStrategy.build(sqlFactory.getDbProfile()).executeDelete(this);
	}

	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed ;
	}

	@Override
	public int executeWithBatchUpdate() {
		executed = true;
		String query = getQuery();
		String[] pks = getOrmClassTool().getDescriptor().getPrimaryKeyColumnJavaNames();
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();

		// WITH BATCH UPDATE VERSION:
		Stream<Object[]> valuesStream = beans.map(bean -> getOrmClassTool().getPersistor().getPropertyValues(pks, bean));
		int[] result = sqlExec.batchUpdate(query, valuesStream);
		return IntStream.of(result).sum();
	}

	@Override
	public int executeWithSimpleUpdate() {
		executed = true;
		String query = getQuery();
		String[] pks = getOrmClassTool().getDescriptor().getPrimaryKeyColumnJavaNames();
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();

		// WITHOUT BATCH UPDATE VERSION:
		return beans.mapToInt(bean -> {
			Object[] values = getOrmClassTool().getPersistor().getPropertyValues(pks, bean);
			return sqlExec.update(query , values);
		}).sum();

	}


}
