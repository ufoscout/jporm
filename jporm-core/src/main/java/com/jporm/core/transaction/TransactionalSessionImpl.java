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
import java.util.concurrent.CompletableFuture;

import com.jporm.exception.OrmException;
import com.jporm.query.SaveUpdateDeleteQueryRoot;
import com.jporm.query.delete.DeleteQuery;
import com.jporm.query.delete.CustomDeleteQuery;
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
import com.jporm.transaction.TransactionVoid;
import com.jporm.transaction.TransactionVoidCallback;
import com.jporm.transaction.TransactionalSession;

public class TransactionalSessionImpl implements TransactionalSession {

	private final Session session;


	@Override
	public <BEAN> int delete(BEAN bean) throws OrmException {
		return session.delete(bean);
	}

	@Override
	public <BEAN> int delete(Collection<BEAN> beans) throws OrmException {
		return session.delete(beans);
	}

	@Override
	public <BEAN> BEAN save(BEAN bean) {
		return session.save(bean);
	}

	@Override
	public <BEAN> List<BEAN> save(Collection<BEAN> beans) throws OrmException {
		return session.save(beans);
	}

	@Override
	public <BEAN> BEAN saveOrUpdate(BEAN bean) throws OrmException {
		return session.saveOrUpdate(bean);
	}

	@Override
	public <BEAN> List<BEAN> saveOrUpdate(Collection<BEAN> beans) throws OrmException {
		return session.saveOrUpdate(beans);
	}

	@Override
	public <BEAN> BEAN update(BEAN bean) throws OrmException {
		return session.update(bean);
	}

	@Override
	public <BEAN> List<BEAN> update(Collection<BEAN> beans) throws OrmException {
		return session.update(beans);
	}

	private final List<SaveUpdateDeleteQueryRoot> saveUpdateDeleteQueries = new ArrayList<SaveUpdateDeleteQueryRoot>();

	public TransactionalSessionImpl(Session session) {
		this.session = session;
	}

	private <T extends SaveUpdateDeleteQueryRoot> T add(T query) {
		saveUpdateDeleteQueries.add(query);
		return query;
	}

	@Override
	public <BEAN> DeleteQuery<BEAN> deleteQuery(BEAN bean) throws OrmException {
		return add(session.deleteQuery(bean));
	}

	@Override
	public <BEAN> DeleteQuery<List<BEAN>> deleteQuery(Collection<BEAN> beans) throws OrmException {
		return add(session.deleteQuery(beans));
	}

	@Override
	public <BEAN> CustomDeleteQuery<BEAN> deleteQuery(Class<BEAN> clazz) throws OrmException {
		return add(session.deleteQuery(clazz));
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

	public List<SaveUpdateDeleteQueryRoot> getSaveUpdateDeleteQueries() {
		return saveUpdateDeleteQueries;
	}

	@Override
	public <BEAN> SaveQuery<BEAN> saveQuery(BEAN bean) {
		return add(session.saveQuery(bean));
	}

	@Override
	public <BEAN> SaveQuery<List<BEAN>> saveQuery(Collection<BEAN> beans) throws OrmException {
		return add(session.saveQuery(beans));
	}

	@Override
	public <BEAN> SaveOrUpdateQuery<BEAN> saveOrUpdateQuery(BEAN bean) throws OrmException {
		return add(session.saveOrUpdateQuery(bean));
	}

	@Override
	public <BEAN> SaveOrUpdateQuery<List<BEAN>> saveOrUpdateQuery(Collection<BEAN> beans) throws OrmException {
		return add(session.saveOrUpdateQuery(beans));
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
	public <T> Transaction<T> tx(TransactionCallback<T> transactionCallback) {
		return session.tx(transactionCallback);
	}

	@Override
	public <T> Transaction<T> tx(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback) {
		return session.tx(transactionDefinition, transactionCallback);
	}

	@Override
	public <T> CompletableFuture<T> txAsync(TransactionCallback<T> transactionCallback) {
		return session.txAsync(transactionCallback);
	}

	@Override
	public <T> CompletableFuture<T> txAsync(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback) {
		return session.txAsync(transactionDefinition, transactionCallback);
	}

	@Override
	public <T> T txNow(TransactionCallback<T> transactionCallback) {
		return session.txNow(transactionCallback);
	}

	@Override
	public <T> T txNow(TransactionDefinition transactionDefinition, TransactionCallback<T> transactionCallback) {
		return session.txNow(transactionDefinition, transactionCallback);
	}

	@Override
	public TransactionVoid txVoid(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback) {
		return session.txVoid(transactionDefinition, transactionCallback);
	}

	@Override
	public TransactionVoid txVoid(TransactionVoidCallback transactionCallback) {
		return session.txVoid(transactionCallback);
	}

	@Override
	public CompletableFuture<Void> txVoidAsync(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback) {
		return session.txVoidAsync(transactionDefinition, transactionCallback);
	}

	@Override
	public CompletableFuture<Void> txVoidAsync(TransactionVoidCallback transactionCallback) {
		return session.txVoidAsync(transactionCallback);
	}

	@Override
	public void txVoidNow(TransactionDefinition transactionDefinition, TransactionVoidCallback transactionCallback) {
		session.txVoidNow(transactionDefinition, transactionCallback);
	}

	@Override
	public void txVoidNow(TransactionVoidCallback transactionCallback) {
		session.txVoidNow(transactionCallback);
	}

	@Override
	public <BEAN> UpdateQuery<BEAN> updateQuery(BEAN bean) throws OrmException {
		return add(session.updateQuery(bean));
	}

	@Override
	public <BEAN> UpdateQuery<List<BEAN>> updateQuery(Collection<BEAN> beans) throws OrmException {
		return add(session.updateQuery(beans));
	}

	@Override
	public <BEAN> CustomUpdateQuery updateQuery(Class<BEAN> clazz) throws OrmException {
		return add(session.updateQuery(clazz));
	}

}
