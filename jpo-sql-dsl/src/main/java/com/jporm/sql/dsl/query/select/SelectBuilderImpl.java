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
package com.jporm.sql.dsl.query.select;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.processor.TablePropertiesProcessor;

public class SelectBuilderImpl<JOIN> implements SelectBuilder<JOIN> {

    private boolean distinct;
    private final String[] selectFields;
    private final DBProfile dbProfile;
    private final TablePropertiesProcessor<JOIN> propertiesProcessor;

    public SelectBuilderImpl(DBProfile dbProfile, final String[] selectFields, TablePropertiesProcessor<JOIN> propertiesProcessor) {
        this.dbProfile = dbProfile;
        this.selectFields = selectFields;
        this.propertiesProcessor = propertiesProcessor;
    }

    @Override
    public SelectBuilder<JOIN> distinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    @Override
    public Select<JOIN> from(JOIN table) {
        return from(table, "");
    }

    @Override
    public Select<JOIN> from(JOIN table, String alias) {
        SelectImpl<JOIN> select = new SelectImpl<>(dbProfile, selectFields, table, propertiesProcessor, alias);
        select.distinct(distinct);
        return select;
    }

}
