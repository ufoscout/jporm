package com.jporm.rx.sync.query.delete.impl;

import co.paralleluniverse.fibers.Suspendable;

import com.jporm.commons.core.query.clause.Where;
import com.jporm.rx.connection.DeleteResult;
import com.jporm.rx.sync.query.common.CommonWhereSync;
import com.jporm.rx.sync.query.delete.CustomDeleteQuerySync;
import com.jporm.rx.sync.query.delete.CustomDeleteQueryWhereSync;

/**
 * Created by ufo on 27/07/15.
 */
@Suspendable
public class CustomDeleteQueryWhereSyncImpl<BEAN> extends CommonWhereSync<CustomDeleteQueryWhereSync<BEAN>>  implements CustomDeleteQueryWhereSync<BEAN> {

	private CustomDeleteQuerySyncImpl<BEAN> customDeleteQuerySyncImpl;

	public CustomDeleteQueryWhereSyncImpl(CustomDeleteQuerySyncImpl<BEAN> customDeleteQuerySyncImpl, Where<?> whereClause) {
		super(whereClause);
		this.customDeleteQuerySyncImpl = customDeleteQuerySyncImpl;
	}

	@Override
	public DeleteResult execute() {
		return customDeleteQuerySyncImpl.execute();
	}

	@Override
	public CustomDeleteQuerySync<BEAN> root() {
		return customDeleteQuerySyncImpl;
	}

	@Override
	protected CustomDeleteQueryWhereSync<BEAN> where() {
		return this;
	}


}
