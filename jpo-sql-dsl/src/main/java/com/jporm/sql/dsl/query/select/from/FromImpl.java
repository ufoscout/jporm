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
import com.jporm.sql.dsl.query.processor.TableName;
import com.jporm.sql.dsl.query.processor.TablePropertiesProcessor;
import com.jporm.sql.dsl.query.select.Select;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public class FromImpl<JOIN> extends ASqlSubElement implements FromProvider<JOIN> {

    private final List<AFromElement> joinElements = new ArrayList<>();
    private final TablePropertiesProcessor<JOIN> nameSolver;
    private final TableName tableName;
    private final Select<JOIN> select;


    public FromImpl(Select<JOIN> select, final TableName tableName, final TablePropertiesProcessor<JOIN> propertiesProcessor) {
        this.tableName = tableName;
        this.select = select;
        nameSolver = propertiesProcessor;
    }

    private FromProvider<JOIN> addJoinElement(final AFromElement joinElement) {
        joinElements.add(joinElement);
        return this;
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        // do nothing
    }

    @Override
    public final Select<JOIN> fullOuterJoin(final JOIN joinTable) {
        fullOuterJoin(joinTable, "");
        return select;
    }

    @Override
    public final Select<JOIN> fullOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(new FullOuterJoinElement(nameSolver.getTableName(joinTable, joinTableAlias)));
        return select;
    }

    @Override
    public final Select<JOIN> fullOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        fullOuterJoin(joinTable, "", onLeftProperty, onRigthProperty);
        return select;
    }

    @Override
    public final Select<JOIN> fullOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(new FullOuterJoinElement(nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
        return select;
    }

    @Override
    public final Select<JOIN> innerJoin(final JOIN joinTable) {
        innerJoin(joinTable, "");
        return select;
    }

    @Override
    public final Select<JOIN> innerJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(new InnerJoinElement(nameSolver.getTableName(joinTable, joinTableAlias)));
        return select;
    }

    @Override
    public final Select<JOIN> innerJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        innerJoin(joinTable, "", onLeftProperty, onRigthProperty);
        return select;
    }

    @Override
    public final Select<JOIN> innerJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(new InnerJoinElement(nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
        return select;
    }

    @Override
    public final Select<JOIN> join(final JOIN joinTable) {
        join(joinTable, "");
        return select;
    }

    @Override
    public final Select<JOIN> join(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(new JoinElement(nameSolver.getTableName(joinTable, joinTableAlias)));
        return select;
    }

    @Override
    public final Select<JOIN> leftOuterJoin(final JOIN joinTable) {
        leftOuterJoin(joinTable, "");
        return select;
    }

    @Override
    public final Select<JOIN> leftOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(new LeftOuterJoinElement(nameSolver.getTableName(joinTable, joinTableAlias)));
        return select;
    }

    @Override
    public final Select<JOIN> leftOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        leftOuterJoin(joinTable, "", onLeftProperty, onRigthProperty);
        return select;
    }

    @Override
    public final Select<JOIN> leftOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(new LeftOuterJoinElement(nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
        return select;
    }

    @Override
    public final Select<JOIN> naturalJoin(final JOIN joinTable) {
        naturalJoin(joinTable, "");
        return select;
    }

    @Override
    public final Select<JOIN> naturalJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(new NaturalJoinElement(nameSolver.getTableName(joinTable, joinTableAlias)));
        return select;
    }

    @Override
    public final void sqlElementQuery(final StringBuilder queryBuilder, final DBProfile dbprofile, final PropertiesProcessor localNameSolver) {
        queryBuilder.append("FROM "); //$NON-NLS-1$
        queryBuilder.append(tableName.getTable());
        queryBuilder.append(" "); //$NON-NLS-1$
        if (tableName.hasAlias()) {
            queryBuilder.append(tableName.getAlias());
            queryBuilder.append(" "); //$NON-NLS-1$
        }
        for (final AFromElement joinElement : joinElements) {
            joinElement.sqlElementQuery(queryBuilder, dbprofile, localNameSolver);
        }
    }

    @Override
    public final Select<JOIN> rightOuterJoin(final JOIN joinTable) {
        rightOuterJoin(joinTable, "");
        return select;
    }

    @Override
    public final Select<JOIN> rightOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(new RightOuterJoinElement(nameSolver.getTableName(joinTable, joinTableAlias)));
        return select;
    }

    @Override
    public final Select<JOIN> rightOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        rightOuterJoin(joinTable, "", onLeftProperty, onRigthProperty);
        return select;
    }

    @Override
    public final Select<JOIN> rightOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(new RightOuterJoinElement(nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
        return select;
    }

}
