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
package com.jporm.sql.query.select.from;

import com.jporm.sql.query.processor.TableName;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public class SimpleJoinElement implements FromElement {

    private static final String EMPTY_STRING = "";
    private static final String JOIN_NAME = ", ";
    private final TableName tableName;

    public SimpleJoinElement(final TableName tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getJoinName() {
        return JOIN_NAME; 
    }

    @Override
    public boolean hasOnClause() {
        return false;
    }

    @Override
    public String onLeftProperty() {
        return EMPTY_STRING; 
    }

    @Override
    public String onRightProperty() {
        return EMPTY_STRING; 
    }

    @Override
    public TableName getTableName() {
        return tableName;
    }

}
