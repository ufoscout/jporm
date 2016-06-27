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

import com.jporm.sql.query.SqlSubElement;
import com.jporm.sql.query.processor.TableName;
import com.jporm.sql.query.select.SelectCommon;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public interface JoinElement extends SqlSubElement {

    static JoinElement build(final JoinType joinType, final TableName tableName) {
        return new TableJoinElement(joinType, tableName);
    }

    static JoinElement build(final JoinType joinType, final TableName tableName, final String onLeftProperty, final String onRigthProperty) {
        return new TableJoinElement(joinType, tableName, onLeftProperty, onRigthProperty);
    }

    static JoinElement build(final JoinType joinType, final SelectCommon selectCommon, final String alias) {
        return new InnerSelectJoinElement(joinType, selectCommon, alias);
    }

    static JoinElement build(final JoinType joinType, final SelectCommon selectCommon, final String alias, final String onLeftProperty, final String onRigthProperty) {
        return new InnerSelectJoinElement(joinType, selectCommon, alias, onLeftProperty, onRigthProperty);
    }

    JoinType getJoinType();

    boolean hasOnClause();

    String onLeftProperty();

    String onRightProperty();

    void renderJoinTable(StringBuilder query);

    String getAlias();

    boolean hasAlias();

}
