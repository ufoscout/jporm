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
package com.jporm.sql.query.clause.impl.value;

import com.jporm.sql.dsl.dialect.DBProfile;

/**
 *
 * @author Francesco Cina
 *
 *         05/giu/2011
 */
public abstract class AColumnValueGenerator {

    private final String name;
    private String generatedColumnName = "";
    private final DBProfile dbProfile;

    public AColumnValueGenerator(final String name, final DBProfile dbProfile) {
        this.name = name;
        this.dbProfile = dbProfile;
    }

    public abstract Object elaborateIdOnSave(Object id);

    protected final DBProfile getDbProfile() {
        return dbProfile;
    }

    public String getGeneratedColumnName() {
        return generatedColumnName;
    }

    public String getName() {
        return name;
    }

    public abstract String insertColumn(String currentValue);

    public abstract String insertQueryParameter(String currentValue);

    public abstract boolean preElaborateIdOnSave();

    public void setGeneratedColumnName(final String generatedColumnName) {
        this.generatedColumnName = generatedColumnName;
    }

}
