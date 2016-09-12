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
package com.jporm.rm.connection;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

/**
 *
 * @author Francesco Cina'
 *
 *         Dec 20, 2011
 *
 *         The implementations of this class MUST be stateless and Thread safe.
 *
 */
public interface Connection {

    int[] batchUpdate(Collection<String> sqls, Function<String, String> sqlPreProcessor) throws JpoException;

    int[] batchUpdate(String sql, BatchPreparedStatementSetter psc) throws JpoException;

    int[] batchUpdate(String sql, Collection<Consumer<Statement>> statementSetters) throws JpoException;

    void execute(String sql) throws JpoException;

    <T> T query(String sql, final Consumer<Statement> statementSetter, Function<ResultSet, T> resultSetReader) throws JpoException;

    void setReadOnly(boolean readOnly);

    void setTimeout(int timeout);

    void setTransactionIsolation(TransactionIsolation isolationLevel);

    int update(String sql, final Consumer<Statement> statementSetter);

    <R> R update(String sql, GeneratedKeyReader<R> generatedKeyReader, final Consumer<Statement> statementSetter);

}
