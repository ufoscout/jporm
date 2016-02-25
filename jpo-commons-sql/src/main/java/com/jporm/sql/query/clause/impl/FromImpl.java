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

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.ASqlSubElement;
import com.jporm.sql.query.clause.From;
import com.jporm.sql.query.clause.impl.from.FromElement;
import com.jporm.sql.query.clause.impl.from.FullOuterJoinElement;
import com.jporm.sql.query.clause.impl.from.InnerJoinElement;
import com.jporm.sql.query.clause.impl.from.JoinElement;
import com.jporm.sql.query.clause.impl.from.LeftOuterJoinElement;
import com.jporm.sql.query.clause.impl.from.NaturalJoinElement;
import com.jporm.sql.query.clause.impl.from.RightOuterJoinElement;
import com.jporm.sql.query.namesolver.PropertiesProcessor;
import com.jporm.sql.query.tool.DescriptorToolMap;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public class FromImpl<BEAN> extends ASqlSubElement implements From {

    private final List<FromElement> joinElements = new ArrayList<>();
    private final ClassDescriptor<BEAN> classDescriptor;
    private final PropertiesProcessor nameSolver;
    private final DescriptorToolMap classDescriptorMap;
    private final String normalizedClassAlias;

    public FromImpl(final DescriptorToolMap classDescriptorMap, final Class<BEAN> clazz, final String normalizedClassAlias, final PropertiesProcessor nameSolver) {
        this.classDescriptorMap = classDescriptorMap;
        this.normalizedClassAlias = normalizedClassAlias;
        this.classDescriptor = classDescriptorMap.get(clazz).getDescriptor();
        this.nameSolver = nameSolver;
    }

    private From addJoinElement(final FromElement joinElement) {
        joinElements.add(joinElement);
        return this;
    }

    @Override
    public final void appendElementValues(final List<Object> values) {
        // do nothing
    }

    @Override
    public final <J> From fullOuterJoin(final Class<J> joinClass) {
        return fullOuterJoin(joinClass, joinClass.getSimpleName());
    }

    @Override
    public final <J> From fullOuterJoin(final Class<J> joinClass, final String joinClassAlias) {
        ClassDescriptor<J> joinClassDescriptor = classDescriptorMap.get(joinClass).getDescriptor();
        String normalizedClassAlias = nameSolver.register(joinClass, joinClassAlias, joinClassDescriptor);
        return addJoinElement(new FullOuterJoinElement<>(joinClassDescriptor, joinClass, normalizedClassAlias));
    }

    @Override
    public final <J> From fullOuterJoin(final Class<J> joinClass, final String onLeftProperty, final String onRigthProperty) {
        return fullOuterJoin(joinClass, joinClass.getSimpleName(), onLeftProperty, onRigthProperty);
    }

    @Override
    public final <J> From fullOuterJoin(final Class<J> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
        ClassDescriptor<J> joinClassDescriptor = classDescriptorMap.get(joinClass).getDescriptor();
        String normalizedClassAlias = nameSolver.register(joinClass, joinClassAlias, joinClassDescriptor);
        return addJoinElement(new FullOuterJoinElement<>(joinClassDescriptor, joinClass, normalizedClassAlias, onLeftProperty, onRigthProperty));
    }

    @Override
    public final <J> From innerJoin(final Class<J> joinClass) {
        return innerJoin(joinClass, joinClass.getSimpleName());
    }

    @Override
    public final <J> From innerJoin(final Class<J> joinClass, final String joinClassAlias) {
        String normalizedClassAlias = nameSolver.register(joinClass, joinClassAlias, classDescriptorMap.get(joinClass).getDescriptor());
        return addJoinElement(new InnerJoinElement<>(classDescriptor, joinClass, normalizedClassAlias));
    }

    @Override
    public final <J> From innerJoin(final Class<J> joinClass, final String onLeftProperty, final String onRigthProperty) {
        return innerJoin(joinClass, joinClass.getSimpleName(), onLeftProperty, onRigthProperty);
    }

    @Override
    public final <J> From innerJoin(final Class<J> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
        ClassDescriptor<J> joinClassDescriptor = classDescriptorMap.get(joinClass).getDescriptor();
        String normalizedClassAlias = nameSolver.register(joinClass, joinClassAlias, joinClassDescriptor);
        return addJoinElement(new InnerJoinElement<>(joinClassDescriptor, joinClass, normalizedClassAlias, onLeftProperty, onRigthProperty));
    }

    @Override
    public final <J> From join(final Class<J> joinClass) {
        return join(joinClass, joinClass.getSimpleName());
    }

    @Override
    public final <J> From join(final Class<J> joinClass, final String joinClassAlias) {
        ClassDescriptor<J> joinClassDescriptor = classDescriptorMap.get(joinClass).getDescriptor();
        String normalizedClassAlias = nameSolver.register(joinClass, joinClassAlias, joinClassDescriptor);
        return addJoinElement(new JoinElement<>(joinClassDescriptor, joinClass, normalizedClassAlias));
    }

    @Override
    public final <J> From leftOuterJoin(final Class<J> joinClass) {
        return leftOuterJoin(joinClass, joinClass.getSimpleName());
    }

    @Override
    public final <J> From leftOuterJoin(final Class<J> joinClass, final String joinClassAlias) {
        ClassDescriptor<J> joinClassDescriptor = classDescriptorMap.get(joinClass).getDescriptor();
        String normalizedClassAlias = nameSolver.register(joinClass, joinClassAlias, joinClassDescriptor);
        return addJoinElement(new LeftOuterJoinElement<>(joinClassDescriptor, joinClass, normalizedClassAlias));
    }

    @Override
    public final <J> From leftOuterJoin(final Class<J> joinClass, final String onLeftProperty, final String onRigthProperty) {
        return leftOuterJoin(joinClass, joinClass.getSimpleName(), onLeftProperty, onRigthProperty);
    }

    @Override
    public final <J> From leftOuterJoin(final Class<J> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
        ClassDescriptor<J> joinClassDescriptor = classDescriptorMap.get(joinClass).getDescriptor();
        String normalizedClassAlias = nameSolver.register(joinClass, joinClassAlias, joinClassDescriptor);
        return addJoinElement(new LeftOuterJoinElement<>(joinClassDescriptor, joinClass, normalizedClassAlias, onLeftProperty, onRigthProperty));
    }

    @Override
    public final <J> From naturalJoin(final Class<J> joinClass) {
        return naturalJoin(joinClass, joinClass.getSimpleName());
    }

    @Override
    public final <J> From naturalJoin(final Class<J> joinClass, final String joinClassAlias) {
        ClassDescriptor<J> joinClassDescriptor = classDescriptorMap.get(joinClass).getDescriptor();
        String normalizedClassAlias = nameSolver.register(joinClass, joinClassAlias, joinClassDescriptor);
        return addJoinElement(new NaturalJoinElement<>(joinClassDescriptor, joinClass, normalizedClassAlias));
    }

    @Override
    public final void renderSqlElement(final DBProfile dbprofile, final StringBuilder queryBuilder, final PropertiesProcessor localNameSolver) {
        queryBuilder.append("FROM "); //$NON-NLS-1$
        queryBuilder.append(classDescriptor.getTableInfo().getTableNameWithSchema());
        queryBuilder.append(" "); //$NON-NLS-1$
        queryBuilder.append(normalizedClassAlias);
        queryBuilder.append(" "); //$NON-NLS-1$
        for (final FromElement joinElement : joinElements) {
            joinElement.renderSqlElement(dbprofile, queryBuilder, localNameSolver);
        }
    }

    @Override
    public final <J> From rightOuterJoin(final Class<J> joinClass) {
        return rightOuterJoin(joinClass, joinClass.getSimpleName());
    }

    @Override
    public final <J> From rightOuterJoin(final Class<J> joinClass, final String joinClassAlias) {
        ClassDescriptor<J> joinClassDescriptor = classDescriptorMap.get(joinClass).getDescriptor();
        String normalizedClassAlias = nameSolver.register(joinClass, joinClassAlias, joinClassDescriptor);
        return addJoinElement(new RightOuterJoinElement<>(joinClassDescriptor, joinClass, normalizedClassAlias));
    }

    @Override
    public final <J> From rightOuterJoin(final Class<J> joinClass, final String onLeftProperty, final String onRigthProperty) {
        return rightOuterJoin(joinClass, joinClass.getSimpleName(), onLeftProperty, onRigthProperty);
    }

    @Override
    public final <J> From rightOuterJoin(final Class<J> joinClass, final String joinClassAlias, final String onLeftProperty, final String onRigthProperty) {
        ClassDescriptor<J> joinClassDescriptor = classDescriptorMap.get(joinClass).getDescriptor();
        String normalizedClassAlias = nameSolver.register(joinClass, joinClassAlias, joinClassDescriptor);
        return addJoinElement(new RightOuterJoinElement<>(joinClassDescriptor, joinClass, normalizedClassAlias, onLeftProperty, onRigthProperty));
    }

}
