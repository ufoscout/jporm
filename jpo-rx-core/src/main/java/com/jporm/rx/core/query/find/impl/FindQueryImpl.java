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
package com.jporm.rx.core.query.find.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.find.impl.CommonFindFromImpl;
import com.jporm.commons.core.query.find.impl.CommonFindQueryImpl;
import com.jporm.persistor.BeanFromResultSet;
import com.jporm.persistor.Persistor;
import com.jporm.rx.core.query.find.FindQuery;
import com.jporm.rx.core.query.find.FindQueryOrderBy;
import com.jporm.rx.core.query.find.FindQueryWhere;
import com.jporm.rx.core.session.SessionProvider;
import com.jporm.sql.query.clause.Select;

/**
 *
 * @author Francesco Cina
 *
 * 20/giu/2011
 */
public class FindQueryImpl<BEAN> extends CommonFindQueryImpl<FindQuery<BEAN>, FindQueryWhere<BEAN>, FindQueryOrderBy<BEAN>> implements FindQuery<BEAN> {

	private ServiceCatalog<?> serviceCatalog;
	private Class<BEAN> clazz;
	private SessionProvider sessionProvider;

	public FindQueryImpl(final ServiceCatalog<?> serviceCatalog, final Class<BEAN> clazz, final String alias, SessionProvider sessionProvider) {
		super(serviceCatalog, clazz, alias);
		this.serviceCatalog = serviceCatalog;
		this.clazz = clazz;
		this.sessionProvider = sessionProvider;
		Select select = getSelect();
		select.selectFields(getAllColumns());
		setFrom(new CommonFindFromImpl<>(select.from(), this));
		setWhere(new FindQueryWhereImpl<>(select.where(), this));
		setOrderBy(new FindQueryOrderByImpl<>(select.orderBy(), this));
	}

	@Override
	public CompletableFuture<BEAN> get() {
		return get(1).thenApply(beans -> {
			if (beans.isEmpty()) {
				return null;
			}
			return beans.get(0);
		});
	}

	private CompletableFuture<List<BEAN>> get(final int ignoreResultsMoreThan) throws JpoException {

		return sessionProvider.getConnection()
		.thenCompose(conn -> {
			final List<Object> params = new ArrayList<Object>();
			appendValues(params);
			return conn.query(renderSql(), params);
		})
		.thenApply(resultSet -> {
			int rowCount = 0;
			final Persistor<BEAN> ormClassTool = serviceCatalog.getClassToolMap().get(clazz).getPersistor();
			List<BEAN> beans = new ArrayList<BEAN>();
			while ( resultSet.next() && (rowCount<ignoreResultsMoreThan)) {
				BeanFromResultSet<BEAN> beanFromRS = ormClassTool.beanFromResultSet(resultSet, getIgnoredFields());
				beans.add( beanFromRS.getBean() );
				rowCount++;
			}
			return beans;
		});

	}

}
