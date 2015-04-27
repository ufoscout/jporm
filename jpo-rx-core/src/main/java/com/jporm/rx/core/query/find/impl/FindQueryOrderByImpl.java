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
package com.jporm.rx.core.query.find.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.io.RowMapper;
import com.jporm.commons.core.query.find.impl.CommonFindQueryOrderByImpl;
import com.jporm.rx.core.query.find.FindQuery;
import com.jporm.rx.core.query.find.FindQueryOrderBy;
import com.jporm.rx.core.query.find.FindQueryWhere;

/**
 *
 * @author ufo
 *
 * @param <BEAN>
 */
public class FindQueryOrderByImpl<BEAN> extends CommonFindQueryOrderByImpl<FindQuery<BEAN>, FindQueryWhere<BEAN>, FindQueryOrderBy<BEAN>> implements FindQueryOrderBy<BEAN> {

	public FindQueryOrderByImpl(com.jporm.sql.query.clause.OrderBy sqlOrderBy, final FindQuery<BEAN> findQuery) {
		super(sqlOrderBy, findQuery);
	}

	@Override
	public CompletableFuture<BEAN> fetch() {
		return root().fetch();
	}

	@Override
	public CompletableFuture<Optional<BEAN>> fetchOptional() {
		return root().fetchOptional();
	}

	@Override
	public CompletableFuture<BEAN> fetchUnique() {
		return root().fetchUnique();
	}

	@Override
	public CompletableFuture<Boolean> exist() {
		return root().exist();
	}

	@Override
	public CompletableFuture<List<BEAN>> fetchList() {
		return root().fetchList();
	}

	@Override
	public CompletableFuture<Integer> fetchRowCount() {
		return root().fetchRowCount();
	}

	@Override
	public CompletableFuture<Void> fetch(RowMapper<BEAN> orm) {
		return root().fetch(orm);
	}

}
