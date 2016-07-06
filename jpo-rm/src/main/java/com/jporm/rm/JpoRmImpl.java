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
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.query.cache.SqlCacheImpl;
import com.jporm.rm.session.Session;
import com.jporm.rm.session.SessionImpl;
import com.jporm.rm.transaction.Transaction;
import com.jporm.rm.transaction.TransactionImpl;
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
    private final ConnectionProvider connectionProvider;
    private final SessionImpl session;
    private final SqlFactory sqlFactory;
    private final SqlCache sqlCache;

    private Supplier<Transaction> transactionFactory = () -> {
        return new TransactionImpl(getConnectionProvider(), getServiceCatalog(), getSqlCache(), getSqlFactory());
    };

    /**
     * Create a new instance of JPOrm.
     *
     * @param sessionProvider
     */
    public JpoRmImpl(final ConnectionProvider connectionProvider, final ServiceCatalog serviceCatalog) {
        this.connectionProvider = connectionProvider;
        instanceCount = JPORM_INSTANCES_COUNT.getAndIncrement();
        logger.info("Building new instance of JPO (instance [{}])", instanceCount);
        this.serviceCatalog = serviceCatalog;
        sqlFactory = new SqlFactory(serviceCatalog.getClassToolMap(), serviceCatalog.getPropertiesFactory(), connectionProvider.getDBProfile().getSqlRender());
        sqlCache = new SqlCacheImpl(sqlFactory, serviceCatalog.getClassToolMap(), connectionProvider.getDBProfile());
        session = new SessionImpl(serviceCatalog, connectionProvider, true, sqlCache, sqlFactory);
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    public ServiceCatalog getServiceCatalog() {
        return serviceCatalog;
    }

    /**
     * @return the transactionFactory
     */
    public Supplier<Transaction> getTransactionFactory() {
        return transactionFactory;
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
    public final Session session() {
        return session;
    }

    /**
     * @param transactionFactory
     *            the transactionFactory to set
     */
    public void setTransactionFactory(final Supplier<Transaction> transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    @Override
    public Transaction transaction() {
        return transactionFactory.get();
    }

    /**
     * @return the sqlFactory
     */
    public SqlFactory getSqlFactory() {
        return sqlFactory;
    }

    /**
     * @return the sqlCache
     */
    public SqlCache getSqlCache() {
        return sqlCache;
    }

}
