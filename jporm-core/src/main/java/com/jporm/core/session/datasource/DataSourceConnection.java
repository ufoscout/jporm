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
package com.jporm.core.session.datasource;

import java.sql.PreparedStatement;

import com.jporm.core.exception.JpoException;
import com.jporm.sql.dialect.statement.StatementStrategy;

/**
 *
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 9, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public interface DataSourceConnection {

    boolean isValid() throws JpoException;

    void setTransactionIsolation(int transactionIsolation) throws JpoException;

    boolean isClosed() throws JpoException;

    void rollback() throws JpoException;

    void commit() throws JpoException;

    PreparedStatement prepareStatement(String sql) throws JpoException;

    PreparedStatement prepareStatement(String sql, String[] generatedColumnNames, final StatementStrategy statementStrategy) throws JpoException;

    DataSourceStatement createStatement() throws JpoException;

    void addCaller(DataSourceConnectionCaller connectionCaller) throws JpoException;

    void close(DataSourceConnectionCaller connectionCaller) throws JpoException;

    void setRollbackOnly() throws JpoException;

    void setReadOnly(boolean readOnly) throws JpoException;

    boolean isRollbackOnly();

}
