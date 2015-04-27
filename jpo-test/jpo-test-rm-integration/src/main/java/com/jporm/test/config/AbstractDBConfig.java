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

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.rm.session.datasource.DataSourceThreadLocalSessionProvider;
import com.jporm.rm.spring.session.jdbctemplate.JdbcTemplateSessionProvider;
import com.jporm.sql.dialect.DBType;

public abstract class AbstractDBConfig {

	public abstract DataSource getDataSource();
	public abstract PlatformTransactionManager getPlatformTransactionManager();

	protected DBData buildDBData(final DBType dbType, final Environment env) {
		DBData dbData = new DBData();

		boolean available = env.getProperty( dbType + ".isDbAvailable" , Boolean.class);
		dbData.setDbAvailable(available);
		if (available) {
			dbData.setDataSource(getDataSource());
			dbData.setDataSourceSessionProvider(new DataSourceThreadLocalSessionProvider(getDataSource()));
			dbData.setJdbcTemplateSessionProvider(new JdbcTemplateSessionProvider(getDataSource(), getPlatformTransactionManager()));
		}

		dbData.setDBType(dbType);

		dbData.setMultipleSchemaSupport(env.getProperty( dbType + ".supportMultipleSchemas" , Boolean.class));

		return dbData;
	}

	protected DataSource buildDataSource(final DBType dbType, final Environment env) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty(dbType + ".jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty(dbType + ".jdbc.url"));
		dataSource.setUsername(env.getProperty(dbType + ".jdbc.username"));
		dataSource.setPassword(env.getProperty(dbType + ".jdbc.password"));
		dataSource.setDefaultAutoCommit(false);
		return dataSource;
	}
}
