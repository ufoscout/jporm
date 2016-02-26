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
package com.jporm.sql.dsl.query;

import java.util.ArrayList;
import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;

/**
 * An {@link RenderableSqlQuery} that keep track of the status of the object.
 * After a call to one of the render methods the result is stored and used for
 * future calls if the status of the object doen't change
 *
 * @author ufo
 *
 */
public abstract class ASql implements Sql {

    private final DBProfile dbProfile;

    public ASql(DBProfile dbProfile) {
        this.dbProfile = dbProfile;
    }

    @Override
    public final List<Object> sqlValues() {
        List<Object> values = new ArrayList<>();
        sqlValues(values);
        return values;
    }

    @Override
    public final String sqlQuery() {
        return sqlQuery(dbProfile);
    }

    @Override
    public final String sqlQuery(DBProfile dbProfile) {
        final StringBuilder queryBuilder = new StringBuilder();
        sqlQuery(dbProfile, queryBuilder);
        return queryBuilder.toString();
    }

    @Override
    public final void sqlQuery(final StringBuilder queryBuilder) {
        sqlQuery(dbProfile, queryBuilder);
    }

    protected DBProfile getDefaultDbProfile() {
        return dbProfile;
    }
}
