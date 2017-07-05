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
package com.jporm.rm.kotlin.query.delete;

import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.query.strategy.DeleteExecutionStrategy;
import com.jporm.commons.core.query.strategy.QueryExecutionStrategy;
import com.jporm.persistor.generator.Persistor;
import com.jporm.rm.kotlin.session.SqlExecutor;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.Statement;

import java.util.List;
import java.util.stream.IntStream;

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
public class DeleteQueryImpl<BEAN> implements DeleteQuery, DeleteExecutionStrategy {

	// private final BEAN bean;
	private final List<BEAN> beans;
	private final SqlExecutor sqlExecutor;
	private final DBProfile dbType;
	private final Class<BEAN> clazz;
	private final SqlCache sqlCache;
	private final ClassTool<BEAN> ormClassTool;

	/**
	 * @param newBean
	 * @param serviceCatalog
	 * @param ormSession
	 */
	public DeleteQueryImpl(final List<BEAN> beans, final Class<BEAN> clazz, final ClassTool<BEAN> ormClassTool, final SqlCache sqlCache, final SqlExecutor sqlExecutor,
						   final DBProfile dbType) {
		this.beans = beans;
		this.clazz = clazz;
		this.ormClassTool = ormClassTool;
		this.sqlCache = sqlCache;
		this.sqlExecutor = sqlExecutor;
		this.dbType = dbType;
	}

	@Override
	public int execute() {
		return QueryExecutionStrategy.build(dbType).executeDelete(this);
	}

	@Override
	public int executeWithBatchUpdate() {
		final String query = sqlCache.delete(clazz);
		final String[] pks = ormClassTool.getDescriptor().getPrimaryKeyColumnJavaNames();

		// WITH BATCH UPDATE VERSION:
		final Persistor<BEAN> persistor = ormClassTool.getPersistor();
		final int[] result = sqlExecutor.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void set(Statement ps, int i) {
				persistor.setBeanValuesToStatement(pks, beans.get(i), ps, 0);
			}

			@Override
			public int getBatchSize() {
				return beans.size();
			}
		});
		return IntStream.of(result).sum();
	}

	@Override
	public int executeWithSimpleUpdate() {
		final String query = sqlCache.delete(clazz);
		final String[] pks = ormClassTool.getDescriptor().getPrimaryKeyColumnJavaNames();
		final Persistor<BEAN> persistor = ormClassTool.getPersistor();

		// WITHOUT BATCH UPDATE VERSION:
		int result = 0;
		for (final BEAN bean : beans) {
			result += sqlExecutor.update(query, statement -> persistor.setBeanValuesToStatement(pks, bean, statement, 0));
		}
		return result;
	}

}
