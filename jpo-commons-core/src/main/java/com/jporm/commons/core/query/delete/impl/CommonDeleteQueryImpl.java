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
package com.jporm.commons.core.query.delete.impl;

import com.jporm.commons.core.query.delete.CommonDeleteQuery;
import com.jporm.commons.core.query.delete.CommonDeleteQueryWhere;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dsl.query.Sql;
import com.jporm.sql.dsl.query.delete.Delete;

/**
 *
 * @author Francesco Cina
 *
 *         10/lug/2011
 */
public class CommonDeleteQueryImpl<DELETE extends CommonDeleteQuery<DELETE, WHERE>, WHERE extends CommonDeleteQueryWhere<DELETE, WHERE>>
        implements CommonDeleteQuery<DELETE, WHERE> {

    private WHERE where;
    private final Delete delete;

    public CommonDeleteQueryImpl(final Class<?> clazz, final SqlFactory sqlFactory) {
        delete = sqlFactory.deleteFrom(clazz);
    }

    /**
     * @return the delete
     */
    public Delete getDelete() {
        return delete;
    }

    /**
     * @return the where
     */
    public WHERE getWhere() {
        return where;
    }

    /**
     * @param where
     *            the where to set
     */
    public void setWhere(final WHERE where) {
        this.where = where;
    }

    @Override
    public Sql sql() {
        return delete;
    }

    @Override
    public final WHERE where() {
        return getWhere();
    }

}
