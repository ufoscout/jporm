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
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

import com.jporm.rx.core.session.ConnectionProvider;
import com.jporm.rx.core.session.datasource.ThreadPoolDataSourceConnectionProvider;
import com.jporm.sql.dialect.DBType;
import com.jporm.test.TestConstants;

@Configuration
public class MySqlConfig extends AbstractDBConfig {

	public static final DBType DB_TYPE = DBType.MYSQL;
	public static final String DATASOURCE_NAME = "MYSQL.DataSource";
	public static final String DB_DATA_NAME = "MYSQL.DA_DATA";
	public static final String LIQUIBASE_BEAN_NAME = "MYSQL.LIQUIBASE";


	@Autowired
	private Environment env;

	@Override
	@Lazy
	@Bean(name={DATASOURCE_NAME})
	public DataSource getDataSource() {
		DataSource dataSource = buildDataSource(DB_TYPE, env);
		return dataSource;
	}

	@Lazy
	@Bean(name=DB_DATA_NAME)
	public DBData getDBData() {
		return buildDBData(DB_TYPE, env);
	}

	@Bean(name=LIQUIBASE_BEAN_NAME)
	public SpringLiquibase getSpringLiquibase() {
		SpringLiquibase liquibase = null;
		if (getDBData().isDbAvailable()) {
			liquibase = new SpringLiquibase();
			liquibase.setDataSource(getDataSource());
			liquibase.setChangeLog(TestConstants.LIQUIBASE_FILE);
			liquibase.setDropFirst(true);
			//liquibase.setContexts("development, production");
		}
		return liquibase;
	}

	@Override
	public ConnectionProvider getConnectionProvider(DataSource dataSource) {
		return new ThreadPoolDataSourceConnectionProvider(dataSource, 10);
	}

	@Override
	public String getDescription() {
		return "MySql-RX-core";
	}

}
