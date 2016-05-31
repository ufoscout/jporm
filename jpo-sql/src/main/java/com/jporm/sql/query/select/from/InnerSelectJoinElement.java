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

import java.util.List;

import com.jporm.sql.query.select.SelectCommon;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public class InnerSelectJoinElement implements JoinElement {

    private static final String CLOSE = ")";
    private static final String OPEN = "(";
    private static final String EMPTY_STRING = "";
    private final String onLeftProperty;
    private final String onRigthProperty;
    private boolean onClause = true;
    private final SelectCommon selectCommon;
    private final String alias;
    private final boolean hasAlias;
    private final JoinType joinType;

    public InnerSelectJoinElement(final JoinType joinType, final SelectCommon selectCommon, final String alias) {
        this(joinType, selectCommon, alias, EMPTY_STRING, EMPTY_STRING);
        onClause = false;
    }

    public InnerSelectJoinElement(final JoinType joinType, final SelectCommon selectCommon, final String alias, final String onLeftProperty, final String onRigthProperty) {
        this.joinType = joinType;
        this.selectCommon = selectCommon;
        this.onLeftProperty = onLeftProperty;
        this.onRigthProperty = onRigthProperty;
        this.alias = alias;
        hasAlias = !alias.isEmpty();
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

    @Override
    public void sqlElementValues(final List<Object> values) {
        selectCommon.sqlValues(values);
    }

    @Override
    public void renderJoinTable(StringBuilder query) {
        query.append(OPEN);
        selectCommon.sqlQuery(query);
        query.append(CLOSE);
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public boolean hasAlias() {
        return hasAlias;
    }

    @Override
    public JoinType getJoinType() {
        return joinType;
    }

}
