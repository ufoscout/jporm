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

import com.jporm.core.inject.ClassTool;
import com.jporm.core.inject.ClassToolMap;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.delete.ADelete;
import com.jporm.core.query.delete.DeleteQueryOrm;
import com.jporm.core.query.find.CustomFindQueryOrm;
import com.jporm.core.query.find.FindImpl;
import com.jporm.core.query.find.FindQueryOrm;
import com.jporm.core.query.save.ASave;
import com.jporm.core.query.save.ASaveOrUpdate;
import com.jporm.core.query.save.SaveQueryOrm;
import com.jporm.core.query.update.AUpdate;
import com.jporm.core.query.update.CustomUpdateQueryImpl;
import com.jporm.core.query.update.UpdateQueryOrm;
import com.jporm.core.session.script.ScriptExecutorImpl;
import com.jporm.core.transaction.TransactionImpl;
import com.jporm.core.transaction.TransactionVoidImpl;
import com.jporm.core.transaction.TransactionalSessionImpl;
import com.jporm.exception.OrmException;
import com.jporm.query.delete.Delete;
import com.jporm.query.delete.DeleteQuery;
import com.jporm.query.delete.DeleteWhere;
import com.jporm.query.find.CustomFindQuery;
import com.jporm.query.find.Find;
import com.jporm.query.find.FindQuery;
import com.jporm.query.save.Save;
import com.jporm.query.save.SaveOrUpdate;
import com.jporm.query.update.CustomUpdateQuery;
import com.jporm.query.update.Update;
import com.jporm.session.ScriptExecutor;
import com.jporm.session.Session;
import com.jporm.session.SqlExecutor;
import com.jporm.transaction.TransactionDefinitionImpl;
import com.jporm.transaction.Transaction;
import com.jporm.transaction.TransactionCallback;
import com.jporm.transaction.TransactionDefinition;
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

	@SuppressWarnings("unchecked")
	@Override
	public <BEAN> Delete<BEAN> delete(final BEAN bean) {
		return new ADelete<BEAN>() {
			@Override
			public int doNow() {
				Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
				final ClassTool<BEAN> ormClassTool = classToolMap.get(clazz);
				DeleteWhere<BEAN> query = deleteQuery(clazz).where();
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
	public final <BEAN> Delete<List<BEAN>> delete(final List<BEAN> beans) throws OrmException {
		return new ADelete<List<BEAN>>(){
			@Override
			public int doNow() {
				int result = 0;
				for (final BEAN bean : beans) {
					result += delete(bean).now();
				}
				return result;
			}
		};
	}

	@Override
	public final <BEAN> DeleteQuery<BEAN> deleteQuery(final Class<BEAN> clazz) throws OrmException {
		final DeleteQueryOrm<BEAN> delete = new DeleteQueryOrm<BEAN>(clazz, serviceCatalog);
		return delete;
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
	public void txVoidNow(final TransactionDefinition transactionDefinition, final TransactionVoidCallback transactionCallback) {
		txVoid(transactionDefinition, transactionCallback).now();
	}

	@Override
	public void txVoidNow(final TransactionVoidCallback transactionCallback) {
		txVoid(transactionCallback).now();
	}

	@Override
	public <T> Transaction<T> tx(TransactionCallback<T> transactionCallback) {
		return new TransactionImpl<T>(transactionCallback, new TransactionDefinitionImpl(), new TransactionalSessionImpl(this), sessionProvider);
	}

	@Override
	public TransactionVoid txVoid(TransactionVoidCallback transactionCallback) {
		return new TransactionVoidImpl(transactionCallback, new TransactionDefinitionImpl(), new TransactionalSessionImpl(this), sessionProvider);
	}

	@Override
	public <T> Transaction<T> tx(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback) {
		return new TransactionImpl<T>(transactionCallback, transactionDefinition, new TransactionalSessionImpl(this), sessionProvider);
	}

	@Override
	public TransactionVoid txVoid(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback) {
		return new TransactionVoidImpl(transactionCallback, transactionDefinition, new TransactionalSessionImpl(this), sessionProvider);
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

	@SuppressWarnings("unchecked")
	@Override
	public <BEAN> Save<BEAN> save(final BEAN bean) {
		return new ASave<BEAN>() {
			@Override
			public BEAN doNow() {
				serviceCatalog.getValidatorService().validator(bean).validateThrowException();
				Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
				final ClassTool<BEAN> ormClassTool = classToolMap.get(clazz);
				BEAN newBean = ormClassTool.getPersistor().clone(bean);
				return new SaveQueryOrm<BEAN>(newBean, serviceCatalog).now();
			}
		};
	}

	@Override
	public <BEAN> Save<List<BEAN>> save(final Collection<BEAN> beans) throws OrmException {
		return new ASave<List<BEAN>>() {
			@Override
			public List<BEAN> doNow() {
				final List<BEAN> result = new ArrayList<BEAN>();
				for (final BEAN bean : beans) {
					result.add(save(bean).now());
				}
				return result;
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public <BEAN> SaveOrUpdate<BEAN> saveOrUpdate(final BEAN bean) throws OrmException {
		final Session session = this;
		return new ASaveOrUpdate<BEAN>() {
			@Override
			public BEAN doNow() {
				serviceCatalog.getValidatorService().validator(bean).validateThrowException();
				Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
				final ClassTool<BEAN> ormClassTool = classToolMap.get(clazz);

				if (ormClassTool.getPersistor().hasGenerator()) {
					if (ormClassTool.getPersistor().useGenerators(bean)) {
						return session.save(bean).now();
					} else {
						return session.update(bean).now();
					}
				} else {
					if (find(bean).exist()) {
						return session.update(bean).now();
					} else {
						return session.save(bean).now();
					}
				}
			}
		};


	}

	@Override
	public <BEAN> SaveOrUpdate<List<BEAN>> saveOrUpdate(final Collection<BEAN> beans) throws OrmException {
		return new ASaveOrUpdate<List<BEAN>>() {
			@Override
			public List<BEAN> doNow() {
				final List<BEAN> result = new ArrayList<BEAN>();
				for (final BEAN bean : beans) {
					result.add(saveOrUpdate(bean).now());
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

	@SuppressWarnings("unchecked")
	@Override
	public <BEAN> Update<BEAN> update(final BEAN bean) throws OrmException {
		return new AUpdate<BEAN>(){
			@Override
			public BEAN doNow() {
				serviceCatalog.getValidatorService().validator(bean).validateThrowException();
				Class<BEAN> clazz = (Class<BEAN>) bean.getClass();
				final ClassTool<BEAN> ormClassTool = classToolMap.get(clazz);
				BEAN newBean = ormClassTool.getPersistor().clone(bean);
				return new UpdateQueryOrm<BEAN>(newBean, serviceCatalog).now();
			}
		};
	}

	@Override
	public <BEAN> Update<List<BEAN>> update(final Collection<BEAN> beans) throws OrmException {
		return new AUpdate<List<BEAN>>(){
			@Override
			public List<BEAN> doNow() {
				final List<BEAN> result = new ArrayList<BEAN>();
				for (final BEAN bean : beans) {
					result.add(update(bean).now());
				}
				return result;
			}
		};
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
