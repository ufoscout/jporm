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
package com.jporm.commons.core.query;

import java.util.function.Supplier;

import com.jporm.commons.core.inject.ClassToolMap;
import com.jporm.commons.core.query.processor.ClassTablePropertiesProcessor;
import com.jporm.commons.core.query.processor.PropertiesFactory;
import com.jporm.sql.dialect.SqlRender;
import com.jporm.sql.query.delete.Delete;
import com.jporm.sql.query.delete.DeleteBuilderImpl;
import com.jporm.sql.query.insert.Insert;
import com.jporm.sql.query.insert.InsertBuilderImpl;
import com.jporm.sql.query.select.SelectBuilder;
import com.jporm.sql.query.select.SelectBuilderImpl;
import com.jporm.sql.query.update.Update;
import com.jporm.sql.query.update.UpdateBuilderImpl;

public class SqlFactory {

    private final PropertiesFactory propertiesFactory;
    private final ClassToolMap classDescriptorMap;
    private final SqlRender sqlRender;

    public SqlFactory(final ClassToolMap classDescriptorMap, final PropertiesFactory propertiesFactory, SqlRender sqlRender) {
        this.classDescriptorMap = classDescriptorMap;
        this.propertiesFactory = propertiesFactory;
        this.sqlRender = sqlRender;
    }

    public Delete deleteFrom(Class<?> table) {
        ClassTablePropertiesProcessor nameSolver = new ClassTablePropertiesProcessor(classDescriptorMap, propertiesFactory, true);
        return new DeleteBuilderImpl<Class<?>>(sqlRender.getDeleteRender(), nameSolver).from(table);
    }

    public Insert insertInto(Class<?> table, String[] columns) {
        ClassTablePropertiesProcessor nameSolver = new ClassTablePropertiesProcessor(classDescriptorMap, propertiesFactory, true);
        return new InsertBuilderImpl<>(sqlRender.getInsertRender(), columns, nameSolver).into(table);
    }

    public SelectBuilder<Class<?>> select(final Supplier<String[]> fields) {
        ClassTablePropertiesProcessor nameSolver = new ClassTablePropertiesProcessor(classDescriptorMap, propertiesFactory, false);
        return new SelectBuilderImpl<>(sqlRender.getSelectRender(), fields, nameSolver);
    }

    public Update update(Class<?> table) {
        ClassTablePropertiesProcessor nameSolver = new ClassTablePropertiesProcessor(classDescriptorMap, propertiesFactory, true);
        return new UpdateBuilderImpl<Class<?>>(sqlRender.getUpdateRender(), nameSolver).update(table);
    }

}
