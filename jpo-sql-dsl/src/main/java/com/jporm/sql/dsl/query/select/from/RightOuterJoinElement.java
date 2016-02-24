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
package com.jporm.sql.dsl.query.select.from;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public class RightOuterJoinElement extends AFromElement {

    private final String onLeftProperty;
    private final String onRigthProperty;
    private boolean onClause = true;

    public RightOuterJoinElement(final String table, final String alias) {
        this(table, alias, "", "");
        onClause = false;
    }

    public RightOuterJoinElement(final String table, final String alias, final String onLeftProperty, final String onRigthProperty) {
        super(table, alias);
        this.onLeftProperty = onLeftProperty;
        this.onRigthProperty = onRigthProperty;
    }

    @Override
    protected String getJoinName() {
        return "RIGHT OUTER JOIN "; //$NON-NLS-1$
    }

    @Override
    protected boolean hasOnClause() {
        return onClause;
    }

    @Override
    protected String onLeftProperty() {
        return onLeftProperty;
    }

    @Override
    protected String onRightProperty() {
        return onRigthProperty;
    }

}
