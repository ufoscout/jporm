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
package com.jporm.sql.dialect;

import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.processor.TableName;
import com.jporm.sql.query.select.from.AFromElement;
import com.jporm.sql.query.select.from.FromImpl;

public interface SqlFromRender {

    String FROM = "FROM ";
    String WHITE_SPACE = " ";

    default void render(FromImpl<?, ?> from, StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        queryBuilder.append(FROM);
        TableName tableName = from.getTableName();
        queryBuilder.append(tableName.getTable());
        queryBuilder.append(WHITE_SPACE);
        if (tableName.hasAlias()) {
            queryBuilder.append(tableName.getAlias());
            queryBuilder.append(WHITE_SPACE);
        }
        for (final AFromElement joinElement : from.getJoinElements()) {
            joinElement.sqlElementQuery(queryBuilder, propertiesProcessor);
        }

    }

}
