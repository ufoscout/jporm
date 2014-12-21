/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.vertx3;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;

import com.jporm.session.TransactionCallback;
import com.jporm.session.TransactionCallbackVoid;

public interface JPO {

	<T> void tx(TransactionCallback<T> session, DeliveryOptions options);

	<T> void tx(TransactionCallback<T> session, DeliveryOptions options, Handler<AsyncResult<Message<T>>> replyHandler);

	<T> void tx(TransactionCallback<T> session);

	<T> void tx(TransactionCallback<T> session, Handler<AsyncResult<Message<T>>> replyHandler);

	void txVoid(TransactionCallbackVoid session, DeliveryOptions options);

	void txVoid(TransactionCallbackVoid session, DeliveryOptions options, Handler<AsyncResult<Message<Object>>> replyHandler);

	void txVoid(TransactionCallbackVoid session);

	void txVoid(TransactionCallbackVoid session, Handler<AsyncResult<Message<Object>>> replyHandler);
}
