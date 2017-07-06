/*******************************************************************************
 * Copyright 2013 Francesco Cina'

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

import com.jporm.rm.kotlin.connection.Transaction
import com.jporm.rm.kotlin.session.Session
import com.jporm.types.TypeConverter
import com.jporm.types.builder.TypeConverterBuilder

/**

 * @author Francesco Cina
 * *
 * *         21/mag/2011
 */
interface JpoRm {

    /**
     * Register a new [TypeConverter]. If a [TypeConverter] wraps a
     * Class that is already mapped, the last registered [TypeConverter]
     * will be used.

     * @param typeConverter
     */
    fun register(typeWrapper: TypeConverter<*, *>)

    /**
     * Register a new [TypeConverterBuilder]. If a [TypeConverter]
     * wraps a Class that is already mapped, the last registered
     * [TypeConverter] will be used.

     * @param typeConverterBuilder
     */
    fun register(typeWrapperBuilder: TypeConverterBuilder<*, *>)

    /**
     * Return a [Session] from the current [JpoRm] implementation

     * @return
     */
    fun session(): Session

    /**
     * Returns a new [Transaction] instance.

     * @return
     */
    fun tx(): Transaction

    /**
     * Execute a new transaction.

     * @return
     */
    fun tx(session: (Session) -> Unit)

    /**
     * Execute a new transaction.

     * @return
     */
    fun <T> tx(session: (Session) -> T): T

}
