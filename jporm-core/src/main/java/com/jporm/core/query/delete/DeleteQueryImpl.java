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
package com.jporm.core.query.delete;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.jporm.core.inject.ClassTool;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.AQueryRoot;
import com.jporm.query.delete.CustomDeleteQuery;
import com.jporm.query.delete.CustomDeleteQueryWhere;

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
public class DeleteQueryImpl<BEAN> extends AQueryRoot implements DeleteQuery {

	//private final BEAN bean;
	private final Class<BEAN> clazz;
	private final ServiceCatalog serviceCatalog;
	private final ClassTool<BEAN> ormClassTool;
	private final Stream<CustomDeleteQuery<BEAN>> queries;
	private boolean executed;
	private Stream<BEAN> beans;

	/**
	 * @param newBean
	 * @param serviceCatalog
	 * @param ormSession
	 */
	public DeleteQueryImpl(final Stream<BEAN> beans, Class<BEAN> clazz, final ServiceCatalog serviceCatalog) {
		super(serviceCatalog);
		this.beans = beans;
		this.serviceCatalog = serviceCatalog;
		this.clazz = clazz;
		ormClassTool = serviceCatalog.getClassToolMap().get(clazz);
		queries = getQueries();
	}

	@Override
	public int now() {
		executed = true;
		return queries.mapToInt(query -> query.now()).sum();
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
	public void renderSql(final StringBuilder queryBuilder) {
		queries.forEach(deleteQuery -> {
			deleteQuery.renderSql(queryBuilder);
			queryBuilder.append("\n");
		});
	}

	@Override
	public void appendValues(List<Object> values) {
		queries.forEach(deleteQuery -> {
			List<Object> innerValues = new ArrayList<>();
			deleteQuery.appendValues(innerValues);
			values.add(innerValues);
		});
	}

	@Override
	public int getStatusVersion() {
		return 0;
	}

	private Stream<CustomDeleteQuery<BEAN>> getQueries() {

		return beans.map(bean -> {
			CustomDeleteQuery<BEAN> query = new CustomDeleteQueryImpl<BEAN>(clazz, serviceCatalog);
			CustomDeleteQueryWhere<BEAN> queryWhere = query.where();
			String[] pks = ormClassTool.getDescriptor().getPrimaryKeyColumnJavaNames();
			Object[] pkValues = ormClassTool.getPersistor().getPropertyValues(pks, bean);
			for (int i = 0; i < pks.length; i++) {
				queryWhere.eq(pks[i], pkValues[i]);
			};
			return query;
		});



	}

}
