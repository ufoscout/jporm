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
package com.jporm.rx.vertx.session.vertx3.sqlservice;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SqlConnection;
import io.vertx.ext.sql.UpdateResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.connection.Connection;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public class Vertx3Connection implements Connection {

	private SqlConnection connection;

	public Vertx3Connection(SqlConnection connection) {
		this.connection = connection;
	}

	public CompletableFuture<Integer> updateWithParams(String sql, List<Object> params) {
		CompletableFuture<Integer> result = new CompletableFuture<>();
		connection.updateWithParams(sql, new JsonArray(params), handler -> {
			UpdateResult updateResult = handler.result();
			if (handler.succeeded()) {
				result.complete(updateResult.getUpdated());
			} else {
				result.completeExceptionally(handler.cause());
			}
		});
		return result;
	}

	@Override
	public CompletableFuture<Void> close() {
		CompletableFuture<Void> result = new CompletableFuture<>();
		connection.close(handler -> {
			if (handler.succeeded()) {
				result.complete(null);
			} else {
				result.completeExceptionally(handler.cause());
			}
		});
		return result;
	}

	@Override
	public CompletableFuture<Void> commit() {
		CompletableFuture<Void> result = new CompletableFuture<>();
		connection.commit(handler -> {
			if (handler.succeeded()) {
				result.complete(null);
			} else {
				result.completeExceptionally(handler.cause());
			}
		});
		return result;
	}

	@Override
	public CompletableFuture<Void> rollback() {
		CompletableFuture<Void> result = new CompletableFuture<>();
		connection.rollback(handler -> {
			if (handler.succeeded()) {
				result.complete(null);
			} else {
				result.completeExceptionally(handler.cause());
			}
		});
		return result;
	}

	@Override
	public <T> CompletableFuture<T> query(String sql, StatementSetter pss, ResultSetReader<T> rse) {
//		CompletableFuture<ResultSet> result = new CompletableFuture<>();
//		connection.queryWithParams(sql, new JsonArray(params), handler -> {
//			if (handler.succeeded()) {
//				result.complete(new Vertx3ResultSet(handler.result()));
//			} else {
//				result.completeExceptionally(handler.cause());
//			}
//		});
//		return result;
		return null;
	}

	@Override
	public CompletableFuture<com.jporm.rx.connection.UpdateResult> update(String sql, GeneratedKeyReader generatedKeyReader, StatementSetter pss) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTransactionIsolation(TransactionIsolation isolation) {
		// TODO Auto-generated method stub

	}

}
