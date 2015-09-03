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
package com.jporm.rm;

import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.rm.session.ConnectionProvider;
import com.jporm.rm.session.Session;
import com.jporm.rm.session.impl.SessionImpl;
import com.jporm.rm.transaction.Transaction;

/**
 *
 * @author Francesco Cina'
 *
 * 26/ago/2011
 */
public class JpoRmImpl implements JpoRm {

	private static Integer JPORM_INSTANCES_COUNT = Integer.valueOf(0);
	private final ServiceCatalog serviceCatalog;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Integer instanceCount;
	private final ConnectionProvider connectionProvider;
	private final SessionImpl session;
	private final BiFunction<ConnectionProvider, ServiceCatalog, Transaction> transactionFactory;

	/**
	 * Create a new instance of JPOrm.
	 *
	 * @param sessionProvider
	 */
	public JpoRmImpl(final ConnectionProvider connectionProvider, final ServiceCatalog serviceCatalog) {
		this.connectionProvider = connectionProvider;
		transactionFactory = connectionProvider.getTransactionFactory();
		synchronized (JPORM_INSTANCES_COUNT) {
			instanceCount = JPORM_INSTANCES_COUNT++;
		}
		logger.info("Building new instance of JPO (instance [{}])", instanceCount);
		this.serviceCatalog = serviceCatalog;
		session = new SessionImpl(serviceCatalog, connectionProvider, true);
	}

	@Override
	public final Session session() {
		return session;
	}

	public ServiceCatalog getServiceCatalog() {
		return serviceCatalog;
	}

	public ConnectionProvider getSessionProvider() {
		return connectionProvider;
	}

	@Override
	public Transaction transaction() {
		return transactionFactory.apply(connectionProvider, serviceCatalog);
	}

}
