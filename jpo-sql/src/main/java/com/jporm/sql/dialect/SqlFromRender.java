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
import com.jporm.sql.query.select.from.FromElement;
import com.jporm.sql.query.select.from.FromImpl;

public interface SqlFromRender {

    public static final String EQUALS = " = ";
    public static final String ON = " ON ";
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
        for (final FromElement joinElement : from.getJoinElements()) {
            renderFromElement(joinElement, queryBuilder, propertiesProcessor);
        }

    }

    default void renderFromElement(FromElement joinElement, final StringBuilder queryBuilder, final PropertiesProcessor propertiesProcessor) {
        queryBuilder.append(joinElement.getJoinName());

        TableName tableName = joinElement.getTableName();
        queryBuilder.append(tableName.getTable());
        if (tableName.hasAlias()) {
            queryBuilder.append(WHITE_SPACE);
            queryBuilder.append(tableName.getAlias());
        }
        if (joinElement.hasOnClause()) {
            queryBuilder.append(ON);
            queryBuilder.append(propertiesProcessor.solvePropertyName(joinElement.onLeftProperty()));
            queryBuilder.append(EQUALS);
            queryBuilder.append(propertiesProcessor.solvePropertyName(joinElement.onRightProperty()));
        }

        queryBuilder.append(WHITE_SPACE);
    }

}
