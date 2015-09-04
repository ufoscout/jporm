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
package com.jporm.rx.vertx.session.vertx3.jdbcclient;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rx.connection.Connection;
import com.jporm.rx.connection.UpdateResultImpl;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

public class Vertx3Connection implements Connection {

	private static final Logger LOGGER = LoggerFactory.getLogger(Vertx3Connection.class);
	private static long COUNT = 0l;

	private final long connectionNumber = COUNT++;
	private SQLConnection connection;

	public Vertx3Connection(SQLConnection connection) {
		this.connection = connection;
	}

	@Override
	public CompletableFuture<Void> close() {
		LOGGER.debug("Connection [{}] - close", connectionNumber);
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
		LOGGER.debug("Connection [{}] - commit", connectionNumber);
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
		LOGGER.debug("Connection [{}] - rollback", connectionNumber);
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
		LOGGER.debug("Connection [{}] - Execute query: [{}]", connectionNumber, sql);

		Vertx3Statement statement = new Vertx3Statement();
		pss.set(statement);
		CompletableFuture<T> result = new CompletableFuture<>();
		connection.queryWithParams(sql, statement.getParams(), handler -> {
			if (handler.succeeded()) {
				result.complete(rse.read(new Vertx3ResultSet(handler.result())));
			} else {
				Throwable cause = handler.cause();
				LOGGER.error("Exception thrown during query execution", cause);
				result.completeExceptionally(cause);
			}
		});
		return result;
	}

	@Override
	public CompletableFuture<com.jporm.rx.connection.UpdateResult> update(String sql, GeneratedKeyReader generatedKeyReader, StatementSetter pss) {
		LOGGER.debug("Connection [{}] - Execute update query: [{}]", connectionNumber, sql);
		Vertx3Statement statement = new Vertx3Statement();
		pss.set(statement);
		CompletableFuture<com.jporm.rx.connection.UpdateResult> result = new CompletableFuture<>();
		connection.updateWithParams(sql, statement.getParams(), handler -> {
			UpdateResult updateResult = handler.result();
			if (handler.succeeded()) {
				generatedKeyReader.read(new Vertx3GeneratedKeysResultSet(updateResult.getKeys(), generatedKeyReader.generatedColumnNames()));
				result.complete( new UpdateResultImpl(updateResult.getUpdated()) );
			} else {
				Throwable cause = handler.cause();
				LOGGER.error("Exception thrown during update execution", cause);
				result.completeExceptionally(cause);
			}
		});
		return result;
	}

	@Override
	public void setTransactionIsolation(TransactionIsolation isolation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTimeout(int timeout) {
		// TODO Auto-generated method stub
	}

}
