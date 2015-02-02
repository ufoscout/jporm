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

import java.util.stream.Stream;

import com.jporm.core.exception.JpoException;
import com.jporm.core.query.ResultSetReader;
import com.jporm.sql.dialect.statement.StatementStrategy;
//import com.jporm.sql.dialect.sql.SqlStrategy;

/**
 *
 * @author Francesco Cina'
 *
 * Dec 20, 2011
 *
 * The implementations of this class MUST be stateless and Thread safe.
 *
 */
public interface SqlPerformerStrategy {

	int[] batchUpdate(Stream<String> sqls) throws JpoException;

	int[] batchUpdate(String sql, BatchPreparedStatementSetter psc) throws JpoException;

	int[] batchUpdate(String sql, Stream<Object[]> args) throws JpoException;

	void execute(String sql) throws JpoException;

	<T> T query(String sql, int maxRows, final PreparedStatementSetter pss, ResultSetReader<T> rse) throws JpoException ;

	int update(String sql, GeneratedKeyReader generatedKeyReader, StatementStrategy statementStrategy, final PreparedStatementSetter pss) throws JpoException;

	int update(String sql, final PreparedStatementSetter pss) throws JpoException;


}
