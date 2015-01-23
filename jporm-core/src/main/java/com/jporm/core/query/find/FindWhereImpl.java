/*******************************************************************************
 * Copyright 2013 Francesco Cina'
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
package com.jporm.core.query.find;

import java.util.List;

import com.jporm.core.query.clause.WhereImpl;
import com.jporm.exception.OrmException;
import com.jporm.exception.OrmNotUniqueResultException;
import com.jporm.query.LockMode;
import com.jporm.query.OrmRowMapper;
import com.jporm.query.find.FindOrderBy;
import com.jporm.query.find.FindQuery;
import com.jporm.query.find.FindWhere;

public class FindWhereImpl<BEAN> extends WhereImpl<FindWhere<BEAN>> implements FindWhere<BEAN> {

	private final FindQuery<BEAN> findQuery;

	@Override
	public String renderSql() {
		return this.findQuery.renderSql();
	}

	@Override
	public void renderSql(final StringBuilder stringBuilder) {
		this.findQuery.renderSql(stringBuilder);
	}

	@Override
	public void appendValues(final List<Object> values) {
		this.findQuery.appendValues(values);
	}

	public FindWhereImpl(final FindQuery<BEAN> findQuery) {
		this.findQuery = findQuery;
	}

	@Override
	public FindQuery<BEAN> query() {
		return this.findQuery;
	}

	@Override
	public FindWhere<BEAN> where() throws OrmException {
		return this;
	}

	@Override
	public FindOrderBy<BEAN> orderBy() throws OrmException {
		return this.findQuery.orderBy();
	}

	@Override
	public List<BEAN> getList() throws OrmException {
		return this.findQuery.getList();
	}

	@Override
	public int getRowCount() throws OrmException {
		return this.findQuery.getRowCount();
	}

	@Override
	public BEAN getUnique() throws OrmException, OrmNotUniqueResultException {
		return this.findQuery.getUnique();
	}

	@Override
	public void get(final OrmRowMapper<BEAN> srr) throws OrmException {
		this.findQuery.get(srr);
	}

	@Override
	public String renderRowCountSql() throws OrmException {
		return this.findQuery.renderRowCountSql();
	}

	@Override
	public FindQuery<BEAN> distinct(final boolean distinct) throws OrmException {
		return this.findQuery.distinct(distinct);
	}

	@Override
	public FindQuery<BEAN> lockMode(final LockMode lockMode) {
		return this.findQuery.lockMode(lockMode);
	}

	@Override
	public FindQuery<BEAN> maxRows(final int maxRows) throws OrmException {
		return this.findQuery.maxRows(maxRows);
	}

	@Override
	public FindQuery<BEAN> timeout(final int queryTimeout) {
		return this.findQuery.timeout(queryTimeout);
	}

	@Override
	public int getTimeout() {
		return this.findQuery.getTimeout();
	}

	@Override
	public BEAN get() throws OrmException, OrmNotUniqueResultException {
		return this.findQuery.get();
	}

	@Override
	public FindQuery<BEAN> firstRow(final int firstRow) throws OrmException {
		return this.findQuery.firstRow(firstRow);
	}



}
