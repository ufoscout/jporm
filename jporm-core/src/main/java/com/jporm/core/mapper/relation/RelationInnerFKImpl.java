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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Mar 3, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.core.mapper.relation;

import com.jporm.annotation.cascade.CascadeInfo;
import com.jporm.core.mapper.clazz.ClassFieldImpl;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 3, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class RelationInnerFKImpl<BEAN, P> extends ClassFieldImpl<BEAN, P> implements RelationInnerFK<BEAN, P> {

    private final CascadeInfo cascadeInfo;

    public RelationInnerFKImpl(final Class<P> type, final String fieldName, final CascadeInfo cascadeInfo) {
        super(type, fieldName);
        this.cascadeInfo = cascadeInfo;
    }

    /**
     * @return the cascadeInfo
     */
    @Override
    public CascadeInfo getCascadeInfo() {
        return cascadeInfo;
    }

}
