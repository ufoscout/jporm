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
package com.jporm.commons.core.connection.impl;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.jporm.commons.core.connection.Connection;
import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.sql.dsl.dialect.DBType;

/**
 *
 * @author Francesco Cina
 *
 *         21/mag/2011
 */
public class DataSourceConnectionProvider implements ConnectionProvider {

    private final DataSource dataSource;
    private DBType dbType;

    public DataSourceConnectionProvider(final DataSource dataSource) {
        this(dataSource, null);
    }

    public DataSourceConnectionProvider(final DataSource dataSource, final DBType dbType) {
        this.dataSource = dataSource;
        this.dbType = dbType;
    }

    @Override
    public Connection getConnection(final boolean autoCommit) throws JpoException {
        java.sql.Connection connection;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(autoCommit);
            return new DataSourceConnection(connection, getDBType());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final DBType getDBType() {
        if (dbType == null) {
            dbType = DBTypeDescription.build(dataSource).getDBType();
        }
        return dbType;
    }

}
