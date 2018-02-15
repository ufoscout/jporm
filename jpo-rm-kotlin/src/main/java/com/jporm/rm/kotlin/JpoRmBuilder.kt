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
package com.jporm.rm.kotlin

import com.jporm.commons.core.builder.AbstractJpoBuilder
import com.jporm.sql.dialect.DBProfile

import javax.sql.DataSource

class JpoRmBuilder private constructor() : AbstractJpoBuilder<JpoRmBuilder>() {

    /**
     * Create a [JpoRm] instance

     * @param transactionProvider
     * *
     * @return
     */
    fun build(transactionProvider: TransactionProvider): JpoRm {
        return JpoRmImpl(transactionProvider, serviceCatalog)
    }

    /**
     * Create a [JpoRm] instance

     * @param dataSource
     * *
     * @return
     */
    fun build(dataSource: DataSource): JpoRm {
        return build(KotlinDataSourceTransactionProvider(dataSource))
    }

    /**
     * Create a [JpoRm] instance

     * @param dataSource
     * *
     * @param dbType
     * *
     * @return
     */
    fun build(dataSource: DataSource, dbType: DBProfile): JpoRm {
        return build(KotlinDataSourceTransactionProvider(dataSource, dbType))
    }

    companion object {

        fun get(): JpoRmBuilder {
            return JpoRmBuilder()
        }
    }

}
