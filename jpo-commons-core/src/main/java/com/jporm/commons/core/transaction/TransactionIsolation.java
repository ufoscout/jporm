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
package com.jporm.commons.core.transaction;

import java.sql.Connection;

/**
 *
 * @author Francesco Cina
 *
 *         13/giu/2011
 */
public enum TransactionIsolation {

    /**
     * Indicates that dirty reads, non-repeatable reads and phantom reads are
     * prevented.
     * <p>
     * This level includes the prohibitions in
     * {@link #ISOLATION_REPEATABLE_READ} and further prohibits the situation
     * where one transaction reads all rows that satisfy a <code>WHERE</code>
     * condition, a second transaction inserts a row that satisfies that
     * <code>WHERE</code> condition, and the first transaction re-reads for the
     * same condition, retrieving the additional "phantom" row in the second
     * read.
     * 
     * @see java.sql.Connection#TRANSACTION_SERIALIZABLE
     */
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE),

    /**
     * Indicates that dirty reads and non-repeatable reads are prevented;
     * phantom reads can occur.
     * <p>
     * This level prohibits a transaction from reading a row with uncommitted
     * changes in it, and it also prohibits the situation where one transaction
     * reads a row, a second transaction alters the row, and the first
     * transaction re-reads the row, getting different values the second time (a
     * "non-repeatable read").
     * 
     * @see java.sql.Connection#TRANSACTION_REPEATABLE_READ
     */
    REPEATABLE_READS(Connection.TRANSACTION_REPEATABLE_READ),

    /**
     * Indicates that dirty reads are prevented; non-repeatable reads and
     * phantom reads can occur. This is the default Transaction Isolation level.
     * <p>
     * This level only prohibits a transaction from reading a row with
     * uncommitted changes in it.
     * 
     * @see java.sql.Connection#TRANSACTION_READ_COMMITTED
     */
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

    /**
     * Indicates that dirty reads, non-repeatable reads and phantom reads can
     * occur.
     * <p>
     * This level allows a row changed by one transaction to be read by another
     * transaction before any changes in that row have been committed (a
     * "dirty read"). If any of the changes are rolled back, the second
     * transaction will have retrieved an invalid row.
     * 
     * @see java.sql.Connection#TRANSACTION_READ_UNCOMMITTED
     */
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED);

    private int isolationLevel;

    private TransactionIsolation(final int isolationLevel) {
        this.isolationLevel = isolationLevel;
    }

    public int getTransactionIsolation() {
        return isolationLevel;
    }
}
