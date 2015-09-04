/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rx.transaction;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.transaction.TransactionDefinition;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.commons.core.transaction.impl.TransactionDefinitionImpl;
import com.jporm.rx.connection.Connection;
import com.jporm.rx.connection.ConnectionUtils;
import com.jporm.rx.session.ConnectionProvider;
import com.jporm.rx.session.Session;
import com.jporm.rx.session.impl.SessionImpl;


public class TransactionImpl implements Transaction {

	private final static Logger LOGGER = LoggerFactory.getLogger(TransactionImpl.class);
	private final ConnectionProvider connectionProvider;
	private final ServiceCatalog serviceCatalog;
	private final TransactionDefinition transactionDefinition = new TransactionDefinitionImpl();

	public TransactionImpl(ServiceCatalog serviceCatalog, ConnectionProvider connectionProvider) {
		this.serviceCatalog = serviceCatalog;
		this.connectionProvider = connectionProvider;
	}

	@Override
	public <T> CompletableFuture<T> execute(Function<Session, CompletableFuture<T>> txSession) {
		return connectionProvider.getConnection(false)
		.thenCompose(connection -> {
			try {
				setTransactionIsolation(connection);
				setTimeout(connection);
				LOGGER.debug("Start new transaction");
				Session session = new SessionImpl(serviceCatalog, new TransactionalConnectionProviderDecorator(connection, connectionProvider), false);
				CompletableFuture<T> result = txSession.apply(session);
				CompletableFuture<T> committedResult = ConnectionUtils.commitOrRollback( result, connection);
				return ConnectionUtils.close(committedResult, connection);
			}
			catch (RuntimeException e) {
				LOGGER.error("Error during transaction execution");
				connection.close();
				throw e;
			}
		});
	}

	private void setTransactionIsolation(Connection connection) {
		if (transactionDefinition.getIsolationLevel() != TransactionIsolation.DEFAULT) {
			connection.setTransactionIsolation(transactionDefinition.getIsolationLevel());
		}
	}

	private void setTimeout(Connection connection) {
		if (transactionDefinition.getTimeout() >= 0) {
			connection.setTimeout(transactionDefinition.getTimeout());
		} else {
			connection.setTimeout(serviceCatalog.getConfigService().getTransactionDefaultTimeoutSeconds());
		}
	}

	@Override
	public Transaction isolation(TransactionIsolation isolation) {
		transactionDefinition.isolation(isolation);
		return this;
	}

	@Override
	public Transaction timeout(int timeoutSeconds) {
		transactionDefinition.timeout(timeoutSeconds);
		return this;
	}

}
