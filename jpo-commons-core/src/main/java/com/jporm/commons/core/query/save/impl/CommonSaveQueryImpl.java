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
package com.jporm.commons.core.query.save.impl;

import com.jporm.commons.core.query.AQueryRoot;
import com.jporm.commons.core.query.save.CommonSaveQuery;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.query.SqlRoot;
import com.jporm.sql.query.clause.Insert;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class CommonSaveQueryImpl<SAVE extends CommonSaveQuery<SAVE>> extends AQueryRoot implements CommonSaveQuery<SAVE> {

    private final Insert insert;

    public CommonSaveQueryImpl(final Class<?> clazz, final SqlFactory sqlFactory, final String[] fields) {
        insert = sqlFactory.insert(clazz, fields);
    }

    /**
     * @return the insert
     */
    public Insert query() {
        return insert;
    }

    @Override
    public SqlRoot sql() {
        return insert;
    }

    @Override
    public final SAVE useGenerators(final boolean useGenerators) {
        insert.useGenerators(useGenerators);
        return (SAVE) this;
    }

    @Override
    public SAVE values(final Object... values) {
        insert.values(values);
        return (SAVE) this;
    }

}
