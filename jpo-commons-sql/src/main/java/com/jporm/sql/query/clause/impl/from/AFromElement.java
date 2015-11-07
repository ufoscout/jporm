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
package com.jporm.sql.query.clause.impl.from;

import java.util.List;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.ASqlSubElement;
import com.jporm.sql.query.namesolver.NameSolver;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public abstract class AFromElement<BEAN> extends ASqlSubElement implements FromElement {

    protected final Class<?> joinClass;
    private final Integer nameSolverClassId;
    private final ClassDescriptor<BEAN> classDescriptor;

    public AFromElement(final ClassDescriptor<BEAN> classDescriptor, final Class<?> joinClass, final Integer nameSolverClassId) {
        this.classDescriptor = classDescriptor;
        this.joinClass = joinClass;
        this.nameSolverClassId = nameSolverClassId;
    }

    @Override
    public final void appendElementValues(final List<Object> values) {
        // do nothing
    }

    protected abstract String getJoinName();

    public Integer getNameSolverClassId() {
        return nameSolverClassId;
    }

    protected abstract boolean hasOnClause();

    protected abstract String onLeftProperty();

    protected abstract String onRightProperty();

    @Override
    public final void renderSqlElement(final DBProfile dbProfile, final StringBuilder queryBuilder, final NameSolver nameSolver) {
        String alias = nameSolver.normalizedAlias(getNameSolverClassId());
        queryBuilder.append(getJoinName());
        queryBuilder.append(classDescriptor.getTableInfo().getTableNameWithSchema());
        queryBuilder.append(" "); //$NON-NLS-1$
        queryBuilder.append(alias);

        if (hasOnClause()) {
            queryBuilder.append(" ON "); //$NON-NLS-1$
            queryBuilder.append(nameSolver.solvePropertyName(onLeftProperty()));
            queryBuilder.append(" = "); //$NON-NLS-1$
            queryBuilder.append(nameSolver.solvePropertyName(onRightProperty()));
        }

        queryBuilder.append(" "); //$NON-NLS-1$
    }

}
