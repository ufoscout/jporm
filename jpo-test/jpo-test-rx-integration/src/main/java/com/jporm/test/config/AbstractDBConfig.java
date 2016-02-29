/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.test.config;

import java.util.function.Function;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.jporm.commons.core.connection.AsyncConnectionProvider;
import com.jporm.sql.dialect.DBType;

public abstract class AbstractDBConfig {

    @Autowired
    private Environment env;
    private BasicDataSource _dataSource;

    protected DBData buildDBData(final DBType dbType, final String description, final Supplier<DataSource> dataSource,
            final Function<DataSource, AsyncConnectionProvider> connectionProvider) {
        DBData dbData = new DBData();

        boolean available = env.getProperty(dbType + ".isDbAvailable", Boolean.class);
        dbData.setDbAvailable(available);
        if (available) {
            dbData.setDataSource(dataSource.get());
            dbData.setConnectionProvider(connectionProvider.apply(dataSource.get()));
        }
        dbData.setDescription(description);
        dbData.setDBType(dbType);

        dbData.setMultipleSchemaSupport(env.getProperty(dbType + ".supportMultipleSchemas", Boolean.class));

        return dbData;
    }

    protected DataSource getDataSource(final DBType dbType) {
        if (_dataSource == null) {
            _dataSource = new BasicDataSource();
            _dataSource.setDriverClassName(env.getProperty(dbType + ".jdbc.driverClassName"));
            _dataSource.setUrl(env.getProperty(dbType + ".jdbc.url"));
            _dataSource.setUsername(env.getProperty(dbType + ".jdbc.username"));
            _dataSource.setPassword(env.getProperty(dbType + ".jdbc.password"));
            _dataSource.setDefaultAutoCommit(false);
        }
        return _dataSource;
    }
}
