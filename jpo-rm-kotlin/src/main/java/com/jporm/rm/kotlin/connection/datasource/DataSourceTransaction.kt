/*******************************************************************************
 * Copyright 2015 Francesco Cina'

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

import com.jporm.commons.core.inject.ServiceCatalog
import com.jporm.commons.core.query.SqlFactory
import com.jporm.commons.core.query.cache.SqlCache
import com.jporm.rm.kotlin.connection.AbstractTransaction
import com.jporm.rm.kotlin.connection.Connection
import com.jporm.rm.kotlin.connection.ConnectionProvider
import com.jporm.rm.kotlin.session.Session
import com.jporm.sql.dialect.DBProfile

class DataSourceTransaction(serviceCatalog: ServiceCatalog, dbProfile: DBProfile, sqlCache: SqlCache, sqlFactory: SqlFactory, private val connectionProvider: ConnectionProvider<DataSourceConnection>) : AbstractTransaction(serviceCatalog, dbProfile, sqlCache, sqlFactory) {

    override fun <T> execute(callback: (Session) -> T): T {
        return connectionProvider.connection(false) {
            execute(it, callback)
        }
    }

    private fun <T> execute(connection: DataSourceConnection, callback: (Session) -> T) : T {
        try {
            setTransactionIsolation(connection)
            setTimeout(connection, timeout)
            connection.setReadOnly(isReadOnly)
            val session = newSession(connection)

            val result: T = callback(session)
            if (!isReadOnly) {
                connection.commit()
            } else {
                connection.rollback()
            }
            return result
        } catch (e: RuntimeException) {
            connection.rollback()
            throw e
        }
    }

    override fun execute(callback: (Session) -> Unit) {
        execute({
            callback(it)
            null
        })
    }

    private fun setTimeout(connection: Connection, timeout: Int) {
        if (timeout >= 0) {
            connection.setTimeout(timeout)
        }
    }

    private fun setTransactionIsolation(connection: Connection) {
        connection.setTransactionIsolation(transactionIsolation)
    }

}
