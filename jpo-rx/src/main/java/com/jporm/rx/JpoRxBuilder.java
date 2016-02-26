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

import javax.sql.DataSource;

import com.jporm.commons.core.builder.AbstractJpoBuilder;
import com.jporm.commons.core.connection.AsyncConnectionProvider;
import com.jporm.commons.core.connection.AsyncConnectionWrapperProvider;
import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.connection.DataSourceConnectionProvider;
import com.jporm.sql.dsl.dialect.DBType;

/**
 *
 * @author cinafr
 *
 */
public class JpoRxBuilder extends AbstractJpoBuilder<JpoRxBuilder> {

    public static JpoRxBuilder get() {
        return new JpoRxBuilder();
    }

    private JpoRxBuilder() {

    }

    /**
     * Create a {@link JpoRx} instance
     * 
     * @param connectionProvider
     * @return
     */
    public JpoRx build(final AsyncConnectionProvider connectionProvider) {
        return new JpoRxImpl(connectionProvider, getServiceCatalog());
    }

    /**
     * Create a {@link JpoRx} instance
     * 
     * @param connectionProvider
     * @return
     */
    public JpoRx build(final ConnectionProvider connectionProvider) {
        return build(new AsyncConnectionWrapperProvider(connectionProvider, getServiceCatalog().getAsyncTaskExecutor()));
    }

    /**
     * Create a {@link JpoRx} instance
     * 
     * @param dataSource
     * @param dbType
     * @return
     */
    public JpoRx build(final DataSource dataSource) {
        return build(new DataSourceConnectionProvider(dataSource));
    }

    /**
     * Create a {@link JpoRx} instance
     * 
     * @param dataSource
     * @param dbType
     * @return
     */
    public JpoRx build(final DataSource dataSource, final DBType dbType) {
        return build(new DataSourceConnectionProvider(dataSource, dbType));
    }

}
