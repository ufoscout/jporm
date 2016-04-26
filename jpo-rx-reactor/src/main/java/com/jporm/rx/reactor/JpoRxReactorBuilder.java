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
package com.jporm.rx.reactor;

import javax.sql.DataSource;

import com.jporm.commons.core.builder.AbstractJpoBuilder;
import com.jporm.commons.core.connection.AsyncConnectionProvider;
import com.jporm.commons.core.connection.AsyncConnectionWrapperProvider;
import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.connection.DataSourceConnectionProvider;
import com.jporm.sql.dialect.DBProfile;

/**
 *
 * @author cinafr
 *
 */
public class JpoRxReactorBuilder extends AbstractJpoBuilder<JpoRxReactorBuilder> {

    public static JpoRxReactorBuilder get() {
        return new JpoRxReactorBuilder();
    }

    private JpoRxReactorBuilder() {

    }

    /**
     * Create a {@link JpoRxReactor} instance
     *
     * @param connectionProvider
     * @return
     */
    public JpoRxReactor build(final AsyncConnectionProvider connectionProvider) {
        return new JpoRxImpl(connectionProvider, getServiceCatalog());
    }

    /**
     * Create a {@link JpoRxReactor} instance
     *
     * @param connectionProvider
     * @return
     */
    public JpoRxReactor build(final ConnectionProvider connectionProvider) {
        return build(new AsyncConnectionWrapperProvider(connectionProvider, getServiceCatalog().getAsyncTaskExecutor()));
    }

    /**
     * Create a {@link JpoRxReactor} instance
     *
     * @param dataSource
     * @param dbType
     * @return
     */
    public JpoRxReactor build(final DataSource dataSource) {
        return build(new DataSourceConnectionProvider(dataSource));
    }

    /**
     * Create a {@link JpoRxReactor} instance
     *
     * @param dataSource
     * @param dbType
     * @return
     */
    public JpoRxReactor build(final DataSource dataSource, final DBProfile dbType) {
        return build(new DataSourceConnectionProvider(dataSource, dbType));
    }

}