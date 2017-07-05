/*******************************************************************************
 * Copyright 2016 Francesco Cina'

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jporm.rm.quasar.connection.datasource

import java.sql.SQLException
import java.util.function.Function

import javax.sql.DataSource

import com.jporm.commons.core.async.AsyncTaskExecutor
import com.jporm.rm.connection.ConnectionProvider
import com.jporm.rm.connection.datasource.DataSourceConnection
import com.jporm.rm.connection.datasource.DataSourceConnectionImpl
import com.jporm.rm.quasar.connection.JpoCompletableWrapper
import com.jporm.sql.dialect.DBProfile

class KotlinDataSourceConnectionProvider internal constructor(private val dataSource: DataSource, private val dbProfile: DBProfile, private val connectionExecutor: AsyncTaskExecutor, private val executor: AsyncTaskExecutor) : ConnectionProvider<DataSourceConnection> {

    override fun <T> connection(autoCommit: Boolean, connection: Function<DataSourceConnection, T>): T {
        try {
            KotlinDataSourceConnection(DataSourceConnectionImpl(sqlConnection, dbProfile), executor).use({ dataSourceConnection ->
                dataSourceConnection.setAutoCommit(autoCommit)
                return connection.apply(dataSourceConnection)
            })
        } catch (e: RuntimeException) {
            throw e
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }

    }

    private val sqlConnection: java.sql.Connection
        @Throws(SQLException::class)
        get() = JpoCompletableWrapper.get(connectionExecutor.execute({
            try {
                return@connectionExecutor.execute dataSource . getConnection ()
            } catch (e: SQLException) {
                throw DataSourceConnectionImpl.translateException("close", "", e)
            }
        }))

}
