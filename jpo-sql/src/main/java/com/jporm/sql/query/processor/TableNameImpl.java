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
package com.jporm.sql.query.processor;

public class TableNameImpl implements TableName {

    private final String table;
    private final String alias;
    private final boolean hasAlias;

    public TableNameImpl(String table, String alias) {
        this.table = table;
        this.alias = alias;
        hasAlias = !alias.isEmpty();
    }

    /**
     * @return the table
     */
    @Override
    public String getTable() {
        return table;
    }
    /**
     * @return the alias
     */
    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public boolean hasAlias() {
        return hasAlias;
    }

}
