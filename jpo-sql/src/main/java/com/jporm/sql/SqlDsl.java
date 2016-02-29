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

import java.util.function.Supplier;

import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.delete.Delete;
import com.jporm.sql.query.delete.DeleteBuilderImpl;
import com.jporm.sql.query.insert.Insert;
import com.jporm.sql.query.insert.InsertBuilderImpl;
import com.jporm.sql.query.processor.NoOpsStringPropertiesProcessor;
import com.jporm.sql.query.processor.TablePropertiesProcessor;
import com.jporm.sql.query.select.SelectBuilder;
import com.jporm.sql.query.select.SelectBuilderImpl;
import com.jporm.sql.query.update.Update;
import com.jporm.sql.query.update.UpdateBuilderImpl;

public class SqlDsl<T> {

    private static final TablePropertiesProcessor<String> DEFAULT_PROPERTIES_PROCESSOR = new NoOpsStringPropertiesProcessor();
    private final Supplier<TablePropertiesProcessor<T>> propertiesProcessorSupplier;
    private final DBProfile dbProfile;

    public static SqlDsl<String> get(final DBProfile dbProfile) {
        return new SqlDsl<>(dbProfile, () -> DEFAULT_PROPERTIES_PROCESSOR);
    }

    private SqlDsl(final DBProfile dbProfile, Supplier<TablePropertiesProcessor<T>> propertiesProcessorSupplier) {
        this.dbProfile = dbProfile;
        this.propertiesProcessorSupplier = propertiesProcessorSupplier;
    }

    public Delete deleteFrom(T table) {
        return new DeleteBuilderImpl<T>(getDbProfile(), propertiesProcessorSupplier.get()).from(table);
    }

    public Insert insertInto(T table, String... columns) {
        return new InsertBuilderImpl<>(getDbProfile(), columns, propertiesProcessorSupplier.get()).into(table);
    }

    public SelectBuilder<T> selectAll() {
        return select("*");
    }

    public SelectBuilder<T> select(final String... fields) {
        return select(()->fields);
    }

    public SelectBuilder<T> select(final Supplier<String[]> fields) {
        return new SelectBuilderImpl<T>(getDbProfile(), fields, propertiesProcessorSupplier.get());
    }

    public Update update(T table) {
        return new UpdateBuilderImpl<T>(getDbProfile(), propertiesProcessorSupplier.get()).update(table);
    }

    /**
     * @return the dbProfile
     */
    public DBProfile getDbProfile() {
        return dbProfile;
    }

}
