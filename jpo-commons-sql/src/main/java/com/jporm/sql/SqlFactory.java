/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.sql;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.sql.query.clause.Delete;
import com.jporm.sql.query.clause.Insert;
import com.jporm.sql.query.clause.Select;
import com.jporm.sql.query.clause.Update;
import com.jporm.sql.query.clause.impl.DeleteImpl;
import com.jporm.sql.query.clause.impl.InsertImpl;
import com.jporm.sql.query.clause.impl.SelectImpl;
import com.jporm.sql.query.clause.impl.UpdateImpl;
import com.jporm.sql.query.namesolver.impl.NameSolverImpl;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;
import com.jporm.sql.query.tool.DescriptorToolMap;

public class SqlFactory {

    private final PropertiesFactory propertiesFactory;
    private final DescriptorToolMap classDescriptorMap;

    public SqlFactory(final DescriptorToolMap classDescriptorMap, final PropertiesFactory propertiesFactory) {
        this.classDescriptorMap = classDescriptorMap;
        this.propertiesFactory = propertiesFactory;
    }

    public <BEAN> Delete delete(final Class<BEAN> clazz) {
        NameSolverImpl nameSolver = new NameSolverImpl(propertiesFactory, true);
        return new DeleteImpl(clazz, nameSolver, aclazz -> {
            ClassDescriptor<BEAN> classDescriptor = classDescriptorMap.get(aclazz).getDescriptor();
            nameSolver.register(aclazz, aclazz.getSimpleName(), classDescriptor);
            return classDescriptor.getTableInfo().getTableNameWithSchema();
        });
    }

    public <BEAN> Insert insert(final Class<BEAN> clazz, final String[] fields) {
        ClassDescriptor<BEAN> classDescriptor = classDescriptorMap.get(clazz).getDescriptor();
        NameSolverImpl nameSolver = new NameSolverImpl(propertiesFactory, true);
        nameSolver.register(clazz, clazz.getSimpleName(), classDescriptor);
        String table = classDescriptor.getTableInfo().getTableNameWithSchema();
        return new InsertImpl(classDescriptor, nameSolver, table, fields);
    }

    public <BEAN> Select select(final Class<BEAN> clazz) {
        return new SelectImpl<BEAN>(classDescriptorMap, propertiesFactory, clazz);
    }

    public <BEAN> Select select(final Class<BEAN> clazz, final String alias) {
        return new SelectImpl<BEAN>(classDescriptorMap, propertiesFactory, clazz, alias);
    }

    public <BEAN> Update update(final Class<BEAN> clazz) {
        ClassDescriptor<BEAN> classDescriptor = classDescriptorMap.get(clazz).getDescriptor();
        NameSolverImpl nameSolver = new NameSolverImpl(propertiesFactory, true);
        nameSolver.register(clazz, clazz.getSimpleName(), classDescriptor);
        String table = classDescriptor.getTableInfo().getTableNameWithSchema();
        return new UpdateImpl(nameSolver, table);
    }

}
