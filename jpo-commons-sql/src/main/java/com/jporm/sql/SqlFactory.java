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
import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.delete.Delete;
import com.jporm.sql.dsl.query.delete.DeleteBuilderImpl;
import com.jporm.sql.dsl.query.select.SelectBuilder;
import com.jporm.sql.dsl.query.select.SelectBuilderImpl;
import com.jporm.sql.dsl.query.update.Update;
import com.jporm.sql.dsl.query.update.UpdateBuilderImpl;
import com.jporm.sql.query.clause.impl.InsertImpl;
import com.jporm.sql.query.namesolver.impl.NameSolverImpl;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;
import com.jporm.sql.query.tool.DescriptorToolMap;

public class SqlFactory {

    private final PropertiesFactory propertiesFactory;
    private final DescriptorToolMap classDescriptorMap;
    private final DBProfile dbProfile;

    public SqlFactory(final DescriptorToolMap classDescriptorMap, final PropertiesFactory propertiesFactory, DBProfile dbProfile) {
        this.classDescriptorMap = classDescriptorMap;
        this.propertiesFactory = propertiesFactory;
        this.dbProfile = dbProfile;
    }

    public Delete deleteFrom(Class<?> table) {
        NameSolverImpl nameSolver = new NameSolverImpl(classDescriptorMap, propertiesFactory, true);
        return new DeleteBuilderImpl<Class<?>>(dbProfile, nameSolver).from(table);
    }

//    public Insert insertInto(Class<?> table, String... columns) {
//        NameSolverImpl nameSolver = new NameSolverImpl(classDescriptorMap, propertiesFactory, true);
//        return new InsertBuilderImpl<>(dbProfile, columns, nameSolver).into(table);
//    }

    @Deprecated
    public <BEAN> InsertImpl legacyInsert(final Class<BEAN> clazz, final String[] fields) {
        ClassDescriptor<BEAN> classDescriptor = classDescriptorMap.get(clazz).getDescriptor();
        NameSolverImpl nameSolver = new NameSolverImpl(classDescriptorMap, propertiesFactory, true);
        String table = nameSolver.getTableName(clazz).getTable();
        return new InsertImpl(dbProfile, classDescriptor, nameSolver, table, fields);
    }

    public SelectBuilder<Class<?>> selectAll() {
        return select("*");
    }

    public SelectBuilder<Class<?>> select(final String... fields) {
        NameSolverImpl nameSolver = new NameSolverImpl(classDescriptorMap, propertiesFactory, false);
        return new SelectBuilderImpl<>(dbProfile, fields, nameSolver);
    }

    public Update update(Class<?> table) {
        NameSolverImpl nameSolver = new NameSolverImpl(classDescriptorMap, propertiesFactory, true);
        return new UpdateBuilderImpl<Class<?>>(dbProfile, nameSolver).update(table);
    }

}
