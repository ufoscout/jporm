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
package com.jporm.core.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jporm.annotation.cascade.CascadeInfo;
import com.jporm.annotation.cascade.CascadeType;
import com.jporm.core.annotation.cache.CacheInfo;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.mapper.ClassToolMap;
import com.jporm.core.mapper.OrmClassTool;
import com.jporm.core.query.delete.DeleteQueryOrm;
import com.jporm.core.query.find.AFind;
import com.jporm.core.query.find.CustomFindQueryOrm;
import com.jporm.core.query.find.FindQueryOrm;
import com.jporm.core.query.save.SaveQueryOrm;
import com.jporm.core.query.update.CustomUpdateQueryImpl;
import com.jporm.core.query.update.UpdateQueryOrm;
import com.jporm.core.session.script.ScriptExecutorImpl;
import com.jporm.exception.OrmException;
import com.jporm.query.delete.DeleteQuery;
import com.jporm.query.delete.DeleteWhere;
import com.jporm.query.find.CustomFindQuery;
import com.jporm.query.find.Find;
import com.jporm.query.find.FindQuery;
import com.jporm.query.find.FindWhere;
import com.jporm.query.save.SaveOrUpdateType;
import com.jporm.query.update.CustomUpdateQuery;
import com.jporm.session.ScriptExecutor;
import com.jporm.session.Session;
import com.jporm.session.SqlExecutor;
import com.jporm.session.TransactionCallback;
import com.jporm.session.TransactionCallbackVoid;
import com.jporm.transaction.OrmTransactionDefinition;
import com.jporm.transaction.TransactionDefinition;

/**
 *
 * @author Francesco Cina
 *
 * 27/giu/2011
 */
public class SessionImpl implements Session {

	private final ServiceCatalog serviceCatalog;
	private final SessionProvider sessionProvider;
	private final ClassToolMap classToolMap;

	public SessionImpl(final ServiceCatalog serviceCatalog, final SessionProvider sessionProvider) {
		this.serviceCatalog = serviceCatalog;
		this.sessionProvider = sessionProvider;
		classToolMap = serviceCatalog.getClassToolMap();
	}

	@Override
	public <BEAN> int delete(final BEAN bean) {
		Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
		final OrmClassTool<BEAN> ormClassTool = classToolMap.getOrmClassTool(clazz);
		DeleteWhere<BEAN> query = deleteQuery(clazz).where();
		String[] pks = ormClassTool.getClassMap().getPrimaryKeyColumnJavaNames();
		Object[] pkValues = ormClassTool.getOrmPersistor().getPropertyValues(pks, bean);
		for (int i = 0; i < pks.length; i++) {
			query.eq(pks[i], pkValues[i]);
		}
		return query.now();
	}

	@Override
	public final <BEAN> int delete(final List<BEAN> beans) throws OrmException {
		int result = 0;
		for (final BEAN bean : beans) {
			result += delete(bean);
		}
		return result;
	}

	@Override
	public final <BEAN> DeleteQuery<BEAN> deleteQuery(final Class<BEAN> clazz) throws OrmException {
		final DeleteQueryOrm<BEAN> delete = new DeleteQueryOrm<BEAN>(clazz, serviceCatalog);
		return delete;
	}

	@Override
	public <T> T doInTransaction(final TransactionCallback<T> transactionCallback)
			throws OrmException {
		return doInTransaction(new OrmTransactionDefinition(), transactionCallback);
	}

	@Override
	public <T> T doInTransaction(final TransactionDefinition transactionDefinition,
			final TransactionCallback<T> transactionCallback) throws OrmException {
		return sessionProvider.doInTransaction(this, transactionDefinition, transactionCallback);
	}

	@Override
	public void doInTransactionVoid(final TransactionDefinition transactionDefinition, final TransactionCallbackVoid transactionCallback) {
		doInTransaction(transactionDefinition, (s) -> {
			transactionCallback.doInTransaction(this);
			return null;
		});
	}

	@Override
	public void doInTransactionVoid(final TransactionCallbackVoid transactionCallback) {
		doInTransaction((s) -> {
			transactionCallback.doInTransaction(this);
			return null;
		});
	}

	@Override
	public final <BEAN> Find<BEAN> find(final BEAN bean) throws OrmException {
		OrmClassTool<BEAN> ormClassTool = (OrmClassTool<BEAN>) classToolMap.getOrmClassTool(bean.getClass());
		String[] pks = ormClassTool.getClassMap().getPrimaryKeyColumnJavaNames();
		Object[] values =  ormClassTool.getOrmPersistor().getPropertyValues(pks, bean);
		return find((Class<BEAN>) bean.getClass(), values);
	}

	@Override
	public final <BEAN> Find<BEAN> find(final Class<BEAN> clazz, final Object value) throws OrmException {
		return this.find(clazz, new Object[]{value});
	}

	@Override
	public final <BEAN> Find<BEAN> find(final Class<BEAN> clazz, final Object[] values) throws OrmException {
		return new AFind<BEAN>() {
			@Override
			public BEAN get() {
				OrmClassTool<BEAN> ormClassTool = classToolMap.getOrmClassTool(clazz);
				CacheInfo cacheInfo = ormClassTool.getClassMap().getCacheInfo();
				FindWhere<BEAN> query = findQuery(clazz, clazz.getSimpleName())
						.cache(cacheInfo.cacheToUse(getCache())).ignore(getIgnoredFields()).where();
				String[] pks = ormClassTool.getClassMap().getPrimaryKeyColumnJavaNames();
				for (int i = 0; i < pks.length; i++) {
					query.eq(pks[i], values[i]);
				}
				return query.maxRows(1).get();
			}

			@Override
			public BEAN getUnique() {
				OrmClassTool<BEAN> ormClassTool = classToolMap.getOrmClassTool(clazz);
				FindWhere<BEAN> query = findQuery(clazz, clazz.getSimpleName())
						.cache(getCache()).ignore(getIgnoredFields()).where();
				String[] pks = ormClassTool.getClassMap().getPrimaryKeyColumnJavaNames();
				for (int i = 0; i < pks.length; i++) {
					query.eq(pks[i], values[i]);
				}
				return query.maxRows(1).getUnique();
			}

			@Override
			public boolean exist() {
				OrmClassTool<BEAN> ormClassTool = classToolMap.getOrmClassTool(clazz);
				FindWhere<BEAN> query = findQuery(clazz).where();
				String[] pks = ormClassTool.getClassMap().getPrimaryKeyColumnJavaNames();
				for (int i = 0; i < pks.length; i++) {
					query.eq(pks[i], values[i]);
				}
				return query.maxRows(1).getRowCount()>0;
			}
		};
	}

	@Override
	public final <BEAN> FindQuery<BEAN> findQuery(final Class<BEAN> clazz) throws OrmException {
		return findQuery(clazz, clazz.getSimpleName());
	}

	@Override
	public final <BEAN> FindQuery<BEAN> findQuery(final Class<BEAN> clazz, final String alias) throws OrmException {
		final FindQueryOrm<BEAN> query = new FindQueryOrm<BEAN>(serviceCatalog, clazz, alias);
		return query;
	}

	@Override
	public final CustomFindQuery findQuery(final String selectClause, final Class<?> clazz, final String alias ) throws OrmException {
		final CustomFindQueryOrm query = new CustomFindQueryOrm(new String[]{selectClause}, serviceCatalog, clazz, alias);
		return query;
	}

	@Override
	public final CustomFindQuery findQuery(final String[] selectFields, final Class<?> clazz, final String alias ) throws OrmException {
		final CustomFindQueryOrm query = new CustomFindQueryOrm(selectFields, serviceCatalog, clazz, alias);
		return query;
	}

	@Override
	public <BEAN> BEAN save(final BEAN bean) {
		if (bean != null) {
			serviceCatalog.getValidatorService().validator(bean)
			.validateThrowException();
			Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
			final OrmClassTool<BEAN> ormClassTool = classToolMap
					.getOrmClassTool(clazz);
			BEAN newBean = ormClassTool.getOrmPersistor().clone(bean);
			return new SaveQueryOrm<BEAN>(newBean, serviceCatalog).now();
		}
		return null;
	}

	@Override
	public <BEAN> List<BEAN> save(final Collection<BEAN> beans)
			throws OrmException {
		final List<BEAN> result = new ArrayList<BEAN>();
		for (final BEAN bean : beans) {
			result.add(save(bean));
		}
		return result;
	}

	@Override
	public <BEAN> BEAN saveOrUpdate(final BEAN bean) throws OrmException {
		return saveOrUpdate(bean, CascadeType.ALWAYS.getInfo());
	}

	public <BEAN> BEAN saveOrUpdate(final BEAN bean,
			final CascadeInfo cascadeInfo) throws OrmException {
		serviceCatalog.getValidatorService().validator(bean)
		.validateThrowException();
		Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
		final OrmClassTool<BEAN> ormClassTool = classToolMap
				.getOrmClassTool(clazz);

		if (ormClassTool.getOrmPersistor().hasGenerator()) {
			if (ormClassTool.getOrmPersistor().useGenerators(bean)) {
				if (cascadeInfo.onSave()) {
					return new SaveQueryOrm<BEAN>(ormClassTool
							.getOrmPersistor().clone(bean), serviceCatalog)
							.saveOrUpdate(SaveOrUpdateType.SAVE_OR_UPDATE)
							.now();
				}
			} else {
				if (cascadeInfo.onUpdate()) {
					return new UpdateQueryOrm<BEAN>(ormClassTool
							.getOrmPersistor().clone(bean), serviceCatalog)
							.saveOrUpdate(SaveOrUpdateType.SAVE_OR_UPDATE)
							.now();
				}
			}
		} else {
			if (find(bean).exist()) {
				if (cascadeInfo.onUpdate()) {
					return new UpdateQueryOrm<BEAN>(ormClassTool
							.getOrmPersistor().clone(bean), serviceCatalog)
							.saveOrUpdate(SaveOrUpdateType.SAVE_OR_UPDATE)
							.now();
				}
			} else {
				if (cascadeInfo.onSave()) {
					return new SaveQueryOrm<BEAN>(ormClassTool
							.getOrmPersistor().clone(bean), serviceCatalog)
							.saveOrUpdate(SaveOrUpdateType.SAVE_OR_UPDATE)
							.now();
				}
			}
		}
		return bean;
	}

	@Override
	public <BEAN> List<BEAN> saveOrUpdate(final Collection<BEAN> beans)
			throws OrmException {
		return saveOrUpdate(beans, CascadeType.ALWAYS.getInfo());
	}

	public <BEAN> List<BEAN> saveOrUpdate(final Collection<BEAN> beans, final CascadeInfo cascadeInfo) throws OrmException {
		final List<BEAN> result = new ArrayList<BEAN>();
		for (final BEAN bean : beans) {
			result.add(saveOrUpdate(bean, cascadeInfo));
		}
		return result;
	}

	@Override
	public final ScriptExecutor scriptExecutor() throws OrmException {
		return new ScriptExecutorImpl(this);
	}

	@Override
	public SqlExecutor sqlExecutor() throws OrmException {
		return new SqlExecutorImpl(sessionProvider.sqlPerformerStrategy(), serviceCatalog);
	}

	@Override
	public <BEAN> BEAN update(final BEAN bean) throws OrmException {
		serviceCatalog.getValidatorService().validator(bean)
		.validateThrowException();
		Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
		final OrmClassTool<BEAN> ormClassTool = classToolMap.getOrmClassTool(clazz);
		BEAN newBean = ormClassTool.getOrmPersistor().clone(bean);
		return new UpdateQueryOrm<BEAN>(newBean, serviceCatalog).now();
	}

	@Override
	public <BEAN> List<BEAN> update(final Collection<BEAN> beans) throws OrmException {
		final List<BEAN> result = new ArrayList<BEAN>();
		for (final BEAN bean : beans) {
			result.add(update(bean));
		}
		return result;
	}

	@Override
	public final <BEAN> CustomUpdateQuery updateQuery(final Class<BEAN> clazz) throws OrmException {
		final CustomUpdateQueryImpl update = new CustomUpdateQueryImpl(clazz, serviceCatalog);
		return update;
	}

	/**
	 * @return the sessionProvider
	 */
	public SessionProvider getSessionProvider() {
		return sessionProvider;
	}

}
