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
package com.jporm.session.jdbctemplate;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.exception.OrmException;
import com.jporm.session.SessionProvider;
import com.jporm.session.SqlPerformerStrategy;
import com.jporm.transaction.Transaction;
import com.jporm.transaction.TransactionDefinition;

/**
 * 
 * @author Francesco Cina
 *
 * 15/giu/2011
 */
public class JdbcTemplateSessionProvider extends SessionProvider {

	private final ThreadLocal<JdbcTemplate> threadLocalJdbcTemplate = new ThreadLocal<JdbcTemplate>();
	private final DataSource dataSource;
	private final SqlPerformerStrategy performerStrategy;
	private final PlatformTransactionManager platformTransactionManager;

	public JdbcTemplateSessionProvider(final DataSource dataSource, final PlatformTransactionManager platformTransactionManager) {
		this.dataSource = dataSource;
		this.platformTransactionManager = platformTransactionManager;
		performerStrategy = new JdbcTemplateSqlPerformerStrategy( this );
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	public JdbcTemplate getJdbcTemplate() {
		getLogger().trace("Connection asked."); //$NON-NLS-1$
		JdbcTemplate conn = threadLocalJdbcTemplate.get();
		if (conn==null) {
			getLogger().trace("No valid connections found, a new one will be created"); //$NON-NLS-1$
			conn = new JdbcTemplate(dataSource);
			threadLocalJdbcTemplate.set(conn);
		}
		return conn;
	}

	@Override
	public Transaction getTransaction(final TransactionDefinition transactionDefinition) throws OrmException {
		try {
			return new JdbcTemplateTransaction(platformTransactionManager, transactionDefinition);
		} catch (Exception e) {
			throw new OrmException(e);
		}
	}

	@Override
	public SqlPerformerStrategy sqlPerformerStrategy() throws OrmException {
		return performerStrategy;
	}
}
