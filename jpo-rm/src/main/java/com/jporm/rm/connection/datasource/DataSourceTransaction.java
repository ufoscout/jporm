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
package com.jporm.rm.connection.datasource;

import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.rm.connection.AbstractTransaction;
import com.jporm.rm.connection.Connection;
import com.jporm.rm.connection.ConnectionProvider;
import com.jporm.rm.session.Session;
import com.jporm.sql.dialect.DBProfile;

public class DataSourceTransaction extends AbstractTransaction {

    private final ConnectionProvider<DataSourceConnection> connectionProvider;

    public DataSourceTransaction(final ServiceCatalog serviceCatalog, DBProfile dbProfile, SqlCache sqlCache, SqlFactory sqlFactory, ConnectionProvider<DataSourceConnection> connectionProvider) {
        super(serviceCatalog, dbProfile, sqlCache, sqlFactory);
        this.connectionProvider = connectionProvider;
    }

    @Override
    public <T> T execute(final Function<Session, T> callback) {
        return connectionProvider.connection(false, connection -> {
            try {
                setTransactionIsolation(connection);
                setTimeout(connection, getTimeout());
                connection.setReadOnly(isReadOnly());
                Session session = newSession(connection);

                T result = callback.apply(session);
                if (!isReadOnly()) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
                return result;
            } catch (RuntimeException e) {
                connection.rollback();
                throw e;
            }
        });

    }

    @Override
    public void execute(final Consumer<Session> callback) {
        execute((session) -> {
            callback.accept(session);
            return null;
        });
    }

    private void setTimeout(final Connection connection, int timeout) {
        if (timeout >= 0) {
            connection.setTimeout(timeout);
        }
    }

    private void setTransactionIsolation(final Connection connection) {
        connection.setTransactionIsolation(getTransactionIsolation());
    }

}
