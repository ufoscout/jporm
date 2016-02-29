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
package com.jporm.rx.query.find.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.query.find.impl.CommonFindQueryOrderByImpl;
import com.jporm.rx.query.find.CustomFindQuery;
import com.jporm.rx.query.find.CustomFindQueryOrderBy;
import com.jporm.rx.query.find.CustomFindQueryWhere;
import com.jporm.sql.query.orderby.OrderBy;
import com.jporm.sql.query.select.pagination.PaginationProvider;

/**
 *
 * @author ufo
 *
 * @param <BEAN>
 */
public class CustomFindQueryOrderByImpl<BEAN> extends CommonFindQueryOrderByImpl<CustomFindQuery<BEAN>, CustomFindQueryWhere<BEAN>, CustomFindQueryOrderBy<BEAN>>
        implements CustomFindQueryOrderBy<BEAN> {

    public CustomFindQueryOrderByImpl(final OrderBy sqlOrderBy, final CustomFindQuery<BEAN> findQuery) {
        super(sqlOrderBy, findQuery);
    }

    @Override
    public CompletableFuture<Boolean> exist() {
        return root().exist();
    }

    @Override
    public CompletableFuture<BEAN> fetch() {
        return root().fetch();
    }

    @Override
    public CompletableFuture<List<BEAN>> fetchList() {
        return root().fetchList();
    }

    @Override
    public CompletableFuture<Optional<BEAN>> fetchOptional() {
        return root().fetchOptional();
    }

    @Override
    public CompletableFuture<Integer> fetchRowCount() {
        return root().fetchRowCount();
    }

    @Override
    public CompletableFuture<BEAN> fetchUnique() {
        return root().fetchUnique();
    }

    @Override
    public PaginationProvider sql() {
        return root().sql();
    }

}
