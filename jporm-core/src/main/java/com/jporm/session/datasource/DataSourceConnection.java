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
package com.jporm.session.datasource;

import java.sql.PreparedStatement;

import com.jporm.dialect.querytemplate.QueryTemplate;
import com.jporm.exception.OrmException;

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

    boolean isValid() throws OrmException;

    void setTransactionIsolation(int transactionIsolation) throws OrmException;

    boolean isClosed() throws OrmException;

    void rollback() throws OrmException;

    void commit() throws OrmException;

    PreparedStatement prepareStatement(String sql) throws OrmException;

    PreparedStatement prepareStatement(String sql, String[] generatedColumnNames, final QueryTemplate queryTemplate) throws OrmException;

    DataSourceStatement createStatement() throws OrmException;

    void addCaller(DataSourceConnectionCaller connectionCaller) throws OrmException;

    void close(DataSourceConnectionCaller connectionCaller) throws OrmException;

    void setRollbackOnly() throws OrmException;

    void setReadOnly(boolean readOnly) throws OrmException;

    boolean isRollbackOnly();

}
