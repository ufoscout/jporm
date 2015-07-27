package com.jporm.rx.sync.query.delete.impl;

import co.paralleluniverse.fibers.Suspendable;

import com.jporm.rx.connection.DeleteResult;
import com.jporm.rx.query.delete.CustomDeleteQuery;
import com.jporm.rx.sync.quasar.JpoCompletableWrapper;
import com.jporm.rx.sync.query.delete.CustomDeleteQuerySync;
import com.jporm.rx.sync.query.delete.CustomDeleteQueryWhereSync;
import com.jporm.sql.query.SqlRoot;

/**
 * Created by ufo on 27/07/15.
 */
@Suspendable
public class CustomDeleteQuerySyncImpl<BEAN> implements CustomDeleteQuerySync<BEAN> {

	private final CustomDeleteQuery<BEAN> customDeleteQuery;
	private final CustomDeleteQueryWhereSync<BEAN> customDeleteQueryWhereSync;

	public CustomDeleteQuerySyncImpl(CustomDeleteQuery<BEAN> customDeleteQuery) {
		this.customDeleteQuery = customDeleteQuery;
		customDeleteQueryWhereSync = new CustomDeleteQueryWhereSyncImpl<BEAN>(this, customDeleteQuery.where());
	}

	@Override
	public SqlRoot sql() {
		return customDeleteQuery.sql();
	}

	@Override
	public DeleteResult execute() {
		return JpoCompletableWrapper.get(customDeleteQuery.execute());
	}

	@Override
	public CustomDeleteQueryWhereSync<BEAN> where() {
		return customDeleteQueryWhereSync;
	}

}
