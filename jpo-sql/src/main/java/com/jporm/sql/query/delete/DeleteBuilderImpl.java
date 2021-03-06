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
package com.jporm.sql.query.delete;

import com.jporm.sql.dialect.SqlDeleteRender;
import com.jporm.sql.query.processor.TablePropertiesProcessor;

public class DeleteBuilderImpl<T> implements DeleteBuilder<T> {

    private final SqlDeleteRender deleteRender;
    private final TablePropertiesProcessor<T> propertiesProcessor;

    public DeleteBuilderImpl(SqlDeleteRender deleteRender, TablePropertiesProcessor<T> propertiesProcessor) {
        this.deleteRender = deleteRender;
        this.propertiesProcessor = propertiesProcessor;
    }

    @Override
    public Delete from(T table) {
        return new DeleteImpl(deleteRender, table, propertiesProcessor);
    }

}
