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
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.jporm.core.session.SessionProvider;
import com.jporm.core.session.SqlPerformerStrategy;
import com.jporm.exception.OrmException;
import com.jporm.session.Session;
import com.jporm.transaction.TransactionCallback;
import com.jporm.transaction.TransactionDefinition;
import com.jporm.transaction.TransactionIsolation;
import com.jporm.transaction.TransactionPropagation;

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
	public SqlPerformerStrategy sqlPerformerStrategy() throws OrmException {
		return performerStrategy;
	}

	@Override
	public <T> T doInTransaction(final Session session, final TransactionDefinition transactionDefinition, final TransactionCallback<T> transactionCallback) {

		try {
			DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
			setIsolationLevel(definition , transactionDefinition.getIsolationLevel());
			setPropagation( definition , transactionDefinition.getPropagation() );
			setTimeout( definition , transactionDefinition.getTimeout() );
			definition.setReadOnly( transactionDefinition.isReadOnly() );

			TransactionTemplate tt = new TransactionTemplate(platformTransactionManager, definition);
			return tt.execute(transactionStatus -> transactionCallback.doInTransaction(session));
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}

	}

	private void setTimeout(final DefaultTransactionDefinition definition, final int timeout) {
		if (timeout >= 0) {
			definition.setTimeout(timeout);
		}
	}

	private void setPropagation(final DefaultTransactionDefinition definition, final TransactionPropagation propagation) {
		switch (propagation) {
		case MANDATORY:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_MANDATORY );
			break;
		case NESTED:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED );
			break;
		case NEVER:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_NEVER );
			break;
		case NOT_SUPPORTED:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_NOT_SUPPORTED );
			break;
		case REQUIRED:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED );
			break;
		case REQUIRES_NEW:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW );
			break;
		case SUPPORTS:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_SUPPORTS );
			break;
		default:
			throw new OrmException("Unknown Transaction Propagation: " + propagation); //$NON-NLS-1$
		}

	}

	private void setIsolationLevel(final DefaultTransactionDefinition definition,
			final TransactionIsolation isolationLevel) {
		if (isolationLevel!=TransactionIsolation.DEFAULT) {
			definition.setIsolationLevel(isolationLevel.getTransactionIsolation());
		}

	}
}
