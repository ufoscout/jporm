package com.jporm.rx.sync.query.save.impl;

import co.paralleluniverse.fibers.Suspendable;
import com.jporm.rx.connection.UpdateResult;
import com.jporm.rx.query.save.CustomSaveQuery;
import com.jporm.rx.sync.quasar.JpoCompletableWrapper;
import com.jporm.rx.sync.query.save.CustomSaveQuerySync;
import com.jporm.sql.query.SqlRoot;

/**
 * Created by ufo on 27/07/15.
 */
@Suspendable
public class CustomSaveQuerySyncImpl implements CustomSaveQuerySync {

    private CustomSaveQuery saveQuery;

    public CustomSaveQuerySyncImpl(CustomSaveQuery saveQuery) {
        this.saveQuery = saveQuery;
    }

    @Override
    public UpdateResult execute() {
        return JpoCompletableWrapper.get(saveQuery.execute());
    }

    @Override
    public CustomSaveQuerySync values(Object... values) {
        saveQuery.values(values);
        return this;
    }

    @Override
    public CustomSaveQuerySync useGenerators(boolean useGenerators) {
        saveQuery.useGenerators(useGenerators);
        return this;
    }

    @Override
    public SqlRoot sql() {
        return saveQuery.sql();
    }

}
