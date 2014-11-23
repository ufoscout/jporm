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
package com.jporm.deprecated.core.mapper;

import com.jporm.core.persistor.OrmPersistor;
import com.jporm.deprecated.core.mapper.clazz.ClassMap;


/**
 * 
 * @author Francesco Cina
 *
 * 22/mag/2011
 */
public class OrmClassToolImpl<BEAN> implements OrmClassTool<BEAN> {

    private final ClassMap<BEAN> classMapper;
    private final OrmPersistor<BEAN> ormPersistor;

    public OrmClassToolImpl(final ClassMap<BEAN> classMapper, final OrmPersistor<BEAN> ormPersistor) {
        this.classMapper = classMapper;
        this.ormPersistor = ormPersistor;
    }

    @Override
    public ClassMap<BEAN> getClassMap() {
        return this.classMapper;
    }

    @Override
    public OrmPersistor<BEAN> getOrmPersistor() {
        return this.ormPersistor;
    }

}
