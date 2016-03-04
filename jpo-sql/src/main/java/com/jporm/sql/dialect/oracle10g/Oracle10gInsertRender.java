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
package com.jporm.sql.dialect.oracle10g;

import java.util.ArrayList;
import java.util.List;

import com.jporm.sql.dialect.SqlFunctionsRender;
import com.jporm.sql.dialect.SqlInsertRender;
import com.jporm.sql.dialect.SqlValuesRender;
import com.jporm.sql.query.insert.InsertImpl;
import com.jporm.sql.query.insert.values.ValuesImpl;
import com.jporm.sql.query.processor.PropertiesProcessor;

public class Oracle10gInsertRender implements SqlInsertRender, SqlValuesRender {

    private static final String SELECT_FROM_DUAL = "SELECT * FROM dual";
    private static final String NEW_LINE = "\n";
    private static final String INTO = "INTO ";
    private static final String INSERT_ALL = "INSERT ALL\n";

    private SqlFunctionsRender functionsRender;

    public Oracle10gInsertRender(SqlFunctionsRender functionsRender) {
        this.functionsRender = functionsRender;
    }

    @Override
    public SqlValuesRender getSqlValuesRender() {
        return this;
    }

    @Override
    public SqlFunctionsRender getFunctionsRender() {
        return functionsRender;
    }

    @Override
    public void render(InsertImpl<?> insert, StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        if (insert.getElemValues().getValues().size() > 1) {
            renderWithMultipleRow(insert, queryBuilder, propertiesProcessor);
        } else {
            queryBuilder.append(SqlInsertRender.INSERT_INTO);
            queryBuilder.append(insert.getTableName().getTable());
            queryBuilder.append(SqlInsertRender.WHITE_SPACE);
            getSqlValuesRender().render(insert.getElemValues(), queryBuilder, propertiesProcessor, getFunctionsRender());
        }
    }

    private void renderWithMultipleRow(InsertImpl<?> insert, StringBuilder queryBuilder, PropertiesProcessor propertiesProcessor) {
        queryBuilder.append(INSERT_ALL);

        ValuesImpl insertValues = insert.getElemValues();
        for (Object[] values : insertValues.getValues()) {
            queryBuilder.append(INTO);
            queryBuilder.append(insert.getTableName().getTable());
            queryBuilder.append(SqlInsertRender.WHITE_SPACE);
            columnToCommaSepareted(insertValues.getFields(), queryBuilder, propertiesProcessor);
            queryBuilder.append(VALUES);
            List<Object[]> list = new ArrayList<>();
            list.add(values);
            valuesToCommaSeparated(list, queryBuilder, functionsRender);
            queryBuilder.append(NEW_LINE);
        }

        queryBuilder.append(SELECT_FROM_DUAL);
    }
}
