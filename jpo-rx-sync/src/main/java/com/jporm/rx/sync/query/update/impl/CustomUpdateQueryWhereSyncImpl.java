package com.jporm.rx.sync.query.update.impl;

import co.paralleluniverse.fibers.Suspendable;
import com.jporm.commons.core.query.clause.Where;
import com.jporm.rx.connection.UpdateResult;
import com.jporm.rx.sync.query.common.CommonWhereSync;
import com.jporm.rx.sync.query.update.CustomUpdateQuerySync;
import com.jporm.rx.sync.query.update.CustomUpdateQueryWhereSync;

/**
 * Created by ufo on 27/07/15.
 */
@Suspendable
public class CustomUpdateQueryWhereSyncImpl extends CommonWhereSync<CustomUpdateQueryWhereSync>  implements CustomUpdateQueryWhereSync {

    private CustomUpdateQuerySync customUpdateQuerySync;

    public CustomUpdateQueryWhereSyncImpl(CustomUpdateQuerySync customUpdateQuerySync, Where<?> whereClause) {
        super(whereClause);
        this.customUpdateQuerySync = customUpdateQuerySync;
    }

    @Override
    public CustomUpdateQuerySync root() {
        return customUpdateQuerySync;
    }

    @Override
    public CustomUpdateQuerySync set(String property, Object value) {
        where().set(property, value);
        return customUpdateQuerySync;
    }

    @Override
    protected CustomUpdateQueryWhereSync where() {
        return this;
    }

    @Override
    public UpdateResult execute() {
        return customUpdateQuerySync.execute();
    }
}
