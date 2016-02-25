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

import java.util.ArrayList;
import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.ASqlSubElement;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;
import com.jporm.sql.dsl.query.select.Select;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public class FromImpl extends ASqlSubElement implements FromProvider {

    private final List<AFromElement> joinElements = new ArrayList<>();
    private final String fromTable;
    private final String fromTableAlias;
    private final Select select;

    public FromImpl(Select select, String fromTable, String fromTableAlias) {
        this.select = select;
        this.fromTable = fromTable;
        this.fromTableAlias = fromTableAlias;
    }

    private FromProvider addJoinElement(final AFromElement joinElement) {
        joinElements.add(joinElement);
        return this;
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        // do nothing
    }

    @Override
    public final Select fullOuterJoin(final String joinTable) {
        fullOuterJoin(joinTable, "");
        return select;
    }

    @Override
    public final Select fullOuterJoin(final String joinTable, final String joinTableAlias) {
        addJoinElement(new FullOuterJoinElement(joinTable, joinTableAlias));
        return select;
    }

    @Override
    public final Select fullOuterJoin(final String joinTable, final String onLeftProperty, final String onRigthProperty) {
        fullOuterJoin(joinTable, "", onLeftProperty, onRigthProperty);
        return select;
    }

    @Override
    public final Select fullOuterJoin(final String joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(new FullOuterJoinElement(joinTable, joinTableAlias, onLeftProperty, onRigthProperty));
        return select;
    }

    @Override
    public final Select innerJoin(final String joinTable) {
        innerJoin(joinTable, "");
        return select;
    }

    @Override
    public final Select innerJoin(final String joinTable, final String joinTableAlias) {
        addJoinElement(new InnerJoinElement(joinTable, joinTableAlias));
        return select;
    }

    @Override
    public final Select innerJoin(final String joinTable, final String onLeftProperty, final String onRigthProperty) {
        innerJoin(joinTable, "", onLeftProperty, onRigthProperty);
        return select;
    }

    @Override
    public final Select innerJoin(final String joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(new InnerJoinElement(joinTable, joinTableAlias, onLeftProperty, onRigthProperty));
        return select;
    }

    @Override
    public final Select join(final String joinTable) {
        join(joinTable, "");
        return select;
    }

    @Override
    public final Select join(final String joinTable, final String joinTableAlias) {
        addJoinElement(new JoinElement(joinTable, joinTableAlias));
        return select;
    }

    @Override
    public final Select leftOuterJoin(final String joinTable) {
        leftOuterJoin(joinTable, "");
        return select;
    }

    @Override
    public final Select leftOuterJoin(final String joinTable, final String joinTableAlias) {
        addJoinElement(new LeftOuterJoinElement(joinTable, joinTableAlias));
        return select;
    }

    @Override
    public final Select leftOuterJoin(final String joinTable, final String onLeftProperty, final String onRigthProperty) {
        leftOuterJoin(joinTable, "", onLeftProperty, onRigthProperty);
        return select;
    }

    @Override
    public final Select leftOuterJoin(final String joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(new LeftOuterJoinElement(joinTable, joinTableAlias, onLeftProperty, onRigthProperty));
        return select;
    }

    @Override
    public final Select naturalJoin(final String joinTable) {
        naturalJoin(joinTable, "");
        return select;
    }

    @Override
    public final Select naturalJoin(final String joinTable, final String joinTableAlias) {
        addJoinElement(new NaturalJoinElement(joinTable, joinTableAlias));
        return select;
    }

    @Override
    public final void sqlElementQuery(final StringBuilder queryBuilder, DBProfile dbProfile, PropertiesProcessor propertiesProcessor) {
        queryBuilder.append("FROM "); //$NON-NLS-1$
        queryBuilder.append(fromTable);
        if (!fromTableAlias.isEmpty()) {
            queryBuilder.append(" "); //$NON-NLS-1$
            queryBuilder.append(fromTableAlias);
        }
        queryBuilder.append(" "); //$NON-NLS-1$
        for (final AFromElement joinElement : joinElements) {
            joinElement.sqlElementQuery(queryBuilder, dbProfile, propertiesProcessor);
        }
    }

    @Override
    public final Select rightOuterJoin(final String joinTable) {
        rightOuterJoin(joinTable, "");
        return select;
    }

    @Override
    public final Select rightOuterJoin(final String joinTable, final String joinTableAlias) {
        addJoinElement(new RightOuterJoinElement(joinTable, joinTableAlias));
        return select;
    }

    @Override
    public final Select rightOuterJoin(final String joinTable, final String onLeftProperty, final String onRigthProperty) {
        rightOuterJoin(joinTable, "", onLeftProperty, onRigthProperty);
        return select;
    }

    @Override
    public final Select rightOuterJoin(final String joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(new RightOuterJoinElement(joinTable, joinTableAlias, onLeftProperty, onRigthProperty));
        return select;
    }

}
