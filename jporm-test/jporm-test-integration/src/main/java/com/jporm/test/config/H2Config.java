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

import liquibase.integration.spring.SpringLiquibase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.jporm.core.dialect.DBType;

@Configuration
public class H2Config {

	public static final DBType DB_TYPE = DBType.H2;
	public static final String DATASOURCE_NAME = "H2.DataSource";
	public static final String TRANSACTION_MANAGER_NAME = "H2.TransactionManager";
	public static final String DB_DATA_NAME = "H2.DA_DATA";

	@Autowired
	private Environment env;

	@Bean(name={DATASOURCE_NAME})
	public DataSource getDataSource() {

		DataSource dataSource = BuilderUtils.buildDataSource(DB_TYPE, env);

		//		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		//		databasePopulator.setContinueOnError(false);
		//		databasePopulator.addScript(new ClassPathResource("/sql/h2_create_db.sql"));
		//		DatabasePopulatorUtils.execute(databasePopulator, dataSource);

		return dataSource;
	}

	@Bean(name=TRANSACTION_MANAGER_NAME)
	public DataSourceTransactionManager getDataSourceTransactionManager() {
		DataSourceTransactionManager txManager = new DataSourceTransactionManager();
		txManager.setDataSource(getDataSource());
		return txManager;
	}

	@Bean(name=DB_DATA_NAME)
	public DBData getDBData() {
		return BuilderUtils.buildDBData(DB_TYPE, env, getDataSource(), getDataSourceTransactionManager());
	}

	@Bean
	public SpringLiquibase getSpringLiquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(getDataSource());
		liquibase.setChangeLog("file:../jporm-test-integration/liquibase/liquibase-0.0.1.xml");
		//liquibase.setContexts("development, production");
		return liquibase;
	}

}
