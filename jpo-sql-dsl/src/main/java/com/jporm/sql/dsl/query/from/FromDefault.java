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
package com.jporm.sql.dsl.query.from;

import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public interface FromDefault<JOIN, FROM extends From<JOIN, FROM>> extends From<JOIN, FROM> {

    @Override
    public default void sqlElementValues(final List<Object> values) {
        getFrom().sqlElementValues(values);
    }

    @Override
    public default FROM fullOuterJoin(final JOIN joinTable) {
        return getFrom().fullOuterJoin(joinTable);
    }

    @Override
    public default FROM fullOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        return getFrom().fullOuterJoin(joinTable, joinTableAlias);
    }

    @Override
    public default FROM fullOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        return getFrom().fullOuterJoin(joinTable, onLeftProperty, onRigthProperty);
    }

    @Override
    public default FROM fullOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        return getFrom().fullOuterJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
    }

    @Override
    public default FROM innerJoin(final JOIN joinTable) {
        return getFrom().innerJoin(joinTable);
    }

    @Override
    public default FROM innerJoin(final JOIN joinTable, final String joinTableAlias) {
        return getFrom().fullOuterJoin(joinTable, joinTableAlias);
    }

    @Override
    public default FROM innerJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        return getFrom().fullOuterJoin(joinTable, onLeftProperty, onRigthProperty);
    }

    @Override
    public default FROM innerJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        return getFrom().fullOuterJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
    }

    @Override
    public default FROM join(final JOIN joinTable) {
        return getFrom().join(joinTable);
    }

    @Override
    public default FROM join(final JOIN joinTable, final String joinTableAlias) {
        return getFrom().join(joinTable, joinTableAlias);
    }

    @Override
    public default FROM leftOuterJoin(final JOIN joinTable) {
        return getFrom().leftOuterJoin(joinTable);
    }

    @Override
    public default FROM leftOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        return getFrom().leftOuterJoin(joinTable, joinTableAlias);
    }

    @Override
    public default FROM leftOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        return getFrom().leftOuterJoin(joinTable, onLeftProperty, onRigthProperty);
    }

    @Override
    public default FROM leftOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        return getFrom().leftOuterJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
    }

    @Override
    public default FROM naturalJoin(final JOIN joinTable) {
        return getFrom().naturalJoin(joinTable);
    }

    @Override
    public default FROM naturalJoin(final JOIN joinTable, final String joinTableAlias) {
        return getFrom().naturalJoin(joinTable, joinTableAlias);
    }

    @Override
    public default void sqlElementQuery(final StringBuilder queryBuilder, final DBProfile dbprofile, final PropertiesProcessor propertiesProcessor) {
    	getFrom().sqlElementQuery(queryBuilder, dbprofile, propertiesProcessor);
    }

    @Override
    public default FROM rightOuterJoin(final JOIN joinTable) {
        return getFrom().rightOuterJoin(joinTable);
    }

    @Override
    public default FROM rightOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        return getFrom().rightOuterJoin(joinTable, joinTableAlias);
    }

    @Override
    public default FROM rightOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        return getFrom().rightOuterJoin(joinTable, onLeftProperty, onRigthProperty);
    }

    @Override
    public default FROM rightOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        return getFrom().rightOuterJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
    }

    FROM getFrom();

}
