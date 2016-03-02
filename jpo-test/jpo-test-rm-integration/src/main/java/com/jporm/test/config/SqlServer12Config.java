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

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.jporm.sql.dialect.DBType;
import com.jporm.test.TestConstants;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
public class SqlServer12Config extends AbstractDBConfig {

    public static final DBType DB_TYPE = DBType.SQLSERVER12;
    public static final String DATASOURCE_NAME = "SQLSERVER12.DataSource";
    public static final String TRANSACTION_MANAGER_NAME = "SQLSERVER12.TransactionManager";
    public static final String DB_DATA_NAME = "SQLSERVER12.DA_DATA";
    public static final String LIQUIBASE_BEAN_NAME = "SQLSERVER12.LIQUIBASE";

    @Autowired
    private Environment env;

    @Override
    @Lazy
    @Bean(name = { DATASOURCE_NAME })
    public DataSource getDataSource() {
        DataSource dataSource = buildDataSource(DB_TYPE, env);
        return dataSource;
    }

    @Lazy
    @Bean(name = DB_DATA_NAME)
    public DBData getDBData() {
        DBData dbData = buildDBData(DB_TYPE, env);
        // init(dbData);
        return dbData;
    }

    @Override
    @Lazy
    @Bean(name = TRANSACTION_MANAGER_NAME)
    public DataSourceTransactionManager getPlatformTransactionManager() {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(getDataSource());
        return txManager;
    }

    @Bean(name = LIQUIBASE_BEAN_NAME)
    public SpringLiquibase getSpringLiquibase() {
        SpringLiquibase liquibase = null;
        if (getDBData().isDbAvailable()) {
            liquibase = new SpringLiquibase();
            liquibase.setDataSource(getDataSource());
            liquibase.setChangeLog(TestConstants.LIQUIBASE_FILE);
            // liquibase.setContexts("development, production");
        }
        return liquibase;
    }

}
