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
package com.jporm.query.update;

import java.util.List;

import com.jporm.query.clause.WhereImpl;

/**
 * 
 * @author ufo
 *
 */
public class CustomUpdateWhereImpl extends WhereImpl<CustomUpdateWhere> implements CustomUpdateWhere {

    private final CustomUpdateQuery updateQuery;

    public CustomUpdateWhereImpl(final CustomUpdateQuery updateQuery) {
        this.updateQuery = updateQuery;

    }

    @Override
    public CustomUpdateQuery query() {
        return updateQuery;
    }

    @Override
    public CustomUpdateSet set() {
        return updateQuery.set();
    }

    @Override
    protected CustomUpdateWhere where() {
        return this;
    }

    @Override
    public int now() {
        return updateQuery.now();
    }

    @Override
    public final void appendValues(final List<Object> values) {
        updateQuery.appendValues(values);
    }

    @Override
    public String renderSql() {
        return updateQuery.renderSql();
    }

    @Override
    public void renderSql(final StringBuilder stringBuilder) {
        updateQuery.renderSql(stringBuilder);
    }

    @Override
    public CustomUpdateQuery queryTimeout(final int queryTimeout) {
        return updateQuery.queryTimeout(queryTimeout);
    }

}
