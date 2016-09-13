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
package com.jporm.rm.spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.util.DBTypeDescription;
import com.jporm.rm.connection.Transaction;
import com.jporm.rm.connection.TransactionProvider;
import com.jporm.sql.dialect.DBProfile;

public class JdbcTemplateTransactionProvider implements TransactionProvider {

    private DBProfile dbType;
    private JdbcTemplateConnectionProvider connectionProvider;
    private final JdbcTemplate jdbcTemplate;
    private final PlatformTransactionManager platformTransactionManager;

    JdbcTemplateTransactionProvider(final JdbcTemplate jdbcTemplate, final PlatformTransactionManager platformTransactionManager) {
        this(jdbcTemplate, platformTransactionManager, null);
    }

    JdbcTemplateTransactionProvider(final JdbcTemplate jdbcTemplate, final PlatformTransactionManager platformTransactionManager, DBProfile dbProfile) {
        this.jdbcTemplate = jdbcTemplate;
        this.platformTransactionManager = platformTransactionManager;
        dbType = dbProfile;
    }

    @Override
    public final DBProfile getDBProfile() {
        if (dbType == null) {
            dbType = DBTypeDescription.build(jdbcTemplate.getDataSource()).getDBType().getDBProfile();
        }
        return dbType;
    }

    @Override
    public Transaction getTransaction(ServiceCatalog serviceCatalog, SqlCache sqlCache, SqlFactory sqlFactory) {
        return new JdbcTemplateTransaction(serviceCatalog, getDBProfile(), sqlCache, sqlFactory, getConnectionProvider(), platformTransactionManager);
    }

    @Override
    public JdbcTemplateConnectionProvider getConnectionProvider() {
        if ( connectionProvider == null) {
            connectionProvider = new JdbcTemplateConnectionProvider(jdbcTemplate, getDBProfile().getStatementStrategy());
        }
        return connectionProvider;
    }

}
