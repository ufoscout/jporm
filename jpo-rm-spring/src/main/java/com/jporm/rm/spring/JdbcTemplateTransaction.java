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

import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.rm.connection.AbstractTransaction;
import com.jporm.rm.session.Session;
import com.jporm.sql.dialect.DBProfile;

public class JdbcTemplateTransaction extends AbstractTransaction {

    private final PlatformTransactionManager platformTransactionManager;
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateTransaction(final ServiceCatalog serviceCatalog, DBProfile dbProfile, SqlCache sqlCache, SqlFactory sqlFactory, JdbcTemplate jdbcTemplate, PlatformTransactionManager platformTransactionManager)  {
        super(serviceCatalog, dbProfile, sqlCache, sqlFactory);
        this.jdbcTemplate = jdbcTemplate;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Override
    public <T> T execute(final Function<Session, T> callback) {
        try {
            DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
            definition.setIsolationLevel(getTransactionIsolation().getTransactionIsolation());
            int timeout = getTimeout();
            if (timeout  >= 0) {
                definition.setTimeout(timeout);
            }
            definition.setReadOnly(isReadOnly());
            JdbcTemplateConnection connection = new JdbcTemplateConnection(jdbcTemplate, getDbProfile().getStatementStrategy());
            Session session =  newSession(connection);
            TransactionTemplate tt = new TransactionTemplate(platformTransactionManager, definition);
            return tt.execute(transactionStatus -> callback.apply(session));
        } catch (final Exception e) {
            throw JdbcTemplateExceptionTranslator.doTranslate(e);
        }
    }


    @Override
    public void executeVoid(final Consumer<Session> callback) {
        execute((session) -> {
            callback.accept(session);
            return null;
        });
    }

}
