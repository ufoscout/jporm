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
 *          ON : Feb 13, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.core.mapper.relation;

import com.jporm.annotation.cascade.CascadeInfo;
import com.jporm.core.mapper.clazz.ClassField;
import com.jporm.core.persistor.reflection.GetManipulator;
import com.jporm.core.persistor.reflection.SetManipulator;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 13, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class RelationOuterFKImpl<BEAN, P, MANIPULATOR> implements RelationOuterFK<BEAN, P, MANIPULATOR> {

    private final Class<P> relationWithClass;
    private final String javaFieldName;
    private GetManipulator<BEAN, MANIPULATOR> getManipulator;
    private SetManipulator<BEAN, MANIPULATOR> setManipulator;
    private final ClassField<P, Object> relationClassField;
    private final boolean oneToMany;
    private final CascadeInfo cascadeInfo;

    public RelationOuterFKImpl(final Class<P> relationWithClass, final ClassField<P, Object> relationClassField, final String javaFieldName, final boolean oneToMany, final CascadeInfo cascadeInfo) {
        this.relationWithClass = relationWithClass;
        this.relationClassField = relationClassField;
        this.javaFieldName = javaFieldName;
        this.oneToMany = oneToMany;
        this.cascadeInfo = cascadeInfo;
    }

    @Override
    public Class<P> getRelationWithClass() {
        return relationWithClass;
    }

    @Override
    public String getJavaFieldName() {
        return javaFieldName;
    }

    @Override
    public GetManipulator<BEAN, MANIPULATOR> getGetManipulator() {
        return getManipulator;
    }

    public void setGetManipulator(final GetManipulator<BEAN, MANIPULATOR> getManipulator) {
        this.getManipulator = getManipulator;
    }

    @Override
    public SetManipulator<BEAN, MANIPULATOR> getSetManipulator() {
        return setManipulator;
    }

    public void setSetManipulator(final SetManipulator<BEAN, MANIPULATOR> setManipulator) {
        this.setManipulator = setManipulator;
    }

    @Override
    public void copyFromTo(final BEAN source, final BEAN destination) {
        getSetManipulator().setValue(destination,  getGetManipulator().getValue(source) );
    }

    @Override
    public ClassField<P, Object> getRelationClassField() {
        return relationClassField;
    }

    @Override
    public boolean isOneToMany() {
        return oneToMany;
    }

    /**
     * @return the cascadeInfo
     */
    @Override
    public CascadeInfo getCascadeInfo() {
        return cascadeInfo;
    }

}
