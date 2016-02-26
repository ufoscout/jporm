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
 *         13/giu/2011
 */
public class NullColumnValueGenerator extends AColumnValueGenerator {

    public NullColumnValueGenerator(final String name, final DBProfile dbProfile) {
        super(name, dbProfile);
    }

    @Override
    public Object elaborateIdOnSave(final Object id) {
        return id;
    }

    @Override
    public String insertColumn(final String currentValue) {
        return currentValue;
    }

    @Override
    public String insertQueryParameter(final String currentValue) {
        return currentValue;
    }

    @Override
    public boolean preElaborateIdOnSave() {
        return false;
    }
}
