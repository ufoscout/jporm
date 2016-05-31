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

import java.util.ArrayList;
import java.util.List;

import com.jporm.sql.query.SqlSubElement;
import com.jporm.sql.query.processor.TableName;
import com.jporm.sql.query.processor.TablePropertiesProcessor;
import com.jporm.sql.query.select.SelectCommon;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public class FromImpl<JOIN, FROM extends From<JOIN, FROM>> implements From<JOIN, FROM>, SqlSubElement {

    private final static String EMPTY_STRING = "";
    private final List<JoinElement> joinElements = new ArrayList<>();
    private final TablePropertiesProcessor<JOIN> nameSolver;
    private final TableName tableName;

    public FromImpl(final TableName tableName, final TablePropertiesProcessor<JOIN> propertiesProcessor) {
        this.tableName = tableName;
        nameSolver = propertiesProcessor;
    }

    private From<JOIN, FROM> addJoinElement(final JoinElement joinElement) {
        joinElements.add(joinElement);
        return this;
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        for (JoinElement joinElement : joinElements) {
            joinElement.sqlElementValues(values);
        }
    }

    @Override
    public final FROM fullOuterJoin(final JOIN joinTable) {
        fullOuterJoin(joinTable, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM fullOuterJoin(final SelectCommon select) {
        fullOuterJoin(select, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM fullOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(JoinElement.build(JoinType.FULL_OUTER_JOIN, nameSolver.getTableName(joinTable, joinTableAlias)));
        return getFrom();
    }


    @Override
    public final FROM fullOuterJoin(final SelectCommon select, final String joinTableAlias) {
        addDynamicAlias(joinTableAlias);
        addJoinElement(JoinElement.build(JoinType.FULL_OUTER_JOIN, select, joinTableAlias));
        return getFrom();
    }

    @Override
    public final FROM fullOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        fullOuterJoin(joinTable, EMPTY_STRING, onLeftProperty, onRigthProperty);
        return getFrom();
    }

    @Override
    public final FROM fullOuterJoin(final SelectCommon select, final String onLeftProperty, final String onRigthProperty) {
        fullOuterJoin(select, EMPTY_STRING, onLeftProperty, onRigthProperty);
        return getFrom();
    }

    @Override
    public final FROM fullOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(JoinElement.build(JoinType.FULL_OUTER_JOIN, nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
        return getFrom();
    }

    @Override
    public final FROM fullOuterJoin(final SelectCommon select, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addDynamicAlias(joinTableAlias);
        addJoinElement(JoinElement.build(JoinType.FULL_OUTER_JOIN, select, joinTableAlias, onLeftProperty, onRigthProperty));
        return getFrom();
    }

    @Override
    public final FROM innerJoin(final JOIN joinTable) {
        innerJoin(joinTable, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM innerJoin(final SelectCommon select) {
        innerJoin(select, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM innerJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(JoinElement.build(JoinType.INNER_JOIN, nameSolver.getTableName(joinTable, joinTableAlias)));
        return getFrom();
    }

    @Override
    public final FROM innerJoin(final SelectCommon select, final String joinTableAlias) {
        addDynamicAlias(joinTableAlias);
        addJoinElement(JoinElement.build(JoinType.INNER_JOIN, select, joinTableAlias));
        return getFrom();
    }

    @Override
    public final FROM innerJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        innerJoin(joinTable, EMPTY_STRING, onLeftProperty, onRigthProperty);
        return getFrom();
    }

    @Override
    public final FROM innerJoin(final SelectCommon select, final String onLeftProperty, final String onRigthProperty) {
        innerJoin(select, EMPTY_STRING, onLeftProperty, onRigthProperty);
        return getFrom();
    }

    @Override
    public final FROM innerJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(JoinElement.build(JoinType.INNER_JOIN, nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
        return getFrom();
    }

    @Override
    public final FROM innerJoin(final SelectCommon select, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addDynamicAlias(joinTableAlias);
        addJoinElement(JoinElement.build(JoinType.INNER_JOIN, select, joinTableAlias, onLeftProperty, onRigthProperty));
        return getFrom();
    }

    @Override
    public final FROM join(final JOIN joinTable) {
        join(joinTable, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM join(final SelectCommon select) {
        join(select, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM join(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(JoinElement.build(JoinType.SIMPLE_JOIN, nameSolver.getTableName(joinTable, joinTableAlias)));
        return getFrom();
    }

    @Override
    public final FROM join(final SelectCommon select, final String joinTableAlias) {
        addDynamicAlias(joinTableAlias);
        addJoinElement(JoinElement.build(JoinType.SIMPLE_JOIN, select, joinTableAlias));
        return getFrom();
    }

    @Override
    public final FROM leftOuterJoin(final JOIN joinTable) {
        leftOuterJoin(joinTable, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM leftOuterJoin(final SelectCommon select) {
        leftOuterJoin(select, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM leftOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(JoinElement.build(JoinType.LEFT_OUTER_JOIN, nameSolver.getTableName(joinTable, joinTableAlias)));
        return getFrom();
    }

    @Override
    public final FROM leftOuterJoin(final SelectCommon select, final String joinTableAlias) {
        addDynamicAlias(joinTableAlias);
        addJoinElement(JoinElement.build(JoinType.LEFT_OUTER_JOIN, select, joinTableAlias));
        return getFrom();
    }

    @Override
    public final FROM leftOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        leftOuterJoin(joinTable, EMPTY_STRING, onLeftProperty, onRigthProperty);
        return getFrom();
    }

    @Override
    public final FROM leftOuterJoin(final SelectCommon select, final String onLeftProperty, final String onRigthProperty) {
        leftOuterJoin(select, EMPTY_STRING, onLeftProperty, onRigthProperty);
        return getFrom();
    }

    @Override
    public final FROM leftOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(JoinElement.build(JoinType.LEFT_OUTER_JOIN, nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
        return getFrom();
    }

    @Override
    public final FROM leftOuterJoin(final SelectCommon select, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addDynamicAlias(joinTableAlias);
        addJoinElement(JoinElement.build(JoinType.LEFT_OUTER_JOIN, select, joinTableAlias, onLeftProperty, onRigthProperty));
        return getFrom();
    }

    @Override
    public final FROM naturalJoin(final JOIN joinTable) {
        naturalJoin(joinTable, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM naturalJoin(final SelectCommon select) {
        naturalJoin(select, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM naturalJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(JoinElement.build(JoinType.NATURAL_JOIN, nameSolver.getTableName(joinTable, joinTableAlias)));
        return getFrom();
    }

    @Override
    public final FROM naturalJoin(final SelectCommon select, final String joinTableAlias) {
        addDynamicAlias(joinTableAlias);
        addJoinElement(JoinElement.build(JoinType.NATURAL_JOIN, select, joinTableAlias));
        return getFrom();
    }

    @Override
    public final FROM rightOuterJoin(final JOIN joinTable) {
        rightOuterJoin(joinTable, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM rightOuterJoin(final SelectCommon select) {
        rightOuterJoin(select, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM rightOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(JoinElement.build(JoinType.RIGHT_OUTER_JOIN, nameSolver.getTableName(joinTable, joinTableAlias)));
        return getFrom();
    }

    @Override
    public final FROM rightOuterJoin(final SelectCommon select, final String joinTableAlias) {
        addDynamicAlias(joinTableAlias);
        addJoinElement(JoinElement.build(JoinType.RIGHT_OUTER_JOIN, select, joinTableAlias));
        return getFrom();
    }

    @Override
    public final FROM rightOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        rightOuterJoin(joinTable, EMPTY_STRING, onLeftProperty, onRigthProperty);
        return getFrom();
    }

    @Override
    public final FROM rightOuterJoin(final SelectCommon select, final String onLeftProperty, final String onRigthProperty) {
        rightOuterJoin(select, EMPTY_STRING, onLeftProperty, onRigthProperty);
        return getFrom();
    }

    @Override
    public final FROM rightOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(JoinElement.build(JoinType.RIGHT_OUTER_JOIN, nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
        return getFrom();
    }

    @Override
    public final FROM rightOuterJoin(final SelectCommon select, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addDynamicAlias(joinTableAlias);
        addJoinElement(JoinElement.build(JoinType.RIGHT_OUTER_JOIN, select, joinTableAlias, onLeftProperty, onRigthProperty));
        return getFrom();
    }

    /**
     * @return the select
     */
    private FROM getFrom() {
        return (FROM) this;
    }

    /**
     * @return the joinElements
     */
    public List<JoinElement> getJoinElements() {
        return joinElements;
    }

    /**
     * @return the tableName
     */
    public TableName getTableName() {
        return tableName;
    }

    private void addDynamicAlias(String joinTableAlias) {
        if (!joinTableAlias.isEmpty()) {
            nameSolver.addDynamicAlias(joinTableAlias);
        }
    }

}
