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
import java.util.concurrent.CompletableFuture;

import com.jporm.core.inject.ClassTool;
import com.jporm.core.inject.ClassToolMap;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.delete.ADelete;
import com.jporm.core.query.delete.CustomDeleteQueryImpl;
import com.jporm.core.query.find.CustomFindQueryImpl;
import com.jporm.core.query.find.FindImpl;
import com.jporm.core.query.find.FindQueryImpl;
import com.jporm.core.query.save.ASave;
import com.jporm.core.query.save.ASaveOrUpdate;
import com.jporm.core.query.save.SaveQueryImpl;
import com.jporm.core.query.update.AUpdate;
import com.jporm.core.query.update.CustomUpdateQueryImpl;
import com.jporm.core.query.update.UpdateQueryImpl;
import com.jporm.core.session.script.ScriptExecutorImpl;
import com.jporm.core.transaction.TransactionImpl;
import com.jporm.core.transaction.TransactionVoidImpl;
import com.jporm.exception.OrmException;
import com.jporm.query.delete.DeleteQuery;
import com.jporm.query.delete.CustomDeleteQuery;
import com.jporm.query.delete.CustomDeleteQueryWhere;
import com.jporm.query.find.CustomFindQuery;
import com.jporm.query.find.Find;
import com.jporm.query.find.FindQuery;
import com.jporm.query.save.SaveQuery;
import com.jporm.query.save.SaveOrUpdateQuery;
import com.jporm.query.update.CustomUpdateQuery;
import com.jporm.query.update.UpdateQuery;
import com.jporm.session.ScriptExecutor;
import com.jporm.session.Session;
import com.jporm.session.SqlExecutor;
import com.jporm.transaction.Transaction;
import com.jporm.transaction.TransactionCallback;
import com.jporm.transaction.TransactionDefinition;
import com.jporm.transaction.TransactionDefinitionImpl;
import com.jporm.transaction.TransactionVoid;
import com.jporm.transaction.TransactionVoidCallback;

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
	public <BEAN> int delete(BEAN bean) throws OrmException {
		return deleteQuery(bean).now();
	}

	@Override
	public <BEAN> int delete(Collection<BEAN> beans) throws OrmException {
		return deleteQuery(beans).now();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <BEAN> DeleteQuery<BEAN> deleteQuery(final BEAN bean) {
		return new ADelete<BEAN>() {
			@Override
			public int doNow() {
				Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
				final ClassTool<BEAN> ormClassTool = classToolMap.get(clazz);
				CustomDeleteQueryWhere<BEAN> query = deleteQuery(clazz).where();
				String[] pks = ormClassTool.getDescriptor().getPrimaryKeyColumnJavaNames();
				Object[] pkValues = ormClassTool.getPersistor().getPropertyValues(pks, bean);
				for (int i = 0; i < pks.length; i++) {
					query.eq(pks[i], pkValues[i]);
				}
				return query.now();
			}
		};
	}

	@Override
	public final <BEAN> CustomDeleteQuery<BEAN> deleteQuery(final Class<BEAN> clazz) throws OrmException {
		final CustomDeleteQueryImpl<BEAN> delete = new CustomDeleteQueryImpl<BEAN>(clazz, serviceCatalog);
		return delete;
	}

	@Override
	public final <BEAN> DeleteQuery<List<BEAN>> deleteQuery(final Collection<BEAN> beans) throws OrmException {
		return new ADelete<List<BEAN>>(){
			@Override
			public int doNow() {
				int result = 0;
				for (final BEAN bean : beans) {
					result += deleteQuery(bean).now();
				}
				return result;
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <BEAN> Find<BEAN> find(final BEAN bean) throws OrmException {
		ClassTool<BEAN> ormClassTool = (ClassTool<BEAN>) classToolMap.get(bean.getClass());
		String[] pks = ormClassTool.getDescriptor().getPrimaryKeyColumnJavaNames();
		Object[] values =  ormClassTool.getPersistor().getPropertyValues(pks, bean);
		return find((Class<BEAN>) bean.getClass(), values);
	}

	@Override
	public final <BEAN> Find<BEAN> find(final Class<BEAN> clazz, final Object value) throws OrmException {
		return this.find(clazz, new Object[]{value});
	}

	@Override
	public final <BEAN> Find<BEAN> find(final Class<BEAN> clazz, final Object[] values) throws OrmException {
		return new FindImpl<>(clazz, values, classToolMap.get(clazz), this);
	}

	@Override
	public final <BEAN> FindQuery<BEAN> findQuery(final Class<BEAN> clazz) throws OrmException {
		return findQuery(clazz, clazz.getSimpleName());
	}

	@Override
	public final <BEAN> FindQuery<BEAN> findQuery(final Class<BEAN> clazz, final String alias) throws OrmException {
		final FindQueryImpl<BEAN> query = new FindQueryImpl<BEAN>(serviceCatalog, clazz, alias);
		return query;
	}

	@Override
	public final CustomFindQuery findQuery(final String selectClause, final Class<?> clazz, final String alias ) throws OrmException {
		final CustomFindQueryImpl query = new CustomFindQueryImpl(new String[]{selectClause}, serviceCatalog, clazz, alias);
		return query;
	}

	@Override
	public final CustomFindQuery findQuery(final String[] selectFields, final Class<?> clazz, final String alias ) throws OrmException {
		final CustomFindQueryImpl query = new CustomFindQueryImpl(selectFields, serviceCatalog, clazz, alias);
		return query;
	}

	/**
	 * @return the sessionProvider
	 */
	public SessionProvider getSessionProvider() {
		return sessionProvider;
	}

	@Override
	public <BEAN> BEAN save(BEAN bean) {
		return saveQuery(bean).now();
	}

	@Override
	public <BEAN> List<BEAN> save(Collection<BEAN> beans) throws OrmException {
		return saveQuery(beans).now();
	}

	@Override
	public <BEAN> BEAN saveOrUpdate(BEAN bean) throws OrmException {
		return saveOrUpdateQuery(bean).now();
	}

	@Override
	public <BEAN> List<BEAN> saveOrUpdate(Collection<BEAN> beans) throws OrmException {
		return saveOrUpdateQuery(beans).now();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <BEAN> SaveOrUpdateQuery<BEAN> saveOrUpdateQuery(final BEAN bean) throws OrmException {
		final Session session = this;
		return new ASaveOrUpdate<BEAN>() {
			@Override
			public BEAN doNow() {
				serviceCatalog.getValidatorService().validator(bean).validateThrowException();
				Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
				final ClassTool<BEAN> ormClassTool = classToolMap.get(clazz);

				if (ormClassTool.getPersistor().hasGenerator()) {
					if (ormClassTool.getPersistor().useGenerators(bean)) {
						return session.saveQuery(bean).now();
					} else {
						return session.updateQuery(bean).now();
					}
				} else {
					if (find(bean).exist()) {
						return session.updateQuery(bean).now();
					} else {
						return session.saveQuery(bean).now();
					}
				}
			}
		};


	}

	@Override
	public <BEAN> SaveOrUpdateQuery<List<BEAN>> saveOrUpdateQuery(final Collection<BEAN> beans) throws OrmException {
		return new ASaveOrUpdate<List<BEAN>>() {
			@Override
			public List<BEAN> doNow() {
				final List<BEAN> result = new ArrayList<BEAN>();
				for (final BEAN bean : beans) {
					result.add(saveOrUpdateQuery(bean).now());
				}
				return result;
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public <BEAN> SaveQuery<BEAN> saveQuery(final BEAN bean) {
		return new ASave<BEAN>() {
			@Override
			public BEAN doNow() {
				serviceCatalog.getValidatorService().validator(bean).validateThrowException();
				Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
				final ClassTool<BEAN> ormClassTool = classToolMap.get(clazz);
				BEAN newBean = ormClassTool.getPersistor().clone(bean);
				return new SaveQueryImpl<BEAN>(newBean, serviceCatalog).now();
			}
		};
	}

	@Override
	public <BEAN> SaveQuery<List<BEAN>> saveQuery(final Collection<BEAN> beans) throws OrmException {
		return new ASave<List<BEAN>>() {
			@Override
			public List<BEAN> doNow() {
				final List<BEAN> result = new ArrayList<BEAN>();
				for (final BEAN bean : beans) {
					result.add(saveQuery(bean).now());
				}
				return result;
			}
		};
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
	public <T> Transaction<T> tx(TransactionCallback<T> transactionCallback) {
		return new TransactionImpl<T>(transactionCallback, new TransactionDefinitionImpl(), this, sessionProvider, serviceCatalog);
	}

	@Override
	public <T> Transaction<T> tx(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback) {
		return new TransactionImpl<T>(transactionCallback, transactionDefinition, this, sessionProvider, serviceCatalog);
	}

	@Override
	public <T> CompletableFuture<T> txAsync(TransactionCallback<T> transactionCallback) {
		return tx(transactionCallback).async();
	}

	@Override
	public <T> CompletableFuture<T> txAsync(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback) {
		return tx(transactionDefinition, transactionCallback).async();
	}

	@Override
	public <T> T txNow(final TransactionCallback<T> transactionCallback)
			throws OrmException {
		return tx(transactionCallback).now();
	}

	@Override
	public <T> T txNow(final TransactionDefinition transactionDefinition, final TransactionCallback<T> transactionCallback) throws OrmException {
		return tx(transactionDefinition, transactionCallback).now();
	}

	@Override
	public TransactionVoid txVoid(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback) {
		return new TransactionVoidImpl(transactionCallback, transactionDefinition, this, sessionProvider, serviceCatalog);
	}

	@Override
	public TransactionVoid txVoid(TransactionVoidCallback transactionCallback) {
		return new TransactionVoidImpl(transactionCallback, new TransactionDefinitionImpl(), this, sessionProvider, serviceCatalog);
	}

	@Override
	public CompletableFuture<Void> txVoidAsync(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback) {
		return txVoid(transactionDefinition, transactionCallback).async();
	}

	@Override
	public CompletableFuture<Void> txVoidAsync(TransactionVoidCallback transactionCallback) {
		return txVoid(transactionCallback).async();
	}

	@Override
	public void txVoidNow(final TransactionDefinition transactionDefinition, final TransactionVoidCallback transactionCallback) {
		txVoid(transactionDefinition, transactionCallback).now();
	}

	@Override
	public void txVoidNow(final TransactionVoidCallback transactionCallback) {
		txVoid(transactionCallback).now();
	}

	@Override
	public <BEAN> BEAN update(BEAN bean) throws OrmException {
		return updateQuery(bean).now();
	}

	@Override
	public <BEAN> List<BEAN> update(Collection<BEAN> beans) throws OrmException {
		return updateQuery(beans).now();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <BEAN> UpdateQuery<BEAN> updateQuery(final BEAN bean) throws OrmException {
		return new AUpdate<BEAN>(){
			@Override
			public BEAN doNow() {
				serviceCatalog.getValidatorService().validator(bean).validateThrowException();
				Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
				final ClassTool<BEAN> ormClassTool = classToolMap.get(clazz);
				BEAN newBean = ormClassTool.getPersistor().clone(bean);
				return new UpdateQueryImpl<BEAN>(newBean, serviceCatalog).now();
			}
		};
	}

	@Override
	public final <BEAN> CustomUpdateQuery updateQuery(final Class<BEAN> clazz) throws OrmException {
		final CustomUpdateQueryImpl update = new CustomUpdateQueryImpl(clazz, serviceCatalog);
		return update;
	}

	@Override
	public <BEAN> UpdateQuery<List<BEAN>> updateQuery(final Collection<BEAN> beans) throws OrmException {
		return new AUpdate<List<BEAN>>(){
			@Override
			public List<BEAN> doNow() {
				final List<BEAN> result = new ArrayList<BEAN>();
				for (final BEAN bean : beans) {
					result.add(updateQuery(bean).now());
				}
				return result;
			}
		};
	}

}
