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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.transaction.TransactionCallback;
import com.jporm.transaction.TransactionVoidCallback;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;

public class JPOVertxExecutor implements JPO {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final DeliveryOptions defaultDeliveryOptions;
	private final Vertx vertx;
	private final JPOVertxNameBuilder nameBuider;

	JPOVertxExecutor(final JPOVertxNameBuilder nameBuider, final Vertx vertx) {
		this.nameBuider = nameBuider;
		this.vertx = vertx;
		defaultDeliveryOptions = new DeliveryOptions();
		defaultDeliveryOptions.setCodecName(nameBuider.getDefaultCodecName());
	}

	@Override
	public <T> void tx(final TransactionCallback<T> session) {
		vertx.eventBus().send(nameBuider.getConsumerNameTransaction(), session, defaultDeliveryOptions);
	};

	@Override
	public <T> void tx(final TransactionCallback<T> session, final DeliveryOptions options) {
		vertx.eventBus().send(nameBuider.getConsumerNameTransaction(), session, getOptions(options));
	};


	@Override
	public <T> void tx(final TransactionCallback<T> session, final Handler<AsyncResult<Message<T>>> replyHandler) {
		logger.debug("Sending message to [{}]", nameBuider.getConsumerNameTransaction());
		vertx.eventBus().send(nameBuider.getConsumerNameTransaction(), session, defaultDeliveryOptions, replyHandler);
	};

	@Override
	public <T> void tx(final TransactionCallback<T> session, final DeliveryOptions options, final Handler<AsyncResult<Message<T>>> replyHandler) {
		vertx.eventBus().send(nameBuider.getConsumerNameTransaction(), session, getOptions(options), replyHandler);
	};

	@Override
	public void txVoid(final TransactionVoidCallback session) {
		vertx.eventBus().send(nameBuider.getConsumerNameTransactionVoid(), session, defaultDeliveryOptions);
	}

	@Override
	public void txVoid(final TransactionVoidCallback session, final DeliveryOptions options) {
		vertx.eventBus().send(nameBuider.getConsumerNameTransactionVoid(), session, getOptions(options));
	}

	@Override
	public void txVoid(final TransactionVoidCallback session, final DeliveryOptions options, final Handler<AsyncResult<Message<Object>>> replyHandler) {
		vertx.eventBus().send(nameBuider.getConsumerNameTransactionVoid(), session, getOptions(options), replyHandler);
	}

	@Override
	public void txVoid(final TransactionVoidCallback session, final Handler<AsyncResult<Message<Object>>> replyHandler) {
		vertx.eventBus().send(nameBuider.getConsumerNameTransactionVoid(), session, defaultDeliveryOptions, replyHandler);
	}

	private DeliveryOptions getOptions(final DeliveryOptions options) {
		if (options.getCodecName()==null) {
			options.setCodecName(nameBuider.getDefaultCodecName());
		}
		return options;
	}


}
