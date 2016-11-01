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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.query.cache.SqlCacheImpl;
import com.jporm.rm.connection.Transaction;
import com.jporm.rm.connection.TransactionProvider;
import com.jporm.rm.session.Session;
import com.jporm.rm.session.SessionImpl;
import com.jporm.rm.session.SqlExecutorImpl;
import com.jporm.types.TypeConverter;
import com.jporm.types.TypeConverterBuilder;

/**
 *
 * @author Francesco Cina'
 *
 *         26/ago/2011
 */
public class JpoRmImpl implements JpoRm {

    private static AtomicInteger JPORM_INSTANCES_COUNT = new AtomicInteger();
    private final ServiceCatalog serviceCatalog;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Integer instanceCount;
    private final TransactionProvider transactionProvider;
    private final SqlFactory sqlFactory;
    private final SqlCache sqlCache;
    private Session session;

    /**
     * Create a new instance of JPOrm.
     *
     * @param sessionProvider
     */
    public JpoRmImpl(final TransactionProvider transactionProvider, final ServiceCatalog serviceCatalog) {
        this.transactionProvider = transactionProvider;
        instanceCount = JPORM_INSTANCES_COUNT.getAndIncrement();
        logger.info("Building new instance of JPO (instance [{}])", instanceCount);
        this.serviceCatalog = serviceCatalog;
        sqlFactory = new SqlFactory(serviceCatalog.getClassToolMap(), serviceCatalog.getPropertiesFactory(), transactionProvider.getDBProfile().getSqlRender());
        sqlCache = new SqlCacheImpl(sqlFactory, serviceCatalog.getClassToolMap(), transactionProvider.getDBProfile());
    }

    public TransactionProvider getTransactionProvider() {
        return transactionProvider;
    }

    public ServiceCatalog getServiceCatalog() {
        return serviceCatalog;
    }

    /**
     * Register a new {@link TypeConverter}. If a {@link TypeConverter} wraps a
     * Class that is already mapped, the last registered {@link TypeConverter}
     * will be used.
     *
     * @param typeConverter
     * @throws OrmConfigurationException
     */
    @Override
    public void register(final TypeConverter<?, ?> typeWrapper) {
        serviceCatalog.getTypeFactory().addTypeConverter(typeWrapper);
    }

    /**
     * Register a new {@link TypeConverterBuilder}. If a {@link TypeConverter}
     * wraps a Class that is already mapped, the last registered
     * {@link TypeConverter} will be used.
     *
     * @param typeConverterBuilder
     * @throws OrmConfigurationException
     */
    @Override
    public void register(final TypeConverterBuilder<?, ?> typeWrapperBuilder) {
        serviceCatalog.getTypeFactory().addTypeConverter(typeWrapperBuilder);
    }

    @Override
    public Transaction tx() {
        return transactionProvider.getTransaction(serviceCatalog, sqlCache, sqlFactory);
    }

    @Override
    public void txVoid(Consumer<Session> session) {
        tx().executeVoid(session);
    }

    @Override
    public <T> T tx(Function<Session, T> session) {
        return tx().execute(session);
    }

    @Override
    public Session session() {
        if (session == null) {
            synchronized (this) {
                final SqlExecutorImpl sqlExecutor = new SqlExecutorImpl(transactionProvider.getConnectionProvider(), serviceCatalog.getTypeFactory());
                Session tempSession = new SessionImpl(serviceCatalog, transactionProvider.getDBProfile(), sqlExecutor, sqlCache, sqlFactory);
                session = tempSession;
            }
        }
        return session;
    }

}
