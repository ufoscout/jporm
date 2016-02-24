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

import com.jporm.annotation.mapper.clazz.ClassDescriptor;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public class JoinElement<BEAN> extends AFromElement<BEAN> {

    public JoinElement(final ClassDescriptor<BEAN> classDescriptor, final Class<?> joinClass, final String normalizedClassAlias) {
        super(classDescriptor, joinClass, normalizedClassAlias);
    }

    @Override
    protected String getJoinName() {
        return ", "; //$NON-NLS-1$
    }

    @Override
    protected boolean hasOnClause() {
        return false;
    }

    @Override
    protected String onLeftProperty() {
        return ""; //$NON-NLS-1$
    }

    @Override
    protected String onRightProperty() {
        return ""; //$NON-NLS-1$
    }

}
