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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.JPOConfig;
import com.jporm.commons.core.JPOConfigImpl;
import com.jporm.commons.core.inject.ServiceCatalogImpl;
import com.jporm.rx.core.session.Session;
import com.jporm.rx.core.session.SessionImpl;
import com.jporm.rx.core.session.SessionProvider;

/**
 *
 * @author Francesco Cina'
 *
 * 26/ago/2011
 */
public class JpoRxImpl implements JpoRX {

	private static Integer JPORM_INSTANCES_COUNT = Integer.valueOf(0);
	private final JPOConfigImpl<Session> config = new JPOConfigImpl<Session>();
	private final ServiceCatalogImpl<Session> serviceCatalog;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Integer instanceCount;
	private final SessionProvider sessionProvider;

	/**
	 * Create a new instance of JPOrm.
	 *
	 * @param sessionProvider
	 */
	public JpoRxImpl(final SessionProvider sessionProvider) {
		this.sessionProvider = sessionProvider;
		synchronized (JPORM_INSTANCES_COUNT) {
			instanceCount = JPORM_INSTANCES_COUNT++;
		}
		logger.info("Building new instance of JPO (instance [{}])", instanceCount);
		serviceCatalog = config.getServiceCatalog();
		serviceCatalog.setSession(new SessionImpl(serviceCatalog, sessionProvider));
		serviceCatalog.setDbProfile(sessionProvider.getDBType().getDBProfile());
	}

	@Override
	public final Session session() {
		return serviceCatalog.getSession();
	}

	/**
	 * @return the config
	 */
	@Override
	public JPOConfig config() {
		return config;
	}

	/**
	 * @return the sessionProvider
	 */
	public SessionProvider getSessionProvider() {
		return sessionProvider;
	}

}
