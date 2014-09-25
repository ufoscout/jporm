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
package com.jporm.core.session;

import java.util.List;

import com.jporm.core.dialect.querytemplate.QueryTemplate;
import com.jporm.exception.OrmException;
import com.jporm.session.BatchPreparedStatementSetter;
import com.jporm.session.GeneratedKeyReader;
import com.jporm.session.PreparedStatementSetter;
import com.jporm.session.ResultSetReader;

/**
 * 
 * @author Francesco Cina'
 *
 * Dec 20, 2011
 */
public class NullSqlPerformerStrategy extends SqlPerformerStrategy {

    @Override
    public void execute(final String sql, final int timeout) throws OrmException {
        // do nothing
    }

    @Override
    public <T> T query(final String sql, final int timeout, final int maxRows, final PreparedStatementSetter pss, final ResultSetReader<T> rse) 	throws OrmException {
        return null;
    }

    @Override
    public int update(final String sql, final int timeout, final PreparedStatementSetter psc) throws OrmException {
        return 0;
    }

    @Override
    public int update(final String sql, final int timeout, final GeneratedKeyReader generatedKeyReader, final QueryTemplate queryTemplate, final PreparedStatementSetter psc) throws OrmException {
        return 0;
    }

    @Override
    public int[] batchUpdate(final List<String> sqls, final int timeout) throws OrmException {
        return new int[0];
    }

    @Override
    public int[] batchUpdate(final String sql, final List<Object[]> args, final int timeout) throws OrmException {
        return new int[0];
    }

    @Override
    public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc, final int timeout) throws OrmException {
        return new int[0];
    }

}
