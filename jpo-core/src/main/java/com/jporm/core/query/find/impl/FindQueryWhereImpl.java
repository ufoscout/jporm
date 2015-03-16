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

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.query.find.impl.CommonFindQueryWhereImpl;
import com.jporm.core.query.OrmRowMapper;
import com.jporm.core.query.find.FindQuery;
import com.jporm.core.query.find.FindQueryOrderBy;
import com.jporm.core.query.find.FindQueryWhere;

public class FindQueryWhereImpl<BEAN> extends CommonFindQueryWhereImpl<FindQuery<BEAN>, FindQueryWhere<BEAN>, FindQueryOrderBy<BEAN>> implements FindQueryWhere<BEAN> {

	public FindQueryWhereImpl(com.jporm.sql.query.clause.Where sqlWhere, final FindQuery<BEAN> findQuery) {
		super(sqlWhere, findQuery);
	}

	@Override
	public boolean exist() {
		return query().exist();
	}

	@Override
	public BEAN get() throws JpoException {
		return query().get();
	}

	@Override
	public void get(final OrmRowMapper<BEAN> srr) throws JpoException {
		query().get(srr);
	}

	@Override
	public List<BEAN> getList() throws JpoException {
		return query().getList();
	}

	@Override
	public Optional<BEAN> getOptional() throws JpoException, JpoNotUniqueResultException {
		return query().getOptional();
	}

	@Override
	public int getRowCount() throws JpoException {
		return query().getRowCount();
	}

	@Override
	public BEAN getUnique() throws JpoException, JpoNotUniqueResultException {
		return query().getUnique();
	}

	@Override
	protected FindQueryWhere<BEAN> where() {
		return this;
	}


}
