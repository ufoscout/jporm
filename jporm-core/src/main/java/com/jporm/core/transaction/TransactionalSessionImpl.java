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
package com.jporm.core.transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jporm.exception.OrmException;
import com.jporm.query.SaveUpdateDeleteQueryRoot;
import com.jporm.query.delete.Delete;
import com.jporm.query.delete.DeleteQuery;
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
import com.jporm.transaction.Transaction;
import com.jporm.transaction.TransactionCallback;
import com.jporm.transaction.TransactionDefinition;
import com.jporm.transaction.TransactionVoid;
import com.jporm.transaction.TransactionVoidCallback;
import com.jporm.transaction.TransactionalSession;

public class TransactionalSessionImpl implements TransactionalSession {

	private final Session session;
	private final List<SaveUpdateDeleteQueryRoot> saveUpdateDeleteQueries = new ArrayList<SaveUpdateDeleteQueryRoot>();

	public TransactionalSessionImpl(Session session) {
		this.session = session;
	}

	@Override
	public <BEAN> Delete<BEAN> delete(BEAN bean) throws OrmException {
		return add(session.delete(bean));
	}

	@Override
	public <BEAN> Delete<List<BEAN>> delete(List<BEAN> beans) throws OrmException {
		return add(session.delete(beans));
	}

	@Override
	public <BEAN> DeleteQuery<BEAN> deleteQuery(Class<BEAN> clazz) throws OrmException {
		return add(session.deleteQuery(clazz));
	}

	@Override
	public <T> Transaction<T> tx(TransactionCallback<T> transactionCallback) {
		return session.tx(transactionCallback);
	}

	@Override
	public TransactionVoid txVoid(TransactionVoidCallback transactionCallback) {
		return session.txVoid(transactionCallback);
	}

	@Override
	public <T> T txNow(TransactionCallback<T> transactionCallback) {
		return session.txNow(transactionCallback);
	}

	@Override
	public void txVoidNow(TransactionVoidCallback transactionCallback) {
		session.txVoidNow(transactionCallback);
	}

	@Override
	public <T> T txNow(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback) {
		return session.txNow(transactionDefinition, transactionCallback);
	}

	@Override
	public void txVoidNow(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback) {
		session.txVoidNow(transactionDefinition, transactionCallback);
	}

	@Override
	public <T> Transaction<T> tx(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback) {
		return session.tx(transactionDefinition, transactionCallback);
	}

	@Override
	public TransactionVoid txVoid(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback) {
		return session.txVoid(transactionDefinition, transactionCallback);
	}

	@Override
	public <BEAN> Find<BEAN> find(BEAN bean) throws OrmException {
		return session.find(bean);
	}

	@Override
	public <BEAN> Find<BEAN> find(Class<BEAN> clazz, Object idValue) {
		return session.find(clazz, idValue);
	}

	@Override
	public <BEAN> Find<BEAN> find(Class<BEAN> clazz, Object[] idValues) throws OrmException {
		return session.find(clazz, idValues);
	}

	@Override
	public <BEAN> FindQuery<BEAN> findQuery(Class<BEAN> clazz) throws OrmException {
		return session.findQuery(clazz);
	}

	@Override
	public <BEAN> FindQuery<BEAN> findQuery(Class<BEAN> clazz, String alias) throws OrmException {
		return session.findQuery(clazz, alias);
	}

	@Override
	public CustomFindQuery findQuery(String select, Class<?> clazz, String alias) throws OrmException {
		return session.findQuery(select, clazz, alias);
	}

	@Override
	public CustomFindQuery findQuery(String[] selectFields, Class<?> clazz, String alias) throws OrmException {
		return session.findQuery(selectFields, clazz, alias);
	}

	@Override
	public <BEAN> Save<BEAN> save(BEAN bean) {
		return add(session.save(bean));
	}

	@Override
	public <BEAN> Save<List<BEAN>> save(Collection<BEAN> beans) throws OrmException {
		return add(session.save(beans));
	}

	@Override
	public <BEAN> SaveOrUpdate<BEAN> saveOrUpdate(BEAN bean) throws OrmException {
		return add(session.saveOrUpdate(bean));
	}

	@Override
	public <BEAN> SaveOrUpdate<List<BEAN>> saveOrUpdate(Collection<BEAN> beans) throws OrmException {
		return add(session.saveOrUpdate(beans));
	}

	@Override
	public ScriptExecutor scriptExecutor() throws OrmException {
		return session.scriptExecutor();
	}

	@Override
	public SqlExecutor sqlExecutor() {
		return session.sqlExecutor();
	}

	@Override
	public <BEAN> Update<BEAN> update(BEAN bean) throws OrmException {
		return add(session.update(bean));
	}

	@Override
	public <BEAN> Update<List<BEAN>> update(Collection<BEAN> beans) throws OrmException {
		return add(session.update(beans));
	}

	@Override
	public <BEAN> CustomUpdateQuery updateQuery(Class<BEAN> clazz) throws OrmException {
		return add(session.updateQuery(clazz));
	}

	private <T extends SaveUpdateDeleteQueryRoot> T add(T query) {
		saveUpdateDeleteQueries.add(query);
		return query;
	}

	public List<SaveUpdateDeleteQueryRoot> getSaveUpdateDeleteQueries() {
		return saveUpdateDeleteQueries;
	}

}
