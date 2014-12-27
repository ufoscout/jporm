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
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.jporm.core.dialect.DBType;

@Configuration
public class PostgresqlConfig extends AbstractDBConfig {

	public static final DBType DB_TYPE = DBType.POSTGRESQL;
	public static final String DATASOURCE_NAME = "POSTGRESQL.DataSource";
	public static final String TRANSACTION_MANAGER_NAME = "POSTGRESQL.TransactionManager";
	public static final String DB_DATA_NAME = "POSTGRESQL.DA_DATA";
	public static final String LIQUIBASE_BEAN_NAME = "POSTGRESQL.LIQUIBASE";

	@Autowired
	private Environment env;

	@Lazy
	@Override
	@Bean(name={DATASOURCE_NAME})
	public DataSource getDataSource() {

		DataSource dataSource = buildDataSource(DB_TYPE, env);

		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		databasePopulator.setIgnoreFailedDrops(true);
		databasePopulator.addScript(new ClassPathResource("/sql/postgresql_drop_db.sql"));
		//databasePopulator.addScript(new ClassPathResource("/sql/postgresql_create_db.sql"));
		DatabasePopulatorUtils.execute(databasePopulator, dataSource);

		return dataSource;
	}

	@Lazy
	@Override
	@Bean(name=TRANSACTION_MANAGER_NAME)
	public DataSourceTransactionManager getPlatformTransactionManager() {
		DataSourceTransactionManager txManager = new DataSourceTransactionManager();
		txManager.setDataSource(getDataSource());
		return txManager;
	}

	@Lazy
	@Bean(name=DB_DATA_NAME)
	public DBData getDBData() {
		return buildDBData(DB_TYPE, env);
	}

}
