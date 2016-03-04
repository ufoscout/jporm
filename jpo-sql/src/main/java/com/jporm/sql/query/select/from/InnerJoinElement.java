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
public class InnerJoinElement implements FromElement {

    private static final String INNER_JOIN = "INNER JOIN ";
    private static final String EMPTY_STRING = "";
    private final String onLeftProperty;
    private final String onRigthProperty;
    private boolean onClause = true;
    private final TableName tableName;

    public InnerJoinElement(final TableName tableName) {
        this(tableName, EMPTY_STRING, EMPTY_STRING);
        onClause = false;
    }

    public InnerJoinElement(final TableName tableName, final String onLeftProperty, final String onRigthProperty) {
        this.tableName = tableName;
        this.onLeftProperty = onLeftProperty;
        this.onRigthProperty = onRigthProperty;
    }

    @Override
    public String getJoinName() {
        return INNER_JOIN; //$NON-NLS-1$
    }

    @Override
    public boolean hasOnClause() {
        return onClause;
    }

    @Override
    public String onLeftProperty() {
        return onLeftProperty;
    }

    @Override
    public String onRightProperty() {
        return onRigthProperty;
    }

    /**
     * @return the tableName
     */
    @Override
    public TableName getTableName() {
        return tableName;
    }

}
