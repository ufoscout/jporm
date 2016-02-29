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

import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.inject.ServiceCatalog;
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

    private static Integer JPORM_INSTANCES_COUNT = Integer.valueOf(0);
    private final ServiceCatalog serviceCatalog;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Integer instanceCount;
    private final ConnectionProvider connectionProvider;
    private final SessionImpl session;
    private BiFunction<ConnectionProvider, ServiceCatalog, Transaction> transactionFactory = (_connectionProvider, _serviceCatalog) -> {
        return new TransactionImpl(_connectionProvider, _serviceCatalog);
    };

    /**
     * Create a new instance of JPOrm.
     *
     * @param sessionProvider
     */
    public JpoRmImpl(final ConnectionProvider connectionProvider, final ServiceCatalog serviceCatalog) {
        this.connectionProvider = connectionProvider;
        synchronized (JPORM_INSTANCES_COUNT) {
            instanceCount = JPORM_INSTANCES_COUNT++;
        }
        logger.info("Building new instance of JPO (instance [{}])", instanceCount);
        this.serviceCatalog = serviceCatalog;
        session = new SessionImpl(serviceCatalog, connectionProvider, true);
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
    public BiFunction<ConnectionProvider, ServiceCatalog, Transaction> getTransactionFactory() {
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
    public void setTransactionFactory(final BiFunction<ConnectionProvider, ServiceCatalog, Transaction> transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    @Override
    public Transaction transaction() {
        return transactionFactory.apply(connectionProvider, serviceCatalog);
    }

}
