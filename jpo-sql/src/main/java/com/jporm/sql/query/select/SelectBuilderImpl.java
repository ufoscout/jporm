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
package com.jporm.sql.query.select;

import java.util.function.Supplier;

import com.jporm.sql.dialect.SqlSelectRender;
import com.jporm.sql.query.processor.TablePropertiesProcessor;

public class SelectBuilderImpl<TYPE> implements SelectBuilder<TYPE> {

    private boolean distinct;
    private final Supplier<String[]> selectFields;
    private final SqlSelectRender selectRender;
    private final TablePropertiesProcessor<TYPE> propertiesProcessor;

    public SelectBuilderImpl(SqlSelectRender selectRender, final Supplier<String[]> selectFields, TablePropertiesProcessor<TYPE> propertiesProcessor) {
        this.selectRender = selectRender;
        this.selectFields = selectFields;
        this.propertiesProcessor = propertiesProcessor;
    }

    @Override
    public SelectBuilder<TYPE> distinct() {
        this.distinct = true;
        return this;
    }

    @Override
    public Select<TYPE> from(TYPE table) {
        return from(table, "");
    }

    @Override
    public Select<TYPE> from(TYPE table, String alias) {
        SelectImpl<TYPE> select = new SelectImpl<>(selectRender, selectFields, table, propertiesProcessor, alias);
        return select.distinct(distinct);
    }

}
