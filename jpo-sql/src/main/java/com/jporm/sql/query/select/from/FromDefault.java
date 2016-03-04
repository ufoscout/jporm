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

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public interface FromDefault<JOIN, FROM extends From<JOIN, FROM>> extends From<JOIN, FROM>, FromProvider<JOIN, FROM> {

    @Override
    public default void sqlElementValues(final List<Object> values) {
        fromImplementation().sqlElementValues(values);
    }

    @Override
    public default FROM fullOuterJoin(final JOIN joinTable) {
        fromImplementation().fullOuterJoin(joinTable);
        return from();
    }

    @Override
    public default FROM fullOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        fromImplementation().fullOuterJoin(joinTable, joinTableAlias);
        return from();
    }

    @Override
    public default FROM fullOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        fromImplementation().fullOuterJoin(joinTable, onLeftProperty, onRigthProperty);
        return from();
    }

    @Override
    public default FROM fullOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        fromImplementation().fullOuterJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
        return from();
    }

    @Override
    public default FROM innerJoin(final JOIN joinTable) {
        fromImplementation().innerJoin(joinTable);
        return from();
    }

    @Override
    public default FROM innerJoin(final JOIN joinTable, final String joinTableAlias) {
        fromImplementation().innerJoin(joinTable, joinTableAlias);
        return from();
    }

    @Override
    public default FROM innerJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        fromImplementation().innerJoin(joinTable, onLeftProperty, onRigthProperty);
        return from();
    }

    @Override
    public default FROM innerJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        fromImplementation().innerJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
        return from();
    }

    @Override
    public default FROM join(final JOIN joinTable) {
        fromImplementation().join(joinTable);
        return from();
    }

    @Override
    public default FROM join(final JOIN joinTable, final String joinTableAlias) {
        fromImplementation().join(joinTable, joinTableAlias);
        return from();
    }

    @Override
    public default FROM leftOuterJoin(final JOIN joinTable) {
        fromImplementation().leftOuterJoin(joinTable);
        return from();
    }

    @Override
    public default FROM leftOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        fromImplementation().leftOuterJoin(joinTable, joinTableAlias);
        return from();
    }

    @Override
    public default FROM leftOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        fromImplementation().leftOuterJoin(joinTable, onLeftProperty, onRigthProperty);
        return from();
    }

    @Override
    public default FROM leftOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        fromImplementation().leftOuterJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
        return from();
    }

    @Override
    public default FROM naturalJoin(final JOIN joinTable) {
        fromImplementation().naturalJoin(joinTable);
        return from();
    }

    @Override
    public default FROM naturalJoin(final JOIN joinTable, final String joinTableAlias) {
        fromImplementation().naturalJoin(joinTable, joinTableAlias);
        return from();
    }

    @Override
    public default FROM rightOuterJoin(final JOIN joinTable) {
        fromImplementation().rightOuterJoin(joinTable);
        return from();
    }

    @Override
    public default FROM rightOuterJoin(final JOIN joinTable, final String joinTableAlias) {
        fromImplementation().rightOuterJoin(joinTable, joinTableAlias);
        return from();
    }

    @Override
    public default FROM rightOuterJoin(final JOIN joinTable, final String onLeftProperty, final String onRigthProperty) {
        fromImplementation().rightOuterJoin(joinTable, onLeftProperty, onRigthProperty);
        return from();
    }

    @Override
    public default FROM rightOuterJoin(final JOIN joinTable, final String joinTableAlias, final String onLeftProperty, final String onRigthProperty) {
        fromImplementation().rightOuterJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
        return from();
    }

    From<JOIN, ?> fromImplementation();

}
