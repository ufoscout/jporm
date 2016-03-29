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
package com.jporm.commons.core.connection;

import java.util.Collection;
import java.util.function.Function;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

/**
 *
 * @author Francesco Cina'
 *
 *         Dec 20, 2011
 */
public class NullConnection implements Connection {

    @Override
    public int[] batchUpdate(Collection<String> sqls, Function<String, String> sqlPreProcessor) throws JpoException {
        return new int[0];
    }

    @Override
    public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
        return new int[0];
    }

    @Override
    public int[] batchUpdate(final String sql, final Collection<StatementSetter> args) throws JpoException {
        return new int[0];

    }

    @Override
    public void close() {
    }

    @Override
    public void commit() {
    }

    @Override
    public void execute(final String sql) throws JpoException {
    }

    @Override
    public <T> T query(final String sql, final StatementSetter pss, final ResultSetReader<T> rse) throws JpoException {
        return null;
    }

    @Override
    public void rollback() {
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
    }

    @Override
    public void setTimeout(final int timeout) {
    }

    @Override
    public void setTransactionIsolation(final TransactionIsolation isolationLevel) {
    }

    @Override
    public int update(final String sql, final StatementSetter psc) throws JpoException {
        return 0;
    }

    @Override
    public <R> R update(String sql, GeneratedKeyReader<R> generatedKeyReader, StatementSetter pss) {
        return null;
    }

}
