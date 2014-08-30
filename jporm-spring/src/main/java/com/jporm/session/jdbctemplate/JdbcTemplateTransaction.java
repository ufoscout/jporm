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

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.jporm.exception.OrmException;
import com.jporm.transaction.Transaction;
import com.jporm.transaction.TransactionDefinition;
import com.jporm.transaction.TransactionIsolation;
import com.jporm.transaction.TransactionPropagation;

/**
 * 
 * @author Francesco Cina
 *
 * 18/giu/2011
 */
public class JdbcTemplateTransaction implements Transaction {

	private final TransactionStatus transactionStatus;
	private final PlatformTransactionManager platformTransactionManager;

	public JdbcTemplateTransaction(final PlatformTransactionManager platformTransactionManager, final TransactionDefinition transactionDefinition) {
		this.platformTransactionManager = platformTransactionManager;
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();

		setIsolationLevel(definition , transactionDefinition.getIsolationLevel());
		setPropagation( definition , transactionDefinition.getPropagation() );
		setTimeout( definition , transactionDefinition.getTimeout() );
		definition.setReadOnly( transactionDefinition.isReadOnly() );

		transactionStatus  = platformTransactionManager.getTransaction(definition);
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

	@Override
	public void setRollbackOnly() throws OrmException {
		try {
			transactionStatus.setRollbackOnly();
		} catch (Exception e) {
			throw new OrmException(e);
		}
	}

	@Override
	public void commit() throws OrmException {
		try {
			if (!transactionStatus.isCompleted()) {
				platformTransactionManager.commit(transactionStatus);
			}
		} catch (Exception e) {
			throw new OrmException(e);
		}
	}

	@Override
	public void rollback() throws OrmException {
		try {
			if (!transactionStatus.isCompleted()) {
				platformTransactionManager.rollback(transactionStatus);
			}
		} catch (Exception e) {
			throw new OrmException(e);
		}
	}

	@Override
	public boolean isClosed() {
		return transactionStatus.isCompleted();
	}

}
