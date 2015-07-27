package com.jporm.rx.sync.query.update.impl;

import co.paralleluniverse.fibers.Suspendable;
import com.jporm.rx.connection.UpdateResult;
import com.jporm.rx.query.update.CustomUpdateQuery;
import com.jporm.rx.sync.quasar.JpoCompletableWrapper;
import com.jporm.rx.sync.query.update.CustomUpdateQuerySync;
import com.jporm.rx.sync.query.update.CustomUpdateQueryWhereSync;
import com.jporm.sql.query.SqlRoot;

/**
 * Created by ufo on 27/07/15.
 */
@Suspendable
public class CustomUpdateQuerySyncImpl implements CustomUpdateQuerySync {

    private final CustomUpdateQuery customUpdateQuery;
    private final CustomUpdateQueryWhereSync customUpdateQueryWhereSync;

    public CustomUpdateQuerySyncImpl(CustomUpdateQuery customUpdateQuery) {
        this.customUpdateQuery = customUpdateQuery;
        customUpdateQueryWhereSync = new CustomUpdateQueryWhereSyncImpl(this, customUpdateQuery.where());
    }

    @Override
    public CustomUpdateQuerySync set(String property, Object value) {
        customUpdateQuery.set(property, value);
        return this;
    }

    @Override
    public CustomUpdateQueryWhereSync where() {
        return customUpdateQueryWhereSync;
    }

    @Override
    public UpdateResult execute() {
        return JpoCompletableWrapper.get(customUpdateQuery.execute());
    }

    @Override
    public SqlRoot sql() {
        return customUpdateQuery.sql();
    }
}
