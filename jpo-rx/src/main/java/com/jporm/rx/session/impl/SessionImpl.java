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
package com.jporm.rx.session.impl;

import java.util.concurrent.CompletableFuture;

import com.jporm.annotation.introspector.cache.CacheInfo;
import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.inject.ClassToolMap;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.persistor.Persistor;
import com.jporm.rx.connection.DeleteResult;
import com.jporm.rx.query.delete.CustomDeleteQuery;
import com.jporm.rx.query.delete.impl.CustomDeleteQueryImpl;
import com.jporm.rx.query.delete.impl.DeleteQueryImpl;
import com.jporm.rx.query.find.CustomFindQueryBuilder;
import com.jporm.rx.query.find.FindQuery;
import com.jporm.rx.query.find.FindQueryCommon;
import com.jporm.rx.query.find.FindQueryWhere;
import com.jporm.rx.query.find.impl.CustomFindQueryBuilderImpl;
import com.jporm.rx.query.find.impl.FindQueryImpl;
import com.jporm.rx.query.save.CustomSaveQuery;
import com.jporm.rx.query.save.impl.CustomSaveQueryImpl;
import com.jporm.rx.query.save.impl.SaveQueryImpl;
import com.jporm.rx.query.update.CustomUpdateQuery;
import com.jporm.rx.query.update.impl.CustomUpdateQueryImpl;
import com.jporm.rx.query.update.impl.UpdateQueryImpl;
import com.jporm.rx.session.ConnectionProvider;
import com.jporm.rx.session.Session;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.SqlFactory;

public class SessionImpl implements Session {

	private final ServiceCatalog serviceCatalog;
	private final ConnectionProvider connectionProvider;
	private final ClassToolMap classToolMap;
	private final SqlFactory sqlFactory;
	private final boolean autoCommit;

	public SessionImpl(ServiceCatalog serviceCatalog, ConnectionProvider connectionProvider, boolean autoCommit) {
		this.serviceCatalog = serviceCatalog;
		this.connectionProvider = connectionProvider;
		this.autoCommit = autoCommit;
		classToolMap = serviceCatalog.getClassToolMap();
		sqlFactory = new SqlFactory(classToolMap, serviceCatalog.getPropertiesFactory());
	}

	@Override
	public final <BEAN> FindQueryCommon<BEAN> findById(final Class<BEAN> clazz, final Object value) throws JpoException {
		ClassTool<BEAN> ormClassTool = classToolMap.get(clazz);
		ClassDescriptor<BEAN> descriptor = ormClassTool.getDescriptor();
		String[] pks = descriptor.getPrimaryKeyColumnJavaNames();
		return this.find(clazz, descriptor, pks, new Object[] { value });
	}

	private final <BEAN> FindQueryCommon<BEAN> find(final Class<BEAN> clazz, ClassDescriptor<BEAN> descriptor, String[] pks, final Object[] values)
			throws JpoException {
		CacheInfo cacheInfo = descriptor.getCacheInfo();
		FindQueryWhere<BEAN> query = find(clazz).cache(cacheInfo.getCacheName()).where();
		for (int i = 0; i < pks.length; i++) {
			query.eq(pks[i], values[i]);
		}
		return query.limit(1);
	}

	@Override
	public final <BEAN> FindQuery<BEAN> find(final Class<BEAN> clazz) throws JpoException {
		return find(clazz, clazz.getSimpleName());
	}

	@Override
	public final <BEAN> FindQuery<BEAN> find(final Class<BEAN> clazz, final String alias) throws JpoException {
		final FindQueryImpl<BEAN> query = new FindQueryImpl<BEAN>(serviceCatalog, clazz, alias, sqlExecutor(), sqlFactory);
		return query;
	}

	@Override
	public <BEAN> CustomFindQueryBuilder find(String... selectFields) {
		return new CustomFindQueryBuilderImpl(selectFields, serviceCatalog, sqlExecutor(), sqlFactory);
	}

	@Override
	public SqlExecutor sqlExecutor() {
		return new SqlExecutorImpl(serviceCatalog.getTypeFactory(), connectionProvider, autoCommit);
	}

	@Override
	public <BEAN> CompletableFuture<BEAN> save(BEAN bean) {
		return new SaveQueryImpl<BEAN>(bean, (Class<BEAN>) bean.getClass(), serviceCatalog, sqlExecutor(), sqlFactory).execute();
	}

	@Override
	public <BEAN> CompletableFuture<DeleteResult> delete(BEAN bean) {
		return new DeleteQueryImpl<BEAN>(bean, (Class<BEAN>) bean.getClass(), serviceCatalog, sqlExecutor(), sqlFactory).execute();
	}

	@Override
	public <BEAN> CompletableFuture<BEAN> update(BEAN bean) {
		return new UpdateQueryImpl<BEAN>(bean, (Class<BEAN>) bean.getClass(), serviceCatalog, sqlExecutor(), sqlFactory).execute();
	}

	@Override
	public <BEAN> CustomDeleteQuery<BEAN> delete(Class<BEAN> clazz) throws JpoException {
		return new CustomDeleteQueryImpl<>(clazz, serviceCatalog, sqlExecutor(), sqlFactory);
	}

	@Override
	public <BEAN> CustomSaveQuery save(Class<BEAN> clazz, String... fields) throws JpoException {
		return new CustomSaveQueryImpl<>(clazz, fields, serviceCatalog, sqlExecutor(), sqlFactory);
	}

	@Override
	public <BEAN> CustomUpdateQuery update(Class<BEAN> clazz) throws JpoException {
		return new CustomUpdateQueryImpl(clazz, serviceCatalog, sqlExecutor(), sqlFactory);
	}

	@Override
	public <BEAN> CompletableFuture<BEAN> saveOrUpdate(BEAN bean) {
		Persistor<BEAN> persistor = (Persistor<BEAN>) serviceCatalog.getClassToolMap().get(bean.getClass()).getPersistor();
		return exist(bean, persistor).thenCompose(exists -> {
			if (exists) {
				return update(bean);
			}
			return save(bean);
		});
	}

	/**
	 * Returns whether a bean has to be saved. Otherwise it has to be updated
	 * because it already exists.
	 *
	 * @return
	 */
	private <BEAN> CompletableFuture<Boolean> exist(BEAN bean, Persistor<BEAN> persistor) {
		if (persistor.hasGenerator()) {
			return CompletableFuture.completedFuture(!persistor.useGenerators(bean));
		} else {
			return findByModel(bean).exist();
		}
	}

	@Override
	public final <BEAN> FindQueryCommon<BEAN> findByModel(final BEAN model) throws JpoException {
		Class<BEAN> modelClass = (Class<BEAN>) model.getClass();
		ClassTool<BEAN> ormClassTool = classToolMap.get(modelClass);
		ClassDescriptor<BEAN> descriptor = ormClassTool.getDescriptor();
		String[] pks = descriptor.getPrimaryKeyColumnJavaNames();
		Object[] values = ormClassTool.getPersistor().getPropertyValues(pks, model);
		return find(modelClass, descriptor, pks, values);
	}

}
