/*******************************************************************************
 * Copyright 2013 Francesco Cina'
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

import com.jporm.sql.dialect.features.DBFeatures;
import com.jporm.sql.dialect.features.PostgresDBFeatures;
import com.jporm.sql.dialect.sql.PostgresSqlStrategy;
import com.jporm.sql.dialect.sql.SqlStrategy;
import com.jporm.sql.dialect.statement.PostgresStatementStrategy;
import com.jporm.sql.dialect.statement.StatementStrategy;

/**
 *
 * @author Francesco Cina
 *
 *         28/giu/2011
 */
public class PostgresDBProfile implements DBProfile {

    private final SqlStrategy sqlStrategy = new PostgresSqlStrategy();
    private final DBFeatures dbFeatures = new PostgresDBFeatures();
    private final StatementStrategy statementStrategy = new PostgresStatementStrategy();

    @Override
    public DBFeatures getDbFeatures() {
        return dbFeatures;
    }

    @Override
    public SqlStrategy getSqlStrategy() {
        return sqlStrategy;
    }

    @Override
    public StatementStrategy getStatementStrategy() {
        return statementStrategy;
    }

    @Override
    public String getDBName() {
        return "Postgres";
    }

}
