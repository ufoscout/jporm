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
package com.jporm.rm.kotlin.connection;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.SqlFactory;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.rm.kotlin.session.Session;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.dialect.DBType;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author Francesco Cina
 *
 *         24/giu/2011
 */
public class NullTransactionProvider implements TransactionProvider {

    private DBType dbType;

    public NullTransactionProvider() {
        this(DBType.UNKNOWN);
    }

    public NullTransactionProvider(final DBType dbType) {
        this.dbType = dbType;
    }

    @Override
    public DBProfile getDBProfile() {
        return dbType.getDBProfile();
    }

    @Override
    public Transaction getTransaction(ServiceCatalog serviceCatalog, SqlCache sqlCache, SqlFactory sqlFactory) {
        return new AbstractTransaction(serviceCatalog, getDBProfile(), sqlCache, sqlFactory) {

            @Override
            public void execute(Consumer<Session> session) {
            }

            @Override
            public <T> T execute(Function<Session, T> session) {
                return null;
            }
        };
    }

    @Override
    public ConnectionProvider<Connection> getConnectionProvider() {
        return new ConnectionProvider<Connection>() {
            @Override
            public <T> T connection(boolean autoCommit, Function<Connection, T> connection) {
                return null;
            }
        };
    }

}
