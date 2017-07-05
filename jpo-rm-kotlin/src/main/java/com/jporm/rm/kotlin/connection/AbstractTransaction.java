/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.rm.kotlin.connection;


import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.inject.config.ConfigService;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rm.kotlin.session.Session;
import com.jporm.rm.kotlin.session.SqlExecutorImpl;
import com.jporm.rm.kotlin.session.SessionImpl;
import com.jporm.sql.dialect.DBProfile;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractTransaction implements Transaction {

    private final ServiceCatalog serviceCatalog;
    private final SqlCache sqlCache;
    private final SqlFactory sqlFactory;
    private final DBProfile dbProfile;
    private TransactionIsolation transactionIsolation;
    private int timeout;
    private boolean readOnly = false;

    public AbstractTransaction(final ServiceCatalog serviceCatalog, DBProfile dbProfile, SqlCache sqlCache, SqlFactory sqlFactory) {
        this.serviceCatalog = serviceCatalog;
        this.dbProfile = dbProfile;
        this.sqlCache = sqlCache;
        this.sqlFactory = sqlFactory;

        ConfigService configService = serviceCatalog.getConfigService();
        transactionIsolation = configService.getDefaultTransactionIsolation();
        timeout = configService.getTransactionDefaultTimeoutSeconds();

    }
    @Override
    public final Transaction isolation(final TransactionIsolation isolation) {
        transactionIsolation = isolation;
        return this;
    }

    @Override
    public final Transaction readOnly(final boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    @Override
    public final Transaction timeout(final int seconds) {
        timeout = seconds;
        return this;
    }

    protected Session newSession(final Connection connection) {
        final SqlExecutorImpl sqlExecutor = new SqlExecutorImpl(new ConnectionProvider<Connection>() {
            @Override
            public <T> T connection(boolean autoCommit, Function<Connection, T> connectionFunction) {
                return connectionFunction.apply(connection);
            }
        }, serviceCatalog.getTypeFactory());
        return new SessionImpl(serviceCatalog, dbProfile, sqlExecutor, sqlCache, sqlFactory);
    }

    /**
     * @return the transactionIsolation
     */
    protected final TransactionIsolation getTransactionIsolation() {
        return transactionIsolation;
    }

    /**
     * @return the timeout
     */
    protected final int getTimeout() {
        return timeout;
    }

    /**
     * @return the readOnly
     */
    protected final boolean isReadOnly() {
        return readOnly;
    }
    /**
     * @return the dbProfile
     */
    public DBProfile getDbProfile() {
        return dbProfile;
    }
}
