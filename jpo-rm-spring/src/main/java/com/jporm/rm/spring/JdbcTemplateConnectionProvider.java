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
package com.jporm.rm.spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.commons.core.connection.Connection;
import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.sql.dialect.DBType;

/**
 *
 * @author Francesco Cina
 *
 *         15/giu/2011
 */
public class JdbcTemplateConnectionProvider implements ConnectionProvider {

    private DBType dbType;
    private final JdbcTemplate jdbcTemplate;

    JdbcTemplateConnectionProvider(final JdbcTemplate jdbcTemplate, final PlatformTransactionManager platformTransactionManager) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Connection getConnection(final boolean autoCommit) throws JpoException {
        return new JdbcTemplateConnection(jdbcTemplate, getDBType().getDBProfile().getStatementStrategy());
    }

    @Override
    public final DBType getDBType() {
        if (dbType == null) {
            dbType = DBTypeDescription.build(jdbcTemplate.getDataSource()).getDBType();
        }
        return dbType;
    }

}
