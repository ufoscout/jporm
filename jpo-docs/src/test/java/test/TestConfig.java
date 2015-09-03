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
package test;

import com.jporm.rm.session.datasource.JPODataSourceBuilder;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import test.all.sql.DB;

import javax.sql.DataSource;

@Configuration
public class TestConfig {

	@Bean
	public DataSource getH2DataSource(final Environment env) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:H2MemoryDB");
		//dataSource.setUsername(env.getProperty("H2.jdbc.username"));
		//dataSource.setPassword(env.getProperty("H2.jdbc.password"));
		dataSource.setDefaultAutoCommit(true);

		new JPODataSourceBuilder().build(dataSource).transaction().executeVoid(session -> {
			session.sqlExecutor().execute(DB.CREATE_USER_SEQUENCE);
			session.sqlExecutor().execute(DB.CREATE_USER_TABLE);
		});

		return dataSource;
	}

	@Bean
	public DataSourceTransactionManager getH2DataSourceTransactionManager(final DataSource dataSource) {
		DataSourceTransactionManager txManager = new DataSourceTransactionManager();
		txManager.setDataSource(dataSource);
		return txManager;
	}
}
