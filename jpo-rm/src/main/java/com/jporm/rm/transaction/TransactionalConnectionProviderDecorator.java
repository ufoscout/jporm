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
package com.jporm.rm.transaction;

import com.jporm.commons.core.connection.Connection;
import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.sql.dialect.DBProfile;

public class TransactionalConnectionProviderDecorator implements ConnectionProvider {

    private final Connection connection;
    private final DBProfile dbProfile;

    public TransactionalConnectionProviderDecorator(final Connection connection, final DBProfile dbProfile) {
        this.connection = connection;
        this.dbProfile = dbProfile;
    }

    @Override
    public Connection getConnection(final boolean autoCommit) {
        return new CloseSuppressingConnectionDecorator(connection);
    }

    @Override
    public DBProfile getDBProfile() {
        return dbProfile;
    }

}
