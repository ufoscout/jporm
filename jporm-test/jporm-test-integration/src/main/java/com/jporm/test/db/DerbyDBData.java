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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.JPO;
import com.jporm.JPOrm;
import com.jporm.dialect.DBType;
import com.jporm.session.ScriptExecutor;
import com.jporm.session.Session;
import com.jporm.session.SessionProvider;
import com.jporm.session.TransactionCallback;
import com.jporm.session.datasource.DataSourceSessionProvider;
import com.jporm.session.jdbctemplate.JdbcTemplateSessionProvider;

@Component
@Lazy
public class DerbyDBData implements DBData {


	@Resource(name = "derbyDataSource")
	private DataSource dataSource;
	@Resource(name = "derbyTransactionManager")
	public PlatformTransactionManager transactionManager;
	@Value("${derby.isDbAvailable}")
	private boolean isDbAvailable;
	@Value("${derby.supportMultipleSchemas}")
	private boolean supportMultipleSchemas;

	@Override
	public DBType getDBType() {
		return DBType.DERBY;
	}

	@Override
	public boolean isDbAvailable() {
		return isDbAvailable;
	}

	@Override
	public SessionProvider getJdbcTemplateSessionProvider() {
		return new JdbcTemplateSessionProvider(getDataSource(), transactionManager);
	}

	@Override
	public SessionProvider getDataSourceSessionProvider() {
		return new DataSourceSessionProvider(getDataSource());
	}

	@Override
	public boolean supportMultipleSchemas() {
		return supportMultipleSchemas;
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@PostConstruct
	private void init() {
		if (isDbAvailable()) {
			final JPO jpOrm = new JPOrm(getDataSourceSessionProvider());

			jpOrm.session().doInTransaction(new TransactionCallback<Void>() {

				@Override
				public Void doInTransaction(final Session session) {
					final ScriptExecutor scriptExecutor = session.scriptExecutor();
					try {
						scriptExecutor.execute(getClass().getResourceAsStream("/sql/derby_create_db.sql")); //$NON-NLS-1$
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					return null;
				}

			});
		}
	}

}
