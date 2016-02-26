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
package com.jporm.sql.query.clause.impl;

import java.util.ArrayList;
import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;
import com.jporm.sql.dsl.query.processor.TableName;
import com.jporm.sql.dsl.query.processor.TablePropertiesProcessor;
import com.jporm.sql.query.ASqlSubElement;
import com.jporm.sql.query.clause.From;
import com.jporm.sql.query.clause.impl.from.FromElement;
import com.jporm.sql.query.clause.impl.from.FullOuterJoinElement;
import com.jporm.sql.query.clause.impl.from.InnerJoinElement;
import com.jporm.sql.query.clause.impl.from.JoinElement;
import com.jporm.sql.query.clause.impl.from.LeftOuterJoinElement;
import com.jporm.sql.query.clause.impl.from.NaturalJoinElement;
import com.jporm.sql.query.clause.impl.from.RightOuterJoinElement;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public class FromImpl<JOIN> extends ASqlSubElement implements From<JOIN> {

    private final List<FromElement> joinElements = new ArrayList<>();
    private final TablePropertiesProcessor<JOIN> nameSolver;
    private final TableName tableName;


    public FromImpl(final TableName tableName, final TablePropertiesProcessor<JOIN> propertiesProcessor) {
        this.tableName = tableName;
        nameSolver = propertiesProcessor;
    }

    private From<JOIN> addJoinElement(final FromElement joinElement) {
        joinElements.add(joinElement);
        return this;
    }

    @Override
    public final void appendElementValues(final List<Object> values) {
        // do nothing
    }

    @Override
    public final From<JOIN> fullOuterJoin(final JOIN joinTable) {
        return fullOuterJoin(joinTable, "");
    }

    @Override
    public final From<JOIN> fullOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        return addJoinElement(new FullOuterJoinElement<>(nameSolver.getTableName(joinTable, joinTableAlias)));
    }

    @Override
    public final From<JOIN> fullOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        return fullOuterJoin(joinTable, "", onLeftProperty, onRigthProperty);
    }

    @Override
    public final From<JOIN> fullOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        return addJoinElement(new FullOuterJoinElement<>(nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
    }

    @Override
    public final From<JOIN> innerJoin(final JOIN joinTable) {
        return innerJoin(joinTable, "");
    }

    @Override
    public final From<JOIN> innerJoin(final JOIN joinTable, final String joinTableAlias) {
        return addJoinElement(new InnerJoinElement<>(nameSolver.getTableName(joinTable, joinTableAlias)));
    }

    @Override
    public final From<JOIN> innerJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        return innerJoin(joinTable, "", onLeftProperty, onRigthProperty);
    }

    @Override
    public final From<JOIN> innerJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        return addJoinElement(new InnerJoinElement<>(nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
    }

    @Override
    public final From<JOIN> join(final JOIN joinTable) {
        return join(joinTable, "");
    }

    @Override
    public final From<JOIN> join(final JOIN joinTable, final String joinTableAlias) {
        return addJoinElement(new JoinElement<>(nameSolver.getTableName(joinTable, joinTableAlias)));
    }

    @Override
    public final From<JOIN> leftOuterJoin(final JOIN joinTable) {
        return leftOuterJoin(joinTable, "");
    }

    @Override
    public final From<JOIN> leftOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        return addJoinElement(new LeftOuterJoinElement<>(nameSolver.getTableName(joinTable, joinTableAlias)));
    }

    @Override
    public final From<JOIN> leftOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        return leftOuterJoin(joinTable, "", onLeftProperty, onRigthProperty);
    }

    @Override
    public final From<JOIN> leftOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        return addJoinElement(new LeftOuterJoinElement<>(nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
    }

    @Override
    public final From<JOIN> naturalJoin(final JOIN joinTable) {
        return naturalJoin(joinTable, "");
    }

    @Override
    public final From<JOIN> naturalJoin(final JOIN joinTable, final String joinTableAlias) {
        return addJoinElement(new NaturalJoinElement<>(nameSolver.getTableName(joinTable, joinTableAlias)));
    }

    @Override
    public final void renderSqlElement(final DBProfile dbprofile, final StringBuilder queryBuilder, final PropertiesProcessor localNameSolver) {
        queryBuilder.append("FROM "); //$NON-NLS-1$
        queryBuilder.append(tableName.getTable());
        queryBuilder.append(" "); //$NON-NLS-1$
        if (tableName.hasAlias()) {
            queryBuilder.append(tableName.getAlias());
            queryBuilder.append(" "); //$NON-NLS-1$
        }
        for (final FromElement joinElement : joinElements) {
            joinElement.renderSqlElement(dbprofile, queryBuilder, localNameSolver);
        }
    }

    @Override
    public final From<JOIN> rightOuterJoin(final JOIN joinTable) {
        return rightOuterJoin(joinTable, "");
    }

    @Override
    public final From<JOIN> rightOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        return addJoinElement(new RightOuterJoinElement<>(nameSolver.getTableName(joinTable, joinTableAlias)));
    }

    @Override
    public final From<JOIN> rightOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        return rightOuterJoin(joinTable, "", onLeftProperty, onRigthProperty);
    }

    @Override
    public final From<JOIN> rightOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        return addJoinElement(new RightOuterJoinElement<>(nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
    }

}
