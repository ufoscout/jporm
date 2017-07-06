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
package com.jporm.rm.kotlin.connection.datasource

import com.jporm.rm.kotlin.connection.ConnectionProvider
import com.jporm.sql.dialect.DBProfile

import javax.sql.DataSource
import java.util.function.Function

class DataSourceConnectionProvider internal constructor(private val dataSource: DataSource, private val dbProfile: DBProfile) : ConnectionProvider<DataSourceConnection> {

    override fun <T> connection(autoCommit: Boolean, connection: (DataSourceConnection) -> T): T {
        try {
            DataSourceConnectionImpl(dataSource.connection, dbProfile).use { dataSourceConnection ->
                dataSourceConnection.setAutoCommit(autoCommit)
                return connection(dataSourceConnection)
            }
        } catch (e: RuntimeException) {
            throw e
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }

    }

}
