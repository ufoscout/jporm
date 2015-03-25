/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rx.core.session.impl;

import java.util.concurrent.CompletableFuture;

import com.jporm.annotation.introspector.cache.CacheInfo;
import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.inject.ClassToolMap;
import com.jporm.commons.core.inject.ServiceCatalogImpl;
import com.jporm.rx.core.query.find.CustomFindQuery;
import com.jporm.rx.core.query.find.FindQuery;
import com.jporm.rx.core.query.find.FindQueryBase;
import com.jporm.rx.core.query.find.FindQueryWhere;
import com.jporm.rx.core.query.find.impl.CustomFindQueryImpl;
import com.jporm.rx.core.query.find.impl.FindQueryImpl;
import com.jporm.rx.core.query.save.impl.SaveQueryImpl;
import com.jporm.rx.core.session.Session;
import com.jporm.rx.core.session.SessionProvider;
import com.jporm.rx.core.session.SqlExecutor;
import com.jporm.sql.SqlFactory;


public class SessionImpl implements Session {

	private final ServiceCatalogImpl<Session> serviceCatalog;
	private final SessionProvider sessionProvider;
	private final ClassToolMap classToolMap;
	private final SqlFactory sqlFactory;
	private final boolean autoCommit;

	public SessionImpl(ServiceCatalogImpl<Session> serviceCatalog, SessionProvider sessionProvider, boolean autoCommit) {
		this.serviceCatalog = serviceCatalog;
		this.sessionProvider = sessionProvider;
		this.autoCommit = autoCommit;
		classToolMap = serviceCatalog.getClassToolMap();
		sqlFactory = new SqlFactory(sessionProvider.getDBType().getDBProfile(), classToolMap, serviceCatalog.getPropertiesFactory());
	}

	@Override
	public <BEAN> FindQueryBase<BEAN> find(BEAN bean) throws JpoException {
		ClassTool<BEAN> ormClassTool = (ClassTool<BEAN>) classToolMap.get(bean.getClass());
		String[] pks = ormClassTool.getDescriptor().getPrimaryKeyColumnJavaNames();
		Object[] values =  ormClassTool.getPersistor().getPropertyValues(pks, bean);
		return find((Class<BEAN>) bean.getClass(), values);
	}

	@Override
	public final <BEAN> FindQueryBase<BEAN> find(final Class<BEAN> clazz, final Object value) throws JpoException {
		return this.find(clazz, new Object[]{value});
	}

	private final <BEAN> FindQueryBase<BEAN> find(final Class<BEAN> clazz, final Object[] values) throws JpoException {
		ClassDescriptor<BEAN> descriptor = classToolMap.get(clazz).getDescriptor();
		CacheInfo cacheInfo = descriptor.getCacheInfo();
		FindQueryWhere<BEAN> query = findQuery(clazz).cache(cacheInfo.getCacheName()).where();
		String[] pks = descriptor.getPrimaryKeyColumnJavaNames();
		for (int i = 0; i < pks.length; i++) {
			query.eq(pks[i], values[i]);
		}
		return query.maxRows(1);
	}

	@Override
	public final <BEAN> FindQuery<BEAN> findQuery(final Class<BEAN> clazz) throws JpoException {
		return findQuery(clazz, clazz.getSimpleName());
	}

	@Override
	public final <BEAN> FindQuery<BEAN> findQuery(final Class<BEAN> clazz, final String alias) throws JpoException {
		final FindQueryImpl<BEAN> query = new FindQueryImpl<BEAN>(serviceCatalog, clazz, alias, sqlExecutor(), sqlFactory);
		return query;
	}

	@Override
	public final CustomFindQuery findQuery(final String selectClause, final Class<?> clazz, final String alias ) throws JpoException {
		final CustomFindQueryImpl query = new CustomFindQueryImpl(new String[]{selectClause}, serviceCatalog, clazz, alias, sqlExecutor(), sqlFactory);
		return query;
	}

	@Override
	public final CustomFindQuery findQuery(final String[] selectFields, final Class<?> clazz, final String alias ) throws JpoException {
		final CustomFindQueryImpl query = new CustomFindQueryImpl(selectFields, serviceCatalog, clazz, alias, sqlExecutor(), sqlFactory);
		return query;
	}

	@Override
	public SqlExecutor sqlExecutor() {
		return new SqlExecutorImpl( serviceCatalog.getTypeFactory(), () -> {
			return sessionProvider.getConnection(autoCommit);
		});
	}

	@Override
	public <BEAN> CompletableFuture<BEAN> save(BEAN bean) {
		return new SaveQueryImpl<BEAN>(bean, (Class<BEAN> ) bean.getClass(), serviceCatalog, sqlExecutor(), sqlFactory).now();
	}


}
