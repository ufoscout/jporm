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
package com.jporm.sql.dialect.hsqldb2;

import com.jporm.sql.dialect.SqlDeleteRender;
import com.jporm.sql.dialect.SqlFunctionsRender;
import com.jporm.sql.dialect.SqlInsertRender;
import com.jporm.sql.dialect.SqlRender;
import com.jporm.sql.dialect.SqlSelectRender;
import com.jporm.sql.dialect.SqlUpdateRender;
import com.jporm.sql.dialect.SqlWhereRender;

public class HSQLDB2SqlRender implements SqlRender {

    private final SqlFunctionsRender functionsRender = new HSQLDB2SqlFunctionsRender();
    private final SqlSelectRender selectRender = new HSQLDB2SqlSelectRender();
    private final SqlInsertRender insertRender = new HSQLDB2SqlInsertRender(functionsRender);
    private final SqlWhereRender whereRender = new HSQLDB2SqlWhereRender();
    private final SqlDeleteRender deleteRender = new HSQLDB2SqlDeleteRender(whereRender);
    private final SqlUpdateRender updateRender = new HSQLDB2SqlUpdateRender(whereRender);

    @Override
    public SqlSelectRender getSelectRender() {
        return selectRender;
    }

    @Override
    public SqlInsertRender getInsertRender() {
        return insertRender;
    }

    @Override
    public SqlDeleteRender getDeleteRender() {
        return deleteRender;
    }

    @Override
    public SqlUpdateRender getUpdateRender() {
        return updateRender;
    }

}
