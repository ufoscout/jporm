/*******************************************************************************
 * Copyright 2013 Francesco Cina'
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.jporm.rx.sync.query.find.impl;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rx.query.find.FindQuery;
import com.jporm.rx.sync.query.common.CommonWhereSync;
import com.jporm.rx.sync.query.find.FindQueryOrderBySync;
import com.jporm.rx.sync.query.find.FindQuerySync;
import com.jporm.rx.sync.query.find.FindQueryWhereSync;

import java.util.List;
import java.util.Optional;

public class FindQueryWhereSyncImpl<BEAN> extends CommonWhereSync<FindQueryWhereSync<BEAN>> implements FindQueryWhereSync<BEAN> {

	private FindQuerySync<BEAN> findQuerySync;

	public FindQueryWhereSyncImpl(FindQuerySync<BEAN> findQuerySync, FindQuery<BEAN> findQuery) {
		super(findQuery.where());
		this.findQuerySync = findQuerySync;
	}

	@Override
	public FindQuerySync<BEAN> root() {
		return findQuerySync;
	}

	@Override
	public FindQueryOrderBySync<BEAN> orderBy() throws JpoException {
		return findQuerySync.orderBy();
	}

	@Override
	public FindQuerySync<BEAN> distinct() throws JpoException {
		return findQuerySync.distinct();
	}

	@Override
	public FindQuerySync<BEAN> forUpdate() {
		return findQuerySync.forUpdate();
	}

	@Override
	public FindQuerySync<BEAN> forUpdateNoWait() {
		return findQuerySync.forUpdateNoWait();
	}

	@Override
	public FindQuerySync<BEAN> limit(int limit) throws JpoException {
		return findQuerySync.limit(limit);
	}

	@Override
	public FindQuerySync<BEAN> offset(int offset) throws JpoException {
		return findQuerySync.offset(offset);
	}

	@Override
	public BEAN fetch() {
		return findQuerySync.fetch();
	}

	@Override
	public Optional<BEAN> fetchOptional() {
		return findQuerySync.fetchOptional();
	}

	@Override
	public BEAN fetchUnique() {
		return findQuerySync.fetchUnique();
	}

	@Override
	public Boolean exist() {
		return exist();
	}

	@Override
	public List<BEAN> fetchList() {
		return findQuerySync.fetchList();
	}

	@Override
	public Integer fetchRowCount() {
		return findQuerySync.fetchRowCount();
	}

	@Override
	protected FindQueryWhereSync<BEAN> where() {
		return this;
	}
}
