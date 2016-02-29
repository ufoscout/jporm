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
 *          ON : Mar 14, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.commons.core.query.cache;

import java.util.HashMap;
import java.util.Map;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.commons.core.inject.ClassToolMap;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.sql.query.delete.Delete;
import com.jporm.sql.query.delete.where.DeleteWhere;
import com.jporm.sql.query.select.Select;
import com.jporm.sql.query.select.where.SelectWhere;
import com.jporm.sql.query.update.Update;
import com.jporm.sql.query.update.where.UpdateWhere;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Mar 14, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class SqlCacheImpl implements SqlCache {

    private final Map<Class<?>, String> delete = new HashMap<>();
    private final Map<Class<?>, String> update = new HashMap<>();
    private final Map<Class<?>, String> saveWithGenerators = new HashMap<>();
    private final Map<Class<?>, String> saveWithoutGenerators = new HashMap<>();
    private final Map<Class<?>, String> find = new HashMap<>();
    private final Map<Class<?>, String> findRowCount = new HashMap<>();
    private final SqlFactory sqlFactory;
    private final ClassToolMap classToolMap;

    public SqlCacheImpl(SqlFactory sqlFactory, final ClassToolMap classToolMap) {
        this.sqlFactory = sqlFactory;
        this.classToolMap = classToolMap;
    }

    @Override
    public String delete(final Class<?> clazz) {
        return delete.computeIfAbsent(clazz, key -> {
            Delete delete = sqlFactory.deleteFrom(clazz);
            DeleteWhere where = delete.where();
            String[] pks = classToolMap.get(clazz).getDescriptor().getPrimaryKeyColumnJavaNames();
            for (String pk : pks) {
                where.eq(pk, "");
            };
            return delete.sqlQuery();
        });
    }

    @Override
    public Map<Class<?>, String> saveWithGenerators() {
        return saveWithGenerators;
    }

    @Override
    public Map<Class<?>, String> saveWithoutGenerators() {
        return saveWithoutGenerators;
    }

    @Override
    public String update(final Class<?> clazz) {
        return update.computeIfAbsent(clazz, key -> {
            ClassDescriptor<?> descriptor = classToolMap.get(clazz).getDescriptor();
            String[] pkAndVersionFieldNames = descriptor.getPrimaryKeyAndVersionColumnJavaNames();
            String[] notPksFieldNames = descriptor.getNotPrimaryKeyColumnJavaNames();

            Update update = sqlFactory.update(clazz);

            UpdateWhere updateQueryWhere = update.where();
            for (String pkAndVersionFieldName : pkAndVersionFieldNames) {
                updateQueryWhere.eq(pkAndVersionFieldName, "");
            }

            for (String notPksFieldName : notPksFieldNames) {
                update.set(notPksFieldName, "");
            }

            return update.sqlQuery();
        });
    }

    @Override
    public String find(Class<?> clazz) {
        return find.computeIfAbsent(clazz, key -> {
            return getSelect(clazz).sqlQuery();
        });
    }

    @Override
    public String findRowCount(Class<?> clazz) {
        return findRowCount.computeIfAbsent(clazz, key -> {
            return getSelect(clazz).sqlRowCountQuery();
        });
    }

    private final Select<Class<?>> getSelect(Class<?> clazz) {

        ClassDescriptor<?> descriptor = classToolMap.get(clazz).getDescriptor();
        String[] fields = descriptor.getAllColumnJavaNames();

        Select<Class<?>> select = sqlFactory.select(fields).from(clazz);

        SelectWhere where = select.where();
        String[] pks = descriptor.getPrimaryKeyColumnJavaNames();
        for (int i = 0; i < pks.length; i++) {
            where.eq(pks[i], "");
        }
        select.limit(1);
        return select;
    }

}
