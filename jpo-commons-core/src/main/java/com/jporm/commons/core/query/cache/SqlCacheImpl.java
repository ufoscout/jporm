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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.commons.core.inject.ClassTool;
import com.jporm.commons.core.inject.ClassToolMap;
import com.jporm.commons.core.inject.ExtendedFieldDescriptor;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.delete.Delete;
import com.jporm.sql.query.delete.where.DeleteWhere;
import com.jporm.sql.query.insert.Insert;
import com.jporm.sql.query.insert.values.Generator;
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

    private final Map<Class<?>, String> delete = new ConcurrentHashMap<>();
    private final Map<Class<?>, String> update = new ConcurrentHashMap<>();
    private final Map<Class<?>, String> saveWithGenerators = new ConcurrentHashMap<>();
    private final Map<Class<?>, String> saveWithoutGenerators = new ConcurrentHashMap<>();
    private final Map<Class<?>, String> find = new ConcurrentHashMap<>();
    private final Map<Class<?>, String> findRowCount = new ConcurrentHashMap<>();
    private final SqlFactory sqlFactory;
    private final ClassToolMap classToolMap;
    private final DBProfile dbProfile;

    public SqlCacheImpl(SqlFactory sqlFactory, final ClassToolMap classToolMap, DBProfile dbProfile) {
        this.sqlFactory = sqlFactory;
        this.classToolMap = classToolMap;
        this.dbProfile = dbProfile;
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

        Select<Class<?>> select = sqlFactory.select(()->fields).from(clazz);

        SelectWhere where = select.where();
        String[] pks = descriptor.getPrimaryKeyColumnJavaNames();
        for (int i = 0; i < pks.length; i++) {
            where.eq(pks[i], "");
        }
        select.limit(1);
        return select;
    }

    @Override
    public String saveWithGenerators(Class<?> clazz) {
        return saveWithGenerators.computeIfAbsent(clazz, key -> {
            ClassTool<?> classTool = classToolMap.get(clazz);

            String[] fields = classTool.getDescriptor().getAllColumnJavaNames();

            List<String> neededFields = new ArrayList<>();
            List<Generator> values = new ArrayList<>();

            for (String field : fields) {
                ExtendedFieldDescriptor<?, Object> fieldDescriptor = classTool.getFieldDescriptorByJavaName(field);
                Generator generator = fieldDescriptor.getGenerator(dbProfile).getGenerator();
                if (generator.isRequiredColumnNameInInsertQuery()) {
                    neededFields.add(field);
                    values.add(generator);
                }
            }

            Insert insert = sqlFactory.insertInto(clazz, neededFields.toArray(new String[0]));
            insert.values(values.toArray());
            return insert.sqlQuery();
        });
    }

    @Override
    public String saveWithoutGenerators(Class<?> clazz) {
        return saveWithoutGenerators.computeIfAbsent(clazz, key -> {
            ClassTool<?> classTool = classToolMap.get(clazz);
            String[] fields = classTool.getDescriptor().getAllColumnJavaNames();
            Insert insert = sqlFactory.insertInto(clazz, fields);
            insert.values(new Object[fields.length]);
            return insert.sqlQuery();
        });
    }

}
