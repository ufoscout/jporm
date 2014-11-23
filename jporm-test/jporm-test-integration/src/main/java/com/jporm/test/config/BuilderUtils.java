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

import com.jporm.core.dialect.DBType;
import com.jporm.core.session.datasource.DataSourceSessionProvider;
import com.jporm.session.jdbctemplate.JdbcTemplateSessionProvider;

public class BuilderUtils {

	public static DataSource buildDataSource(final DBType dbType, final Environment env) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty(dbType + ".jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty(dbType + ".jdbc.url"));
		dataSource.setUsername(env.getProperty(dbType + ".jdbc.username"));
		dataSource.setPassword(env.getProperty(dbType + ".jdbc.password"));
		dataSource.setDefaultAutoCommit(false);
		return dataSource;
	}

	public static DBData buildDBData(final DBType dbType, final Environment env, final DataSource dataSource,
			final PlatformTransactionManager transactionManager) {
		DBData dbData = new DBData();

		dbData.setDataSource(dataSource);
		dbData.setDataSourceSessionProvider(new DataSourceSessionProvider(dataSource));
		dbData.setJdbcTemplateSessionProvider(new JdbcTemplateSessionProvider(dataSource, transactionManager));

		dbData.setDbAvailable(env.getProperty( dbType + ".isDbAvailable" , Boolean.class));
		dbData.setDBType(dbType);

		dbData.setMultipleSchemaSupport(env.getProperty( dbType + ".supportMultipleSchemas" , Boolean.class));

		return dbData;
	}

}
