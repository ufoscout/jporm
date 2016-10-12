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

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.query.cache.SqlCacheImpl;
import com.jporm.rx.connection.MaybeFunction;
import com.jporm.rx.connection.RxTransaction;
import com.jporm.rx.connection.RxTranscationProvider;
import com.jporm.rx.connection.SingleFunction;
import com.jporm.rx.session.Session;
import com.jporm.rx.session.SessionImpl;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 *
 * @author Francesco Cina'
 *
 *         26/ago/2011
 */
public class JpoRxImpl implements JpoRx {

    private static AtomicInteger JPORM_INSTANCES_COUNT = new AtomicInteger();
    private final ServiceCatalog serviceCatalog;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Integer instanceCount;
    private final RxTranscationProvider transactionProvider;
    private final SqlFactory sqlFactory;
    private final SqlCache sqlCache;
    private Session session;

    /**
     * Create a new instance of JPOrm.
     *
     * @param transactionProvider
     */
    public JpoRxImpl(final RxTranscationProvider transactionProvider, final ServiceCatalog serviceCatalog) {
        this.transactionProvider = transactionProvider;
        this.serviceCatalog = serviceCatalog;
        instanceCount = JPORM_INSTANCES_COUNT.getAndIncrement();
        logger.info("Building new instance of JPO (instance [{}])", instanceCount);
        sqlFactory = new SqlFactory(serviceCatalog.getClassToolMap(), serviceCatalog.getPropertiesFactory(), transactionProvider.getDBProfile().getSqlRender());
        sqlCache = new SqlCacheImpl(sqlFactory, serviceCatalog.getClassToolMap(), transactionProvider.getDBProfile());
    }

    @Override
    public RxTransaction tx() {
        return transactionProvider.getTransaction(serviceCatalog, sqlCache, sqlFactory);
    }

    @Override
    public <T> Maybe<T> tx(MaybeFunction<T> txSession) {
        return tx().execute(txSession);
    }

    @Override
    public Session session() {
        if (session == null) {
            session = new SessionImpl(serviceCatalog, transactionProvider.getDBProfile(), transactionProvider.getConnectionProvider(), sqlCache, sqlFactory);
        }
        return session;
    }

    @Override
    public <T> Single<T> tx(SingleFunction<T> txSession) {
        return tx().execute(txSession);
    }

}
