package com.jporm.rx.sync.session.impl;

import co.paralleluniverse.fibers.Suspendable;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rx.connection.DeleteResult;
import com.jporm.rx.query.find.FindQuery;
import com.jporm.rx.session.Session;
import com.jporm.rx.sync.quasar.JpoCompletableWrapper;
import com.jporm.rx.sync.query.delete.CustomDeleteQuerySync;
import com.jporm.rx.sync.query.delete.impl.CustomDeleteQuerySyncImpl;
import com.jporm.rx.sync.query.find.CustomFindQueryBuilderSync;
import com.jporm.rx.sync.query.find.FindQueryCommonSync;
import com.jporm.rx.sync.query.find.FindQuerySync;
import com.jporm.rx.sync.query.find.impl.CustomFindQueryBuilderSyncImpl;
import com.jporm.rx.sync.query.find.impl.FindQuerySyncImpl;
import com.jporm.rx.sync.query.save.CustomSaveQuerySync;
import com.jporm.rx.sync.query.save.impl.CustomSaveQuerySyncImpl;
import com.jporm.rx.sync.query.update.CustomUpdateQuerySync;
import com.jporm.rx.sync.query.update.impl.CustomUpdateQuerySyncImpl;
import com.jporm.rx.sync.session.SessionSync;
import com.jporm.rx.sync.session.SqlExecutorSync;

/**
 * Created by ufo on 26/07/15.
 */
@Suspendable
public class SessionSyncImpl implements SessionSync {

	private Session session;

	public SessionSyncImpl(Session session) {
		this.session = session;
	}

	@Override
	public SqlExecutorSync sqlExecutor() {
		return new SqlExecutorSyncImpl(session.sqlExecutor());
	}

	@Override
	public <BEAN> DeleteResult delete(BEAN bean) throws JpoException {
		return JpoCompletableWrapper.get(session.delete(bean));
	}

	@Override
	public <BEAN> CustomDeleteQuerySync<BEAN> delete(Class<BEAN> clazz) throws JpoException {
		return new CustomDeleteQuerySyncImpl<BEAN>(session.delete(clazz));
	}

	@Override
	public <BEAN> FindQueryCommonSync<BEAN> findById(Class<BEAN> clazz, Object idValue) {
		return new FindQuerySyncImpl<BEAN>((FindQuery<BEAN>) session.findById(clazz, idValue));
	}

	@Override
	public <BEAN> FindQuerySync<BEAN> find(Class<BEAN> clazz) throws JpoException {
		return new FindQuerySyncImpl<BEAN>(session.find(clazz));
	}

	@Override
	public <BEAN> FindQuerySync<BEAN> find(Class<BEAN> clazz, String alias) throws JpoException {
		return new FindQuerySyncImpl<BEAN>(session.find(clazz, alias));
	}

	@Override
	public <BEAN> CustomFindQueryBuilderSync find(String... selectFields) {
		return new CustomFindQueryBuilderSyncImpl(session.find(selectFields));
	}

	@Override
	public <BEAN> BEAN save(BEAN bean) {
		return JpoCompletableWrapper.get(session.save(bean));
	}

	@Override
	public <BEAN> CustomSaveQuerySync save(Class<BEAN> clazz, String... fields) throws JpoException {
		return new CustomSaveQuerySyncImpl(session.save(clazz, fields));
	}

	@Override
	public <BEAN> BEAN update(BEAN bean) throws JpoException {
		return JpoCompletableWrapper.get(session.update(bean));
	}

	@Override
	public <BEAN> CustomUpdateQuerySync update(Class<BEAN> clazz) throws JpoException {
		return new CustomUpdateQuerySyncImpl(session.update(clazz));
	}

	@Override
	public <BEAN> BEAN saveOrUpdate(BEAN bean) {
		return JpoCompletableWrapper.get(session.saveOrUpdate(bean));
	}
}
