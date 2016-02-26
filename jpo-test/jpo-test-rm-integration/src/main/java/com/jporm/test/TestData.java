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
package com.jporm.test;

import javax.sql.DataSource;

import com.jporm.rm.JpoRm;
import com.jporm.sql.dsl.dialect.DBType;

public class TestData {

    private final DBType dbType;
    private final boolean supportMultipleSchemas;
    private final DataSource dataSource;
    private final JpoRm jpo;

    public TestData(final JpoRm jpo, final DataSource dataSource, final DBType dbType, final boolean supportMultipleSchemas) {
        this.jpo = jpo;
        this.dataSource = dataSource;
        this.dbType = dbType;
        this.supportMultipleSchemas = supportMultipleSchemas;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public DBType getDBType() {
        return dbType;
    }

    public JpoRm getJpo() {
        return jpo;
    }

    public boolean isSupportMultipleSchemas() {
        return supportMultipleSchemas;
    }

}
