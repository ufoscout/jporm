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
package com.jporm.sql.query.where.expression;

import java.util.List;

import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.where.WhereExpressionElement;

public class ConnectorElement implements WhereExpressionElement {

    private final String connector;

    public ConnectorElement(String connector) {
        this.connector = connector;
    }

    @Override
    public void sqlElementValues(List<Object> values) {
    }

    @Override
    public void sqlElementQuery(StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        queryBuilder.append(connector);
    }

}