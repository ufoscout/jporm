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
package com.jporm.rx;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.rx.core.session.ConnectionProvider;
import com.jporm.rx.core.session.Session;
import com.jporm.rx.core.session.impl.SessionImpl;
import com.jporm.rx.core.transaction.TransactionImpl;

/**
 *
 * @author Francesco Cina'
 *
 * 26/ago/2011
 */
public class JpoRxImpl implements JpoRX {

	private static Integer JPORM_INSTANCES_COUNT = Integer.valueOf(0);
	private final ServiceCatalog serviceCatalog;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Integer instanceCount;
	private final ConnectionProvider sessionProvider;
	private final SessionImpl session;

	/**
	 * Create a new instance of JPOrm.
	 *
	 * @param sessionProvider
	 */
	public JpoRxImpl(final ConnectionProvider sessionProvider, ServiceCatalog serviceCatalog) {
		this.sessionProvider = sessionProvider;
		this.serviceCatalog = serviceCatalog;
		synchronized (JPORM_INSTANCES_COUNT) {
			instanceCount = JPORM_INSTANCES_COUNT++;
		}
		logger.info("Building new instance of JPO (instance [{}])", instanceCount);
		session = new SessionImpl(serviceCatalog, sessionProvider, true);
	}

	@Override
	public final Session session() {
		return session;
	}

	/**
	 * @return the sessionProvider
	 */
	public ConnectionProvider getSessionProvider() {
		return sessionProvider;
	}

	@Override
	public <T> CompletableFuture<T> transaction(Function<Session, CompletableFuture<T>> txSession) {
		return new TransactionImpl(serviceCatalog, sessionProvider).doInTransaction(txSession);
	}

}
