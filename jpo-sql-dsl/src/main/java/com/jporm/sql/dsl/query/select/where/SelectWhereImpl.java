/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.sql.dsl.query.select.where;

import com.jporm.sql.dsl.query.select.LockMode;
import com.jporm.sql.dsl.query.select.Select;
import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.sql.dsl.query.select.SelectCommonProvider;
import com.jporm.sql.dsl.query.select.SelectUnionsProvider;
import com.jporm.sql.dsl.query.select.groupby.SelectGroupBy;
import com.jporm.sql.dsl.query.select.orderby.SelectOrderBy;
import com.jporm.sql.dsl.query.where.WhereImpl;

public class SelectWhereImpl extends WhereImpl<SelectWhere> implements SelectWhere {

    private final Select<?> select;

    public SelectWhereImpl(Select<?> select) {
        super(select);
        this.select = select;
    }

    @Override
    public SelectGroupBy groupBy() {
        return select.groupBy();
    }

    @Override
    public SelectOrderBy orderBy() {
        return select.orderBy();
    }

    @Override
    public String sqlRowCountQuery() {
        return select.sqlRowCountQuery();
    }

    @Override
    public SelectUnionsProvider union(SelectCommon select) {
       return this.select.union(select);
    }

    @Override
    public SelectUnionsProvider unionAll(SelectCommon select) {
        return this.select.unionAll(select);
    }

    @Override
    public SelectUnionsProvider except(SelectCommon select) {
        return this.select.except(select);
    }

    @Override
    public SelectUnionsProvider intersect(SelectCommon select) {
        return this.select.intersect(select);
    }

    @Override
    protected SelectWhere getWhere() {
        return this;
    }

    @Override
    public SelectCommonProvider limit(int limit) {
        return select.limit(limit);
    }

    @Override
    public SelectCommonProvider lockMode(LockMode lockMode) {
        return select.lockMode(lockMode);
    }

    @Override
    public SelectCommonProvider forUpdate() {
        return select.forUpdate();
    }

    @Override
    public SelectCommonProvider forUpdateNoWait() {
        return select.forUpdateNoWait();
    }

    @Override
    public SelectCommonProvider offset(int offset) {
        return select.offset(offset);
    }
}
