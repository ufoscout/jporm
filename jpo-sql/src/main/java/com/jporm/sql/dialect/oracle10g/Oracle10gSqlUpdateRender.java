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

import com.jporm.sql.dialect.SqlSetRender;
import com.jporm.sql.dialect.SqlUpdateRender;
import com.jporm.sql.dialect.SqlWhereRender;

public class Oracle10gSqlUpdateRender implements SqlUpdateRender, SqlSetRender {

    private final SqlWhereRender whereRender;

    public Oracle10gSqlUpdateRender(SqlWhereRender whereRender) {
        this.whereRender = whereRender;
    }

    @Override
    public SqlWhereRender getWhereRender() {
        return whereRender;
    }

    @Override
    public SqlSetRender getSetRender() {
        return this;
    }

}
