/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rx.core.connection;

import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public interface Connection {

	  /**
	   * Executes the given SQL <code>SELECT</code> prepared statement which returns the results of the query.
	   *
	   * @param sql  the SQL to execute. For example <code>SELECT * FROM table ...</code>.
	   * @param params  these are the parameters to fill the statement.
	   * @param resultHandler  the handler which is called once the operation completes. It will return a ResultSet.
	   *
	   * @see java.sql.Statement#executeQuery(String)
	   * @see java.sql.PreparedStatement#executeQuery(String)
	   */
	  <T> CompletableFuture<T> query(String sql, final StatementSetter pss, ResultSetReader<T> rse);

	  CompletableFuture<UpdateResult> update(String sql, GeneratedKeyReader generatedKeyReader, final StatementSetter pss);

	  /**
	   * Closes the connection. Important to always close the connection when you are done so it's returned to the pool.
	   *
	   */
	  CompletableFuture<Void> close();

	  /**
	   * Commits all changes made since the previous commit/rollback.
	   *
	   */
	  CompletableFuture<Void> commit();

	  /**
	   * Rolls back all changes made since the previous commit/rollback.
	   */
	  CompletableFuture<Void> rollback();

	  void setTransactionIsolation(TransactionIsolation isolation);

}
