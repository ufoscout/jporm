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
package com.jporm.core.query.update;

import java.util.List;

import com.jporm.core.query.clause.SetImpl;
import com.jporm.query.update.CustomUpdateQuery;
import com.jporm.query.update.CustomUpdateSet;
import com.jporm.query.update.CustomUpdateWhere;

/**
 * 
 * @author ufo
 *
 */
public class CustomUpdateSetImpl extends SetImpl<CustomUpdateSet> implements CustomUpdateSet {

    private final CustomUpdateQuery query;

    public CustomUpdateSetImpl(final CustomUpdateQuery query) {
        this.query = query;
    }

    @Override
    public String renderSql() {
        return query.renderSql();
    }

    @Override
    public void renderSql(final StringBuilder stringBuilder) {
        query.renderSql(stringBuilder);
    }

    @Override
    public void appendValues(final List<Object> values) {
        query.appendValues(values);
    }

    @Override
    public final int now() {
        return query.now();
    }

    @Override
    public CustomUpdateQuery query() {
        return query;
    }

    @Override
    protected CustomUpdateSet set() {
        return this;
    }

    @Override
    public CustomUpdateWhere where() {
        return query.where();
    }

    @Override
    public CustomUpdateQuery queryTimeout(final int queryTimeout) {
        return query.queryTimeout(queryTimeout);
    }

}
