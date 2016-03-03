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

import com.jporm.sql.dialect.derby.DerbyDBProfile;
import com.jporm.sql.dialect.h2.H2DBProfile;
import com.jporm.sql.dialect.hsqldb2.HSQLDB2DBProfile;
import com.jporm.sql.dialect.mysql.MySqlDBProfile;
import com.jporm.sql.dialect.oracle10g.Oracle10gDBProfile;
import com.jporm.sql.dialect.postgres.PostgresDBProfile;
import com.jporm.sql.dialect.sqlserver2012.SQLServer2012DBProfile;
import com.jporm.sql.dialect.unknown.UnknownDBProfile;

/**
 *
 * @author ufo
 *
 */
public enum DBType {

    UNKNOWN(new UnknownDBProfile()), DERBY(new DerbyDBProfile()), H2(new H2DBProfile()), HSQLDB(new HSQLDB2DBProfile()), MYSQL(new MySqlDBProfile()), ORACLE(
            new Oracle10gDBProfile()), POSTGRESQL(new PostgresDBProfile()), SQLSERVER12(new SQLServer2012DBProfile());

    private final DBProfile dbProfile;

    private DBType(final DBProfile dbProfile) {
        this.dbProfile = dbProfile;
    }

    public DBProfile getDBProfile() {
        return dbProfile;
    }

}
