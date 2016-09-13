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
package com.jporm.rm.quasar.connection.datasource;

import java.sql.SQLException;
import java.util.function.Function;

import javax.sql.DataSource;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.rm.connection.ConnectionProvider;
import com.jporm.rm.connection.datasource.DataSourceConnection;
import com.jporm.rm.connection.datasource.DataSourceConnectionImpl;
import com.jporm.rm.quasar.connection.JpoCompletableWrapper;
import com.jporm.sql.dialect.DBProfile;

public class QuasarDataSourceConnectionProvider implements ConnectionProvider<DataSourceConnection> {

    private final DataSource dataSource;
    private final DBProfile dbProfile;
    private final AsyncTaskExecutor connectionExecutor;
    private final AsyncTaskExecutor executor;

    QuasarDataSourceConnectionProvider(DataSource dataSource, DBProfile dbProfile, AsyncTaskExecutor connectionExecutor, AsyncTaskExecutor executor) {
        this.dataSource = dataSource;
        this.dbProfile = dbProfile;
        this.connectionExecutor = connectionExecutor;
        this.executor = executor;
    }

    @Override
    public <T> T connection(boolean autoCommit, Function<DataSourceConnection, T> connection) {
        try (DataSourceConnection dataSourceConnection = new QuasarDataSourceConnection(new DataSourceConnectionImpl(getSqlConnection(), dbProfile), executor)) {
            dataSourceConnection.setAutoCommit(autoCommit);
            return connection.apply(dataSourceConnection);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private java.sql.Connection getSqlConnection() throws SQLException {
        return JpoCompletableWrapper.get(connectionExecutor.execute(() -> {
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                throw DataSourceConnectionImpl.translateException("close", "", e);
            }
        }));
    }

}
