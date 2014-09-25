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
package com.jporm.core.persistor.type;

import com.jporm.wrapper.TypeWrapper;
import com.jporm.wrapper.TypeWrapperBuilder;

/**
 * <class_description> 
 * <p><b>notes</b>:
 * <p>ON : Nov 21, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class TypeWrapperBuilderDefault<P, DB> implements
        TypeWrapperBuilder<P, DB> {

    private final TypeWrapper<P, DB> typeWrapper;

    public TypeWrapperBuilderDefault(TypeWrapper<P, DB> typeWrapper) {
        this.typeWrapper = typeWrapper;
    }
    
    @Override
    public Class<DB> jdbcType() {
        return typeWrapper.jdbcType();
    }

    @Override
    public Class<P> propertyType() {
        return typeWrapper.propertyType();
    }

    @Override
    public TypeWrapper<P, DB> build(Class<P> pClass) {
        return typeWrapper;
    }

}
