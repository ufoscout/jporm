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

import io.vertx.core.Vertx;
import liquibase.integration.spring.SpringLiquibase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.jporm.rx.core.session.datasource.ThreadPoolDataSourceConnectionProvider;
import com.jporm.rx.vertx.session.vertx3.datasource.Vertx3DataSourceConnectionProvider;
import com.jporm.sql.dialect.DBType;
import com.jporm.test.TestConstants;

@Configuration
public class OracleConfig extends AbstractDBConfig {

	public static final DBType DB_TYPE = DBType.ORACLE;
	public static final String DB_DATA_NAME = "ORACLE.DA_DATA";
	public static final String LIQUIBASE_BEAN_NAME = "ORACLE.LIQUIBASE";

	@Lazy
	@Bean(name=DB_DATA_NAME + "-rx-core")
	public DBData getDBDataRxCore() {
		return buildDBData(DB_TYPE, "ORACLE-RX-core", () -> getDataSource(DB_TYPE), (dataSource) -> new ThreadPoolDataSourceConnectionProvider(dataSource, 10));
	}

	@Lazy
	@Bean(name=DB_DATA_NAME + "-rx-vertx3")
	public DBData getDBDataRxVertx() {
		return buildDBData(DB_TYPE, "ORACLE-RX-vertx3", () -> getDataSource(DB_TYPE), (dataSource) -> new Vertx3DataSourceConnectionProvider(dataSource, Vertx.vertx()));
	}

	@Bean(name=LIQUIBASE_BEAN_NAME)
	public SpringLiquibase getSpringLiquibaseRxCore() {
		SpringLiquibase liquibase = null;
		if (getDBDataRxCore().isDbAvailable()) {
			liquibase = new SpringLiquibase();
			liquibase.setDataSource(getDBDataRxCore().getDataSource());
			liquibase.setChangeLog(TestConstants.LIQUIBASE_FILE);
			//liquibase.setContexts("development, production");
		}
		return liquibase;
	}

}
