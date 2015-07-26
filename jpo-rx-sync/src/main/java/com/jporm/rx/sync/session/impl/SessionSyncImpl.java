package com.jporm.rx.sync.session.impl;

import co.paralleluniverse.fibers.Suspendable;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.rx.connection.DeleteResult;
import com.jporm.rx.session.Session;
import com.jporm.rx.sync.quasar.JpoCompletableWrapper;
import com.jporm.rx.sync.query.delete.CustomDeleteQuerySync;
import com.jporm.rx.sync.query.find.CustomFindQueryBuilderSync;
import com.jporm.rx.sync.query.find.FindQueryCommonSync;
import com.jporm.rx.sync.query.find.FindQuerySync;
import com.jporm.rx.sync.query.save.CustomSaveQuerySync;
import com.jporm.rx.sync.query.update.CustomUpdateQuerySync;
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
        return null;
    }

    @Override
    public <BEAN> FindQueryCommonSync<BEAN> findById(Class<BEAN> clazz, Object idValue) {

        return null;
    }

    @Override
    public <BEAN> FindQuerySync<BEAN> find(Class<BEAN> clazz) throws JpoException {
        return null;
    }

    @Override
    public <BEAN> FindQuerySync<BEAN> find(Class<BEAN> clazz, String alias) throws JpoException {
        return null;
    }

    @Override
    public <BEAN> CustomFindQueryBuilderSync find(String... selectFields) {

        return null;
    }

    @Override
    public <BEAN> BEAN save(BEAN bean) {
        return JpoCompletableWrapper.get(session.save(bean));
    }

    @Override
    public <BEAN> CustomSaveQuerySync save(Class<BEAN> clazz, String... fields) throws JpoException {
        return null;
    }

    @Override
    public <BEAN> BEAN update(BEAN bean) throws JpoException {
        return JpoCompletableWrapper.get(session.update(bean));
    }

    @Override
    public <BEAN> CustomUpdateQuerySync update(Class<BEAN> clazz) throws JpoException {
        return null;
    }

    @Override
    public <BEAN> BEAN saveOrUpdate(BEAN bean) {
        return JpoCompletableWrapper.get(session.saveOrUpdate(bean));
    }
}
