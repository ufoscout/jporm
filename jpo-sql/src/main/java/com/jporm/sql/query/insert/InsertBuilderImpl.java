/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.sql.query.insert;

import com.jporm.sql.dialect.SqlInsertRender;
import com.jporm.sql.query.processor.TablePropertiesProcessor;

public class InsertBuilderImpl<T> implements InsertBuilder<T> {

    private final String[] columns;
    private final SqlInsertRender insertRender;
    private final TablePropertiesProcessor<T> propertiesProcessor;

    public InsertBuilderImpl(SqlInsertRender insertRender, String[] columns, TablePropertiesProcessor<T> propertiesProcessor) {
        this.insertRender = insertRender;
        this.columns = columns;
        this.propertiesProcessor = propertiesProcessor;
    }

    @Override
    public Insert into(T tableName) {
        return new InsertImpl<>(insertRender, columns, tableName, propertiesProcessor);
    }

}
