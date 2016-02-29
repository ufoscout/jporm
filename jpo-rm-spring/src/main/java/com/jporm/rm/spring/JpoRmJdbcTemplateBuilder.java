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
package com.jporm.rm.spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.jporm.commons.core.builder.AbstractJpoBuilder;
import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.rm.JpoRm;
import com.jporm.rm.JpoRmImpl;

/**
 *
 * @author cinafr
 *
 */
public class JpoRmJdbcTemplateBuilder extends AbstractJpoBuilder<JpoRmJdbcTemplateBuilder> {

    public static JpoRmJdbcTemplateBuilder get() {
        return new JpoRmJdbcTemplateBuilder();
    }

    private JpoRmJdbcTemplateBuilder() {

    }

    /**
     * Create a {@link JpoRm} instance
     *
     * @param sessionProvider
     * @return
     */
    public JpoRm build(final JdbcTemplate jdbcTemplate, final PlatformTransactionManager platformTransactionManager) {
        JpoRmImpl jpo = new JpoRmImpl(new JdbcTemplateConnectionProvider(jdbcTemplate, platformTransactionManager), getServiceCatalog());
        ConnectionProvider connectionProvider = jpo.getConnectionProvider();
        ServiceCatalog serviceCatalog = jpo.getServiceCatalog();
        SqlCache sqlCache = jpo.getSqlCache();
        SqlFactory sqlFactory = jpo.getSqlFactory();
        jpo.setTransactionFactory(() -> {
            return new JdbcTemplateTransaction(connectionProvider, serviceCatalog, platformTransactionManager, sqlCache, sqlFactory);
        });
        return jpo;
    }

}
