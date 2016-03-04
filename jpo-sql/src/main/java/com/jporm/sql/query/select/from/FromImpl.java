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

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public abstract class FromImpl<JOIN, FROM extends From<JOIN, FROM>> implements From<JOIN, FROM>, SqlSubElement {

    private final static String EMPTY_STRING = "";
    private final List<FromElement> joinElements = new ArrayList<>();
    private final TablePropertiesProcessor<JOIN> nameSolver;
    private final TableName tableName;

    public FromImpl(final TableName tableName, final TablePropertiesProcessor<JOIN> propertiesProcessor) {
        this.tableName = tableName;
        nameSolver = propertiesProcessor;
    }

    private From<JOIN, FROM> addJoinElement(final FromElement joinElement) {
        joinElements.add(joinElement);
        return this;
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        // do nothing
    }

    @Override
    public final FROM fullOuterJoin(final JOIN joinTable) {
        fullOuterJoin(joinTable, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM fullOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(new FullOuterJoinElement(nameSolver.getTableName(joinTable, joinTableAlias)));
        return getFrom();
    }

    @Override
    public final FROM fullOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        fullOuterJoin(joinTable, EMPTY_STRING, onLeftProperty, onRigthProperty);
        return getFrom();
    }

    @Override
    public final FROM fullOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(new FullOuterJoinElement(nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
        return getFrom();
    }

    @Override
    public final FROM innerJoin(final JOIN joinTable) {
        innerJoin(joinTable, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM innerJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(new InnerJoinElement(nameSolver.getTableName(joinTable, joinTableAlias)));
        return getFrom();
    }

    @Override
    public final FROM innerJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        innerJoin(joinTable, EMPTY_STRING, onLeftProperty, onRigthProperty);
        return getFrom();
    }

    @Override
    public final FROM innerJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(new InnerJoinElement(nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
        return getFrom();
    }

    @Override
    public final FROM join(final JOIN joinTable) {
        join(joinTable, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM join(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(new SimpleJoinElement(nameSolver.getTableName(joinTable, joinTableAlias)));
        return getFrom();
    }

    @Override
    public final FROM leftOuterJoin(final JOIN joinTable) {
        leftOuterJoin(joinTable, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM leftOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(new LeftOuterJoinElement(nameSolver.getTableName(joinTable, joinTableAlias)));
        return getFrom();
    }

    @Override
    public final FROM leftOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        leftOuterJoin(joinTable, EMPTY_STRING, onLeftProperty, onRigthProperty);
        return getFrom();
    }

    @Override
    public final FROM leftOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(new LeftOuterJoinElement(nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
        return getFrom();
    }

    @Override
    public final FROM naturalJoin(final JOIN joinTable) {
        naturalJoin(joinTable, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM naturalJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(new NaturalJoinElement(nameSolver.getTableName(joinTable, joinTableAlias)));
        return getFrom();
    }

    @Override
    public final FROM rightOuterJoin(final JOIN joinTable) {
        rightOuterJoin(joinTable, EMPTY_STRING);
        return getFrom();
    }

    @Override
    public final FROM rightOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        addJoinElement(new RightOuterJoinElement(nameSolver.getTableName(joinTable, joinTableAlias)));
        return getFrom();
    }

    @Override
    public final FROM rightOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        rightOuterJoin(joinTable, EMPTY_STRING, onLeftProperty, onRigthProperty);
        return getFrom();
    }

    @Override
    public final FROM rightOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        addJoinElement(new RightOuterJoinElement(nameSolver.getTableName(joinTable, joinTableAlias), onLeftProperty, onRigthProperty));
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
    public List<FromElement> getJoinElements() {
        return joinElements;
    }

    /**
     * @return the tableName
     */
    public TableName getTableName() {
        return tableName;
    };

}
