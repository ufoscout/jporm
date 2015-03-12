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
package com.jporm.core.query.find.impl;

import java.util.List;
import java.util.Optional;

import com.jporm.annotation.LockMode;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.query.clause.impl.WhereImpl;
import com.jporm.core.query.OrmRowMapper;
import com.jporm.core.query.find.FindQuery;
import com.jporm.core.query.find.FindQueryOrderBy;
import com.jporm.core.query.find.FindQueryWhere;

public class FindQueryWhereImpl<BEAN> extends WhereImpl<FindQueryWhere<BEAN>> implements FindQueryWhere<BEAN> {

	private final FindQuery<BEAN> findQuery;

	public FindQueryWhereImpl(com.jporm.sql.query.clause.Where sqlWhere, final FindQuery<BEAN> findQuery) {
		super(sqlWhere);
		this.findQuery = findQuery;
	}

	@Override
	public void appendValues(final List<Object> values) {
		this.findQuery.appendValues(values);
	}

	@Override
	public FindQuery<BEAN> distinct(final boolean distinct) throws JpoException {
		return this.findQuery.distinct(distinct);
	}

	@Override
	public boolean exist() {
		return findQuery.exist();
	}

	@Override
	public FindQuery<BEAN> firstRow(final int firstRow) throws JpoException {
		return this.findQuery.firstRow(firstRow);
	}

	@Override
	public BEAN get() throws JpoException {
		return findQuery.get();
	}

	@Override
	public void get(final OrmRowMapper<BEAN> srr) throws JpoException {
		this.findQuery.get(srr);
	}

	@Override
	public List<BEAN> getList() throws JpoException {
		return this.findQuery.getList();
	}

	@Override
	public Optional<BEAN> getOptional() throws JpoException, JpoNotUniqueResultException {
		return this.findQuery.getOptional();
	}

	@Override
	public int getRowCount() throws JpoException {
		return this.findQuery.getRowCount();
	}

	@Override
	public BEAN getUnique() throws JpoException, JpoNotUniqueResultException {
		return this.findQuery.getUnique();
	}

	@Override
	public FindQuery<BEAN> lockMode(final LockMode lockMode) {
		return this.findQuery.lockMode(lockMode);
	}

	@Override
	public FindQuery<BEAN> maxRows(final int maxRows) throws JpoException {
		return this.findQuery.maxRows(maxRows);
	}

	@Override
	public FindQueryOrderBy<BEAN> orderBy() throws JpoException {
		return this.findQuery.orderBy();
	}

	@Override
	public FindQuery<BEAN> query() {
		return this.findQuery;
	}

	@Override
	public String renderRowCountSql() throws JpoException {
		return this.findQuery.renderRowCountSql();
	}

	@Override
	public String renderSql() {
		return this.findQuery.renderSql();
	}

	@Override
	public void renderSql(final StringBuilder stringBuilder) {
		this.findQuery.renderSql(stringBuilder);
	}

	@Override
	public FindQueryWhere<BEAN> where() throws JpoException {
		return this;
	}

	@Override
	public int getVersion() {
		return findQuery.getVersion();
	}
}
