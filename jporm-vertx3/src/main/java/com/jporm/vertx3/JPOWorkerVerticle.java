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

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.JPO;
import com.jporm.session.TransactionCallback;
import com.jporm.session.TransactionCallbackVoid;

public class JPOWorkerVerticle extends AbstractVerticle {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final DeliveryOptions defaultDeliveryOptions;
	private final JPO jpo;

	private final int verticleNumber;

	private final JPOVertxNameBuilder nameBuider;

	JPOWorkerVerticle(final JPO jpo, final JPOVertxNameBuilder nameBuider, final int verticleNumber) {
		this.jpo = jpo;
		this.nameBuider = nameBuider;
		this.verticleNumber = verticleNumber;
		defaultDeliveryOptions = new DeliveryOptions();
		defaultDeliveryOptions.setCodecName(nameBuider.getDefaultCodecName());
	}

	@Override
	public void start(final Future<Void> startFuture) {
		logger.debug("Starting JPO Worker verticle number [{}] with JPO instanceId [{}]", verticleNumber, nameBuider.getInstanceId());
		registerTransactionHandler(startFuture);
	}

	private void handleTransaction(final Message<TransactionCallback<?>> message) {
		logger.debug("Request received");
		TransactionCallback<?> transactionCallback = message.body();
		Object reply = jpo.session().doInTransaction(transactionCallback);
		message.reply(reply, defaultDeliveryOptions);
	}

	private void handleTransactionVoid(final Message<TransactionCallbackVoid> message) {
		logger.debug("Request received");
		TransactionCallbackVoid transactionCallback = message.body();
		jpo.session().doInTransactionVoid(transactionCallback);
		message.reply("", defaultDeliveryOptions);
	}

	private void registerTransactionHandler(Future<Void> startFuture) {
		logger.debug("Creating transaction consumer [{}]", nameBuider.getConsumerNameTransaction());
		MessageConsumer<TransactionCallback<?>> consumerTx = vertx.eventBus().localConsumer(nameBuider.getConsumerNameTransaction());
		consumerTx.handler(this::handleTransaction);
		consumerTx.completionHandler(completionResult -> {
			registerTransactionVoidHandler(startFuture);
		});
	}

	private void registerTransactionVoidHandler(Future<Void> startFuture) {
		logger.debug("Creating transaction consumer [{}]", nameBuider.getConsumerNameTransactionVoid());
		MessageConsumer<TransactionCallbackVoid> consumerTxVoid = vertx.eventBus().localConsumer(nameBuider.getConsumerNameTransactionVoid());
		consumerTxVoid.handler(this::handleTransactionVoid);
		consumerTxVoid.completionHandler(completionHandlerTxVoid -> {
			completeVerticleStartup(startFuture);
		});
	}

	private void completeVerticleStartup(Future<Void> startFuture) {
		logger.debug("JPO Worker verticle number [{}] started", verticleNumber);
		startFuture.complete();
	}
}
