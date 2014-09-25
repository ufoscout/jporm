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
package com.jporm.test.db;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.dialect.DBType;
import com.jporm.session.SessionProvider;
import com.jporm.session.datasource.DataSourceSessionProvider;
import com.jporm.session.jdbctemplate.JdbcTemplateSessionProvider;

@Component
@Lazy
public class MysqlDBData implements DBData {

	@Resource(name = "mysqlDataSource")
	private DataSource dataSource;
	@Resource(name = "mysqlTransactionManager")
	public PlatformTransactionManager transactionManager;
	@Value("${mysql.isDbAvailable}")
	private boolean isDbAvailable;
	@Value("${mysql.supportMultipleSchemas}")
	private boolean supportMultipleSchemas;

	@Override
	public boolean isDbAvailable() {
		return isDbAvailable;
	}

	@Override
	public SessionProvider getJdbcTemplateSessionProvider() {
		return new JdbcTemplateSessionProvider(dataSource, transactionManager);
	}

	@Override
	public SessionProvider getDataSourceSessionProvider() {
		return new DataSourceSessionProvider(dataSource);
	}

	@Override
	public DBType getDBType() {
		return DBType.MYSQL;
	}

	@Override
	public boolean supportMultipleSchemas() {
		return supportMultipleSchemas;
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

}
