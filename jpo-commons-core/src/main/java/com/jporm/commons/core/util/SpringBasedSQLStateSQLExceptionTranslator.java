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
package com.jporm.commons.core.util;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.HashSet;
import java.util.Set;

import com.jporm.commons.core.exception.JpoTransactionTimedOutException;
import com.jporm.commons.core.exception.sql.JpoSqlBadGrammarException;
import com.jporm.commons.core.exception.sql.JpoSqlConcurrencyFailureException;
import com.jporm.commons.core.exception.sql.JpoSqlDataAccessResourceFailureException;
import com.jporm.commons.core.exception.sql.JpoSqlDataIntegrityViolationException;
import com.jporm.commons.core.exception.sql.JpoSqlException;
import com.jporm.commons.core.exception.sql.JpoSqlTransientDataAccessResourceException;

/**
 *
 * An exception wrapper based on the code of
 * org.springframework.jdbc.support.SQLStateSQLExceptionTranslator of
 * spring-jdbc module
 *
 * @author cinafr
 *
 */
public class SpringBasedSQLStateSQLExceptionTranslator {

    private static final Set<String> BAD_SQL_GRAMMAR_CODES = new HashSet<String>(8);

    private static final Set<String> DATA_INTEGRITY_VIOLATION_CODES = new HashSet<String>(8);

    private static final Set<String> DATA_ACCESS_RESOURCE_FAILURE_CODES = new HashSet<String>(8);

    private static final Set<String> TRANSIENT_DATA_ACCESS_RESOURCE_CODES = new HashSet<String>(8);

    private static final Set<String> CONCURRENCY_FAILURE_CODES = new HashSet<String>(4);

    static {
        BAD_SQL_GRAMMAR_CODES.add("07"); // Dynamic SQL error //$NON-NLS-1$
        BAD_SQL_GRAMMAR_CODES.add("21"); // Cardinality violation //$NON-NLS-1$
        BAD_SQL_GRAMMAR_CODES.add("2A"); // Syntax error direct //$NON-NLS-1$
                                         // SQL
        BAD_SQL_GRAMMAR_CODES.add("37"); // Syntax error dynamic //$NON-NLS-1$
                                         // SQL
        BAD_SQL_GRAMMAR_CODES.add("42"); // General SQL syntax //$NON-NLS-1$
                                         // error
        BAD_SQL_GRAMMAR_CODES.add("65"); // Oracle: unknown //$NON-NLS-1$
                                         // identifier
        BAD_SQL_GRAMMAR_CODES.add("S0"); // MySQL uses this - from //$NON-NLS-1$
                                         // ODBC error codes?

        DATA_INTEGRITY_VIOLATION_CODES.add("01"); // Data //$NON-NLS-1$
                                                  // truncation
        DATA_INTEGRITY_VIOLATION_CODES.add("02"); // No data found //$NON-NLS-1$
        DATA_INTEGRITY_VIOLATION_CODES.add("22"); // Value out of //$NON-NLS-1$
                                                  // range
        DATA_INTEGRITY_VIOLATION_CODES.add("23"); // Integrity //$NON-NLS-1$
                                                  // constraint violation
        DATA_INTEGRITY_VIOLATION_CODES.add("27"); // Triggered //$NON-NLS-1$
                                                  // data change violation
        DATA_INTEGRITY_VIOLATION_CODES.add("44"); // With check //$NON-NLS-1$
                                                  // violation

        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("08"); // Connection //$NON-NLS-1$
                                                      // exception
        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("53"); // PostgreSQL: //$NON-NLS-1$
                                                      // insufficient resources
                                                      // (e.g. disk full)
        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("54"); // PostgreSQL: //$NON-NLS-1$
                                                      // program limit exceeded
                                                      // (e.g. statement too
                                                      // complex)
        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("57"); // DB2: //$NON-NLS-1$
                                                      // out-of-memory exception
                                                      // / database not started
        DATA_ACCESS_RESOURCE_FAILURE_CODES.add("58"); // DB2: //$NON-NLS-1$
                                                      // unexpected system error

        TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("JW"); // Sybase: //$NON-NLS-1$
                                                        // internal I/O error
        TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("JZ"); // Sybase: //$NON-NLS-1$
                                                        // unexpected I/O error
        TRANSIENT_DATA_ACCESS_RESOURCE_CODES.add("S1"); // DB2: //$NON-NLS-1$
                                                        // communication failure

        CONCURRENCY_FAILURE_CODES.add("40"); // Transaction //$NON-NLS-1$
                                             // rollback
        CONCURRENCY_FAILURE_CODES.add("61"); // Oracle: deadlock //$NON-NLS-1$
    }

    private static String buildMessage(final String task, final String sql, final SQLException ex) {
        return task + "; SQL [" + sql + "]; " + ex.getMessage(); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static RuntimeException doTranslate(final String task, final String sql, final SQLException ex) {
        if (ex instanceof SQLTimeoutException) {
            return new JpoTransactionTimedOutException(ex);
        }
        String sqlState = getSqlState(ex);
        if ((sqlState != null) && (sqlState.length() >= 2)) {
            String classCode = sqlState.substring(0, 2);
            if (BAD_SQL_GRAMMAR_CODES.contains(classCode)) {
                return new JpoSqlBadGrammarException(buildMessage(task, sql, ex), ex);
            } else if (DATA_INTEGRITY_VIOLATION_CODES.contains(classCode)) {
                return new JpoSqlDataIntegrityViolationException(buildMessage(task, sql, ex), ex);
            } else if (DATA_ACCESS_RESOURCE_FAILURE_CODES.contains(classCode)) {
                return new JpoSqlDataAccessResourceFailureException(buildMessage(task, sql, ex), ex);
            } else if (TRANSIENT_DATA_ACCESS_RESOURCE_CODES.contains(classCode)) {
                return new JpoSqlTransientDataAccessResourceException(buildMessage(task, sql, ex), ex);
            } else if (CONCURRENCY_FAILURE_CODES.contains(classCode)) {
                return new JpoSqlConcurrencyFailureException(buildMessage(task, sql, ex), ex);
            }
        }
        return new JpoSqlException(buildMessage(task, sql, ex), ex);
    }

    /**
     * Gets the SQL state code from the supplied {@link SQLException exception}.
     * <p>
     * Some JDBC drivers nest the actual exception from a batched update, so we
     * might need to dig down into the nested exception.
     * 
     * @param ex
     *            the exception from which the {@link SQLException#getSQLState()
     *            SQL state} is to be extracted
     * @return the SQL state code
     */
    private static String getSqlState(final SQLException ex) {
        String sqlState = ex.getSQLState();
        if (sqlState == null) {
            SQLException nestedEx = ex.getNextException();
            if (nestedEx != null) {
                sqlState = nestedEx.getSQLState();
            }
        }
        return sqlState;
    }

    private SpringBasedSQLStateSQLExceptionTranslator() {
    }

}
