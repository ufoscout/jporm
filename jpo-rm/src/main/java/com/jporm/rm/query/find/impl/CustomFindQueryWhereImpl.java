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
package com.jporm.rm.query.find.impl;

import java.util.List;
import java.util.Optional;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.io.RowMapper;
import com.jporm.commons.core.query.find.impl.CommonFindQueryWhereImpl;
import com.jporm.rm.query.find.CustomFindQuery;
import com.jporm.rm.query.find.CustomFindQueryOrderBy;
import com.jporm.rm.query.find.CustomFindQueryWhere;
import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.sql.dsl.query.select.where.SelectWhere;

public class CustomFindQueryWhereImpl<BEAN> extends CommonFindQueryWhereImpl<CustomFindQuery<BEAN>, CustomFindQueryWhere<BEAN>, CustomFindQueryOrderBy<BEAN>>
        implements CustomFindQueryWhere<BEAN> {

    public CustomFindQueryWhereImpl(final SelectWhere sqlWhere, final CustomFindQuery<BEAN> findQuery) {
        super(sqlWhere, findQuery);
    }

    @Override
    public boolean exist() {
        return root().exist();
    }

    @Override
    public BEAN fetch() throws JpoException {
        return root().fetch();
    }

    @Override
    public void fetch(final RowMapper<BEAN> srr) throws JpoException {
        root().fetch(srr);
    }

    @Override
    public List<BEAN> fetchList() throws JpoException {
        return root().fetchList();
    }

    @Override
    public Optional<BEAN> fetchOptional() throws JpoException, JpoNotUniqueResultException {
        return root().fetchOptional();
    }

    @Override
    public int fetchRowCount() throws JpoException {
        return root().fetchRowCount();
    }

    @Override
    public BEAN fetchUnique() throws JpoException, JpoNotUniqueResultException {
        return root().fetchUnique();
    }

    @Override
    public SelectCommon sql() {
        return root().sql();
    }

}
