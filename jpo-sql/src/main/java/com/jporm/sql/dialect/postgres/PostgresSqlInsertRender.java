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
package com.jporm.sql.dialect.postgres;

import com.jporm.sql.dialect.SqlFunctionsRender;
import com.jporm.sql.dialect.SqlInsertRender;
import com.jporm.sql.dialect.SqlValuesRender;

public class PostgresSqlInsertRender implements SqlInsertRender, SqlValuesRender {

    private SqlFunctionsRender functionsRender;

    public PostgresSqlInsertRender(SqlFunctionsRender functionsRender) {
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

}
