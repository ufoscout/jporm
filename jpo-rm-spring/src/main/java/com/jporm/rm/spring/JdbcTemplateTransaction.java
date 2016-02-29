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
package com.jporm.rm.spring;

import java.util.concurrent.CompletableFuture;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.inject.config.ConfigService;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rm.session.Session;
import com.jporm.rm.session.SessionImpl;
import com.jporm.rm.transaction.Transaction;
import com.jporm.rm.transaction.TransactionCallback;
import com.jporm.rm.transaction.TransactionVoidCallback;

public class JdbcTemplateTransaction implements Transaction {

    private final ConnectionProvider sessionProvider;
    private final ServiceCatalog serviceCatalog;
    private final PlatformTransactionManager platformTransactionManager;
    private final SqlCache sqlCache;
    private final SqlFactory sqlFactory;
    private TransactionIsolation transactionIsolation;
    private int timeout;
    private boolean readOnly = false;

    public JdbcTemplateTransaction(final ConnectionProvider sessionProvider, final ServiceCatalog serviceCatalog,
            final PlatformTransactionManager platformTransactionManager, SqlCache sqlCache, SqlFactory sqlFactory) {
        this.serviceCatalog = serviceCatalog;
        this.sessionProvider = sessionProvider;
        this.platformTransactionManager = platformTransactionManager;
        this.sqlCache = sqlCache;
        this.sqlFactory = sqlFactory;

        ConfigService configService = serviceCatalog.getConfigService();
        transactionIsolation = configService.getDefaultTransactionIsolation();
        timeout = configService.getTransactionDefaultTimeoutSeconds();

    }

    @Override
    public <T> T execute(final TransactionCallback<T> callback) {
        try {
            DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
            definition.setIsolationLevel(transactionIsolation.getTransactionIsolation());
            if (timeout >= 0) {
                definition.setTimeout(timeout);
            } else {
                definition.setTimeout(serviceCatalog.getConfigService().getTransactionDefaultTimeoutSeconds());
            }
            definition.setReadOnly(readOnly);

            Session session = new SessionImpl(serviceCatalog, sessionProvider, false, sqlCache, sqlFactory);
            TransactionTemplate tt = new TransactionTemplate(platformTransactionManager, definition);
            return tt.execute(transactionStatus -> callback.doInTransaction(session));
        } catch (final Exception e) {
            throw JdbcTemplateExceptionTranslator.doTranslate(e);
        }
    }

    @Override
    public <T> CompletableFuture<T> executeAsync(final TransactionCallback<T> callback) {
        return serviceCatalog.getAsyncTaskExecutor().execute(() -> {
            return execute(callback);
        });
    }

    @Override
    public void executeVoid(final TransactionVoidCallback callback) {
        execute((session) -> {
            callback.doInTransaction(session);
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> executevoidAsync(final TransactionVoidCallback callback) {
        return serviceCatalog.getAsyncTaskExecutor().execute(() -> {
            executeVoid(callback);
        });
    }

    @Override
    public Transaction isolation(final TransactionIsolation isolation) {
        transactionIsolation = isolation;
        return this;
    }

    @Override
    public Transaction readOnly(final boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    @Override
    public Transaction timeout(final int seconds) {
        timeout = seconds;
        return this;
    }

}
