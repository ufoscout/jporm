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
package com.jporm.sql.dsl;

import java.util.function.Supplier;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.delete.Delete;
import com.jporm.sql.dsl.query.delete.DeleteBuilderImpl;
import com.jporm.sql.dsl.query.insert.Insert;
import com.jporm.sql.dsl.query.insert.InsertBuilderImpl;
import com.jporm.sql.dsl.query.processor.NoOpsPropertiesProcessor;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;
import com.jporm.sql.dsl.query.select.SelectBuilder;
import com.jporm.sql.dsl.query.select.SelectBuilderImpl;
import com.jporm.sql.dsl.query.update.Update;
import com.jporm.sql.dsl.query.update.UpdateBuilderImpl;

public class SqlDsl {

    private static final PropertiesProcessor DEFAULT_PROPERTIES_PROCESSOR = new NoOpsPropertiesProcessor();
    private final Supplier<PropertiesProcessor> propertiesProcessorSupplier;
    private final DBProfile dbProfile;

    public SqlDsl(final DBProfile dbProfile) {
        this(dbProfile, () -> DEFAULT_PROPERTIES_PROCESSOR);
    }

    public SqlDsl(final DBProfile dbProfile, Supplier<PropertiesProcessor> propertiesProcessorSupplier) {
        this.dbProfile = dbProfile;
        this.propertiesProcessorSupplier = propertiesProcessorSupplier;
    }

    public Delete deleteFrom(String table) {
        return new DeleteBuilderImpl(getDbProfile(), propertiesProcessorSupplier.get()).from(table);
    }

    public Insert insertInto(String table, String... columns) {
        return new InsertBuilderImpl(getDbProfile(), columns, propertiesProcessorSupplier.get()).into(table);
    }

    public SelectBuilder selectAll() {
        return select("*");
    }

    public SelectBuilder select(final String... fields) {
        return new SelectBuilderImpl(getDbProfile(), fields, propertiesProcessorSupplier.get());
    }

    public Update update(String table) {
        return new UpdateBuilderImpl(getDbProfile(), propertiesProcessorSupplier.get()).update(table);
    }

    /**
     * @return the dbProfile
     */
    public DBProfile getDbProfile() {
        return dbProfile;
    }

}
